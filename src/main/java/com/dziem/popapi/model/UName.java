package com.dziem.popapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="\"u_name\"")
public class UName {
    @Id
    private UUID userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
    private String name;
    private LocalDateTime lastUpdate;
}
