package com.lec.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")//添加前缀
public class AliOssProperties {
    //名称必须与配置文件中一一对应且正确
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}