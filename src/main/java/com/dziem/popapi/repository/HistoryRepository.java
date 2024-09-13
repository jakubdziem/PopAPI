package com.dziem.popapi.repository;

import com.dziem.popapi.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
}
