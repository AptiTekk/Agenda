/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aptitekk.agenda.core.entity;

import com.aptitekk.agenda.core.utilities.EqualsHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author kevint
 */
@Entity
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private String subject;

    @Column(length = 2048)
    private String body;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creation = new Date();

    private Boolean notif_read = false;

    public Notification() {
        super();
    }

    public Notification(User user, String subject, String body) {
        setUser(user);
        setSubject(subject);
        setBody(body);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public Boolean getRead() {
        return notif_read;
    }

    public void setRead(Boolean notif_read) {
        this.notif_read = notif_read;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null) return false;

        if (!(o instanceof Notification)) return false;

        Notification other = (Notification) o;

        return EqualsHelper.areEquals(getSubject(), other.getSubject())
                && EqualsHelper.areEquals(getBody(), other.getBody())
                && EqualsHelper.areEquals(getCreation(), other.getCreation())
                && EqualsHelper.areEquals(getRead(), other.getRead());
    }

    @Override
    public int hashCode() {
        return EqualsHelper.calculateHashCode(getSubject(), getBody(), getCreation(), getRead());
    }

    @Override
    public String toString() {
        return "com.AptiTekk.Agenda.core.entity.Notifications[ id=" + id + " ]";
    }

}
