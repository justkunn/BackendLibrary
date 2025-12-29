package com.example.library.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.library.Entity.notificationEntity;

@Repository
public interface NotificationRepository extends JpaRepository<notificationEntity, Integer>{
    Optional<notificationEntity> findById(Integer idNotification);
}