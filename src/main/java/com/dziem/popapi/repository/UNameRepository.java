package com.dziem.popapi.repository;

import com.dziem.popapi.model.UName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UNameRepository extends JpaRepository<UName, UUID> {
}
