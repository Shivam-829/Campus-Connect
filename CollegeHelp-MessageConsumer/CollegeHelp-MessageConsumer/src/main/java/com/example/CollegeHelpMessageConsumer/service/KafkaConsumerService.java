package com.example.CollegeHelpMessageConsumer.service;

import org.example.dto.MailMessage;
import org.example.dto.MessageMail_SMS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.messaging.simp.SimpAttributes;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${gmail}")
    private String gmail;

    @KafkaListener(topics = "MESSAGING-SERVICE",groupId = "MY-GROUP")
    public void consume1(MessageMail_SMS message){
        LOGGER.info("{} : {}",message.getMail(),message.getMailMessage());
        LOGGER.info("{} : {}",message.getNumber(),message.getSmsMessage());

        SimpleMailMessage message1 = new SimpleMailMessage();
        message1.setSubject("Empty");
        message1.setText("Mail : " + message.getMailMessage() + "SMS : " + message.getSmsMessage());
        message1.setFrom(gmail);
        message1.setTo(message.getMail());

        mailSender.send(message1);
    }

    @KafkaListener(topics = "MAIL",groupId = "MY-GROUP")
    public void consume2(MailMessage message){
        LOGGER.info("{} : {}",message.getMail(),message.getMailMessage());

        SimpleMailMessage message1 = new SimpleMailMessage();
        message1.setSubject("Empty");
        message1.setText("Mail : " + message.getMailMessage());
        message1.setFrom(gmail);
        message1.setTo(message.getMail());

        mailSender.send(message1);
    }

}
