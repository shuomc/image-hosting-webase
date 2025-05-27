package com.sjy.imagechain.controller;


import com.sjy.imagechain.common.dto.AjaxResult;
import com.sjy.imagechain.service.NFTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/nft")
public class NFTController {

    @Autowired
    private NFTService nftService;

    @GetMapping("/list")
    public AjaxResult getNFTList(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false) String query) {
        return AjaxResult.success(nftService.getNFTList(page, pageSize, query));
    }

    @GetMapping("/my")
    public AjaxResult getMyNFTs(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(defaultValue = "owned") String mode) {
        return AjaxResult.success(nftService.getMyNFTs(page, pageSize, mode));
    }

    @PostMapping("/mint")
    public AjaxResult mintNFT(@RequestBody Map<String, Object> params) {
        String imageId = (String) params.get("imageId");
        String description = (String) params.get("description");
        BigDecimal price = new BigDecimal(params.get("price").toString());
        String minioUrl = (String) params.get("minioUrl");
        return AjaxResult.success(nftService.mintNFT(imageId, description, price, minioUrl));
    }

    @PostMapping("/buy/{nftId}")
    public AjaxResult buyNFT(@PathVariable String nftId) {
        return AjaxResult.success(nftService.buyNFT(nftId));
    }

    @PostMapping("/price/{nftId}")
    public AjaxResult setNFTPrice(@PathVariable String nftId, @RequestBody Map<String, Object> params) {
        BigDecimal price = new BigDecimal(params.get("price").toString());
        return AjaxResult.success(nftService.setNFTPrice(nftId, price));
    }

    @PostMapping("/cancel/{nftId}")
    public AjaxResult cancelNFTSale(@PathVariable String nftId) {
        return AjaxResult.success(nftService.cancelNFTSale(nftId));
    }

    @GetMapping("/{nftId}")
    public AjaxResult getNFTDetail(@PathVariable String nftId) {
        return AjaxResult.success(nftService.getNFTDetail(nftId));
    }

    @GetMapping("/transactions/{nftId}")
    public AjaxResult getNFTTransactions(@PathVariable String nftId,
                                       @RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(nftService.getNFTTransactions(nftId, page, pageSize));
    }
} 