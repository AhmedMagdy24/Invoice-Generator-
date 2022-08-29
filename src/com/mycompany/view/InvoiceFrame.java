package com.mycompany.view;
import com.mycompany.model.InvoiceListener;
import javax.naming.directory.NoSuchAttributeException;
import com.mycompany.controller.Bill;
import com.mycompany.controller.component;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.util.Arrays;

public class InvoiceFrame extends JFrame implements ActionListener {

    private JTable invoice;
    private JLabel invoiceNumberValue;
    private JTextField billDateValue;
    private JTextField cxNameValue;
    private JLabel billTotalValue;
    private JTable billItemsTable;
    private String currentTime = "./data/";
    private InvoiceListener invoiceListener = new InvoiceListener(null, this, currentTime);
    // Invoices
    private Bill[] invoicesArray = invoiceListener.updateInvoice( invoiceListener.loadInvoiceHeader(""), invoiceListener.loadInvoiceLine(""));


    public InvoiceFrame() throws HeadlessException {
        super("Sale Invoice Generator");
        setLayout(new FlowLayout());


        JMenuItem loadFiles = new JMenuItem("Load files",'L');
        JMenuItem saveFiles = new JMenuItem("Save files",'S');


        loadFiles.setAccelerator(KeyStroke.getKeyStroke('L',KeyEvent.CTRL_DOWN_MASK));
        saveFiles.setAccelerator(KeyStroke.getKeyStroke('S',KeyEvent.CTRL_DOWN_MASK));
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        loadFiles.addActionListener(this);
        saveFiles.addActionListener(this);
        loadFiles.setActionCommand("L");
        saveFiles.setActionCommand("S");
        menuBar.add(fileMenu);
        fileMenu.add(loadFiles);
        fileMenu.add(saveFiles);
        setJMenuBar(menuBar);


        JPanel leftSide = new JPanel();
        leftSide.setBorder(new EmptyBorder(10,10,10,10));
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));

        JPanel billTablePanels = new JPanel();
        billTablePanels.setLayout( new FlowLayout());
        billTablePanels.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Invoice Table", TitledBorder.LEFT,
                TitledBorder.TOP));


        invoice = new JTable();
        displayInvoice();
        invoice.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    Bill selectedBill = invoicesArray[row];
                    invoiceNumberValue.setText( String.valueOf( selectedBill.getInvoiceNumber() ) );
                    if ( selectedBill.getBillDate() == null) {
                        billDateValue.setText("");
                    }else {
                        billDateValue.setText(selectedBill.getBillDate());
                    }
                    if ( selectedBill.getCustomerName() == null) {
                        cxNameValue.setText("");
                    } else {
                        cxNameValue.setText(selectedBill.getCustomerName());
                    }
                    if(selectedBill.getItems_Array() == null ) {
                        billTotalValue.setText("0");
                        String[] columnNames = {"No.", "Item Name", "Item Price", "Count", "Item Total"};
                        String[][] data = { {"1","","","",""}};
                        boolean[] isEditable = {false,true,true,true,false};
                        DefaultTableModel model = new DefaultTableModel(data, columnNames){
                            @Override
                            public boolean isCellEditable(int row, int column) {
                                return isEditable[column];
                            }
                        };
                        billItemsTable.setModel( model);
                    } else {
                        billTotalValue.setText( String.valueOf( selectedBill.getTotalAmount() ) );
                        String[] columnNames = {
                                "No.", "Item Name", "Item Price", "Count", "Item Total"};
                        String[][] data = selectedBill.getItems_Array();
                        boolean[] isEditable = {false,true,true,true,false};
                        DefaultTableModel model = new DefaultTableModel(data, columnNames){
                            @Override
                            public boolean isCellEditable(int row, int column) {
                                return isEditable[column];
                            }
                        };
                        billItemsTable.setModel( model );
                    }
                }
            }
        });
        JScrollPane invoices_sp = new JScrollPane(invoice);
        billTablePanels.add(invoices_sp);



        JPanel leftSideButtons = new JPanel();
        JButton btn = new JButton("Create New Invoice");
        leftSideButtons.setLayout(new FlowLayout());


        btn.setActionCommand("C");
        btn.addActionListener(this);
        JButton delete = new JButton("Delete Invoice");
        delete.addActionListener(this);
        delete.setActionCommand("D");
        leftSideButtons.add(delete);
        leftSideButtons.add(btn);

        leftSide.add(billTablePanels);
        leftSide.add(leftSideButtons);
        add(leftSide);

        JPanel rightSide = new JPanel();
        rightSide.setBorder(new EmptyBorder(10,10,10,200));
        rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.Y_AXIS));


        JPanel invoiceData = new JPanel();
        invoiceData.setBorder(new EmptyBorder(10,0,10,200));
        invoiceData.setLayout(new GridLayout(4, 1, 0, 7));

        JLabel invoiceNum = new JLabel("Invoice Number");
        invoiceData.add(invoiceNum);

        invoiceNumberValue = new JLabel("");
        invoiceData.add(invoiceNumberValue);

        JLabel invoiceDate = new JLabel("Invoice Date");
        invoiceData.add(invoiceDate);

        billDateValue = new JTextField();
        invoiceData.add(billDateValue);


        JLabel customerName = new JLabel("Customer Name");
        invoiceData.add(customerName);

        cxNameValue = new JTextField();
        invoiceData.add(cxNameValue);







        JLabel invoiceTotal = new JLabel("Invoice Total");
        invoiceData.add(invoiceTotal);

        billTotalValue = new JLabel("");
        invoiceData.add(billTotalValue);





        JPanel invoiceItems = new JPanel();
        invoiceItems.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Invoice Items", TitledBorder.LEFT,
                TitledBorder.TOP));
        String[][] invoiceItemsData ={};
        String[] invoiceItemsColumn ={"No.","Item Name","Item Price","Count","Item Total"};
        DefaultTableModel model = new DefaultTableModel(invoiceItemsData, invoiceItemsColumn);
        billItemsTable = new JTable(model);
        JLabel information = new JLabel("You can insert and remove items from item table");
        JLabel information2 = new JLabel(" by pressing delete and insert key on keyboard.");
        invoiceItems.add(information);
        invoiceItems.add(information2);
        JScrollPane invoiceItems_sp = new JScrollPane(billItemsTable);
        invoiceItems.add(invoiceItems_sp);



        JButton save = new JButton("Save");
        save.addActionListener(this);
        save.setActionCommand("SE");
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        cancel.setActionCommand("CE");


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(save);
        buttonsPanel.add(cancel);

        rightSide.add(invoiceData);
        rightSide.add(invoiceItems);
        rightSide.add(buttonsPanel);

        add(rightSide);
        setLayout(new GridLayout(1, 2, 10, 15));
        setSize(1500, 700);
        setLocation(100,100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        billItemsTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()==KeyEvent.VK_INSERT){
                    DefaultTableModel model = (DefaultTableModel) billItemsTable.getModel();
                    model.addRow(new Object[]{String.valueOf( model.getRowCount() + 1 ), "", "", ""});
                }else if(e.getKeyCode()==KeyEvent.VK_DELETE){
                    int lastRow = billItemsTable.getRowCount();
                    int selectedRow = billItemsTable.getSelectedRow();
                    DefaultTableModel model = (DefaultTableModel) billItemsTable.getModel();
                    model.removeRow(selectedRow);
                    if (lastRow-1 == 0){
                        model.addRow(new Object[]{String.valueOf( model.getRowCount() + 1 ), "", "", ""});
                    }else {
                        billItemsTable.requestFocus();
                        billItemsTable.setRowSelectionInterval(0,0);

                    }
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {

            case "L":
                invoiceListener.UpdateBill(invoice);
                invoicesArray = invoiceListener.loadBill(invoicesArray);
                displayInvoice();
                break;
            // Save file
            case "S":
                invoiceListener.UpdateBill(invoice);
                invoiceListener.saveDate(invoicesArray);
                break;
            case "C":
                // Create empty row
                DefaultTableModel model = (DefaultTableModel) invoice.getModel();
                int invRowCount = invoice.getRowCount();
                if (invoice.getValueAt(invRowCount-1,3) == ""){
                    JOptionPane.showMessageDialog(new JFrame(),
                            "The last invoice is empty " + "Invoice No." + invRowCount + "" +
                                    " \n and doesn't contain a total amount", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                else {
                    model.addRow(new Object[]{String.valueOf(model.getRowCount() + 1), "", "", ""});
                    invoice.setModel(model);
                    Bill[] newInvoicesArray = Arrays.copyOf(invoicesArray, invoicesArray.length + 1);
                    Bill newBill = new Bill();
                    newBill.setInvoiceNumber(model.getRowCount());
                    invoicesArray = newInvoicesArray;
                    invoicesArray[invoicesArray.length - 1] = newBill;
                }
                break;
            case "D":

                // Delete selected row
                model = (DefaultTableModel) invoice.getModel();
                DefaultTableModel model2 = (DefaultTableModel) billItemsTable.getModel();
                int row = invoice.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(new JFrame(), "Select an invoice", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } if (row == 0){
                JOptionPane.showMessageDialog(new JFrame(), "You can't delete the master invoice you can update it", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            else {
                model.removeRow(row);
                invoice.setModel(model);
                Bill[] deleteInvoicesArray = new Bill[ invoicesArray.length - 1];
                for (int i = 0; i < deleteInvoicesArray.length; i++) {
                    if (i < row) {
                        deleteInvoicesArray[i] = invoicesArray[i];

                    }
                    else {
                        invoicesArray[i+1].setInvoiceNumber( invoicesArray[i+1].getInvoiceNumber() - 1 );
                        deleteInvoicesArray[i] = invoicesArray[i+1];
                    }

                }
                invoicesArray = deleteInvoicesArray;
                billDateValue.setText("");
                cxNameValue.setText("");
                billTotalValue.setText("");
                invoiceNumberValue.setText("");
                model2.setRowCount(0);
                model2.fireTableDataChanged();
            }

                break;
            case "SE":
                int rowCount = billItemsTable.getRowCount();
                billItemsTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
                try {

                    if (billItemsTable.getValueAt(billItemsTable.getSelectedRow(), 2) == "") {
                        throw new NoSuchAttributeException() ;
                    } if (billItemsTable.getValueAt(rowCount-1, 3) == "") {
                        throw new NoSuchAttributeException() ;
                    }
                    int invoiceNumber = Integer.parseInt( invoiceNumberValue.getText() );
                    invoicesArray[invoiceNumber - 1].setBillDate( billDateValue.getText() );
                    invoicesArray[invoiceNumber - 1].setCustomerName( cxNameValue.getText() );
                    model = (DefaultTableModel) billItemsTable.getModel();
                    int itemsCount = 0;
                    for (int i = 0; i < model.getRowCount(); i++) {
                        if( model.getValueAt(i, 0) == "" ) { break;}
                        itemsCount++;
                    }
                    com.mycompany.controller.component[] components = new component[itemsCount];

                    for (int i = 0; i < itemsCount; i++) {
                        com.mycompany.controller.component component = new component();
                        component.setItemName( (String) model.getValueAt(i, 1) );
                        component.setItemPrice(Integer.parseInt((String) model.getValueAt(i, 2)));
                        component.setCountValue(Integer.parseInt((String) model.getValueAt(i, 3)));
                        model.setValueAt( String.valueOf(component.getItemTotalValue()) , i, 4);
                        components[i] = component;
                    }
                    invoicesArray[invoiceNumber - 1].setItems(components);
                    billTotalValue.setText(String.valueOf(invoicesArray[invoiceNumber - 1].getTotalAmount()));
                    model.addRow(new Object[]{String.valueOf( model.getRowCount() + 1 ), "", "", ""});
                    displayInvoice();
                    if (billItemsTable.getValueAt(0,4) == ""){
                        throw new RuntimeException();
                    }
                }
                catch (NoSuchAttributeException saveButton) {
                    JOptionPane.showMessageDialog(new JFrame(), "You must add a price for the item and their count.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                catch (RuntimeException saveButton){
                    JOptionPane.showMessageDialog(new JFrame(), "You can't save an empty invoice.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "CE":

                // Discard change
                int selectedInvoice;
                try {

                    selectedInvoice = Integer.parseInt(invoiceNumberValue.getText());
                    billDateValue.setText("");
                    cxNameValue.setText("");
                    billTotalValue.setText("");
                    invoiceNumberValue.setText("");

                    String[] columnNames = {
                            "No.", "Item Name", "Item Price", "Count", "Item Total"};
                    String [][] originalData = invoicesArray[selectedInvoice - 1].getItems_Array();
                    DefaultTableModel originalModel = new DefaultTableModel(originalData, columnNames);
                    billItemsTable.setModel(originalModel);
                    DefaultTableModel modelA = (DefaultTableModel) billItemsTable.getModel();
                    modelA.setRowCount(0);
                    modelA.fireTableDataChanged();
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(new JFrame(), "Select an invoice", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                break;

        }
    }

    private void displayInvoice() {
        String[][] invoicesData = new String[invoicesArray.length][4];
        for (int i = 0; i < invoicesArray.length; i++) {
            invoicesData[i] = invoicesArray[i].getBillArray();
        }

        String[] invoicesColumn ={"No.","Date","Customer","Total"};
        DefaultTableModel model = new DefaultTableModel(invoicesData, invoicesColumn){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        invoice.setModel( model );
    }
}