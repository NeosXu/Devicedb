package org.DeviceM.dao;

import java.io.Serializable;
import java.sql.Timestamp;

public class Issue implements Serializable {
    public Integer issueId;
    public Integer deviceId;
    public Timestamp time;
    public String reason;
    public Integer expectedDays;
    public String status;

    public Integer getIssueId() {
        return issueId;
    }

    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getExpectedDays() {
        return expectedDays;
    }

    public void setExpectedDays(Integer expectedDays) {
        this.expectedDays = expectedDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "issueId=" + issueId +
                ", deviceId=" + deviceId +
                ", time=" + time +
                ", reason='" + reason + '\'' +
                ", expectedDays=" + expectedDays +
                ", status='" + status + '\'' +
                '}';
    }
}
