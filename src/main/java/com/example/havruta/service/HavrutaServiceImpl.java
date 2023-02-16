package com.example.havruta.service;

import com.example.havruta.data.dao.HavrutaDao;
import com.example.havruta.data.dto.*;
import com.example.havruta.data.entity.CategoryEntity;
import com.example.havruta.data.entity.GroupEntity;
import com.example.havruta.data.repository.CategoryClosureRepository;
import com.example.havruta.data.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HavrutaServiceImpl implements HavrutaService {
    private final HavrutaDao havrutaDao;
    private final CategoryRepository categoryRepository;
    private final CategoryClosureRepository categoryClosureRepository;

    @Autowired
    public HavrutaServiceImpl(
            HavrutaDao havrutaDao,
            CategoryRepository categoryRepository,
            CategoryClosureRepository categoryClosureRepository
    ) {
        this.havrutaDao = havrutaDao;
        this.categoryRepository = categoryRepository;
        this.categoryClosureRepository = categoryClosureRepository;
    }

    public GroupListResponseDto mainPage() {
        List<GroupEntity> groupEntityList = havrutaDao.findAllGroup();
        List<GroupDto> groupDtoList = new ArrayList<GroupDto>();

        GroupListResponseDto dto = new GroupListResponseDto();
        for (GroupEntity groupEntity : groupEntityList) {
            GroupDto groupDto = new GroupDto();
            groupDto.setGroupId(groupEntity.getGroupId());
            groupDto.setGroupName(groupEntity.getGroupName());
            groupDtoList.add(groupDto);
        }
        dto.setGroupList(groupDtoList);
        return dto;
    }


}
