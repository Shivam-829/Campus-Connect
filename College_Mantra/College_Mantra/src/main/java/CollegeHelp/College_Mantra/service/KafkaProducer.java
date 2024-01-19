package CollegeHelp.College_Mantra.service;

import org.apache.kafka.clients.admin.NewTopic;
import org.example.dto.MailMessage;
import org.example.dto.MessageMail_SMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Autowired
    @Qualifier("BOTH")
    private NewTopic both;

    @Autowired
    @Qualifier("MAIL")
    private NewTopic mail;

    @Autowired
    @Qualifier("mail_sms")
    private KafkaTemplate<String,MessageMail_SMS> kafkaTemplate1;

    @Autowired
    @Qualifier("sms")
    private KafkaTemplate<String, MailMessage> kafkaTemplate2;

    public void sendInBoth(MessageMail_SMS message){
        Message<MessageMail_SMS> message1 = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC,both.name())
                .build();

        kafkaTemplate1.send(message1);
    }

    public void sendInMail(MailMessage message){
        Message<MailMessage> message1 = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC,mail.name())
                .build();

        kafkaTemplate2.send(message1);
    }

}
