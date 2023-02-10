package com.example.havruta.service;

import com.example.havruta.data.dao.HavrutaDao;
import com.example.havruta.data.dao.impl.HavrutaDaoImpl;
import com.example.havruta.data.dto.GroupDto;
import com.example.havruta.data.dto.GroupListResponseDto;
import com.example.havruta.data.entity.GroupEntity;
import com.example.havruta.data.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class HavrutaServiceImpl implements HavrutaService{
    private final HavrutaDao havrutaDao;

    @Autowired
    public HavrutaServiceImpl(HavrutaDao havrutaDao) {
        this.havrutaDao = havrutaDao;
    }

    public GroupListResponseDto mainPage(){
        List<GroupEntity> groupEntityList = havrutaDao.findAllGroup();
        List<GroupDto> groupDtoList = new ArrayList<GroupDto>();

        GroupListResponseDto dto = new GroupListResponseDto();
        for(GroupEntity groupEntity : groupEntityList){
            GroupDto groupDto = new GroupDto();
            groupDto.setGroupId(groupEntity.getGroupId());
            groupDto.setGroupName(groupEntity.getGroupName());
            groupDtoList.add(groupDto);
        }
        dto.setGroupList(groupDtoList);
        return dto;
    }

}
