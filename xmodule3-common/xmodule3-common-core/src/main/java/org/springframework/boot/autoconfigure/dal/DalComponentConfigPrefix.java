package org.springframework.boot.autoconfigure.dal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DAL组件配置前缀
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/12/8 11:30
 */
public enum DalComponentConfigPrefix {

    DATASOURCE_CONFIG_PREFIX("spring.datasource", Arrays.asList("url", "username", "password")),

    MYBATIS_CONFIG_PREFIX("spring.mybatis", Arrays.asList("configLocation|config-location", "mapperLocations|mapper-locations"));

    private final String configPrefix;

    private final Set<String> requiredProperties;

    DalComponentConfigPrefix(String configPrefix, List<String> requiredProperties) {
        this.configPrefix = configPrefix;
        this.requiredProperties = new HashSet<>(requiredProperties);
    }

    public String getConfigPrefix() {
        return configPrefix;
    }

    public Set<String> getRequiredProperties() {
        return requiredProperties;
    }

    public static DalComponentConfigPrefix configPrefixOf(String configPrefix) {
        for(DalComponentConfigPrefix em : values()) {
            if(em.getConfigPrefix().equals(configPrefix)) {
                return em;
            }
        }
        return null;
    }

}
