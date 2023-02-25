package com.example.havruta.service;

import com.example.havruta.data.dao.HavrutaDao;
import com.example.havruta.data.dto.*;
import com.example.havruta.data.entity.CategoryEntity;
import com.example.havruta.data.entity.GroupEntity;
import com.example.havruta.data.entity.UserEntity;
import com.example.havruta.data.repository.CategoryClosureRepository;
import com.example.havruta.data.repository.CategoryRepository;
import com.example.havruta.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HavrutaServiceImpl implements HavrutaService {
    private final HavrutaDao havrutaDao;
    private final JwtUtil jwtUtil;

    @Autowired
    public HavrutaServiceImpl(HavrutaDao havrutaDao, JwtUtil jwtUtil) {
        this.havrutaDao = havrutaDao;
        this.jwtUtil = jwtUtil;
    }

    public ResponseDto signIn(SignInRequestDto reqbody){
        /*
            1. reqbody.googleToken validation
            2. reqbody.googleToken 에서 Email 추출.
            3. email + reqbody.userName 으로 users에 저장
        */
        ResponseDto dto = new ResponseDto();
        return dto;
    }

    public UserNameDto login(LoginRequestDto req){
        String userEmail = "hosung_user_email";
        UserNameDto dto = new UserNameDto();
        Optional<UserEntity> userEntity = havrutaDao.findByEmail(userEmail);

        if(userEntity.isPresent()) {
            dto.setUserName(userEntity.get().getUserName());
        }
        else{
            dto.setUserName("NOT PRESENT");
        }
        return dto;
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
