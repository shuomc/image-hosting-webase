package moe.imtop1.imagehosting.nft.controller;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import moe.imtop1.imagehosting.common.dto.AjaxResult;
import moe.imtop1.imagehosting.system.domain.UserInfo;
import moe.imtop1.imagehosting.nft.service.INFTService;
import moe.imtop1.imagehosting.system.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/nft")
public class NFTController {

    @Autowired
    private INFTService nftService;

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 激活/注册区块链账户
     * 场景：用户在个人中心点击 "激活 Web3 账户"
     * 逻辑：后端自动读取当前登录用户的资料，发起注册
     */
    @PostMapping("/user/register")
    public AjaxResult registerBlockchainAccount() {
        // 1. 获取当前登录用户ID
        String userId = StpUtil.getLoginIdAsString();

        // 2. 查询用户详细信息 (作为注册源数据)
        UserInfo user = userInfoService.getById(userId);
        if (user == null) {
            return AjaxResult.error("用户信息获取失败");
        }

        // 3. 调用 NFTService 发起远程注册
        return nftService.registerUser(
                user.getUserId(),
                user.getUserName(),
                user.getUserEmail(),
                user.getPassword(),
                user.getAvatarUrl(),
                user.getBio()
        );
    }

    /**
     * 检查当前用户是否已激活区块链账户
     * 场景：前端页面加载时调用，决定显示 "激活" 按钮还是 "钱包地址"
     */
    @GetMapping("/user/check")
    public AjaxResult checkRegistration() {
        // 获取当前登录用户ID (使用 Sa-Token)
        String userId = StpUtil.getLoginIdAsString();
        return nftService.checkUserRegistration(userId);
    }


    // ==========================================
    // 1. 浏览与查询
    // ==========================================

    @GetMapping("/list")
    public AjaxResult getNFTList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "new") String sort) {
        return nftService.getNFTList(page, pageSize, query, category, sort);
    }

    @GetMapping("/my")
    public AjaxResult getMyNFTs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "owned") String mode) {
        return nftService.getMyNFTs(page, pageSize, mode);
    }

    @GetMapping("/detail/{nftId}")
    public AjaxResult getNFTDetail(@PathVariable String nftId) {
        return nftService.getNFTDetail(nftId);
    }

        @GetMapping("/transactions")
    public AjaxResult getTransactions(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                      @RequestParam(required = false) String type) {
        return nftService.getNFTTransactions(page, pageSize, type);
    }

    @GetMapping("/transactions/all")
    public AjaxResult getAllTransactions(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(required = false) String type) {
        return nftService.getAllNFTTransactions(page, pageSize, type);
    }

    @GetMapping("/transactions/stats")
    public AjaxResult getTransactionStats(@RequestParam(defaultValue = "day") String type) {
        return nftService.getTransactionStats(type);
    }

    @GetMapping("/balance")
    public AjaxResult getBalance() {
        return nftService.getBalance();
    }

    // ==========================================
    // 2. 核心交易 (铸造/购买/转赠)
    // ==========================================

    @PostMapping("/mint")
    public AjaxResult mintNFT(@RequestBody Map<String, Object> params) {
        // 推荐使用 @RequestBody 接收复杂参数，方便前端传 JSON
        String imageId= (String) params.get("imageId");
        String thumbnailMinioUrl = (String) params.get("thumbnailMinioUrl");
        String name = (String) params.get("name");
        String fileHash = (String) params.get("fileHash");
        String description = (String) params.get("description");
        BigDecimal price = new BigDecimal(params.get("price").toString());
        Integer collectionId = params.get("collectionId") != null ?
                Integer.parseInt(params.get("collectionId").toString()) : null;
        log.info("params:{}", params);
        return nftService.mintNFT(imageId,thumbnailMinioUrl, name, description, price, collectionId, fileHash);
    }

    @PostMapping("/buy/{nftId}")
    public AjaxResult buyNFT(@PathVariable String nftId) {
        return nftService.buyNFT(nftId);
    }

    @PostMapping("/transfer")
    public AjaxResult transferNFT(@RequestBody Map<String, String> params) {
        String nftId = params.get("nftId");
        String toAddress = params.get("toAddress");
        return nftService.transferNFT(nftId, toAddress);
    }

    // ==========================================
    // 3. 商品管理 (改价/上下架)
    // ==========================================

    @PostMapping("/price/{nftId}")
    public AjaxResult setNFTPrice(
            @PathVariable String nftId,
            @RequestParam BigDecimal price) {
        return nftService.setNFTPrice(nftId, price);
    }

    /**
     * 上架
     */
    @PostMapping("/shelf/on/{nftId}")
    public AjaxResult putOnShelf(@PathVariable String nftId) {
        return nftService.putOnShelf(nftId);
    }

    /**
     * 下架 (原 cancel)
     */
    @PostMapping("/shelf/off/{nftId}")
    public AjaxResult offShelf(@PathVariable String nftId) {
        return nftService.offShelf(nftId);
    }

    // ==========================================
    // 4. 资金管理
    // ==========================================

    @PostMapping("/deposit")
    public AjaxResult deposit(@RequestParam BigDecimal amount) {
        return nftService.deposit(amount);
    }

    @PostMapping("/withdraw")
    public AjaxResult withdraw(@RequestParam BigDecimal amount) {
        return nftService.withdraw(amount);
    }

    // ==========================================
    // 5. 兼容旧 Webase 接口
    // ==========================================

    @GetMapping("/webase/balance")
    public AjaxResult getWebaseBalance() {
        return nftService.getWebaseBalance();
    }

    @PostMapping("/webase/deposit")
    public AjaxResult webaseDeposit(@RequestParam BigDecimal amount) {
        return nftService.webaseDeposit(amount);
    }

    @GetMapping("/webase/nft/{tokenId}")
    public AjaxResult getWebaseNFTInfo(@PathVariable String tokenId) {
        return nftService.getWebaseNFTInfo(tokenId);
    }

    @GetMapping("/webase/owned")
    public AjaxResult getWebaseOwnedNFTs() {
        return nftService.getWebaseOwnedNFTs();
    }
}