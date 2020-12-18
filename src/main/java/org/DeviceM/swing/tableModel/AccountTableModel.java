package org.DeviceM.swing.tableModel;

import org.DeviceM.dao.Account;
import org.DeviceM.swing.table.AdminTable;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class AccountTableModel extends AbstractTableModel {

    String[] columnNames = new String[] {"id", "name", "password", "isTeacher"};

    public List<Account> accountList;

    public AccountTableModel() {
        super();
        this.accountList = AdminTable.accountList;
    }

    @Override
    public int getRowCount() {
        return accountList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Account account = accountList.get(rowIndex);
        switch (columnIndex) {
            case 0: return account.getId();
            case 1: return account.getName();
            case 2: return account.getPassword();
            case 3: return String.valueOf(account.getTeacher());
            default: return null;
        }
    }

    public void insert(Account account) {
        accountList.add(account);
    }

    public void delete(int row) {
        accountList.remove(row);
    }
}
