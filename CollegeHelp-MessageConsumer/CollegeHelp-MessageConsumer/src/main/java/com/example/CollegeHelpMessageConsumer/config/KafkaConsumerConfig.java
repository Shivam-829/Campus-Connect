package com.example.CollegeHelpMessageConsumer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Properties;

@Configuration
public class KafkaConsumerConfig {

    public Properties getConsumerConfig(){
        Properties consumerConfig = new Properties();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG,"MY-GROUP");
        consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerConfig.put(JsonDeserializer.TRUSTED_PACKAGES,"*");

        return consumerConfig;
    }

    @Bean
    public ConsumerFactory getConsumerFactory(){
        return new DefaultKafkaConsumerFactory(getConsumerConfig());
    }

}
