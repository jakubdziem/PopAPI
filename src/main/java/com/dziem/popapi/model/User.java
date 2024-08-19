package com.dziem.popapi.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "\"user\"")
public class User {
    @Id
    @Column(name="user_id")
    private UUID userId;
    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Stats statistics;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Score> bestScores;
}
