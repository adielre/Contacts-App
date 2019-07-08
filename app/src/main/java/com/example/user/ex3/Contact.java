package com.example.user.ex3;

public class Contact
{
    //fields
    private String name;
    private String phone;

    //constructor
    public Contact(String name, String phone)
    {
        this.name=name;
        this.phone=phone;
    }

    //getters and setters
    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setName(String name) { this.name = name; }

}// end Contact class