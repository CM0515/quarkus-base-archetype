package com.example.microservice.domain.client.domain.model;

public class Client {

    private String name;
    private  String email;
    private  String phone;
    private   String address;
    private  ClientRole role;

    public Client(String name, String email, String phone, String address, ClientRole role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }
    public void update(
            String name,
            String email,
            String phone,
            String address,
            ClientRole role
    ) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public String name() {
        return name;
    }

    public String email() {
        return email;
    }

    public String phone() {
        return phone;
    }

    public String address() {
        return address;
    }

    public ClientRole role() {
        return role;
    }

}
