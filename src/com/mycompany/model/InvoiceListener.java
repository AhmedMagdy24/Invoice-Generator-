package com.mycompany.model;

import com.mycompany.controller.Bill;
import com.mycompany.controller.component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class InvoiceListener extends Component {

    JTable bill;
    private String[][] a;
    private String[][] b;
    Component thisElement;
    String contemporaryDate;


    public InvoiceListener(JTable bill, Component thisElement, String contemporaryDate) {
        this.bill = bill;
        this.thisElement = thisElement;
        this.contemporaryDate = contemporaryDate;
    }


    public void UpdateBill(JTable invoices) {
        this.bill = invoices;
    }


    public Bill[] loadBill(Bill[] invoicesArray) {
        JOptionPane.showMessageDialog(thisElement, "You need to select the 2 files together", "Warning",
                JOptionPane.ERROR_MESSAGE);
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        int result = fc.showOpenDialog(thisElement);

        if (result == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(thisElement, "No file has been selected", "Error",
                    JOptionPane.ERROR_MESSAGE);
            contemporaryDate = "./data/";
            return invoicesArray;


        }


        if (result == JFileChooser.APPROVE_OPTION) {

            File[] files = fc.getSelectedFiles();
            File invoiceHeader;
            File invoiceLine;
            try {
                String file0 = files[0].getName();
                String file1 = files[1].getName();

                if (file0.equals("InvoiceHeader.csv")) {
                    invoiceHeader = files[0];
                    invoiceLine = files[1];
                } else if (file1.equals("InvoiceHeader.csv")) {
                    invoiceHeader = files[1];
                    invoiceLine = files[0];
                } else {
                    throw new IOException();
                }
                String[][] invoiceHeaderCsv = loadInvoiceHeader(invoiceHeader.getPath());
                String[][] invoiceLineCsv = loadInvoiceLine(invoiceLine.getPath());
                invoicesArray = updateInvoice(invoiceHeaderCsv, invoiceLineCsv);
                contemporaryDate = invoiceHeader.getParent() + File.separatorChar;
                Bill[] finalInvoicesArray = invoicesArray;
                return finalInvoicesArray;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(thisElement, "Please select InvoiceHeader.csv and InvoiceLine.csv files", "Error",
                        JOptionPane.ERROR_MESSAGE);

                return invoicesArray;

            }


        }

        return null;
    }

    private void renderInvoiceTable(Bill[] invoicesArray) {
        String[][] invoicesData = new String[invoicesArray.length][4];
        for (int i = 0; i < invoicesArray.length; i++) {
            invoicesData[i] = invoicesArray[i].getBillArray();
        }

        String[] invoicesColumn = {"No.", "Date", "Customer", "Total"};
        DefaultTableModel model = new DefaultTableModel(invoicesData, invoicesColumn);
        this.bill.setModel(model);
    }

    public void saveDate(Bill[] invoicesArray) {

        String invoiceHeaderCSV = "";
        for (Bill value : invoicesArray) {
            String[] invoice = value.getBillArray();

            invoiceHeaderCSV = invoiceHeaderCSV + invoice[0];
            invoiceHeaderCSV = invoiceHeaderCSV + ",";
            invoiceHeaderCSV = invoiceHeaderCSV + invoice[1];
            invoiceHeaderCSV = invoiceHeaderCSV + ",";
            invoiceHeaderCSV = invoiceHeaderCSV + invoice[2];
            invoiceHeaderCSV = invoiceHeaderCSV + "\r\n";

        }
        File invoiceHeader = new File(contemporaryDate + "InvoiceHeader.csv");
        try {
            FileWriter writer = new FileWriter(invoiceHeader);
            writer.write(invoiceHeaderCSV);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        String invoiceLineCSV = "";
        for (int i = 0; i < invoicesArray.length; i++) {
            String[][] items = invoicesArray[i].getItems_Array();

            for (int j = 0; j < items.length; j++) {
                invoiceLineCSV = invoiceLineCSV + invoicesArray[i].getInvoiceNumber();
                invoiceLineCSV = invoiceLineCSV + ",";
                invoiceLineCSV = invoiceLineCSV + items[j][1];
                invoiceLineCSV = invoiceLineCSV + ",";
                invoiceLineCSV = invoiceLineCSV + items[j][2];
                invoiceLineCSV = invoiceLineCSV + ",";
                invoiceLineCSV = invoiceLineCSV + items[j][3];
                invoiceLineCSV = invoiceLineCSV + "\r\n";
            }
        }
        File invoiceLine = new File(contemporaryDate + "InvoiceLine.csv");
        try {
            FileWriter writer = new FileWriter(invoiceLine);
            writer.write(invoiceLineCSV);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[][] loadInvoiceHeader(String path) {
        if (path.equals("")) {
            path = "./data/InvoiceHeader.csv";
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int size = 0;
        try {
            size = fis.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] b = new byte[size];
        try {
            fis.read(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileString = new String(b);
        String[] fileLines = fileString.split("\\r?\\n");
        String[][] invoicesCSV = new String[fileLines.length][3];
        for (int i = 0; i < fileLines.length; i++) {
            String[] row = fileLines[i].split(",");
            System.arraycopy(row, 0, invoicesCSV[i], 0, row.length);
        }
        return invoicesCSV;
    }

    public String[][] loadInvoiceLine(String path) {
        if (path.equals("")) {
            path = "./data/InvoiceLine.csv";
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int size = 0;
        try {
            size = fis.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] b = new byte[size];
        try {
            fis.read(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileString = new String(b);
        String[] fileLines = fileString.split("\\r?\\n");
        String[][] invoicesItemsCSV = new String[fileLines.length][4];
        for (int i = 0; i < fileLines.length; i++) {
            String[] row = fileLines[i].split(",");
            System.arraycopy(row, 0, invoicesItemsCSV[i], 0, row.length);
        }
        return invoicesItemsCSV;
    }

    public Bill[] updateInvoice(String[][] invoiceHeader, String[][] invoiceLine) {
        Bill[] invoicesArray = new Bill[invoiceHeader.length];
        for (int i = 0; i < invoicesArray.length; i++) {

            int itemsCount = 0;
            for (int j = 0; j < invoiceLine.length; j++) {
                if (Integer.parseInt(invoiceLine[j][0]) == i + 1) {
                    itemsCount++;
                } else if (Integer.parseInt(invoiceLine[j][0]) > i + 1) {
                    break;
                }
            }
            component[] components = new component[itemsCount];
            int itemIndex = 0;
            for (int j = 0; j < invoiceLine.length; j++) {
                if (Integer.parseInt(invoiceLine[j][0]) == i + 1) {
                    components[itemIndex] = new component();
                    components[itemIndex].setItemName(invoiceLine[j][1]);
                    components[itemIndex].setItemPrice(Integer.parseInt(invoiceLine[j][2]));
                    components[itemIndex].setCountValue(Integer.parseInt(invoiceLine[j][3]));
                    itemIndex++;
                } else if (Integer.parseInt(invoiceLine[j][0]) > i + 1) {
                    break;
                }
            }
            invoicesArray[i] = new Bill();
            invoicesArray[i].setInvoiceNumber(i + 1);
            invoicesArray[i].setBillDate(invoiceHeader[i][1]);
            invoicesArray[i].setCustomerName(invoiceHeader[i][2]);
            invoicesArray[i].setItems(components);

        }

        return invoicesArray;
    }

}