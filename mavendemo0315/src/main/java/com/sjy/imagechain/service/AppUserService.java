package com.sjy.imagechain.service;

import com.sjy.imagechain.domain.dto.UserRegistrationRequest;
import com.sjy.imagechain.domain.dto.UserRegistrationResponse;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;

public interface AppUserService {

    /**
     * 从图床系统注册用户
     * @param request 注册请求信息
     * @return 注册结果响应
     */
    UserRegistrationResponse registerUserFromImageHosting(UserRegistrationRequest request);

    CryptoKeyPair getCryptoKeyPairByUserId(String userId);

    /**
     * 根据区块链地址获取用户密钥对
     * @param blockchainAddress 区块链钱包地址
     * @return 密钥对
     */
    CryptoKeyPair getCryptoKeyPairByAddress(String blockchainAddress);

}