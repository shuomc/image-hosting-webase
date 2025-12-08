package moe.imtop1.imagehosting.nft.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import moe.imtop1.imagehosting.common.dto.AjaxResult;
import moe.imtop1.imagehosting.images.domain.ImageData;
import moe.imtop1.imagehosting.images.service.ImageService;
import moe.imtop1.imagehosting.nft.service.INFTService;
import moe.imtop1.imagehosting.system.domain.UserInfo;
import moe.imtop1.imagehosting.system.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.StringUtils; // 推荐使用工具类处理字符串

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static cn.dev33.satoken.SaManager.log;

@Service
public class INFTServiceImpl implements INFTService {

    @Value("${blockchain.api-url}")
    private String blockchainApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private ImageService imageService;

    // ==========================================
    // 1. 浏览与查询
    // ==========================================

    @Override
    public AjaxResult getNFTList(Integer page, Integer pageSize, String query, String category, String sort) {
        StringBuilder url = new StringBuilder(blockchainApiUrl + "/nft/list?page=" + page + "&pageSize=" + pageSize);
        if (StringUtils.hasText(query)) {
            url.append("&query=").append(query);
        }
        if (StringUtils.hasText(category)) {
            url.append("&category=").append(category);
        }
        if (StringUtils.hasText(sort)) {
            url.append("&sort=").append(sort);
        }
        return forwardRequest(url.toString(), HttpMethod.GET, null);
    }

    @Override
    public AjaxResult getMyNFTs(Integer page, Integer pageSize, String mode) {
        String url = blockchainApiUrl + "/nft/my?page=" + page + "&pageSize=" + pageSize;
        if (StringUtils.hasText(mode)) {
            url += "&mode=" + mode;
        }
        return forwardRequest(url, HttpMethod.GET, null);
    }

    @Override
    public AjaxResult getNFTDetail(String nftId) {
        // URL 变更为 /nft/{nftId}
        String url = blockchainApiUrl + "/nft/" + nftId;
        return forwardRequest(url, HttpMethod.GET, null);
    }

    @Override
    public AjaxResult getNFTTransactions(Integer page, Integer pageSize, String type) {
        // 变更为查询“我的”交易记录: /nft/transactions/my
        String url = blockchainApiUrl + "/nft/transactions/my?page=" + page + "&pageSize=" + pageSize;
        if (StringUtils.hasText(type)) {
            url += "&type=" + type;
        }
        return forwardRequest(url, HttpMethod.GET, null);
    }

    @Override
    public AjaxResult getBalance() {
        String url = blockchainApiUrl + "/nft/balance";
        return forwardRequest(url, HttpMethod.GET, null);
    }

    // ==========================================
    // 2. 核心交易
    // ==========================================

    @Override
    public AjaxResult mintNFT(String imageId, String thumbnailMinioUrl, String name, String description, BigDecimal price, Integer collectionId, String fileHash) {
        String url = blockchainApiUrl + "/nft/mint";

        // 构造 Controller 需要的参数
        Map<String, Object> body = new HashMap<>();
        body.put("imageId", imageId);
        body.put("thumbnailMinioUrl", thumbnailMinioUrl);
        body.put("name", name);
        body.put("description", description);
        body.put("price", price);
        body.put("fileHash", fileHash);
        if (collectionId != null) {
            body.put("collectionId", collectionId);
        }

        AjaxResult result = forwardRequest(url, HttpMethod.POST, body);

        if (result.isSuccess()) {  // 区块链系统铸造成功
            String newNftId = (String) result.getData();

            // 3. 使用 MyBatis-Plus 的 LambdaUpdateWrapper 进行更新
            // 语义：UPDATE image_table SET nft_id = newNftId WHERE image_id = imageId
            boolean updateSuccess = imageService.update(new LambdaUpdateWrapper<ImageData>() // 泛型换成您的图片实体类名，如 ImageInfo
                    .eq(ImageData::getImageId, imageId) // 条件：image_id 等于传入的 imageId
                    .set(ImageData::getNftId, newNftId) // 动作：将 nft_id 字段设为 newNftId
            );

            if (!updateSuccess) {
                // 记录日志：虽然链上成功了，但本地没更新到数据（可能图片ID不存在）
                log.error("NFT铸造成功，但本地关联图片失败，ImageId: {}", imageId);
            }
        }

        return result;
    }

