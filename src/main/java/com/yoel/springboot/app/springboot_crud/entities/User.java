package com.yoel.springboot.app.springboot_crud.entities;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@ExistsByUsername
    @Column(nullable = false, unique = true)
    @NotBlank(message = "no puede estar vacío")
    @Size(min = 3, message = "debe tener al menos 3 caracteres")
    private String username;
    
    @Column(nullable = false)
    @JsonProperty( access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "no puede estar vacío")
    private String password;

    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = @jakarta.persistence.JoinColumn(name = "user_id"),
        inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "role_id"),
        uniqueConstraints = {
            @jakarta.persistence.UniqueConstraint(columnNames = {"user_id", "role_id"})//al estar los dos valores la restricción es por par de valores
        }
    )
    private List<Role> roles;

    @Transient// Este campo no se persistirá en la base de datos
    @JsonProperty( access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    private boolean enabled = true;

}
