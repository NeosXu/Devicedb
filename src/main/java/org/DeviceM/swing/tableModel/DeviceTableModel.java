package org.DeviceM.swing.tableModel;

import org.DeviceM.dao.Device;
import org.DeviceM.swing.table.AdminTable;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class DeviceTableModel extends AbstractTableModel {

    String[] columnNames = new String[] {"id", "name", "type", "purchaseTime", "price", "producer", "warrantyUntil",
        "transactorId", "deviceStatus", "adminId"};

    public List<Device> deviceList;

    public DeviceTableModel() {
        super();
        deviceList = AdminTable.deviceList;
    }

    @Override
    public int getRowCount() {
        return deviceList.size();
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
        Device device = this.deviceList.get(rowIndex);
        switch (columnIndex) {
            case 0: return device.id;
            case 1: return device.name;
            case 2: return device.type;
            case 3: return String.valueOf(device.purchaseTime);
            case 4: return device.price;
            case 5: return device.producer;
            case 6: return String.valueOf(device.warrantyUntil);
            case 7: return device.transactorId;
            case 8: return device.deviceStatus;
            case 9: return device.adminId;
            default: return null;
        }
    }

    public void add(Device device) {
        deviceList.add(device);
    }

    public void delete(int row) {
        deviceList.remove(row);
    }
}
