package org.DeviceM.swing.panel;

import org.DeviceM.App;
import org.DeviceM.dao.Account;
import org.DeviceM.mapper.AccountMapper;
import org.DeviceM.mapper.FunctionMapper;
import org.DeviceM.swing.frame.LoginFrame;
import org.DeviceM.swing.frame.ManagementFrame;
import org.DeviceM.swing.table.AdminTable;
import org.DeviceM.util.Transaction;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AccountPanel extends JPanel {

    protected ManagementFrame parentManagementFrame;
    protected Account currentAccount;

    public AccountPanel(Account account, ManagementFrame parentManagementFrame) {
        super();
        this.currentAccount = account;
        this.parentManagementFrame = parentManagementFrame;
    }

    protected boolean changePassWord(Account a, String passWord) {
        try {
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    Transaction.start((SqlSession session) -> {
                        FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                        mapper.changePassword(a.id, passWord);
                        return null;
                    });
                    return null;
                }
            };
            worker.execute();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected JButton makeLogoutButton() {
        JButton logoutButton = new JButton("退出登陆");

        logoutButton.addActionListener(e -> {
            parentManagementFrame.dispose();
            new LoginFrame();
        });

        return logoutButton;
    }
}
