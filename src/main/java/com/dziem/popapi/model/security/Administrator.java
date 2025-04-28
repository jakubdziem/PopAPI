package com.dziem.popapi.model.security;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.UUID;
@Data
@Entity
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nickname;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "administator_roles", joinColumns = @JoinColumn(name = "administrator_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @ToString.Exclude
    private List<Role> roles;
}
