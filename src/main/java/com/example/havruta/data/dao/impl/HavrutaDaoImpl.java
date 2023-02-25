package com.example.havruta.data.dao.impl;

import com.example.havruta.data.dao.HavrutaDao;
import com.example.havruta.data.entity.GroupEntity;
import com.example.havruta.data.entity.UserEntity;
import com.example.havruta.data.repository.GroupRepository;
import com.example.havruta.data.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HavrutaDaoImpl implements HavrutaDao {

    private GroupRepository groupRepository;
    private UserRepository userRepository;

    public HavrutaDaoImpl(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<GroupEntity> findAllGroup() {
        return groupRepository.findAll();
    }

    public Optional<GroupEntity> findGroupById(Integer id){return groupRepository.findById(id);}

    public Optional<UserEntity> findByEmail(String email){return userRepository.findByUserEmail(email);}
}
