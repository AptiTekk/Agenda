package com.AptiTekk.Agenda.web.controllers;

import com.AptiTekk.Agenda.core.ReservableTypeService;
import com.AptiTekk.Agenda.core.ReservationService;
import com.AptiTekk.Agenda.core.entity.Reservable;
import com.AptiTekk.Agenda.core.entity.ReservableType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.AptiTekk.Agenda.core.utilities.AgendaLogger;
import com.AptiTekk.Agenda.core.utilities.TimeRange;
import java.text.DateFormat;
import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class TimeSelectionController {

    private Date date = Calendar.getInstance().getTime();

    private List<String> startTimes;
    private String startTime;

    private List<String> endTimes;
    private String endTime;

    private TimeRange allowedTimes;

    private final DateFormat timeFormat = new SimpleDateFormat("h:mm a");
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");

    private List<ReservableType> reservableTypes;
    private ReservableType reservableType;
    
    private List<Reservable> results;

    @Inject
    private ReservationService reservationService;

    @Inject
    private ReservableTypeService reservableTypeService;

    @PostConstruct
    public void init() {
        // ---- Temporary code to generate an allowed time TimeRange. Should
        // ideally come from a settings page somewhere. ----//
        Calendar startTime = Calendar.getInstance();
        startTime.set(0, 0, 0, 6, 30);

        Calendar endTime = Calendar.getInstance();
        endTime.set(0, 0, 0, 20, 30);

        allowedTimes = new TimeRange(startTime, endTime);

        // ---- End Temporary Code ----//
        calculateStartTimes();

        reservableTypes = reservableTypeService.getAll();
    }

    /**
     * Generates a list of times that can be selected for the "Start Time". This
     * list will range from the minimum time allowed to 30 minutes before the
     * maximum time allowed (To allow for the end time to be at least 30 minutes
     * away from the selected time)
     */
    private void calculateStartTimes() {
        startTimes = new ArrayList<>();

        Calendar counterCalendar = (Calendar) allowedTimes.getStartTime().clone();

        while ((double) (counterCalendar.get(Calendar.HOUR_OF_DAY)
                + (counterCalendar.get(Calendar.MINUTE) / 60d)) != (double) (allowedTimes.getEndTime()
                .get(Calendar.HOUR_OF_DAY) + (allowedTimes.getEndTime().get(Calendar.MINUTE) / 60d))) {
            String value = timeFormat.format(counterCalendar.getTime());
            startTimes.add(value);

            counterCalendar.add(Calendar.MINUTE, 30);
        }

    }

    /**
     * Generates a list of times that can be selected for the "End Time" based
     * on the currently selected time.
     */
    private void calculateEndTimes() {
        endTimes = new ArrayList<>();

        try {
            Date parsedDate = timeFormat.parse(startTime);
            Calendar minCalendar = Calendar.getInstance();
            minCalendar.setTime(parsedDate);

            Calendar counterCalendar = (Calendar) minCalendar.clone();
            counterCalendar.add(Calendar.MINUTE, 30);

            while ((double) (counterCalendar.get(Calendar.HOUR_OF_DAY)
                    + (counterCalendar.get(Calendar.MINUTE) / 60d)) != (double) (allowedTimes.getEndTime()
                    .get(Calendar.HOUR_OF_DAY) + (allowedTimes.getEndTime().get(Calendar.MINUTE) / 60d))
                    + 0.5) {
                String value = timeFormat.format(counterCalendar.getTime());
                endTimes.add(value);

                counterCalendar.add(Calendar.MINUTE, 30);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            endTimes.add("--- Internal Server Error ---");
        }
    }

    public void searchForReservable() {
        AgendaLogger.logVerbose("Searching for Reservable");

        try {
        DateFormat datetimeFormat = new SimpleDateFormat("dd/MM/YYYY h:mm a");
        Date startDateTime = datetimeFormat.parse(dateFormat.format(date) + " " + startTime);
        Date endDateTime = datetimeFormat.parse(dateFormat.format(date) + " " + endTime);

        this.results = reservationService.findAvailableReservables(reservableType, startDateTime, endDateTime);
        AgendaLogger.logVerbose(results.toString());
        } catch(ParseException e) {
            e.printStackTrace();
            this.results = null;
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        if (date != null) {
            AgendaLogger.logVerbose("Date Selected: " + dateFormat.format(date));
        } else {
            AgendaLogger.logVerbose("Date Empty - Setting to today");
            date = Calendar.getInstance().getTime();
        }
        this.date = date;
    }

    public List<String> getStartTimes() {
        return startTimes;
    }

    public void setStartTimes(List<String> startTimes) {
        this.startTimes = startTimes;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        AgendaLogger.logVerbose("Start Time Selected: " + startTime);
        this.startTime = startTime;

        if (startTime != null) {
            calculateEndTimes();
        } else {
            endTimes = null;
        }
    }

    public List<String> getEndTimes() {
        return endTimes;
    }

    public void setEndTimes(List<String> endTimes) {
        this.endTimes = endTimes;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        AgendaLogger.logVerbose("End Time Selected: " + endTime);
        this.endTime = endTime;
    }

    public List<ReservableType> getReservableTypes() {
        return reservableTypes;
    }

    public void setReservableTypes(List<ReservableType> reservableTypes) {
        this.reservableTypes = reservableTypes;
    }

    public ReservableType getReservableType() {
        return reservableType;
    }

    public void setReservableType(ReservableType reservableType) {
        AgendaLogger.logVerbose("Reservable Type Selected: " + reservableType.getName());
        this.reservableType = reservableType;
    }
    
    public List<Reservable> getResults()
    {
        return this.results;
    }

}
