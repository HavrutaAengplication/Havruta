package com.example.havruta.data.dao.impl;

import com.example.havruta.data.dao.WonbinDao;
import com.example.havruta.data.entity.CategoryEntity;
import com.example.havruta.data.entity.GroupEntity;
import com.example.havruta.data.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class WonbinDaoImpl implements WonbinDao {
    private CategoryRepository categoryRepository;
    private CategoryClosureRepository categoryClosureRepository;
    private GroupRepository groupRepository;
    private ProblemRepository problemRepository;
    private UserRepository userRepository;

    @Autowired
    public WonbinDaoImpl(CategoryRepository categoryRepository,
                         CategoryClosureRepository categoryClosureRepository,
                         GroupRepository groupRepository,
                         ProblemRepository problemRepository,
                         UserRepository userRepository
                         ) {
        this.categoryRepository = categoryRepository;
        this.categoryClosureRepository = categoryClosureRepository;
        this.groupRepository = groupRepository;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<CategoryEntity> findAllCategory(){return categoryRepository.findAll();}

    @Override
    public Optional<GroupEntity> findGroupById(Integer groupId){return groupRepository.findById(groupId); }
}
