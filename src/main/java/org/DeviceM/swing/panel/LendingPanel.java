package org.DeviceM.swing.panel;

import org.DeviceM.App;
import org.DeviceM.dao.Account;
import org.DeviceM.dao.Lending;
import org.DeviceM.mapper.AccountMapper;
import org.DeviceM.mapper.FunctionMapper;
import org.DeviceM.swing.table.AdminTable;
import org.DeviceM.swing.tableModel.LendingTableModel;
import org.DeviceM.util.Transaction;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LendingPanel extends JPanel {

    private Account currentAccount;

    private LendingTableModel lendingTableModel;
    private JTable table;
    private JPanel panel;
    private Box box;

    public LendingPanel(Account account) {
        super();
        this.currentAccount = account;
        lendingTableModel = new LendingTableModel();
        this.table = new JTable();
        this.panel = new JPanel();
        this.box = Box.createVerticalBox();

        makePanel();
    }

    private void makePanel() {
        this.table.setModel(this.lendingTableModel);
        JScrollPane jScrollPane = new JScrollPane(this.table);
        jScrollPane.setMaximumSize(new Dimension(1150, 600));
        jScrollPane.setPreferredSize(new Dimension(1150, 600));

        JButton returnedButton = makeReturnedButton();
        this.panel.add(returnedButton);

        this.add(this.box);
        this.box.add(jScrollPane);
        this.box.add(Box.createVerticalStrut(10));
        this.box.add(this.panel);
    }

    private JButton makeReturnedButton() {
        JButton returnedButton = new JButton("归还");

        returnedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    doReturnedDevice();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(box, "请选择正确的记录！");
                }
            }
        });

        return returnedButton;
    }

    private void doReturnedDevice() {
        int row = this.table.getSelectedRow();
        Lending lending = this.lendingTableModel.lendingList.get(row);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Transaction.start((SqlSession session) -> {
                    FunctionMapper mapper = session.getMapper(FunctionMapper.class);
                    mapper.returnDevice(lending.deviceId);
                    lending.returned = true;
                    table.updateUI();
                    return null;
                });
                return null;
            }
        };
        worker.execute();
    }

    public void updateTable() {
        this.table.updateUI();
    }

}
