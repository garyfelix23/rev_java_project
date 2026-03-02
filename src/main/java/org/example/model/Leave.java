package org.example.model;

import java.time.LocalDate;

public class Leave {
    private int id;
    private int user_id;
    private LocalDate start_date;
    private LocalDate end_date;
    private String reason;
    private String status;

    public Leave(){}

    public Leave(int id, int user_id, LocalDate start_date, LocalDate end_date, String reason, String status){
        this.id = id;
        this.user_id = user_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.reason = reason;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}
