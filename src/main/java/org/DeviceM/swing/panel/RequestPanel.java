package org.DeviceM.swing.panel;

import org.DeviceM.App;
import org.DeviceM.dao.Account;
import org.DeviceM.dao.Request;
import org.DeviceM.mapper.AccountMapper;
import org.DeviceM.mapper.FunctionMapper;
import org.DeviceM.swing.dialog.DatePickerDialog;
import org.DeviceM.swing.table.AdminTable;
import org.DeviceM.swing.tableModel.RequestTableModel;
import org.DeviceM.util.Transaction;
import org.apache.ibatis.session.SqlSession;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

public class RequestPanel extends JPanel {

    private Account currentAccount;

    private RequestTableModel requestTableModel;
    private JTable table;
    private JPanel panel;
    private Box box;

    public RequestPanel(Account account) {
        this.currentAccount = account;
        this.requestTableModel = new RequestTableModel();
        this.table = new JTable();
        this.panel = new JPanel();
        this.box = Box.createVerticalBox();

        makePanel();
    }

    private void makePanel() {
        this.table.setModel(this.requestTableModel);
        JScrollPane jScrollPane = new JScrollPane(this.table);
        jScrollPane.setMaximumSize(new Dimension(1150, 600));
        jScrollPane.setPreferredSize(new Dimension(1150, 600));

        this.table.getColumnModel().getColumn(4).setPreferredWidth(200);
        this.table.getColumnModel().getColumn(6).setPreferredWidth(400);

        JButton addButton = makeAddButton();
        this.panel.add(addButton);

        if (isAdmin() || isTeacher()) {
            JButton rejectButton = makeRejectButton();
            JButton approveButton = makeApproveButton();
            JButton clearButton = makeClearButton();
            this.panel.add(rejectButton);
            this.panel.add(approveButton);
            this.panel.add(clearButton);
        }

        this.add(this.box);
        this.box.add(jScrollPane);
        this.box.add(Box.createVerticalStrut(10));
        this.box.add(this.panel);
    }

