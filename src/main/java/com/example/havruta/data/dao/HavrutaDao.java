package com.example.havruta.data.dao;

import com.example.havruta.data.entity.GroupEntity;

import java.util.List;

public interface HavrutaDao {
    List<GroupEntity> findAllGroup();
}
