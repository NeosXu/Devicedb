package org.DeviceM.swing.table;

import org.DeviceM.dao.*;
import org.DeviceM.mapper.*;
import org.DeviceM.util.Transaction;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class AdminTable {

    private Account currentAccount;

    public static List<Account> accountList;
    public static List<Device> deviceList;
    public static List<Issue> issueList;
    public static List<Lending> lendingList;
    public static List<Request> requestList;

    public AdminTable(Account account) {
        this.currentAccount = account;
        Transaction.start((SqlSession session) -> {
            AccountMapper accountMapper = session.getMapper(AccountMapper.class);
            DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
            IssueMapper issueMapper = session.getMapper(IssueMapper.class);
            LendingMapper lendingMapper = session.getMapper(LendingMapper.class);
            RequestMapper requestMapper = session.getMapper(RequestMapper.class);

            this.accountList = accountMapper.getAllAccount();
            this.deviceList = deviceMapper.getAllDevice();
            this.issueList = issueMapper.getAllIssue();
            if (account.id == 0) {
                this.lendingList = lendingMapper.getAllLending();
                this.requestList = requestMapper.getAllRequest();
            }
            else if (account.isTeacher) {
                this.lendingList = lendingMapper.getTeacherAllLendingById(account.id);
                this.requestList = requestMapper.getTeacherAllRequestById(account.id);
            }
            else {
                this.lendingList = lendingMapper.getStudentAllLendingById(account.id);
                this.requestList = requestMapper.getStudentAllRequestById(account.id);
            }

            return null;
        });
    }

    public static void updateAccountList() {
        Transaction.start((SqlSession session) -> {
            AccountMapper accountMapper = session.getMapper(AccountMapper.class);
            accountList.clear();
            accountList.addAll(accountMapper.getAllAccount());

            return null;
        });
    }

    public static void updateDeviceList() {
        Transaction.start((SqlSession session) -> {
            DeviceMapper deviceMapper = session.getMapper(DeviceMapper.class);
            deviceList.clear();
            deviceList.addAll(deviceMapper.getAllDevice());

            return null;
        });
    }

    public static void updateIssueList() {
        Transaction.start((SqlSession session) -> {
            IssueMapper issueMapper = session.getMapper(IssueMapper.class);
            issueList.clear();
            issueList.addAll(issueMapper.getAllIssue());

            return null;
        });
    }

    public static void updateLendingList() {
        Transaction.start((SqlSession session) -> {
            LendingMapper lendingMapper = session.getMapper(LendingMapper.class);
            lendingList.clear();
            lendingList.addAll(lendingMapper.getAllLending());

            return null;
        });
    }

    public static void updateStudentLendingList(Integer id) {
        Transaction.start((SqlSession session) -> {
            LendingMapper lendingMapper = session.getMapper(LendingMapper.class);
            lendingList.clear();
            lendingList.addAll(lendingMapper.getStudentAllLendingById(id));

            return null;
        });
    }

    public static void updateTeacherLendingList(Integer id) {
        Transaction.start((SqlSession session) -> {
            LendingMapper lendingMapper = session.getMapper(LendingMapper.class);
            lendingList.clear();
            lendingList.addAll(lendingMapper.getTeacherAllLendingById(id));

            return null;
        });
    }

    public static void updateRequestList() {
        Transaction.start((SqlSession session) -> {
            RequestMapper requestMapper = session.getMapper(RequestMapper.class);
            requestList.clear();
            requestList.addAll(requestMapper.getAllRequest());

            return null;
        });
    }

    public static void updateStudentRequestList(Integer id) {
        Transaction.start((SqlSession session) -> {
            RequestMapper requestMapper = session.getMapper(RequestMapper.class);
            requestList.clear();
            requestList.addAll(requestMapper.getStudentAllRequestById(id));

            return null;
        });
    }

    public static void updateTeacherRequestList(Integer id) {
        Transaction.start((SqlSession session) -> {
            RequestMapper requestMapper = session.getMapper(RequestMapper.class);
            requestList.clear();
            requestList.addAll(requestMapper.getTeacherAllRequestById(id));

            return null;
        });
    }

}
