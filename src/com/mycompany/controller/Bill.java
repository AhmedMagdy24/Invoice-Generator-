package com.mycompany.controller;


public class Bill {
    private String invoiceDate;
    private int invoiceNumber;
    private component[] components;
    private String customerName;
    public Bill(){}
    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }


    public void setBillDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public void setItems(component[] components) {
        this.components = components;
    }
    public int getInvoiceNumber() {
        return invoiceNumber;
    }
    public String getBillDate() {
        return invoiceDate;
    }
    public String getCustomerName() {
        return customerName;
    }
    public int getTotalAmount() {
        int totalAmount = 0;
        for (component component : this.components) {
            if (component == null) {
                break;
            }
            totalAmount += component.getItemTotalValue();
        }
        return totalAmount;
    }


    public String[][] getItems_Array() {
        if (components == null) {
            return null;
        }
        String [][] items_Array = new String[this.components.length][5];
        for (int i = 0; i < this.components.length; i++) {
            items_Array[i][0] = String.valueOf( i + 1 );
            items_Array[i][1] = this.components[i].getItemName();
            items_Array[i][2] = String.valueOf( this.components[i].getItemPriceValue() );
            items_Array[i][3] = String.valueOf( this.components[i].getCountValue() );
            items_Array[i][4] = String.valueOf( this.components[i].getItemTotalValue() );
        }
        return items_Array;
    }

    public String[] getBillArray() {
        String[] invoice_Array = new String[4];
        invoice_Array[0] = String.valueOf( getInvoiceNumber() );
        invoice_Array[1] = String.valueOf( getBillDate() );
        invoice_Array[2] = String.valueOf( getCustomerName() );
        invoice_Array[3] = String.valueOf( getTotalAmount() );
        return invoice_Array;
    }
}
