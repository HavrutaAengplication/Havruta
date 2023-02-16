package com.example.havruta.data.dao;

import com.example.havruta.data.entity.CategoryEntity;
import com.example.havruta.data.entity.GroupEntity;

import java.util.List;
import java.util.Optional;

public interface WonbinDao {
    public List<CategoryEntity> findAllCategory();
    public Optional<GroupEntity> findGroupById(Integer groupId);
}
