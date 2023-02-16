package com.example.havruta.data.dao.impl;

import com.example.havruta.data.dao.HavrutaDao;
import com.example.havruta.data.entity.GroupEntity;
import com.example.havruta.data.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HavrutaDaoImpl implements HavrutaDao {

    private GroupRepository groupRepository;

    @Autowired
    public HavrutaDaoImpl(GroupRepository groupRepository){
        this.groupRepository = groupRepository;
    }
    @Override
    public List<GroupEntity> findAllGroup() {
        return groupRepository.findAll();
    }

    public Optional<GroupEntity> findGroupById(Integer id){return groupRepository.findById(id);}
}
