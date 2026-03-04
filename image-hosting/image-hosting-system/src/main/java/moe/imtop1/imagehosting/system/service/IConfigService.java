package moe.imtop1.imagehosting.system.service;

import moe.imtop1.imagehosting.system.domain.Config;
import java.util.Map;

public interface IConfigService {
    /**
     * Get config value by key
     */
    String getConfigValue(String key);

    /**
     * Get all configs as a map
     */
    Map<String, String> getAllConfigs();

    /**
     * Set config value
     */
    void setConfigValue(String key, String value);

    /**
     * Batch update configs
     */
    void updateConfigs(Map<String, String> configs);
}
