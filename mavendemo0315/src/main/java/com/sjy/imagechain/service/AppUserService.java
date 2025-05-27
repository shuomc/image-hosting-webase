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

    // 其他用户服务方法...
}