package CollegeHelp.College_Mantra.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.dto.MailMessage;
import org.example.dto.MessageMail_SMS;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Properties;

@Configuration
public class KafkaProducerConfig {

    public Properties getProducerConfig(){
        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return producerConfig;
    }

    @Bean
    public ProducerFactory getProducerFactory(){
        return new DefaultKafkaProducerFactory(getProducerConfig());
    }

    @Bean("BOTH")
    public NewTopic topic1(){
        return TopicBuilder
                .name("MESSAGING-SERVICE")
                .build();
    }

    @Bean("MAIL")
    public NewTopic topic2(){
        return TopicBuilder
                .name("MAIL")
                .build();
    }

    @Bean("mail_sms")
    public KafkaTemplate getKafkaTemplate(){
        return new KafkaTemplate<String, MessageMail_SMS>(getProducerFactory());
    }

    @Bean("sms")
    public KafkaTemplate getKafkaTemplate2(){
        return new KafkaTemplate<String, MailMessage>(getProducerFactory());
    }

}
