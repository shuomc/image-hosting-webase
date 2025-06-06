package com.sjy.imagechain.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sjy.imagechain.domain.AppRole;
import com.sjy.imagechain.domain.AppUser;
import com.sjy.imagechain.domain.dto.UserRegistrationRequest;
import com.sjy.imagechain.domain.dto.UserRegistrationResponse;
import com.sjy.imagechain.mapper.AppRoleMapper;
import com.sjy.imagechain.mapper.AppUserMapper;
import com.sjy.imagechain.service.AppUserService;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shuomc
 * @Date 2025/5/14
 * @Description
 */

/**
 * 用户服务实现类
 */
@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private AppRoleMapper appRoleMapper;

    @Autowired
    private BcosSDK bcosSDK;

    private String[] generateAndSaveBlockchainAccount() {
        try {
            Client client = bcosSDK.getClient(1);
            CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();

            String address = keyPair.getAddress();
            String privateKeyHex = keyPair.getHexPrivateKey(); // 注意：不要明文存储！

            return new String[]{address, privateKeyHex};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UserRegistrationResponse registerUserFromImageHosting(UserRegistrationRequest request) {
        UserRegistrationResponse response = new UserRegistrationResponse();

        // !!! 新增: 校验图床系统提供的用户ID是否为空 !!!
        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            response.setSuccess(false);
            response.setMessage("Image hosting user ID is required.");
            return response;
        }

        // 1. 检查该图床用户ID是否已在区块链系统中注册过
        AppUser existingUser = appUserMapper.selectById(request.getUserId());
        if (existingUser != null) {

            response.setSuccess(true);
            response.setMessage("User already registered.");
            response.setUserId(existingUser.getUserId());
            response.setUserName(existingUser.getUsername());
            response.setUserEmail(existingUser.getUserEmail());
            response.setBlockchainAddress(existingUser.getBlockchainAddress()); // 返回已有的地址
            return response;

            // 如果要求同一个图床用户ID只能注册一次，则返回错误：
            // response.setSuccess(false);
            // response.setMessage("User with this ID is already registered.");
            // return response;
        }


        // 2. 检查用户名或邮箱是否已存在
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        // 排除当前尝试注册的ID，以防上面的 selectById 判断漏掉什么
        queryWrapper.ne("user_id", request.getUserId())
                .and(qw -> qw.eq("user_name", request.getUserName()).or().eq("user_email", request.getUserEmail()));

        if (appUserMapper.selectCount(queryWrapper) > 0) {
            response.setSuccess(false);
            response.setMessage("Username or email conflicts with another user.");
            return response;
        }


        // 3. 分配默认角色
        AppRole defaultRole = appRoleMapper.selectByRoleName("user");
        if (defaultRole == null) {
            response.setSuccess(false);
            response.setMessage("Default user role not found in the system.");
            return response;
        }
        Integer roleId = defaultRole.getRoleId();


        // 4. 生成或获取区块链
        String[] blockchainAccount = generateAndSaveBlockchainAccount(); // 调用生成地址的方法
        if (blockchainAccount == null) {
            response.setSuccess(false);
            response.setMessage("Failed to generate blockchain address.");
            return response;
        }
        // 检查生成的地址是否已存在
        if (appUserMapper.selectCount(new QueryWrapper<AppUser>().eq("blockchain_address", blockchainAccount[0])) > 0) {
            response.setSuccess(false);
            response.setMessage("Generated blockchain address conflicts with existing user.");
            return response; // 简单处理，生产环境中可能需要重试生成地址
        }


        // 5. 构建 AppUser 实体
        AppUser appUser = new AppUser();
        appUser.setUserId(request.getUserId());
        appUser.setUsername(request.getUserName());
        appUser.setUserEmail(request.getUserEmail());
        appUser.setPasswordHash(request.getPasswordHash());
        appUser.setBlockchainAddress(blockchainAccount[0]); // 设置生成的区块链地址
        appUser.setEncryptedPrivateKey(blockchainAccount[1]);
        appUser.setRoleId(roleId); // 设置角色ID


        // 6. 保存到数据库
        int rowsAffected = appUserMapper.insert(appUser);

        if (rowsAffected > 0) {
            response.setSuccess(true);
            response.setMessage("User registered successfully in blockchain system database.");
            response.setUserId(appUser.getUserId()); // 返回实际保存的 userId
            response.setUserName(appUser.getUsername()); // 返回实际保存的用户名
            response.setUserEmail(appUser.getUserEmail()); // 返回实际保存的邮箱
            response.setBlockchainAddress(blockchainAccount[0]); // 返回分配的地址
            response.setPrivateKeyHex(blockchainAccount[1]);
        } else {
            response.setSuccess(false);
            response.setMessage("Failed to insert user record into database.");
        }

        return response;
    }

    @Override
    public CryptoKeyPair getCryptoKeyPairByUserId(String userId) {
        // 从数据库获取加密的私钥
        AppUser appUser = appUserMapper.selectById(userId);
        if (appUser == null) {
            throw new RuntimeException("用户不存在");
        }

        if (appUser.getEncryptedPrivateKey() == null) {
            throw new RuntimeException("用户未绑定区块链账户");
        }

        // 创建 keyPair
        Client client = bcosSDK.getClient(1);
        return client.getCryptoSuite().createKeyPair(appUser.getEncryptedPrivateKey());
    }
}
