package org.example.restaurant_management_system.Model;

import java.util.Date;


public class CustomerData {

    private Integer id;
    private Integer customerID;
    private Double total;
    private Date date;
    private String emUsername;


    public CustomerData(Integer id, Integer customerID, Double total) {
        this.id = id;
        this.customerID = customerID;
        this.total = total;
    }

    public CustomerData(Integer id, Integer customerID, Double total, Date date, String emUsername) {
        this.id = id;
        this.customerID = customerID;
        this.total = total;
        this.date = date;
        this.emUsername = emUsername;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public Double getTotal() {
        return total;
    }

    public Date getDate() {
        return date;
    }

    public String getEmUsername() {
        return emUsername;
    }

}