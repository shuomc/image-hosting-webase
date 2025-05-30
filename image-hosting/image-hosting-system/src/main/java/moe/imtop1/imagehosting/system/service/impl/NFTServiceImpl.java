package moe.imtop1.imagehosting.system.service.impl;

import lombok.extern.java.Log;
import moe.imtop1.imagehosting.common.dto.AjaxResult;
import moe.imtop1.imagehosting.system.service.NFTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import cn.dev33.satoken.stp.StpUtil;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class NFTServiceImpl implements NFTService {

    @Value("${blockchain.api-url}")
    private String blockchainApiUrl;

    @Autowired
    private RestTemplate restTemplate;
    @Override
    public AjaxResult getNFTList(Integer page, Integer pageSize, String query) {
        String url = blockchainApiUrl + "/nft/list?page=" + page + "&pageSize=" + pageSize;
        if (query != null) {
            url += "&query=" + query;
        }
        return forwardRequest(url, HttpMethod.GET, null);
    }

    @Override
    public AjaxResult getMyNFTs(Integer page, Integer pageSize) {
        String url = blockchainApiUrl + "/nft/my?page=" + page + "&pageSize=" + pageSize;
        return forwardRequest(url, HttpMethod.GET, null);
    }

    @Override
    public AjaxResult mintNFT(String imageId, String description, BigDecimal price, String minioUrl) {
        String url = blockchainApiUrl + "/nft/mint";

        Map<String, Object> body = Map.of(
            "imageId", imageId,
            "description", description,
            "price", price,
                "minioUrl", minioUrl
        );
        return forwardRequest(url, HttpMethod.POST, body);
    }

    @Override
    public AjaxResult buyNFT(String nftId) {
        String url = blockchainApiUrl + "/nft/buy/" + nftId;
        return forwardRequest(url, HttpMethod.POST, null);
    }

    @Override
    public AjaxResult setNFTPrice(String nftId, BigDecimal price) {
        String url = blockchainApiUrl + "/nft/price/" + nftId;
        Map<String, Object> body = Map.of("price", price);
        return forwardRequest(url, HttpMethod.POST, body);
    }

    @Override
    public AjaxResult cancelNFTSale(String nftId) {
        String url = blockchainApiUrl + "/nft/cancel/" + nftId;
        return forwardRequest(url, HttpMethod.POST, null);
    }

    @Override
    public AjaxResult getNFTDetail(String nftId) {
        String url = blockchainApiUrl + "/nft/detail/" + nftId;
        return forwardRequest(url, HttpMethod.GET, null);
    }

    @Override
    public AjaxResult getNFTTransactions() {
        String url = blockchainApiUrl + "/nft/transactions";
        return forwardRequest(url, HttpMethod.GET, null);
    }

    @Override
    public AjaxResult getBalance() {
        String url = blockchainApiUrl + "/nft/balance";
        return forwardRequest(url, HttpMethod.GET, null);
    }

    @Override
    public AjaxResult deposit(BigDecimal amount) {
        String url = blockchainApiUrl + "/nft/deposit";
        Map<String, Object> body = Map.of("amount", amount);
        return forwardRequest(url, HttpMethod.POST, body);
    }

    @Override
    public AjaxResult getWebaseBalance() {
        String url = blockchainApiUrl + "/nft/webase/balance";
        return forwardRequest(url, HttpMethod.GET, null);
    }

    @Override
    public AjaxResult webaseDeposit(BigDecimal amount) {
        String url = blockchainApiUrl + "/nft/webase/deposit";
        Map<String, Object> body = Map.of("amount", amount);
        return forwardRequest(url, HttpMethod.POST, body);
    }

    @Override
    public AjaxResult getWebaseNFTInfo(String tokenId) {
        String url = blockchainApiUrl + "/nft/webase/nft/" + tokenId;
        return forwardRequest(url, HttpMethod.GET, null);
    }

    @Override
    public AjaxResult getWebaseOwnedNFTs() {
        String url = blockchainApiUrl + "/nft/webase/owned";
        return forwardRequest(url, HttpMethod.GET, null);
    }

    private AjaxResult forwardRequest(String url, HttpMethod method, Object body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            String token = StpUtil.getTokenValue();
            headers.set("Authorization", token);

            HttpEntity<Object> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                method,
                entity,
                Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return AjaxResult.success(response.getBody());
            } else {
                return AjaxResult.error("请求区块链服务失败");
            }
        } catch (Exception e) {
            return AjaxResult.error("请求区块链服务异常：" + e.getMessage());
        }
    }
} 