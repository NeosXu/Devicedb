package org.DeviceM.swing.dialog;

import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.Date;

public class DatePickerDialog extends JDialog {

    String message;

    JLabel messageLabel;
    JXDatePicker jxDatePicker;
    JButton confirmButton;
    JButton cancelButton;

    Timestamp timestamp;

    public DatePickerDialog(String message) {
        this.message = message;

        this.messageLabel = new JLabel(message);
        this.jxDatePicker = new JXDatePicker(new Date());
        this.jxDatePicker.setMaximumSize(new Dimension(200, 30));
        this.jxDatePicker.setPreferredSize(new Dimension(200, 30));
        this.confirmButton = makeConfirmButton();
        this.cancelButton = makeCancelButton();

        setTitle("日期");
        setModal(true);
        setBounds(200, 200, 300, 200);

        makePanel();
        setVisible(true);
    }

    private void makePanel() {
        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalStrut(10));

        JPanel messagePanel = new JPanel();
        messagePanel.add(this.messageLabel);
        box.add(messagePanel);

        box.add(Box.createVerticalGlue());

        JPanel datePickerPanel = new JPanel();
        datePickerPanel.add(this.jxDatePicker);
        box.add(datePickerPanel);

        box.add(Box.createVerticalGlue());

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(this.confirmButton);
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(this.cancelButton);
        buttonBox.add(Box.createHorizontalStrut(10));

        box.add(buttonBox);
        this.add(box);
    }

    private JButton makeConfirmButton() {
        JButton confirmButton = new JButton("确定");

        confirmButton.addActionListener(e -> {
            try {
                this.timestamp = new Timestamp(this.jxDatePicker.getDate().getTime());
                this.dispose();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "请选择正确的日期！");
            }
        });

        return confirmButton;
    }

    private JButton makeCancelButton() {
        JButton cancelButton = new JButton("取消");

        cancelButton.addActionListener(e -> this.dispose());

        return cancelButton;
    }

    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    public static Timestamp showDatePickerDialog(String message) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(message);
        return datePickerDialog.getTimestamp();
    }

}
