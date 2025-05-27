package com.sjy.imagechain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sjy.imagechain.domain.AppRole;
import com.sjy.imagechain.domain.AppUser;
import com.sjy.imagechain.domain.ArtworkAsset;
import com.sjy.imagechain.domain.dto.UserRegistrationRequest;
import com.sjy.imagechain.domain.dto.UserRegistrationResponse;
import com.sjy.imagechain.mapper.AppRoleMapper;
import com.sjy.imagechain.mapper.AppUserMapper;
import com.sjy.imagechain.service.AppUserService;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.utils.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPublicKey;
import java.util.UUID;

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
        // 区块链地址生成逻辑... (与之前一致，此处省略重复的代码)
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
            // 用户已存在，根据需求可以返回成功（幂等性）或返回已注册的错误
            // 这里选择返回成功，并包含已存在的用户信息（但不重新生成地址）
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


        // 2. 检查用户名或邮箱是否已存在 (避免不同图床用户但相同名称或邮箱的情况，根据业务决定是否需要此校验)
        // 如果要求用户名或邮箱在区块链系统全局唯一，则保留此校验
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        // 排除当前尝试注册的ID，以防上面的 selectById 判断漏掉什么（不太可能，但写在这里逻辑更清晰）
        queryWrapper.ne("user_id", request.getUserId())
                .and(qw -> qw.eq("user_name", request.getUserName()).or().eq("user_email", request.getUserEmail()));

        if (appUserMapper.selectCount(queryWrapper) > 0) {
            response.setSuccess(false);
            response.setMessage("Username or email conflicts with another user.");
            return response;
        }


        // 3. 分配默认角色 (假设 'user' 角色存在且只有一个)
        AppRole defaultRole = appRoleMapper.selectByRoleName("user"); // 假设 'user' 是默认角色名
        if (defaultRole == null) {
            response.setSuccess(false);
            response.setMessage("Default user role not found in the system.");
            return response;
        }
        Integer roleId = defaultRole.getRoleId();


        // 4. 生成或获取区块链
        String[] blockchainAccount = generateAndSaveBlockchainAccount(); // !!! 调用生成地址的方法 !!!
        if (blockchainAccount == null) {
            response.setSuccess(false);
            response.setMessage("Failed to generate blockchain address.");
            return response;
        }
        // 检查生成的地址是否已存在（概率极低，但仍需考虑）
        if (appUserMapper.selectCount(new QueryWrapper<AppUser>().eq("blockchain_address", blockchainAccount[0])) > 0) {
            response.setSuccess(false);
            response.setMessage("Generated blockchain address conflicts with existing user.");
            return response; // 简单处理，生产环境中可能需要重试生成地址
        }


        // 5. 构建 AppUser 实体
        AppUser appUser = new AppUser();
        appUser.setUserId(request.getUserId()); // !!! 设置从图床系统接收的 userId 作为主键 !!!
        appUser.setUsername(request.getUserName()); // !!! 设置从图床系统接收的 userName !!!
        appUser.setUserEmail(request.getUserEmail()); // !!! 设置从图床系统接收的 userEmail !!!
        appUser.setPasswordHash(request.getPasswordHash());
        appUser.setBlockchainAddress(blockchainAccount[0]); // 设置生成的区块链地址
        appUser.setEncryptedPrivateKey(blockchainAccount[1]);
        appUser.setRoleId(roleId); // 设置角色ID
        // BaseEntity 的 createTime, updateTime, isDeleted 会由 BaseEntity 或 Mybatis-Plus 自动处理


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
