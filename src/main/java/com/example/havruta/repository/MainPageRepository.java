package com.example.havruta.repository;

import com.example.havruta.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainPageRepository  extends JpaRepository<UserEntity, Integer> {

}
