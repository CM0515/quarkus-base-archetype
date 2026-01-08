package com.example.microservice.domain.user.entity;

import com.example.microservice.common.entity.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(name = "name", length = 255)
    private String name;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    @Column(name = "email", length = 255, unique = true)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", length = 500)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 50)
    private UserRole role = UserRole.USER;

    public User() {
    }

    public User(String name, String email, String phone, String address, UserRole role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public enum UserRole {
        ADMIN,
        USER,
        GUEST
    }
}
