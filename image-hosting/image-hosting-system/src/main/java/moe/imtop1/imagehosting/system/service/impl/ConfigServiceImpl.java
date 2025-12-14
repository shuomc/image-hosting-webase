package moe.imtop1.imagehosting.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import moe.imtop1.imagehosting.system.domain.Config;
import moe.imtop1.imagehosting.system.mapper.GlobalSettingsMapper;
import moe.imtop1.imagehosting.system.service.IConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements IConfigService {

    private final GlobalSettingsMapper configMapper;

    @Override
    public String getConfigValue(String key) {
        Config config = configMapper.selectOne(new LambdaQueryWrapper<Config>()
                .eq(Config::getConfigKey, key));
        return config != null ? config.getConfigValue() : null;
    }

    @Override
    public Map<String, String> getAllConfigs() {
        List<Config> configs = configMapper.selectList(null);
        Map<String, String> result = new HashMap<>();
        for (Config config : configs) {
            result.put(config.getConfigKey(), config.getConfigValue());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setConfigValue(String key, String value) {
        Config config = configMapper.selectOne(new LambdaQueryWrapper<Config>()
                .eq(Config::getConfigKey, key));
        
        if (config == null) {
            config = new Config();
            config.setConfigKey(key);
            config.setConfigValue(value);
            configMapper.insert(config);
        } else {
            config.setConfigValue(value);
            configMapper.updateById(config);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConfigs(Map<String, String> configs) {
        for (Map.Entry<String, String> entry : configs.entrySet()) {
            setConfigValue(entry.getKey(), entry.getValue());
        }
    }
}
