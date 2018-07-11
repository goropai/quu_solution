package com.goropai.entities;

import java.util.Date;

public class Wait {
    private String service_id;
    private String question_type_id;
    private ResponseType responseType;
    private Date date;
    private Long waitingTimeInMinutes;

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getQuestion_type_id() {
        return question_type_id;
    }

    public void setQuestion_type_id(String question_type_id) {
        this.question_type_id = question_type_id;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getWaitingTimeInMinutes() {
        return waitingTimeInMinutes;
    }

    public void setWaitingTimeInMinutes(Long waitingTimeInMinutes) {
        this.waitingTimeInMinutes = waitingTimeInMinutes;
    }
}
