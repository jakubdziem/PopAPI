package com.dziem.popapi.repository;

import com.dziem.popapi.model.SocialMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SocialMediaRepository extends JpaRepository<SocialMedia, Long> {
    @Query(value = "select * from social_media where type = ?1" , nativeQuery = true)
    List<SocialMedia> findAllByType(String type);

}
