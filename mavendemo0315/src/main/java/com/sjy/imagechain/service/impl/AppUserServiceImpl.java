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
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

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

    /**
     * 生成区块链账户辅助方法
     */
    private String[] generateAndSaveBlockchainAccount() {
        try {
            Client client = bcosSDK.getClient(1);
            CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
            // 返回 [地址, 私钥Hex]
            return new String[]{keyPair.getAddress(), keyPair.getHexPrivateKey()};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UserRegistrationResponse registerUserFromImageHosting(UserRegistrationRequest request) {
        UserRegistrationResponse response = new UserRegistrationResponse();

        // 1. 基础参数校验
        if (!StringUtils.hasText(request.getUserId())) {
            response.setSuccess(false);
            response.setMessage("Image hosting user ID is required.");
            return response;
        }

        // 2. 幂等性检查：检查该图床用户ID是否已存在
        AppUser existingUser = appUserMapper.selectById(request.getUserId());
        if (existingUser != null) {
            response.setSuccess(true);
            response.setMessage("User already registered.");
            // 返回已有信息
            response.setUserId(existingUser.getUserId());
            response.setUserName(existingUser.getUsername());
            response.setUserEmail(existingUser.getUserEmail());
            response.setBlockchainAddress(existingUser.getBlockchainAddress());
            return response;
        }

        // 3. 唯一性冲突检查 (用户名或邮箱)
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("user_id", request.getUserId())
                .and(qw -> qw.eq("user_name", request.getUserName())
                        .or()
                        .eq("user_email", request.getUserEmail()));

        if (appUserMapper.selectCount(queryWrapper) > 0) {
            response.setSuccess(false);
            response.setMessage("Username or email conflicts with another user.");
            return response;
        }

        // 4. 分配默认角色
        AppRole defaultRole = appRoleMapper.selectByRoleName("user");
        if (defaultRole == null) {
            response.setSuccess(false);
            response.setMessage("Default user role not found in the system.");
            return response;
        }

        // 5. 生成区块链钱包地址
        String[] blockchainAccount = generateAndSaveBlockchainAccount();
        if (blockchainAccount == null) {
            response.setSuccess(false);
            response.setMessage("Failed to generate blockchain address.");
            return response;
        }
        // 地址冲突检查 (极低概率，但为了严谨)
        if (appUserMapper.selectCount(new QueryWrapper<AppUser>().eq("blockchain_address", blockchainAccount[0])) > 0) {
            response.setSuccess(false);
            response.setMessage("Generated blockchain address conflicts with existing user.");
            return response;
        }

        // 6. 构建 AppUser 实体
        AppUser appUser = new AppUser();

        // === 基础信息 ===
        appUser.setUserId(request.getUserId());
        appUser.setUsername(request.getUserName());
        appUser.setUserEmail(request.getUserEmail());
        appUser.setPasswordHash(request.getPasswordHash());
        appUser.setRoleId(defaultRole.getRoleId());
        appUser.setAvatarUrl(request.getAvatarUrl());

        // === 区块链身份 ===
        appUser.setBlockchainAddress(blockchainAccount[0]);
        appUser.setEncryptedPrivateKey(blockchainAccount[1]); // 注意：生产环境建议加密后再存
        appUser.setNonce(UUID.randomUUID().toString());       // 新增: 初始化 Web3 登录随机数

        // === 系统字段 ===
        appUser.setCreateTime(LocalDateTime.now());           // 新增: 创建时间
        appUser.setIsDelete(false);                           // 新增: 逻辑删除标志

        // 7. 保存到数据库
        int rowsAffected = appUserMapper.insert(appUser);

        if (rowsAffected > 0) {
            response.setSuccess(true);
            response.setMessage("User registered successfully.");
            // 返回关键信息
            response.setUserId(appUser.getUserId());
            response.setUserName(appUser.getUsername());
            response.setUserEmail(appUser.getUserEmail());
            response.setBlockchainAddress(blockchainAccount[0]);
            response.setPrivateKeyHex(blockchainAccount[1]);
        } else {
            response.setSuccess(false);
            response.setMessage("Failed to insert user record into database.");
        }

        return response;
    }

    @Override
    public CryptoKeyPair getCryptoKeyPairByUserId(String userId) {
        AppUser appUser = appUserMapper.selectById(userId);
        if (appUser == null) {
            throw new RuntimeException("用户不存在: " + userId);
        }
        if (!StringUtils.hasText(appUser.getEncryptedPrivateKey())) {
            throw new RuntimeException("用户未绑定区块链账户私钥");
        }

        // 创建 keyPair (这里使用的是 Hex 私钥直接恢复，如果字段名是 encrypted 但存的是明文 Hex，则无需解密)
        // 如果存的是 AES 加密后的串，此处需要先解密
        Client client = bcosSDK.getClient(1);
        return client.getCryptoSuite().createKeyPair(appUser.getEncryptedPrivateKey());
    }
}