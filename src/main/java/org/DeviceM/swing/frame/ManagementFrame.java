package org.DeviceM.swing.frame;

import org.DeviceM.App;
import org.DeviceM.dao.Account;
import org.DeviceM.mapper.AccountMapper;
import org.DeviceM.swing.panel.*;
import org.DeviceM.swing.table.AdminTable;
import org.DeviceM.util.Transaction;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.IOException;

public class ManagementFrame extends JFrame {
    Account currentAccount;

    AccountPanel accountPanel;
    DevicePanel devicePanel;
    IssuePanel issuePanel;
    LendingPanel lendingPanel;
    RequestPanel requestPanel;

    public ManagementFrame(Account account) {
        super();
        this.currentAccount = account;
        if (isAdmin()) {
            this.accountPanel = new AdminAccountPanel(currentAccount);
        }
        else {
            this.accountPanel = new StudentAccountPanel(currentAccount);
        }
        this.devicePanel = new DevicePanel(currentAccount);
        this.issuePanel = new IssuePanel(currentAccount);
        this.lendingPanel = new LendingPanel(currentAccount);
        this.requestPanel = new RequestPanel(currentAccount);

        makeFrame();
    }

    private void makeFrame() {
        setTitle(String.format("User: %s", this.currentAccount.name));
        setBounds(100, 100, 1200, 720);

        JTabbedPane jTabbedPane = new JTabbedPane();
        jTabbedPane.add(this.accountPanel);
        jTabbedPane.add(this.devicePanel);
        jTabbedPane.add(this.issuePanel);
        jTabbedPane.add(this.lendingPanel);
        jTabbedPane.add(this.requestPanel);

        jTabbedPane.setTitleAt(0, "Account");
        jTabbedPane.setTitleAt(1, "Device");
        jTabbedPane.setTitleAt(2, "Issue");
        jTabbedPane.setTitleAt(3, "Lending");
        jTabbedPane.setTitleAt(4, "Request");

        jTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        int index = jTabbedPane.getSelectedIndex();
                        switch (index) {
                            case 0:
//                                AdminTable.updateAccountList();
//                                accountPanel.updateUI();
                                break;
                            case 1:
                                AdminTable.updateDeviceList();
                                devicePanel.updateTable();
                                break;
                            case 2:
                                AdminTable.updateIssueList();
                                issuePanel.updateTable();
                                break;
                            case 3:
                                if (isAdmin()) {
                                    AdminTable.updateLendingList();
                                }
                                else if (isTeacher()) {
                                    AdminTable.updateTeacherLendingList(currentAccount.id);
                                }
                                else {
                                    AdminTable.updateStudentLendingList(currentAccount.id);
                                }
                                lendingPanel.updateTable();
                                break;
                            case 4:
                                if (isAdmin()) {
                                    AdminTable.updateRequestList();
                                }
                                else if (isTeacher()) {
                                    AdminTable.updateTeacherRequestList(currentAccount.id);
                                }
                                else {
                                    AdminTable.updateStudentRequestList(currentAccount.id);
                                }
                                requestPanel.updateTable();
                                break;
                            default: break;
                        }
                    return null;
                    }
                };
                worker.execute();
            }
        });

        add(jTabbedPane);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private boolean isTeacher() {
        return this.currentAccount.isTeacher;
    }

    private boolean isAdmin() {
        return this.currentAccount.id == 0;
    }

}
