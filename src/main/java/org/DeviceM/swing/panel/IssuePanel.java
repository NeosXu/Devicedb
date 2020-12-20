package org.DeviceM.swing.panel;

import org.DeviceM.App;
import org.DeviceM.dao.Account;
import org.DeviceM.dao.Issue;
import org.DeviceM.mapper.AccountMapper;
import org.DeviceM.mapper.FunctionMapper;
import org.DeviceM.swing.dialog.DatePickerDialog;
import org.DeviceM.swing.table.AdminTable;
import org.DeviceM.swing.tableModel.IssueTableModel;
import org.DeviceM.util.Transaction;
import org.apache.ibatis.session.SqlSession;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

public class IssuePanel extends JPanel {
    Account currentAccount;

    private IssueTableModel issueTableModel;
    private JTable table;
    private JPanel panel;
    private Box box;

    public IssuePanel(Account account) {
        super();
        this.currentAccount = account;
        this.issueTableModel = new IssueTableModel();
        this.table = new JTable();
        this.panel = new JPanel();
        this.box = Box.createVerticalBox();

        makePanel();
    }

    private void makePanel() {
        this.table.setModel(this.issueTableModel);
        JScrollPane jScrollPane = new JScrollPane(this.table);
        jScrollPane.setMaximumSize(new Dimension(1150, 600));
        jScrollPane.setPreferredSize(new Dimension(1150, 600));

        this.table.getColumnModel().getColumn(0).setPreferredWidth(10);
        this.table.getColumnModel().getColumn(1).setPreferredWidth(10);
        this.table.getColumnModel().getColumn(2).setPreferredWidth(100);
        this.table.getColumnModel().getColumn(3).setPreferredWidth(50);
        this.table.getColumnModel().getColumn(4).setPreferredWidth(50);

        if (isAdmin() || isTeacher()) {
            JButton addButton = makeAddButton();
            JComboBox deleteComboBox = makeDeleteComboBox();
            JComboBox updateComboBox = makeUpdateComboBox();
            this.panel.add(addButton);
            this.panel.add(deleteComboBox);
            this.panel.add(updateComboBox);
        }

        this.add(this.box);
        this.box.add(jScrollPane);
        this.box.add(Box.createVerticalStrut(10));
        this.box.add(this.panel);
    }