    @Override
    public AjaxResult buyNFT(String nftId) {
        String url = blockchainApiUrl + "/nft/buy/" + nftId;
        return forwardRequest(url, HttpMethod.POST, null);
    }

    @Override
    public AjaxResult transferNFT(String nftId, String toAddress) {
        String url = blockchainApiUrl + "/nft/transfer";
        Map<String, Object> body = Map.of(
                "nftId", nftId,
                "toAddress", toAddress
        );
        return forwardRequest(url, HttpMethod.POST, body);
    }

    // ==========================================
    // 3. 商品管理 (改价/上下架)
    // ==========================================

    @Override
    public AjaxResult setNFTPrice(String nftId, BigDecimal price) {
        String url = blockchainApiUrl + "/nft/price/" + nftId;
        Map<String, Object> body = Map.of("price", price);
        return forwardRequest(url, HttpMethod.POST, body);
    }

    @Override
    public AjaxResult putOnShelf(String nftId) {
        // 上架接口 /shelf/on/{nftId}
        String url = blockchainApiUrl + "/nft/shelf/on/" + nftId;
        return forwardRequest(url, HttpMethod.POST, null);
    }

    @Override
    public AjaxResult offShelf(String nftId) { // 原 cancelNFTSale
        // 下架接口 /shelf/off/{nftId}
        String url = blockchainApiUrl + "/nft/shelf/off/" + nftId;
        return forwardRequest(url, HttpMethod.POST, null);
    }

    // ==========================================
    // 4. 资金与旧接口适配
    // ==========================================

    @Override
    public AjaxResult deposit(BigDecimal amount) {
        String url = blockchainApiUrl + "/nft/deposit";
        Map<String, Object> body = Map.of("amount", amount);
        return forwardRequest(url, HttpMethod.POST, body);
    }

    @Override
    public AjaxResult withdraw(BigDecimal amount) {
        String url = blockchainApiUrl + "/nft/withdraw";
        Map<String, Object> body = Map.of("amount", amount);
        return forwardRequest(url, HttpMethod.POST, body);
    }

    // --- 以下是旧接口的兼容/移除处理 ---

    @Override
    public AjaxResult getWebaseBalance() {
        // 兼容处理：直接调用新的 balance 接口
        return getBalance();
    }

    @Override
    public AjaxResult webaseDeposit(BigDecimal amount) {
        // 兼容处理：直接调用新的 deposit 接口
        return deposit(amount);
    }

    @Override
    public AjaxResult getWebaseNFTInfo(String tokenId) {
        // 警告：新 Controller 移除了基于 tokenId 的直接查询 (/webase/nft/{tokenId})
        // 如果必须保留，需要去新服务增加对应接口。
        // 这里返回不支持的提示
        return AjaxResult.error("该接口已废弃，请使用 getNFTDetail 查询");
    }

    @Override
    public AjaxResult getWebaseOwnedNFTs() {
        // 兼容处理：调用我的 NFT 接口
        return getMyNFTs(1, 100, "owned");
    }

    // ==========================================
    // 5. 用户同步注册接口
    // ==========================================

