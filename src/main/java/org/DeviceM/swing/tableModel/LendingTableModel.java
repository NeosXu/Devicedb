package org.DeviceM.swing.tableModel;

import org.DeviceM.dao.Lending;
import org.DeviceM.swing.table.AdminTable;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class LendingTableModel extends AbstractTableModel {

    String[] columnNames = new String[] {"recId", "deviceId", "personId", "requestId", "returned"};

    public List<Lending> lendingList;

    public LendingTableModel() {
        super();
        this.lendingList = AdminTable.lendingList;
    }

    @Override
    public int getRowCount() {
        return lendingList.size();
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
        Lending lending = lendingList.get(rowIndex);
        switch (columnIndex) {
            case 0: return lending.recId;
            case 1: return lending.deviceId;
            case 2: return lending.personId;
            case 3: return lending.requestId;
            case 4: return String.valueOf(lending.returned);
            default: return null;
        }
    }

    public void add(Lending lending) {
        lendingList.add(lending);
    }

    public void delete(int row) {
        lendingList.remove(row);
    }
}