    private JButton makeAddButton() {
        JButton addButton = new JButton("添加");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog jDialog = new JDialog();
                jDialog.setModal(true);
                jDialog.setTitle("新报修设备");
                jDialog.setBounds(200, 200, 600, 400);

                JLabel deviceIdLabel = new JLabel("设备ID");
                JTextField deviceIdTextField = new JTextField();
                deviceIdTextField.setMaximumSize(new Dimension(200, 30));
                deviceIdTextField.setPreferredSize(new Dimension(200, 30));

                JLabel timeLabel = new JLabel("报修日期");
                JXDatePicker timeDatePicker = new JXDatePicker(new Date());
                timeDatePicker.setMaximumSize(new Dimension(200, 30));
                timeDatePicker.setPreferredSize(new Dimension(200, 30));

                JLabel expectedDayLabel = new JLabel("预计维修天数");
                JTextField expectedDayTextField = new JTextField();
                expectedDayTextField.setMaximumSize(new Dimension(200, 30));
                expectedDayTextField.setPreferredSize(new Dimension(200, 30));

                JLabel reasonLabel = new JLabel("损坏原因");
                JTextArea reasonTextField = new JTextArea();
                reasonTextField.setLineWrap(true);
                reasonTextField.setMaximumSize(new Dimension(200, 100));
                reasonTextField.setPreferredSize(new Dimension(200, 100));

                JButton confirmButton = new JButton("确认");
                JButton cancelButton = new JButton("取消");

                Box buttonBox = Box.createHorizontalBox();
                buttonBox.add(Box.createHorizontalStrut(120));
                buttonBox.add(confirmButton);
                buttonBox.add(Box.createHorizontalGlue());
                buttonBox.add(cancelButton);
                buttonBox.add(Box.createHorizontalStrut(120));

                // 创建组件布局
                Box contentBox = Box.createVerticalBox();

                // 上方输入区域
                JPanel contentPanel = new JPanel();
                GroupLayout layout = new GroupLayout(contentPanel);
                contentPanel.setLayout(layout);
                // 横向布局
                GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
                hGroup.addGap(5);
                hGroup.addGroup(layout.createParallelGroup()
                        .addComponent(deviceIdLabel)
                        .addComponent(timeLabel)
                        .addComponent(expectedDayLabel)
                        .addComponent(reasonLabel));
                hGroup.addGap(5);
                hGroup.addGroup(layout.createParallelGroup()
                        .addComponent(deviceIdTextField)
                        .addComponent(timeDatePicker)
                        .addComponent(expectedDayTextField)
                        .addComponent(reasonTextField));
                hGroup.addGap(5);
                layout.setHorizontalGroup(hGroup);

                // 纵向布局
                GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
                vGroup.addGap(10);
                vGroup.addGroup(layout.createParallelGroup()
                        .addComponent(deviceIdLabel).addComponent(deviceIdTextField));
                vGroup.addGap(10);
                vGroup.addGroup(layout.createParallelGroup()
                        .addComponent(timeLabel).addComponent(timeDatePicker));
                vGroup.addGap(10);
                vGroup.addGroup(layout.createParallelGroup()
                        .addComponent(expectedDayLabel).addComponent(expectedDayTextField));
                vGroup.addGap(10);
                vGroup.addGroup(layout.createParallelGroup()
                        .addComponent(reasonLabel).addComponent(reasonTextField));
                vGroup.addGap(10);
                layout.setVerticalGroup(vGroup);

                // 填装content Box
                contentBox.add(Box.createVerticalStrut(20));
                contentBox.add(contentPanel);
                contentBox.add(buttonBox);
                jDialog.add(contentBox);

                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Issue issue = new Issue();
                        try {
                            issue.deviceId = Integer.valueOf(deviceIdTextField.getText());
                            issue.time = new Timestamp(timeDatePicker.getDate().getTime());
                            if (!"".equals(expectedDayTextField.getText())) {
                                issue.expectedDays = Integer.valueOf(expectedDayTextField.getText());
                            }
                            issue.reason = reasonTextField.getText();
                            doCreateIssue(issue);
                            jDialog.dispose();
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(box, "请输入正确的设备信息！");
                        }
                    }
                });

                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jDialog.dispose();
                    }
                });

                jDialog.setVisible(true);
            }
        });

        return addButton;
    }

    // 评估、标记报废、标记修复完成
    private JComboBox makeUpdateComboBox() {
        String[] update = new String[] {"评估", "标记报废", "修复完成"};
        JComboBox updateComboBox = new JComboBox(update);

        updateComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = updateComboBox.getSelectedIndex();
                if (index == 0) {
                    String expected = JOptionPane.showInputDialog(box, "预计修复完成的天数");
                    if (expected != null) {
                        try {
                            int expectedDay = Integer.parseInt(expected);
                            doEvaluateIssue(expectedDay);
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(box, "请输入正确的日期！");
                        }
                    }
                }
                else if (index == 1 || index == 2) {
                    doHandleIssue(index == 2);
                }
                else {
                    JOptionPane.showMessageDialog(box, "暂不支持");
                }
            }
        });

        return updateComboBox;
    }

    private JComboBox makeDeleteComboBox() {
        String[] delete = new String[] {"删除", "清除"};
        JComboBox deleteComboBox = new JComboBox(delete);

        deleteComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = deleteComboBox.getSelectedIndex();
                if (index == 0) {
                    int option = JOptionPane.showConfirmDialog(box, "确认删除？");
                    if (JOptionPane.OK_OPTION == option) {
                        try {
                            doDeleteDevice();
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(box, "请选择正确的设备！");
                        }
                    }
                }
                else if (index == 1) {
                    try {
                        Timestamp days = DatePickerDialog.showDatePickerDialog("删除选择的日期以前的记录");
                        if (days != null) {
                            doClearIssue(days);
                        }
                    } catch (Exception e1) {
                            JOptionPane.showMessageDialog(box, "请选择正确的日期！");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(box, "暂不支持");
                }
            }
        });

        return deleteComboBox;
    }

    private void doCreateIssue(Issue issue) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Transaction.start((SqlSession session) -> {
                    FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                    Integer newIssueId = mapper.newIssue(issue);
                    if (newIssueId != null) {
                        issue.issueId = newIssueId;
                        issue.status = "repairing";
                        issueTableModel.insert(issue);
                        table.updateUI();
                    }
                    return null;
                });
                return null;
            }
        };
        worker.execute();
    }

    private void doDeleteDevice() {
        int row = this.table.getSelectedRow();
        Issue issue = this.issueTableModel.issueList.get(row);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Transaction.start((SqlSession session) -> {
                    FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                    mapper.deleteIssue(issue.issueId);
                    issueTableModel.delete(row);
                    table.updateUI();
                    return null;
                });
                return null;
            }
        };
        worker.execute();
    }

    private void doHandleIssue(boolean repaired) {
        int row = this.table.getSelectedRow();
        Issue issue = this.issueTableModel.issueList.get(row);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Transaction.start((SqlSession session) -> {
                    FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                    mapper.handleIssue(issue.issueId, repaired);
                    if ("repairing".equals(issue.status)) {
                        issueTableModel.issueList.get(row).status = repaired ? "repaired" : "scrapped";
                        table.updateUI();
                    }
                    return null;
                });
                return null;
            }
        };
        worker.execute();
    }

    private void doEvaluateIssue(int expectedDay) {
        int row = this.table.getSelectedRow();
        Issue issue = this.issueTableModel.issueList.get(row);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Transaction.start((SqlSession session) -> {
                    FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                    mapper.evaluateIssue(issue.issueId, expectedDay);
                    issueTableModel.issueList.get(row).expectedDays = expectedDay;
                    table.updateUI();
                    return null;
                });
                return null;
            }
        };
        worker.execute();
    }

    private void doClearIssue(Timestamp day) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Transaction.start((SqlSession session) -> {
                    FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                    mapper.clearHandledIssue(day);
                    return null;
                });
                AdminTable.updateIssueList();
                table.updateUI();
                return null;
            }
        };
        worker.execute();
    }

    private boolean isTeacher() {
        return this.currentAccount.isTeacher;
    }

    private boolean isAdmin() {
        return this.currentAccount.id == 0;
    }

    public void updateTable() {
        this.table.updateUI();
        this.updateUI();
    }

}