    @Override
    public AjaxResult registerUser(String userId, String userName, String userEmail, String passwordHash, String avatarUrl, String bio) {

        String url = blockchainApiUrl + "/api/v1/users/register";

        // 构造请求体
        Map<String, Object> body = new HashMap<>();
        body.put("userId", userId);
        body.put("userName", userName);
        body.put("userEmail", userEmail);
        body.put("passwordHash", passwordHash);

        if (StringUtils.hasText(avatarUrl)) body.put("avatarUrl", avatarUrl);
        if (StringUtils.hasText(bio)) body.put("bio", bio);

        try {
            // 准备请求头 (注册接口通常无需 Token，如果是系统间调用可能需要 API Key，这里暂留空)
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<Object> entity = new HttpEntity<>(body, headers);

            // 发起 POST 请求
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            // 处理响应
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> respBody = response.getBody();

                // 解析 UserRegistrationResponse 字段
                Boolean success = (Boolean) respBody.get("success");
                String message = (String) respBody.get("message");

                if (Boolean.TRUE.equals(success)) {
                    // 获取返回的区块链地址
                    String blockchainAddress = (String) respBody.get("blockchainAddress");

                    if (StringUtils.hasText(blockchainAddress)) {
                        // 更新本地数据库
                        // 使用 MyBatis-Plus 的 Service
                        UserInfo userInfo = userInfoService.getById(userId);
                        if (userInfo != null) {
                            userInfo.setBlockchainAddress(blockchainAddress);
                            boolean updateResult = userInfoService.updateById(userInfo);
                            // 将区块链地址写入数据库
                            if(!updateResult) {
                                log.error("警告：本地数据库更新区块链地址失败");
                            }
                            log.info("更新区块链地址成功");
                        }
                    }

                    // 返回成功结果
                    return AjaxResult.success(message, respBody);
                } else {
                    return AjaxResult.error(message);
                }
            } else {
                return AjaxResult.error("区块链注册失败: " + response.getStatusCode());
            }

        } catch (Exception e) {
            // 捕获 RestTemplate 的 4xx/5xx 异常
            if (e instanceof org.springframework.web.client.HttpClientErrorException) {
                org.springframework.web.client.HttpClientErrorException clientError = (org.springframework.web.client.HttpClientErrorException) e;
                try {
                    Map respBody = clientError.getResponseBodyAs(Map.class);
                    if (respBody != null && respBody.containsKey("message")) {
                        return AjaxResult.error((String) respBody.get("message"));
                    }
                } catch (Exception ignored) {}
            }
            e.printStackTrace();
            return AjaxResult.error("请求区块链注册接口异常：" + e.getMessage());
        }
    }

    // ==========================================
    // 6. 用户状态查询
    // ==========================================

    /**
     * 检查用户是否已注册区块链账户
     * 逻辑：查询本地数据库是否有 blockchainAddress
     */
    @Override
    public AjaxResult checkUserRegistration(String userId) {
        if (!StringUtils.hasText(userId)) {
            return AjaxResult.error("用户ID不能为空");
        }

        // 1. 查询本地数据库
        UserInfo userInfo = userInfoService.getById(userId);

        if (userInfo == null) {
            return AjaxResult.error("用户不存在");
        }

        // 2. 判断是否有区块链地址
        boolean isRegistered = StringUtils.hasText(userInfo.getBlockchainAddress());

        Map<String, Object> data = new HashMap<>();
        data.put("isRegistered", isRegistered);
        data.put("blockchainAddress", userInfo.getBlockchainAddress()); // 如果有，顺便返回地址

        return AjaxResult.success(data);
    }

    // ==========================================
    // 辅助方法
    // ==========================================

    private AjaxResult forwardRequest(String url, HttpMethod method, Object body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            // 获取当前用户的 Token 并透传
            if (StpUtil.isLogin()) {
                String token = StpUtil.getTokenValue();
                headers.set("Authorization", token);
            }

            HttpEntity<Object> entity = new HttpEntity<>(body, headers);

            // 发起请求
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    method,
                    entity,
                    Map.class
            );

            // 处理响应
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> respBody = response.getBody();
                // 检查远程服务返回的 code (假设远程 AjaxResult 结构包含 code 字段)
                Object code = respBody.get("code");
                Object msg = respBody.get("msg");
                Object data = respBody.get("data");

                // 透传远程服务的状态码和消息
                if (code != null && (Integer) code == 200) {
                    return AjaxResult.success((String) msg, data);
                } else {
                    return AjaxResult.error((String) msg, data);
                }
            } else {
                return AjaxResult.error("请求区块链服务失败: " + response.getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("请求区块链服务异常：" + e.getMessage());
        }
    }
}