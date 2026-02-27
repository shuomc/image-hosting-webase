package moe.imtop1.imagehosting.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import moe.imtop1.imagehosting.system.domain.UserStats;
import moe.imtop1.imagehosting.system.mapper.UserStatsMapper;
import moe.imtop1.imagehosting.system.service.IUserStatsService;
import moe.imtop1.imagehosting.system.vo.UserDashboardStatsVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import cn.dev33.satoken.stp.StpUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户统计服务实现类
 * @author shuomc
 */
@Service
@RequiredArgsConstructor
public class UserStatsServiceImpl extends ServiceImpl<UserStatsMapper, UserStats> implements IUserStatsService {

    private final UserStatsMapper userStatsMapper;
    private final RestTemplate restTemplate;

    @Value("${blockchain.api-url}")
    private String blockchainApiUrl;

    @Override
    public UserStats selectUserStatsById(String userId) {
        return userStatsMapper.selectById(userId);
    }

    @Override
    public void refreshAllUserStats() {
        userStatsMapper.refreshUserStats();
    }

    @Override
    public UserDashboardStatsVO getUserDashboardStats(String userId) {
        UserDashboardStatsVO vo = new UserDashboardStatsVO();

        // 1. User Stats from database
        vo.setUserStats(selectUserStatsById(userId));

        // 2. Image counts
        Long total = userStatsMapper.countTotalImagesByUserId(userId);
        Long minted = userStatsMapper.countMintedByUserId(userId);
        vo.setTotalImages(total);
        vo.setMintedCount(minted);
        vo.setUnmintedCount(Math.max(0, total - minted));
        
        vo.setPublicCount(userStatsMapper.countPublicByUserId(userId));
        vo.setPrivateCount(userStatsMapper.countPrivateByUserId(userId));

        // 3. NFT Balance & Transaction Count
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", StpUtil.getTokenValue());
            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            // Balance
            ResponseEntity<Map> balanceResponse = restTemplate.exchange(
                    blockchainApiUrl + "/nft/balance", HttpMethod.GET, entity, Map.class);
            if (balanceResponse.getStatusCode().is2xxSuccessful() && balanceResponse.getBody() != null) {
                Map<String, Object> body = balanceResponse.getBody();
                if (body.get("data") != null) {
                    vo.setWalletBalance(body.get("data").toString());
                }
            }

            // Transaction Count
            ResponseEntity<Map> transResponse = restTemplate.exchange(
                    blockchainApiUrl + "/nft/transactions/my?page=1&pageSize=1", HttpMethod.GET, entity, Map.class);
            if (transResponse.getStatusCode().is2xxSuccessful() && transResponse.getBody() != null) {
                Map<String, Object> body = transResponse.getBody();
                if (body.get("data") != null && body.get("data") instanceof Map) {
                    Map<String, Object> data = (Map<String, Object>) body.get("data");
                    if (data.containsKey("total")) {
                        vo.setTransactionCount(Long.parseLong(data.get("total").toString()));
                    }
                }
            }

            // Total NFTs Owned
            ResponseEntity<Map> nftsResponse = restTemplate.exchange(
                    blockchainApiUrl + "/nft/my?page=1&pageSize=1&mode=owned", HttpMethod.GET, entity, Map.class);
            if (nftsResponse.getStatusCode().is2xxSuccessful() && nftsResponse.getBody() != null) {
                Map<String, Object> body = nftsResponse.getBody();
                if (body.get("data") != null && body.get("data") instanceof Map) {
                    Map<String, Object> data = (Map<String, Object>) body.get("data");
                    if (data.containsKey("total")) {
                        vo.setTotalNfts(Long.parseLong(data.get("total").toString()));
                    }
                }
            }
        } catch (Exception e) {
            vo.setWalletBalance("0");
            vo.setTransactionCount(0L);
            vo.setTotalNfts(0L);
        }

        // 4. Transaction Trends
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", StpUtil.getTokenValue());
            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            ResponseEntity<Map> trendResponse = restTemplate.exchange(
                    blockchainApiUrl + "/nft/transactions/stats?type=day", HttpMethod.GET, entity, Map.class);
            if (trendResponse.getStatusCode().is2xxSuccessful() && trendResponse.getBody() != null) {
                Map<String, Object> body = trendResponse.getBody();
                if (body.get("data") != null && body.get("data") instanceof List) {
                    List<Map<String, Object>> statsList = (List<Map<String, Object>>) body.get("data");
                    List<String> dates = new ArrayList<>();
                    List<BigDecimal> amounts = new ArrayList<>();
                    for (Map<String, Object> stat : statsList) {
                        dates.add(stat.get("date").toString());
                        amounts.add(new BigDecimal(stat.get("volume").toString()));
                    }
                    vo.setTransactionDates(dates);
                    vo.setTransactionAmounts(amounts);
                }
            }
        } catch (Exception e) {
            vo.setTransactionDates(new ArrayList<>());
            vo.setTransactionAmounts(new ArrayList<>());
        }

        return vo;
    }
}
