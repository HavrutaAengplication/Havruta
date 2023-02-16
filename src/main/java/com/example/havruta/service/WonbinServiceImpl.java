package com.example.havruta.service;

import com.example.havruta.data.dao.WonbinDao;
import com.example.havruta.data.dto.*;
import com.example.havruta.data.entity.GroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.midi.Track;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WonbinServiceImpl implements WonbinService{
    private final WonbinDao wonbinDao;

    @Autowired
    public WonbinServiceImpl(WonbinDao wonbinDao){this.wonbinDao = wonbinDao;}

    public SpecificGroupResponseDto specificGroupPage(Integer groupId){
        //사용자 정보 확인 및 isAdmin, isMember 받아오기
        //groupId로 groupName, root_category_Id 찾기
        //root_category_ID로 자식 카테고리 Id, name, depth 다 받아오기
        SpecificGroupResponseDto dto = new SpecificGroupResponseDto();
        GroupEntity groupEntity = new GroupEntity();
        Optional<GroupEntity> searchResult = wonbinDao.findGroupById(groupId);

        if(searchResult.isPresent()) {
            groupEntity = searchResult.get();
        }else {
            groupEntity = searchResult.orElse(null);
        }

        //일단 임의로 데이터 생성
        List<CategoryDto> categoryDtoList = new ArrayList<CategoryDto>();
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryId(10);
        categoryDto.setCategoryName("qwerasdf");
        categoryDto.setDepth(15);
        categoryDtoList.add(categoryDto);

        dto.setGroupName(groupEntity.getGroupName());
        dto.setCategoryList(categoryDtoList);
        dto.setIsAdmin(Boolean.TRUE);
        dto.setIsMember(Boolean.TRUE);
        return dto;
    }

    public AdminResponseDto admin(Integer groupId) {
        //사용자 정보 확인
        //groupId로 groupName, root_category_Id 찾기
        //root_category_ID로 자식 카테고리 Id, name 다 받아오기
        return null;
    }

    public AdminMembersResponseDto adminMembers(Integer groupId) {
        //사용자 정보 확인 및 isAdmin
        //groupId로 user_ID 찾고 is_Member에 따라 다른 리스트에 넣기
        return null;
    }

    public ResponseDto designateAdmin(Integer newAdminId, Integer groupId) {
        //사용자 정보 확인
        //groupId와 newAdminId로 members에서 튜플 찾아서 isAdmin 바꿔주기
        return null;
    }

    public ResponseDto dropMember(Integer userId, Integer groupId) {
        //사용자 정보 확인
        //groupId와 userId로 members에서 튜플 찾아서 제거
        return null;
    }

    public ResponseDto confirm(Integer userId, Integer groupId) {
        //사용자 정보 확인
        //groupId와 userId로 members에서 튜플 찾아서 isMember 바꿔주기
        return null;
    }

    public ResponseDto deleteGroup(Integer groupId) {
        //사용자 정보 확인
        //groupId로 그룹 찾아서 삭제
        return null;
    }

    public ResponseDto updateGroup(String newGroupName, Integer groupId) {
        //사용자 정보 확인
        //groupId로 그룹 찾아서 새이름을 변경
        return null;
    }
}
