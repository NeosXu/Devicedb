package org.DeviceM.swing.panel;

import org.DeviceM.App;
import org.DeviceM.dao.Account;
import org.DeviceM.dao.Device;
import org.DeviceM.mapper.AccountMapper;
import org.DeviceM.mapper.FunctionMapper;
import org.DeviceM.swing.table.AdminTable;
import org.DeviceM.swing.tableModel.DeviceTableModel;
import org.DeviceM.util.Transaction;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Timestamp;

public class DevicePanel extends JPanel {

    private Account currentAccount;

    private DeviceTableModel deviceTableModel;
    private JTable table;
    private JPanel panel;
    private Box box;

    public DevicePanel(Account account) {
        super();
        this.currentAccount = account;
        this.deviceTableModel = new DeviceTableModel();
        this.table = new JTable();
        this.panel = new JPanel();
        this.box = Box.createVerticalBox();

        makePanel();
    }

    private void makePanel() {
        this.table.setModel(this.deviceTableModel);
        JScrollPane jScrollPane = new JScrollPane(this.table);
        jScrollPane.setMaximumSize(new Dimension(1150, 600));
        jScrollPane.setPreferredSize(new Dimension(1150, 600));

        this.table.getColumnModel().getColumn(0).setPreferredWidth(10);
        this.table.getColumnModel().getColumn(7).setPreferredWidth(10);
        this.table.getColumnModel().getColumn(9).setPreferredWidth(10);

        if (isAdmin() || isTeacher()) {
            JButton warehousingButton = makeWarehousingButton();
            JButton deleteButton = makeDeleteButton();
            JButton resetAdminButton = makeResetAdminButton();
            JButton setDeviceDamagedButton = makeSetDeviceDamagedButton();
            this.panel.add(warehousingButton);
            this.panel.add(deleteButton);
            this.panel.add(resetAdminButton);
            this.panel.add(setDeviceDamagedButton);
        }

        this.add(this.box);
        this.box.add(jScrollPane);
        this.box.add(Box.createVerticalStrut(10));
        this.box.add(this.panel);
    }

