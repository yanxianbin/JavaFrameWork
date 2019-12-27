package com.config;

import com.annotations.SerialNumberAnnotation;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname CommonNumberConfig
 * @Description TODO
 * @Date 2019/12/25 14:28
 * @Created by 125937
 */
@Configuration
public class CommonNumberConfig {

    @SerialNumberAnnotation(numberCode = "common-code",numberDesc = "测试用的",numberFormat = "BM-{0}-{1}")
    public class CommonNumber{}
}
