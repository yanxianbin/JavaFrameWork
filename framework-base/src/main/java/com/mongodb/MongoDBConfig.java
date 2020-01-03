package com.mongodb;

import com.mongodb.convert.DateToTimestamp;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname MongoDBConfig
 * @Description TODO
 * @Date 2019/12/31 16:19
 * @Created by 125937
 */
@Configuration
public class MongoDBConfig {

    @Bean
    @ConditionalOnMissingBean({MappingMongoConverter.class})
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper((String) null));
        mappingConverter.setCustomConversions(customConversions());
        return mappingConverter;
    }

    @Bean
    public MongoCustomConversions customConversions(){
        List<Converter<?, ?>> converterList = new ArrayList<Converter<?, ?>>();
        converterList.add(new DateToTimestamp());
        return new MongoCustomConversions(converterList);
    }
}
