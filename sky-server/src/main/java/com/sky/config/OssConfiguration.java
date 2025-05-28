package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: 孙毅
 * @Date: 2025/5/26 20:40
 * @Description:
 * 配置类，用于创建AliOssUtil对象
 */
@Configuration// Spring 会扫描其中的 @Bean 方法，并注册到容器中;不加的话会有单例问题
@Slf4j
public class OssConfiguration {

    // AliOssProperties对象存储了阿里云 OSS 的配置信息（如 endpoint、accessKeyId 等）
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
        log.info("开始创建阿里云上传工具类对象：{}",aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}
