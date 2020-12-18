package org.DeviceM;

import static org.junit.Assert.assertTrue;

import org.DeviceM.dao.*;
import org.DeviceM.mapper.*;
import org.DeviceM.util.Transaction;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Before
    public void initTest() throws IOException {
        App.connectDatabase();
        System.out.println("Database connected.");
    }

    @Test
    public void createAccount() {
        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            Account account = new Account();
            account.name = "汪武";
            account.password = "123";
            account.isTeacher = true;
            Integer id = mapper.createAccount(account);
            System.out.println(id);
            return id;
        });
    }

    /**
     * 如果账户当前有借书或者各种记录则不能够删除账户！这个情况比较严重，需要报错
     */
    @Test
    public void deleteAccount() {
        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            mapper.deleteAccount(2);
            return null;
        });
    }

    @Test
    public void changePassword() {
        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            mapper.changePassword(12, "123");
            return null;
        });
    }

    @Test
    public void addDevice() {
        Device device = new Device();
        device.adminId = 1;
        device.transactorId = 1;
        device.type = "键盘";
        device.name = "k122键盘";
        device.price = 149.0;
        device.purchaseTime = Timestamp.valueOf("2014-02-14 14:00:00");
        device.warrantyUntil = Timestamp.valueOf("2017-2-14 14:00:00");
        device.producer = "Star Company";
        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            Integer id = mapper.addDevice(device);
//            // 加了两部设备
//            device.adminId = 2;
//            device.transactorId = 1;
//            mapper.addDevice(device);
            System.out.println(id);
            return id;
        });
    }

    @Test
    public void resetAdmin() {
        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            mapper.resetAdmin(3, 12);
            return null;
        });
    }

    @Test
    public void deleteDevice() {
        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            mapper.deleteDevice(5);
            return null;
        });
    }

    @Test
    public void setDeviceDamaged() {
        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            mapper.setDeviceDamaged(2);
            return null;
        });
    }

    @Test
    public void newIssue() {
        Issue issue = new Issue();
        issue.deviceId = 2;
        issue.time = Timestamp.valueOf(LocalDateTime.now());
        issue.reason = "设备老旧，打不出来字了";

        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            Integer id = mapper.newIssue(issue);
            System.out.println(id);
            return id;
        });
    }

    @Test
    public void evaluateIssue() {
        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            mapper.evaluateIssue(1, 5);
            return null;
        });
    }

    @Test
    public void handleIssue() {
        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            mapper.handleIssue(1, true);
            return null;
        });
    }

    @Test
    public void deleteIssue() {
        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            mapper.deleteIssue(1);
            return null;
        });
    }

    @Test
    public void clearHandledIssue() {
        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            mapper.clearHandledIssue(Timestamp.valueOf("2021-04-10 12:12:00"));
            return null;
        });
    }

    @Test
    public void newRequest() {
        Request request = new Request();
        request.deviceId = 2;
        request.period = 30;
        request.personId = 2;
        request.reason = "想要白嫖";
        request.requestDate = Date.valueOf(LocalDate.now());

        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            mapper.newRequest(request);
            return null;
        });
    }

    // 不是很明白为什么拒绝就不需要管理员，允许就需要管理员符合……
    @Test
    public void rejectRequest() {
        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            mapper.rejectRequest(2);
            return null;
        });
    }

    @Test
    public void approveRequest() {
        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            mapper.approveRequest(3, 2);
            return null;
        });
    }

    @Test
    public void returnDevice() {
        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            mapper.returnDevice(2);
            return null;
        });
    }

    @Test
    public void clearOutdatedRequests() {
        Date date = Date.valueOf(LocalDate.now().plusDays(1));
        Transaction.start((SqlSession session) -> {
            FunctionMapper mapper = session.getMapper(FunctionMapper.class);
            mapper.clearOutdatedRequests(date);
            return null;
        });
    }

    @Test
    public void testGetAllDevices() {
        Transaction.start((SqlSession session) -> {
            DeviceMapper mapper = session.getMapper(DeviceMapper.class);
            List<Device> list = mapper.getAllDevice();
            System.out.println(list);
            return null;
        });
    }

    @Test
    public void testGetAllAccount() {
        Transaction.start((SqlSession session) -> {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            List<Account> list = mapper.getAllAccount();
            System.out.println(list);
            return null;
        });
    }

    @Test
    public void testGetPassWordById() {
        Transaction.start((SqlSession session) -> {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            String passWord = mapper.getPassWordById(1);
            System.out.println(passWord);
            return null;
        });
    }

    @Test
    public void testGetAllIssue() {
        Transaction.start((SqlSession session) -> {
            IssueMapper mapper = session.getMapper(IssueMapper.class);
            List<Issue> list = mapper.getAllIssue();
            System.out.println(list);
            return null;
        });
    }

    @Test
    public void testGetAllLending() {
        Transaction.start((SqlSession session) -> {
            LendingMapper mapper = session.getMapper(LendingMapper.class);
            List<Lending> list = mapper.getAllLending();
            System.out.println(list);
            return null;
        });
    }

    @Test
    public void testGetAllRequest() {
        Transaction.start((SqlSession session) -> {
            RequestMapper mapper = session.getMapper(RequestMapper.class);
            List<Request> list = mapper.getAllRequest();
            System.out.println(list);
            return null;
        });
    }

    @Test
    public void testGetAccountById() {
        Transaction.start((SqlSession session) -> {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            Account account = mapper.getAccountById(1);
            System.out.println(account);
            return null;
        });
    }
}
