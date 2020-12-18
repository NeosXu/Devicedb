package org.DeviceM.swing.panel;

import org.DeviceM.dao.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeacherAccountPanel extends AccountPanel {

    public TeacherAccountPanel(Account account) {
        super(account);
    }

    private void makeTeacherPanel() {
        JLabel accountLabel = new JLabel(String.format("账号：%d", currentAccount.id));
        JLabel nameLabel = new JLabel(String.format("姓名：%s", currentAccount.name));
        JButton changePassWordButton = new JButton("更改密码");

        Box b = Box.createVerticalBox();
        add(b);
        b.add(Box.createVerticalStrut(100));
        b.add(accountLabel);
        b.add(Box.createVerticalStrut(50));
        b.add(nameLabel);
        b.add(Box.createVerticalStrut(100));
        b.add(changePassWordButton);
        b.add(Box.createVerticalStrut(50));

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
                            changePassWord(currentAccount, newPassWord);
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
}
