package org.example.dto;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageMail_SMS implements Serializable {

    private String number;
    private String mail;

    private String mailMessage;
    private String smsMessage;

}
