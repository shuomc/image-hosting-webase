package com.sjy.imagechain.controller;


import com.sjy.imagechain.common.dto.AjaxResult;

import com.sjy.imagechain.domain.NftInfo;
import com.sjy.imagechain.domain.vo.NftTransactionVO;
import com.sjy.imagechain.domain.vo.NftVO;
import com.sjy.imagechain.domain.vo.PageData;
import com.sjy.imagechain.service.NftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/nft")
public class NftController {

    @Autowired
    private NftService nftService;

    // ==========================================
    // 1. 浏览与查询接口
    // ==========================================

    /**
     * 获取市场 NFT 列表
     */
    @GetMapping("/list")
    public AjaxResult getNFTList(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false) String query,
                                 @RequestParam(required = false) String category,
                                 @RequestParam(defaultValue = "new") String sort) {
        PageData<NftVO> data = nftService.getNFTList(page, pageSize, query, category, sort);
        return AjaxResult.success(data);
    }

    /**
     * 获取单个 NFT 详情
     */
    @GetMapping("/{nftId}")
    public AjaxResult getNFTDetail(@PathVariable String nftId) {
        NftVO vo = nftService.getNFTDetail(nftId);
        return AjaxResult.success(vo);
    }

    /**
     * 获取"我的"资产
     * mode: "owned"(持有), "created"(铸造), "sold"(已卖出)
     */
    @GetMapping("/my")
    public AjaxResult getMyNFTs(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(defaultValue = "owned") String mode) {
        PageData<NftVO> data = nftService.getMyNFTs(page, pageSize, mode);
        return AjaxResult.success(data);
    }

    /**
     * 获取"我的"交易记录
     */
    @GetMapping("/transactions/my")
    public AjaxResult getMyTransactions(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                        @RequestParam(required = false) String type) {
        PageData<NftTransactionVO> data = nftService.getMyTransactions(page, pageSize, type);
        return AjaxResult.success(data);
    }

    /**
     * 获取所有交易记录
     */
    @GetMapping("/transactions/all")
    public AjaxResult getAllTransactions(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(required = false) String type) {
        PageData<NftTransactionVO> data = nftService.getAllTransactions(page, pageSize, type);
        return AjaxResult.success(data);
    }

    /**
     * 获取交易统计
     */
    @GetMapping("/transactions/stats")
    public AjaxResult getTransactionStats(@RequestParam(defaultValue = "day") String type) {
        return AjaxResult.success(nftService.getTransactionStats(type));
    }

    /**
     * 获取当前用户余额
     */
    @GetMapping("/balance")
    public AjaxResult getBalance() {
        BigDecimal balance = nftService.getBalance();
        return AjaxResult.success("查询成功", balance);
    }

    // ==========================================
    // 2. 核心交易接口 (铸造/购买/赠送)
    // ==========================================

    /**
     * 铸造 NFT
     * 参数: imageId (必需), name, description, price
     */
    @PostMapping("/mint")
    public AjaxResult mintNFT(@RequestBody Map<String, Object> params) {
        String imageId = (String) params.get("imageId");
        String thumbnailMinioUrl = (String) params.get("thumbnailMinioUrl");
        String name = (String) params.get("name");
        String description = (String) params.get("description");
        String fileHash = (String) params.get("fileHash");
        // 处理 BigDecimal 转换异常
        BigDecimal price = params.get("price") != null ?
                new BigDecimal(params.get("price").toString()) : BigDecimal.ZERO;
        Integer collectionId = params.get("collectionId") != null ?
                Integer.parseInt(params.get("collectionId").toString()) : null;

        NftInfo nftInfo = nftService.mintNFT(imageId,thumbnailMinioUrl, name, description, price, collectionId, fileHash);
        return AjaxResult.success("铸造成功", nftInfo);
    }

    /**
     * 购买 NFT
     */
    @PostMapping("/buy/{nftId}")
    public AjaxResult buyNFT(@PathVariable String nftId) {
        nftService.buyNFT(nftId);
        return AjaxResult.success("购买成功");
    }

    /**
     * 赠送/转移 NFT
     */
    @PostMapping("/transfer")
    public AjaxResult transferNFT(@RequestBody Map<String, String> params) {
        String nftId = params.get("nftId");
        String toAddress = params.get("toAddress");
        nftService.transferNFT(nftId, toAddress);
        return AjaxResult.success("转赠成功");
    }

    // ==========================================
    // 3. 商品管理接口 (改价/上下架)
    // ==========================================

    /**
     * 修改价格
     */
    @PostMapping("/price/{nftId}")
    public AjaxResult updatePrice(@PathVariable String nftId, @RequestBody Map<String, Object> params) {
        BigDecimal price = new BigDecimal(params.get("price").toString());
        nftService.updateNFTPrice(nftId, price);
        return AjaxResult.success("价格更新成功");
    }

    /**
     * 上架 (Put On Shelf)
     * 注意：上架前 NFT 必须有有效价格 (>0)
     */
    @PostMapping("/shelf/on/{nftId}")
    public AjaxResult putOnShelf(@PathVariable String nftId) {
        nftService.putOnShelf(nftId);
        return AjaxResult.success("上架成功");
    }

    /**
     * 下架 (Off Shelf)
     */
    @PostMapping("/shelf/off/{nftId}")
    public AjaxResult offShelf(@PathVariable String nftId) {
        nftService.offShelf(nftId);
        return AjaxResult.success("下架成功");
    }

    /**
     * 系统级下架 (用于管理员删除图片时自动下架)
     * 不需要用户认证，使用NFT所有者的密钥执行
     */
    @PostMapping("/shelf/system-off/{nftId}")
    public AjaxResult systemOffShelf(@PathVariable String nftId) {
        nftService.systemOffShelf(nftId);
        return AjaxResult.success("系统下架成功");
    }

    // ==========================================
    // 4. 资金接口
    // ==========================================

    /**
     * 充值 (模拟)
     */
    @PostMapping("/deposit")
    public AjaxResult deposit(@RequestBody Map<String, Object> params) {
        BigDecimal amount = new BigDecimal(params.get("amount").toString());
        nftService.deposit(amount);
        return AjaxResult.success("充值成功");
    }

    /**
     * 提现
     */
    @PostMapping("/withdraw")
    public AjaxResult withdraw(@RequestBody Map<String, Object> params) {
        BigDecimal amount = new BigDecimal(params.get("amount").toString());
        nftService.withdraw(amount);
        return AjaxResult.success("提现申请成功");
    }

    /**
     * 根据图片ID删除NFT信息
     */
    @DeleteMapping("/image/{imageId}")
    public AjaxResult deleteByImageId(@PathVariable String imageId) {
        nftService.deleteByImageId(imageId);
        return AjaxResult.success("删除成功");
    }
}