package com.example.havruta.data.repository;

import com.example.havruta.data.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupEntity,Integer> {
}
