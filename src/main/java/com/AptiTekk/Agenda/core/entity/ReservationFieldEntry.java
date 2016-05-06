package com.AptiTekk.Agenda.core.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Entity implementation class for Entity: ReservationFieldEntry
 *
 */
@Entity

public class ReservationFieldEntry implements Serializable {

  @Id
  @GeneratedValue
  private int id;
  @ManyToOne
  private Reservation reservation;
  @ManyToOne
  private ReservationField field;
  private String content;
  private static final long serialVersionUID = 1L;

  public ReservationFieldEntry() {
    super();
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Reservation getReservation() {
    return this.reservation;
  }

  public void setReservation(Reservation reservation) {
    this.reservation = reservation;
  }

  public ReservationField getField() {
    return this.field;
  }

  public void setField(ReservationField field) {
    this.field = field;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

}
