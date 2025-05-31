package com.sjy.imagechain.controller;

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
    public Map<String,Object> getNFTList(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false) String query) {
        return nftService.getNFTList(page, pageSize, query);
    }

    @GetMapping("/my")
    public Map<String,Object> getMyNFTs(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(defaultValue = "owned") String mode) {
        return nftService.getMyNFTs(page, pageSize, mode);
    }

    @PostMapping("/mint")
    public Map<String,Object> mintNFT(@RequestBody Map<String, Object> params) {
        String imageId = (String) params.get("imageId");
        String description = (String) params.get("description");
        BigDecimal price = new BigDecimal(params.get("price").toString());
        String minioUrl = (String) params.get("minioUrl");
        return nftService.mintNFT(imageId, description, price, minioUrl);
    }

    @PostMapping("/buy/{nftId}")
    public Map<String,Object> buyNFT(@PathVariable String nftId) {
        return nftService.buyNFT(nftId);
    }

    @PostMapping("/price/{nftId}")
    public Map<String,Object> setNFTPrice(@PathVariable String nftId, @RequestBody Map<String, Object> params) {
        BigDecimal price = new BigDecimal(params.get("price").toString());
        return nftService.setNFTPrice(nftId, price);
    }

    @PostMapping("/cancel/{nftId}")
    public Map<String,Object> cancelNFTSale(@PathVariable String nftId) {
        return nftService.cancelNFTSale(nftId);
    }

    @GetMapping("/{nftId}")
    public Map<String,Object> getNFTDetail(@PathVariable String nftId) {
        return nftService.getNFTDetail(nftId);
    }

    @GetMapping("/transactions")
    public Map<String,Object> getNFTTransactions() {
        return nftService.getNFTTransactions();
    }

    @PostMapping("/webase/deposit")
    public Map<String,Object> deposit(@RequestBody Map<String, Object> params) {
        BigDecimal amount = new BigDecimal(params.get("amount").toString());
        return nftService.deposit(amount);
    }

    @GetMapping("/webase/balance")
    public Map<String,Object> getBalance() {
        return nftService.getBalance();
    }

    @GetMapping("/webase/nft/{tokenId}")
    public Map<String,Object> getNFTInfo(@PathVariable String tokenId) {
        return nftService.getNFTInfo(tokenId);
    }

    @GetMapping("/webase/owned")
    public Map<String,Object> getOwnedNFTs() {
        return nftService.getOwnedNFTs();
    }
} 