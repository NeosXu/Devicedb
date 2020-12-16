package org.DeviceM.dao;

import java.util.Date;

public class Request {
    public Integer requestId;
    public Integer deviceId;
    public Integer personId;
    public String status;
    public Date requestDate;
    public Integer period;
    public String reason;

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestId=" + requestId +
                ", deviceId=" + deviceId +
                ", personId=" + personId +
                ", status='" + status + '\'' +
                ", requestDate=" + requestDate +
                ", period=" + period +
                ", reason='" + reason + '\'' +
                '}';
    }
}
