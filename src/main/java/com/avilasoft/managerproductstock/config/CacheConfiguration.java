package com.avilasoft.managerproductstock.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.avilasoft.managerproductstock.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.avilasoft.managerproductstock.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.avilasoft.managerproductstock.domain.User.class.getName());
            createCache(cm, com.avilasoft.managerproductstock.domain.Authority.class.getName());
            createCache(cm, com.avilasoft.managerproductstock.domain.User.class.getName() + ".authorities");
            createCache(cm, com.avilasoft.managerproductstock.domain.BusinessAssociate.class.getName());
            createCache(cm, com.avilasoft.managerproductstock.domain.BusinessAssociate.class.getName() + ".providers");
            createCache(cm, com.avilasoft.managerproductstock.domain.BusinessAssociate.class.getName() + ".projects");
            createCache(cm, com.avilasoft.managerproductstock.domain.Unit.class.getName());
            createCache(cm, com.avilasoft.managerproductstock.domain.Unit.class.getName() + ".projectItemListItems");
            createCache(cm, com.avilasoft.managerproductstock.domain.Unit.class.getName() + ".providerItems");
            createCache(cm, com.avilasoft.managerproductstock.domain.Project.class.getName());
            createCache(cm, com.avilasoft.managerproductstock.domain.ProjectGroup.class.getName());
            createCache(cm, com.avilasoft.managerproductstock.domain.ProjectItemList.class.getName());
            createCache(cm, com.avilasoft.managerproductstock.domain.ProjectItemListItem.class.getName());
            createCache(cm, com.avilasoft.managerproductstock.domain.Item.class.getName());
            createCache(cm, com.avilasoft.managerproductstock.domain.Item.class.getName() + ".providerItems");
            createCache(cm, com.avilasoft.managerproductstock.domain.Provider.class.getName());
            createCache(cm, com.avilasoft.managerproductstock.domain.ProviderItem.class.getName());
            createCache(cm, com.avilasoft.managerproductstock.domain.UnitPriceList.class.getName());
            createCache(cm, com.avilasoft.managerproductstock.domain.UnitPriceList.class.getName() + ".unitPriceListItems");
            createCache(cm, com.avilasoft.managerproductstock.domain.UnitPriceList.class.getName() + ".computedPriceListItems");
            createCache(cm, com.avilasoft.managerproductstock.domain.UnitPriceListItem.class.getName());
            createCache(cm, com.avilasoft.managerproductstock.domain.ComputedPriceList.class.getName());
            createCache(cm, com.avilasoft.managerproductstock.domain.ComputedPriceList.class.getName() + ".computedPriceListItems");
            createCache(cm, com.avilasoft.managerproductstock.domain.ComputedPriceListItem.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
