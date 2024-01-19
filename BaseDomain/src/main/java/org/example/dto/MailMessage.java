package org.example.dto;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailMessage implements Serializable {

    private String mail;
    private String mailMessage;

}
