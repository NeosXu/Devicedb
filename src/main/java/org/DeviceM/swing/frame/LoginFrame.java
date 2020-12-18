package org.DeviceM.swing.frame;

import org.DeviceM.App;
import org.DeviceM.dao.Account;
import org.DeviceM.mapper.AccountMapper;
import org.DeviceM.swing.table.AdminTable;
import org.DeviceM.util.Transaction;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LoginFrame extends JFrame {

    public LoginFrame() throws HeadlessException {
        super();
        setTitle("Login");
        setBounds(200, 200, 600, 400);
        setResizable(false);
        Box b = Box.createVerticalBox();
        add(b);
        b.add(Box.createVerticalStrut(100));

        JPanel textPanel = new JPanel();
        JLabel deviceManagementLabel = new JLabel("Device Management");
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

        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("登陆");
        buttonPanel.add(loginButton);
        b.add(buttonPanel);
        b.add(Box.createVerticalStrut(20));

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = accountText.getText();
                String passwd = String.valueOf(passwdText.getPassword());
                if ("".equals(id) || "".equals(passwd)) {
                    JOptionPane.showMessageDialog(b, "请输入用户名或密码！");
                    return;
                }
                try {
                    Account account = getAccountById(id);
                    if (passwd.equals(account.password)) {
                        JOptionPane.showMessageDialog(b, "登陆成功！");
                        dispose();
                        new AdminTable(account);
                        new ManagementFrame(account);
                    } else {
                        JOptionPane.showMessageDialog(b, "用户名或密码错误！");
                    }
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(b, "用户名或密码错误！");
                }
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private Account getAccountById(String account) throws ExecutionException, InterruptedException {
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                return Transaction.start((SqlSession session) -> {
                    AccountMapper mapper = session.getMapper(AccountMapper.class);
                    return mapper.getAccountById(Integer.parseInt(account));
                });
            }
        };
        worker.execute();
        return (Account) worker.get();
    }

    private boolean checkPassWord(String account, String passWord) throws ExecutionException, InterruptedException {
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                return Transaction.start((SqlSession session) -> {
                    AccountMapper mapper = session.getMapper(AccountMapper.class);
                    return mapper.getPassWordById(Integer.parseInt(account));
                });
            }
        };
        worker.execute();
        String correctPassWord = (String) worker.get();
        return passWord.equals(correctPassWord);
    }

    public static void main(String[] args) throws IOException {
        App.connectDatabase();
        LoginFrame lf = new LoginFrame();
    }
}
