package org.DeviceM.dao;

public class Lending {
    public Integer recId;
    public Integer deviceId;
    public Integer personId;
    public Integer requestId;
    public Boolean returned;

    public Integer getRecId() {
        return recId;
    }

    public void setRecId(Integer recId) {
        this.recId = recId;
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

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Boolean getReturned() {
        return returned;
    }

    public void setReturned(Boolean returned) {
        this.returned = returned;
    }

    @Override
    public String toString() {
        return "Account{" +
                "recId=" + recId +
                ", deviceId=" + deviceId +
                ", personId=" + personId +
                ", requestId=" + requestId +
                ", returned=" + returned +
                '}';
    }
}
