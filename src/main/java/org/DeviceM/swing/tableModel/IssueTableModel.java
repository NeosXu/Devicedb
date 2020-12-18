package org.DeviceM.swing.tableModel;

import org.DeviceM.dao.Issue;
import org.DeviceM.swing.table.AdminTable;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class IssueTableModel extends AbstractTableModel {

    String[] columnNames = new String[] {"issueId", "deviceId", "time", "status", "expectedDays", "reason"};

    public List<Issue> issueList;

    public IssueTableModel() {
        super();
        this.issueList = AdminTable.issueList;
    }

    @Override
    public int getRowCount() {
        return issueList.size();
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
        Issue issue = this.issueList.get(rowIndex);
        switch (columnIndex) {
            case 0: return issue.issueId;
            case 1: return issue.deviceId;
            case 2: return String.valueOf(issue.time);
            case 3: return issue.status;
            case 4: return issue.expectedDays;
            case 5: return issue.reason;
            default: return null;
        }
    }

    public void insert(Issue issue) {
        issueList.add(issue);
    }

    public void delete(int row) {
        issueList.remove(row);
    }
}
