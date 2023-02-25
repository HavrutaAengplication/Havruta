package com.example.havruta.data.dao;

import com.example.havruta.data.entity.GroupEntity;
import com.example.havruta.data.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface HavrutaDao {
    /* 각 entity 별로 Dao 만드는 것이 좋아보임. GroupDao로 수정하는 것 어떤지? */
    List<GroupEntity> findAllGroup();
    Optional<GroupEntity> findGroupById(Integer id);
    Optional<UserEntity> findByEmail(String email);
}
