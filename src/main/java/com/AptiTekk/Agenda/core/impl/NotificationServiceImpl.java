/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.AptiTekk.Agenda.core.impl;

import com.AptiTekk.Agenda.core.MailingService;
import com.AptiTekk.Agenda.core.NotificationService;
import com.AptiTekk.Agenda.core.entity.Notification;
import com.AptiTekk.Agenda.core.entity.QNotification;
import com.AptiTekk.Agenda.core.entity.User;
import com.AptiTekk.Agenda.core.utilities.NotificationFactory;
import com.mysema.query.jpa.impl.JPAQuery;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;

/**
 *
 * @author kevint
 */
@Stateless
public class NotificationServiceImpl extends EntityServiceAbstract<Notification> implements NotificationService {
    
    QNotification table = QNotification.notification;
    
    @Inject
    MailingService mailingService;

    public NotificationServiceImpl() {
        super(Notification.class);
    }

    @Override
    public void sendEmailNotification(Notification n) 
            throws MessagingException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        mailingService.send(NotificationFactory.convert(n));
    }

    @Override
    public void markAsRead(Notification n) {
        n.setRead(Boolean.TRUE);
        merge(n);
    }

    @Override
    public List<Notification> getUnread(User user) {
        return new JPAQuery(entityManager).from(table).where(table.user.eq(user))
                .list(table);
    }

}
