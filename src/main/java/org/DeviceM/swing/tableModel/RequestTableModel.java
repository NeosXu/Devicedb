package org.DeviceM.swing.tableModel;

import org.DeviceM.dao.Request;
import org.DeviceM.swing.table.AdminTable;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class RequestTableModel extends AbstractTableModel {

    String[] columnNames = new String[] {"requestId", "deviceId", "personId", "status", "requestDate", "period", "reason"};

    public List<Request> requestList;

    public RequestTableModel() {
        super();
        this.requestList = AdminTable.requestList;
    }

    @Override
    public int getRowCount() {
        return requestList.size();
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
        Request request = requestList.get(rowIndex);
        switch (columnIndex) {
            case 0: return request.requestId;
            case 1: return request.deviceId;
            case 2: return request.personId;
            case 3: return request.status;
            case 4: return String.valueOf(request.requestDate);
            case 5: return request.period;
            case 6: return request.reason;
            default: return null;
        }
    }

    public void add(Request request) {
        requestList.add(request);
    }

    public void delete(int row) {
        requestList.remove(row);
    }
}