    // add, reject, approve, clear
    private JButton makeAddButton() {
        JButton addButton = new JButton("添加");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog jDialog = new JDialog();
                jDialog.setModal(true);
                jDialog.setTitle("新申请");
                jDialog.setBounds(200, 200, 600, 400);

                JPanel deviceIdPanel = new JPanel();
                JLabel deviceIdLabel = new JLabel("设备ID");
                JTextField deviceIdTextField = new JTextField();
                deviceIdTextField.setMaximumSize(new Dimension(200, 30));
                deviceIdTextField.setPreferredSize(new Dimension(200, 30));
                deviceIdPanel.add(deviceIdLabel);
                deviceIdPanel.add(deviceIdTextField);

                JPanel personIdPanel = new JPanel();
                JLabel personIdLabel = new JLabel("账户ID");
                JTextField personIdTextField = new JTextField();
                personIdTextField.setMaximumSize(new Dimension(200, 30));
                personIdTextField.setPreferredSize(new Dimension(200, 30));
                personIdPanel.add(personIdLabel);
                personIdPanel.add(personIdTextField);

                JPanel datePanel = new JPanel();
                JLabel dateLabel = new JLabel("开始日期");
                JXDatePicker datePicker = new JXDatePicker(new java.util.Date());
                datePicker.setMaximumSize(new Dimension(200, 30));
                datePicker.setPreferredSize(new Dimension(200, 30));
                datePanel.add(dateLabel);
                datePanel.add(datePicker);

                JPanel periodPanel = new JPanel();
                JLabel periodLabel = new JLabel("借用时间");
                JTextField periodTextField = new JTextField();
                periodTextField.setMaximumSize(new Dimension(200, 30));
                periodTextField.setPreferredSize(new Dimension(200, 30));
                periodPanel.add(periodLabel);
                periodPanel.add(periodTextField);

                JPanel reasonPanel = new JPanel();
                JLabel reasonLabel = new JLabel("原因");
                JTextArea reasonTextArea = new JTextArea();
                reasonTextArea.setMaximumSize(new Dimension(200, 100));
                reasonTextArea.setPreferredSize(new Dimension(200, 100));
                reasonPanel.add(reasonLabel);
                reasonPanel.add(reasonTextArea);

                JButton confirmButton = new JButton("确认");
                JButton cancelButton = new JButton("取消");

                Box jDialogBox = Box.createVerticalBox();
                jDialogBox.add(Box.createVerticalStrut(10));
                jDialogBox.add(deviceIdPanel);
                jDialogBox.add(Box.createVerticalGlue());

                if (isAdmin()) {
                    jDialogBox.add(personIdPanel);
                    jDialogBox.add(Box.createVerticalGlue());
                }

                jDialogBox.add(datePanel);
                jDialogBox.add(Box.createVerticalGlue());
                jDialogBox.add(periodPanel);
                jDialogBox.add(Box.createVerticalGlue());
                jDialogBox.add(reasonPanel);
                jDialogBox.add(Box.createVerticalGlue());

                Box buttonBox = Box.createHorizontalBox();
                jDialogBox.add(buttonBox);
                buttonBox.add(Box.createHorizontalStrut(50));
                buttonBox.add(confirmButton);
                buttonBox.add(Box.createHorizontalGlue());
                buttonBox.add(cancelButton);
                buttonBox.add(Box.createHorizontalStrut(50));
                jDialogBox.add(Box.createVerticalStrut(10));

                jDialog.add(jDialogBox);

                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Request request = new Request();
                        try {
                            request.deviceId = Integer.valueOf(deviceIdTextField.getText());

                            if (isAdmin()) {
                                request.personId = Integer.valueOf(personIdTextField.getText());
                            }
                            else {
                                request.personId = currentAccount.id;
                            }

                            request.requestDate = new Timestamp(datePicker.getDate().getTime());
                            request.period = Integer.valueOf(periodTextField.getText());
                            request.reason = reasonTextArea.getText();
                            doCreateRequest(request);
                            jDialog.dispose();
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(box, "请输入正确的信息！");
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

    private JButton makeRejectButton() {
        JButton rejectButton = new JButton("拒绝");

        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    doRejectRequest();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(box, "选择正确的请求！");
                }
            }
        });

        return rejectButton;
    }

    private JButton makeApproveButton() {
        JButton approveButton = new JButton("批准");

        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    doApproveRequest();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(box, "选择正确的请求！");
                }
            }
        });

        return approveButton;
    }

    private JButton makeClearButton() {
        JButton clearButton = new JButton("清除");

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Timestamp days = DatePickerDialog.showDatePickerDialog("删除选择的日期以前的记录");
                    if (days != null) {
                        Date date = Date.valueOf(String.valueOf(days).split(" ")[0]);
                        doClearRequest(date);
                    }
                } catch (Exception e1) {
                        JOptionPane.showMessageDialog(box, "请输入正确的日期！");
                }
            }
        });

        return clearButton;
    }

    private void doCreateRequest(Request request) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Transaction.start((SqlSession session) -> {
                    FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                    int newId = mapper.newRequest(request);
                    request.requestId = newId;
                    request.status = "unhandled";
                    requestTableModel.requestList.add(request);
                    table.updateUI();
                    return null;
                });
                return null;
            }
        };
        worker.execute();
    }

    private void doRejectRequest() {
        int row = this.table.getSelectedRow();
        Request request = requestTableModel.requestList.get(row);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Transaction.start((SqlSession session) -> {
                    FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                    mapper.rejectRequest(request.requestId);
                    requestTableModel.requestList.get(row).status = "rejected";
                    table.updateUI();
                    return null;
                });
                return null;
            }
        };
        worker.execute();
    }

    private void doApproveRequest() {
        int row = this.table.getSelectedRow();
        Request request = requestTableModel.requestList.get(row);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Transaction.start((SqlSession session) -> {
                    FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                    if (mapper.approveRequest(request.requestId, currentAccount.id)){
                        requestTableModel.requestList.get(row).status = "accepted";
                        table.updateUI();
                    }
                    return null;
                });
                return null;
            }
        };
        worker.execute();
    }

    private void doClearRequest(Date date) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Transaction.start((SqlSession session) -> {
                    FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                    mapper.clearOutdatedRequests(date);
                    return null;
                });
                if (isAdmin()) {
                    AdminTable.updateRequestList();
                }
                else if (isTeacher()) {
                    AdminTable.updateTeacherRequestList(currentAccount.id);
                }
                else {
                    AdminTable.updateStudentRequestList(currentAccount.id);
                }
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

//    public static void main(String[] args) throws IOException {
//        App.connectDatabase();
//        AdminTable adminTable = new AdminTable();
//        Account ac = (Account) Transaction.start((SqlSession session) -> {
//            AccountMapper mapper = session.getMapper(AccountMapper.class);
//            return mapper.getAccountById(0);
//        });
//        JFrame jf = new JFrame();
//        jf.setBounds(100, 100, 1200, 700);
//        RequestPanel requestPanel = new RequestPanel(ac);
//        jf.add(requestPanel);
//        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        jf.setVisible(true);
//    }

}
