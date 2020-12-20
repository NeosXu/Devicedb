package org.DeviceM.swing.panel;

import org.DeviceM.dao.Account;
import org.DeviceM.mapper.FunctionMapper;
import org.DeviceM.swing.frame.ManagementFrame;
import org.DeviceM.swing.tableModel.AccountTableModel;
import org.DeviceM.util.Transaction;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminAccountPanel extends AccountPanel {

    AccountTableModel accountTableModel;
    private JTable table;
    private JPanel p;

    public AdminAccountPanel(Account account, ManagementFrame managementFrame) {
        super(account, managementFrame);
        accountTableModel = new AccountTableModel();
        table = new JTable(accountTableModel);
        JScrollPane sp = new JScrollPane(table);
        sp.setMaximumSize(new Dimension(1150, 600));
        sp.setPreferredSize(new Dimension(1150, 600));
        JButton registerButton = new JButton("注册");
        JButton deleteButton = new JButton("删除");
        JButton changePassWordButton = new JButton("更改密码");
        JButton logoutButton = makeLogoutButton();
        p = new JPanel();

        Box bp = Box.createHorizontalBox();
        p.add(bp);
        bp.add(Box.createHorizontalStrut(50));
        bp.add(registerButton);
        bp.add(Box.createHorizontalGlue());
        bp.add(deleteButton);
        bp.add(Box.createHorizontalStrut(50));
        bp.add(changePassWordButton);
        bp.add(Box.createHorizontalGlue());
        bp.add(logoutButton);
        bp.add(Box.createHorizontalStrut(50));

        Box b = Box.createVerticalBox();
        add(b);
        b.add(sp);
        b.add(Box.createVerticalGlue());
        b.add(p);
        b.add(Box.createVerticalStrut(50));

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(b, "确认删除？");
                if (JOptionPane.OK_OPTION == option) {
                    doDeleteAccount(e);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog d = new JDialog();
                d.setModal(true);
                d.setTitle("注册账户");
                d.setBounds(300, 250, 400, 300);
                Box b = Box.createVerticalBox();
                d.add(b);
                b.add(Box.createVerticalStrut(50));

                JPanel textPanel = new JPanel();
                JLabel deviceManagementLabel = new JLabel("注册账号");
                deviceManagementLabel.setFont(new java.awt.Font("黑体", 1, 24));
                deviceManagementLabel.setForeground(Color.red);
                textPanel.add(deviceManagementLabel);
                b.add(textPanel);
                b.add(Box.createVerticalGlue());

                // 账号面板
                JPanel accountPanel = new JPanel();
                // 账号标签和文本框
                JLabel accountLabel = new JLabel("账号：");
                JTextField accountText = new JTextField();
                accountText.setPreferredSize(new Dimension(160, 30));

                accountPanel.add(accountLabel);
                accountPanel.add(accountText);

                //密码面板
                JPanel passwdPanel = new JPanel();
                // 密码标签和文本框
                JLabel passwdLabel = new JLabel("密码：");
                JPasswordField passwdText = new JPasswordField();
                passwdText.setPreferredSize(new Dimension(160, 30));

                passwdPanel.add(passwdLabel);
                passwdPanel.add(passwdText);

                b.add(accountPanel);
                b.add(passwdPanel);
                b.add(Box.createVerticalStrut(10));

                JPanel jrbPanel = new JPanel();
                JRadioButton isATeacher = new JRadioButton("老师");
                jrbPanel.add(isATeacher);
                b.add(jrbPanel);
                b.add(Box.createVerticalStrut(10));

                JPanel buttonPanel = new JPanel();
                JButton registerButton1 = new JButton("注册");
                buttonPanel.add(registerButton1);
                b.add(buttonPanel);
                b.add(Box.createVerticalStrut(20));

                registerButton1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String account = accountText.getText();
                        String passwd = String.valueOf(passwdText.getPassword());
                        if ("".equals(account) || "".equals(passwd)) {
                            JOptionPane.showMessageDialog(b, "请输入用户名或密码！");
                            return;
                        }
                        else {
                            Account newAccount = new Account();
                            newAccount.setName(account);
                            newAccount.setPassword(passwd);
                            newAccount.setTeacher(isATeacher.isSelected());
                            doRegisterAccount(newAccount);
                            d.dispose();
                        }
                    }
                });
                d.setVisible(true);
            }
        });

        changePassWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog d = new JDialog();
                d.setModal(true);
                d.setTitle("更改密码");
                d.setBounds(300, 250, 400, 300);

                JLabel newPassWordLabel = new JLabel("新密码：");
                JPasswordField newPassWordField = new JPasswordField();
                newPassWordField.setMaximumSize(new Dimension(80, 30));
                newPassWordField.setPreferredSize(new Dimension(80, 30));
                JLabel confirmPassWordLabel = new JLabel("确认密码：");
                JPasswordField confirmPassWordField = new JPasswordField();
                confirmPassWordField.setMaximumSize(new Dimension(80, 30));
                confirmPassWordField.setPreferredSize(new Dimension(80, 30));
                JButton confirmButton = new JButton("确认");
                JButton cancelButton = new JButton("取消");

                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String newPassWord = String.valueOf(newPassWordField.getPassword());
                        String confirmPassWord = String.valueOf(confirmPassWordField.getPassword());

                        if ("".equals(newPassWord) || "".equals(confirmPassWord)) {
                            JOptionPane.showMessageDialog(b, "请输入密码！");
                        }
                        else if (!newPassWord.equals(confirmPassWord)) {
                            JOptionPane.showMessageDialog(b, "密码不一致！");
                        }
                        else {
                            changePassWord(newPassWord);
                            d.dispose();
                        }
                    }
                });

                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        d.dispose();
                    }
                });

                Box db = Box.createVerticalBox();
                d.add(db);
                db.add(Box.createVerticalStrut(50));
                Box db1 = Box.createHorizontalBox();
                db1.add(newPassWordLabel);
                db1.add(newPassWordField);
                db.add(db1);
                db.add(Box.createVerticalStrut(20));
                Box db2 = Box.createHorizontalBox();
                db2.add(confirmPassWordLabel);
                db2.add(confirmPassWordField);
                db.add(db2);
                db.add(Box.createVerticalStrut(100));
                Box db3 = Box.createHorizontalBox();
                db3.add(Box.createHorizontalStrut(30));
                db3.add(confirmButton);
                db3.add(Box.createHorizontalGlue());
                db3.add(cancelButton);
                db3.add(Box.createHorizontalStrut(30));
                db.add(db3);
                db.add(Box.createVerticalStrut(50));

                d.setVisible(true);
            }
        });
    }

    private void doDeleteAccount(ActionEvent e) {
        int row = table.getSelectedRow();
        Account account = accountTableModel.accountList.get(row);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Transaction.start((SqlSession session) -> {
                    FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                    mapper.deleteAccount(account.id);
                    if (account.id != 0) {
                        accountTableModel.delete(row);
                        table.updateUI();
                    }
                    return null;
                });
                return null;
            }
        };
        worker.execute();
    }

    private void doRegisterAccount(Account account) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Transaction.start((SqlSession session) -> {
                    FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                    int id = mapper.createAccount(account);
                    account.setId(id);
                    accountTableModel.insert(account);
                    table.updateUI();
                    return null;
                });
                return null;
            }
        };
        worker.execute();
    }

    protected boolean changePassWord(String passWord) {
        int row = table.getSelectedRow();
        Account account = accountTableModel.accountList.get(row);
        try {
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    Transaction.start((SqlSession session) -> {
                        FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                        mapper.changePassword(account.id, passWord);
                        account.setPassword(passWord);
                        table.updateUI();
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
}
