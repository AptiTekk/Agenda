/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.AptiTekk.Agenda.core;

import com.AptiTekk.Agenda.core.entity.AppProperty;
import com.AptiTekk.Agenda.core.entity.Notification;
import com.AptiTekk.Agenda.core.entity.User;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.mail.MessagingException;

/**
 *
 * @author kevint
 */
public interface NotificationService extends EntityService<Notification> {
    
    public static final AppProperty NOTIFICATION_DATEFORMAT = new AppProperty("agenda.notification.dateFormat", "MM-dd-yyyy hh:mm");
    
    void sendEmailNotification(Notification n) 
            throws MessagingException, NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException;
    
    void markAsRead(Notification n);
    
    List<Notification> getUnread(User user);
    
}
