package moe.imtop1.imagehosting.system.controller;

import moe.imtop1.imagehosting.common.dto.AjaxResult;
import moe.imtop1.imagehosting.system.service.NFTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/nft")
public class NFTController {

    @Autowired
    private NFTService nftService;

    @GetMapping("/list")
    public AjaxResult getNFTList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String query) {
        return nftService.getNFTList(page, pageSize, query);
    }

    @GetMapping("/my")
    public AjaxResult getMyNFTs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return nftService.getMyNFTs(page, pageSize);
    }

    @PostMapping("/mint")
    public AjaxResult mintNFT(
            @RequestParam String imageId,
            @RequestParam String description,
            @RequestParam BigDecimal price,
            @RequestParam String minioUrl) {
        return nftService.mintNFT(imageId, description, price, minioUrl);
    }

    @PostMapping("/buy/{nftId}")
    public AjaxResult buyNFT(@PathVariable String nftId) {
        return nftService.buyNFT(nftId);
    }

    @PostMapping("/price/{nftId}")
    public AjaxResult setNFTPrice(
            @PathVariable String nftId,
            @RequestParam BigDecimal price) {
        return nftService.setNFTPrice(nftId, price);
    }

    @PostMapping("/cancel/{nftId}")
    public AjaxResult cancelNFTSale(@PathVariable String nftId) {
        return nftService.cancelNFTSale(nftId);
    }

    @GetMapping("/detail/{nftId}")
    public AjaxResult getNFTDetail(@PathVariable String nftId) {
        return nftService.getNFTDetail(nftId);
    }

    @GetMapping("/transactions")
    public AjaxResult getNFTTransactions() {
        return nftService.getNFTTransactions();
    }

    @GetMapping("/balance")
    public AjaxResult getBalance() {
        return nftService.getBalance();
    }

    @PostMapping("/deposit")
    public AjaxResult deposit(@RequestParam BigDecimal amount) {
        return nftService.deposit(amount);
    }

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