    private JButton makeWarehousingButton() {
        JButton warehousingButton = new JButton("登记");

        warehousingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog jDialog = new JDialog();
                jDialog.setModal(true);
                jDialog.setTitle("新设备登记");
                jDialog.setBounds(200, 200, 600, 400);

                JPanel namePanel = new JPanel();
                JLabel nameLabel = new JLabel("名称");
                JTextField nameTextField = new JTextField();
                nameTextField.setMaximumSize(new Dimension(200, 30));
                nameTextField.setPreferredSize(new Dimension(200, 30));
                namePanel.add(nameLabel);
                namePanel.add(nameTextField);

                JPanel typePanel = new JPanel();
                JLabel typeLabel = new JLabel("类型");
                JTextField typeTextField = new JTextField();
                typeTextField.setMaximumSize(new Dimension(200, 30));
                typeTextField.setPreferredSize(new Dimension(200, 30));
                typePanel.add(typeLabel);
                typePanel.add(typeTextField);

                JPanel purchaseTimePanel = new JPanel();
                JLabel purchaseTimeLabel = new JLabel("购买时间");
                JTextField purchaseTimeTextField = new JTextField();
                purchaseTimeTextField.setMaximumSize(new Dimension(200, 30));
                purchaseTimeTextField.setPreferredSize(new Dimension(200, 30));
                purchaseTimePanel.add(purchaseTimeLabel);
                purchaseTimePanel.add(purchaseTimeTextField);

                JPanel pricePanel = new JPanel();
                JLabel priceLabel = new JLabel("价格");
                JTextField priceTextField = new JTextField();
                priceTextField.setMaximumSize(new Dimension(200, 30));
                priceTextField.setPreferredSize(new Dimension(200, 30));
                pricePanel.add(priceLabel);
                pricePanel.add(priceTextField);

                JPanel producerPanel = new JPanel();
                JLabel producerLabel = new JLabel("生产厂家");
                JTextField producerTextField = new JTextField();
                producerTextField.setMaximumSize(new Dimension(200, 30));
                producerTextField.setPreferredSize(new Dimension(200, 30));
                producerPanel.add(producerLabel);
                producerPanel.add(producerTextField);

                JPanel warrantyUntilPanel = new JPanel();
                JLabel warrantyUntilLabel = new JLabel("保修截至日期");
                JTextField warrantyUntilTextField = new JTextField();
                warrantyUntilTextField.setMaximumSize(new Dimension(200, 30));
                warrantyUntilTextField.setPreferredSize(new Dimension(200, 30));
                warrantyUntilPanel.add(warrantyUntilLabel);
                warrantyUntilPanel.add(warrantyUntilTextField);

                JPanel transactorIdPanel = new JPanel();
                JLabel transactorIdLabel = new JLabel("经购人");
                JTextField transactorIdTextField = new JTextField();
                transactorIdTextField.setMaximumSize(new Dimension(200, 30));
                transactorIdTextField.setPreferredSize(new Dimension(200, 30));
                transactorIdPanel.add(transactorIdLabel);
                transactorIdPanel.add(transactorIdTextField);

                JPanel adminIdPanel = new JPanel();
                JLabel adminIdLabel = new JLabel("管理者");
                JTextField adminIdTextField = new JTextField();
                adminIdTextField.setMaximumSize(new Dimension(200, 30));
                adminIdTextField.setPreferredSize(new Dimension(200, 30));
                adminIdPanel.add(adminIdLabel);
                adminIdPanel.add(adminIdTextField);

                JButton confirmButton = new JButton("确认");
                JButton cancelButton = new JButton("取消");

                Box jDialogBox = Box.createVerticalBox();
                jDialogBox.add(Box.createVerticalStrut(10));
                jDialogBox.add(namePanel);
                jDialogBox.add(Box.createVerticalGlue());
                jDialogBox.add(typePanel);
                jDialogBox.add(Box.createVerticalGlue());
                jDialogBox.add(purchaseTimePanel);
                jDialogBox.add(Box.createVerticalGlue());
                jDialogBox.add(pricePanel);
                jDialogBox.add(Box.createVerticalGlue());
                jDialogBox.add(producerPanel);
                jDialogBox.add(Box.createVerticalGlue());
                jDialogBox.add(warrantyUntilPanel);
                jDialogBox.add(Box.createVerticalGlue());
                jDialogBox.add(transactorIdPanel);
                jDialogBox.add(Box.createVerticalGlue());
                jDialogBox.add(adminIdPanel);
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
                        Device device = new Device();
                        try {
                            device.name = nameTextField.getText();
                            device.type = typeTextField.getText();
                            device.purchaseTime = Timestamp.valueOf(purchaseTimeTextField.getText());
                            device.price = Double.valueOf(priceTextField.getText());
                            device.producer = producerTextField.getText();
                            device.warrantyUntil = Timestamp.valueOf(warrantyUntilTextField.getText());
                            device.transactorId = Integer.valueOf(transactorIdTextField.getText());
                            device.adminId = Integer.valueOf(adminIdTextField.getText());
                            doWarehousing(device);
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

        return warehousingButton;
    }

    private JButton makeDeleteButton() {
        JButton deleteButton = new JButton("删除");

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(box, "确认删除？");
                if (JOptionPane.OK_OPTION == option) {
                    doDeleteDevice();
                }
            }
        });

        return deleteButton;
    }

    private JButton makeResetAdminButton() {
        JButton resetAdminButton = new JButton("重设管理员");

        resetAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog jDialog = new JDialog();
                jDialog.setModal(true);
                jDialog.setTitle("重新设置管理员");
                jDialog.setBounds(200, 200, 400, 300);
                Box jDialogBox = Box.createVerticalBox();

                JPanel panel = new JPanel(new FlowLayout());
                JLabel label = new JLabel("新管理员id");
                JTextField newIdField = new JTextField();
                newIdField.setMaximumSize(new Dimension(80, 30));
                newIdField.setPreferredSize(new Dimension(80, 30));
                panel.add(label);
                panel.add(newIdField);

                JButton confirmButton = new JButton("确认");
                JButton cancelButton = new JButton("取消");

                Box buttonBox = Box.createHorizontalBox();
                buttonBox.add(Box.createHorizontalStrut(50));
                buttonBox.add(confirmButton);
                buttonBox.add(Box.createHorizontalGlue());
                buttonBox.add(cancelButton);
                buttonBox.add(Box.createHorizontalStrut(50));

                jDialogBox.add(Box.createVerticalStrut(50));
                jDialogBox.add(panel);
                jDialogBox.add(Box.createVerticalGlue());
                jDialogBox.add(buttonBox);
                jDialogBox.add(Box.createVerticalStrut(10));
                jDialog.add(jDialogBox);

                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Device device = new Device();
                        try {
                            int newId = Integer.parseInt(newIdField.getText());
                            doResetAdmin(newId);
                            jDialog.dispose();
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(box, "请输入正确的管理员！");
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

        return resetAdminButton;
    }

    private JButton makeSetDeviceDamagedButton() {
        JButton setDeviceDamagedButton = new JButton("标记损坏");

        setDeviceDamagedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    doSetDeviceDamaged();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(box, "请选择正确的设备！");
                }
            }
        });

        return setDeviceDamagedButton;
    }

    private void doWarehousing(Device device) {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Transaction.start((SqlSession session) -> {
                    FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                    int newId = mapper.addDevice(device);
                    device.id = newId;
                    device.deviceStatus = "inStock";
                    deviceTableModel.add(device);
                    table.updateUI();
                    return null;
                });
                return null;
            }
        };
        worker.execute();
    }

    private void doDeleteDevice() {
        int row = this.table.getSelectedRow();
        Device device = this.deviceTableModel.deviceList.get(row);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Transaction.start((SqlSession session) -> {
                    FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                    mapper.deleteDevice(device.id);
                    if ("inStock".equals(device.deviceStatus)) {
                        deviceTableModel.delete(row);
                        table.updateUI();
                    }
                    return null;
                });
                return null;
            }
        };
        worker.execute();
    }

    private void doResetAdmin(int newId) {
        int row = this.table.getSelectedRow();
        Device device = this.deviceTableModel.deviceList.get(row);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Transaction.start((SqlSession session) -> {
                    FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                    mapper.resetAdmin(device.id, newId);
                    deviceTableModel.deviceList.get(row).adminId = newId;
                    table.updateUI();
                    return null;
                });
                return null;
            }
        };
        worker.execute();
    }

    private void doSetDeviceDamaged() {
        int row = this.table.getSelectedRow();
        Device device = this.deviceTableModel.deviceList.get(row);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Transaction.start((SqlSession session) -> {
                    FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                    mapper.setDeviceDamaged(device.id);
                    if ("inStock".equals(deviceTableModel.deviceList.get(row).deviceStatus)) {
                        deviceTableModel.deviceList.get(row).deviceStatus = "damaged";
                        table.updateUI();
                    }
                    return null;
                });
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
    }

}
