package com.example.microservice.domain.client.infrastructure.entity;

import com.example.microservice.domain.client.domain.model.ClientRole;
import com.example.microservice.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="clients")
public class ClientEntity extends PanacheEntityBase {

@Id
@GeneratedValue
public Long id;

@NotBlank(message = "El nombre es requerido")
@JsonProperty("name")
public String name;

@NotBlank(message = "El email es requerido")
@Email(message = "El email debe ser válido")
@JsonProperty("email")
public String email;

@JsonProperty("phone")
public String phone;

@JsonProperty("address")
public String address;

@JsonProperty("role")
public ClientRole role;
}
