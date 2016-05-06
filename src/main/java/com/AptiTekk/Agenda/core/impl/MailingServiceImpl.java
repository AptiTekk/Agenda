package com.AptiTekk.Agenda.core.impl;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.AptiTekk.Agenda.core.MailingService;
import com.AptiTekk.Agenda.core.utilities.notification.EmailNotification;
import javax.ejb.Stateless;

@Stateless
public class MailingServiceImpl implements MailingService {

  @Resource(name = "java:jboss/Resource/AgendaMail")
  private Session mailSession;

  @Override
  public boolean send(EmailNotification email) {
    try {

      Message message = new MimeMessage(mailSession);
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email.getTo().getEmail()));
      message.setSubject(email.getSubject());
      message.setContent(email.getContent());

      Transport.send(message);

      return true;
    } catch (MessagingException e) {
      e.printStackTrace();
      return false;
    }

  }

}
