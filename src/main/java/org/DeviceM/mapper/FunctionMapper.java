package org.DeviceM.mapper;

import org.DeviceM.dao.Account;
import org.DeviceM.dao.Device;
import org.DeviceM.dao.Issue;
import org.DeviceM.dao.Request;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.sql.Timestamp;

// map into functions stored in postgresql database
public interface FunctionMapper {
    // 1
    public Integer addDevice(Device device);

    // 2
    public void resetAdmin(@Param("deviceId") Integer deviceId, @Param("adminId") Integer adminId);

    // 3
    public void deleteDevice(Integer deviceId);

    // 4
    public void setDeviceDamaged(Integer deviceId);

    // 5
    public Integer newIssue(Issue issue);

    // 6
    public void evaluateIssue(@Param("issueId") Integer issueId, @Param("days") Integer days);

    // 7/8
    public void handleIssue(@Param("issueId") Integer issueId, @Param("repaired") Boolean repaired);

    // 9
    public void deleteIssue(Integer issueId);

    // 10
    public void clearHandledIssue(Timestamp time);

    // 11
    public Integer newRequest(Request request);

    // 12
    public void rejectRequest(Integer requestId);

    // 13
    public boolean approveRequest(@Param("requestId") Integer requestId, @Param("operatorId") Integer operatorId);

    // 14
    public void returnDevice(Integer deviceId);

    // 15
    public void clearOutdatedRequests(Date date);

    // 16
    // 返回一个创建成功后的id比较好
    public Integer createAccount(Account account);

    // 17
    public void deleteAccount(Integer id);

    // 18
    public void changePassword(@Param("id") Integer id, @Param("password") String password);
}
