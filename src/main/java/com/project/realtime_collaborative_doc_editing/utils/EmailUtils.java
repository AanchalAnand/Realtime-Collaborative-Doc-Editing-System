package com.project.realtime_collaborative_doc_editing.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class EmailUtils {

  private final JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String sentFrom;


  public void sendMail(String toEmail, String subject, String body){

    //String sentFrom = "rsanvu001@gmail.com";
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(sentFrom);
    mailMessage.setTo(toEmail);
    mailMessage.setSubject(subject);
    mailMessage.setSentDate(new Date());
    mailMessage.setText(body);

    javaMailSender.send(mailMessage);
  }

}