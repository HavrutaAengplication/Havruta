package com.example.havruta.service;

import com.example.havruta.data.dao.WonbinDao;
import com.example.havruta.data.dto.*;
import com.example.havruta.data.entity.*;
import com.example.havruta.errorAndException.ErrorCode;
import com.example.havruta.errorAndException.NoGroupException;
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
        //사용자 정보 확인 (우리가 발행한 토큰 넘어올 것) 및 isAdmin, isMember 받아오기 -> member entity 생성 필요?
        //groupId로 groupName, root_category_Id 찾기
        //root_category_ID로 자식 카테고리 Id, name, depth 다 받아오기 -> 쿼리 짜야 됨
        //root_category_ID가 parent_ID인 모든 category_closure 받아옴. 받아온 child_id로 같은 일 반복해서 한 리스트에 모아줌

        UserEntity userEntity = new UserEntity();//일단 임의로 userdata 생성 -> 원래는 토큰으로 알아내야 됨
        userEntity.setUserId(1);
        userEntity.setUserName("Kyle");
        userEntity.setUserEmail("gogo4261@naver.com");//isAdmin에 따라?

        SpecificGroupResponseDto dto = new SpecificGroupResponseDto();

        GroupEntity groupEntity = new GroupEntity();
        Optional<GroupEntity> groupSearchResult = wonbinDao.findGroupById(groupId);

        if(groupSearchResult.isPresent()) {//그룹 검색 결과 확인
            groupEntity = groupSearchResult.get();
        }else {
            throw new NoGroupException("There is No Group", ErrorCode.NO_GROUP_ERROR);
        }

        MemberEntity memberEntity = new MemberEntity();
        Optional<MemberEntity> memberSearchResult = wonbinDao.findMemberById(userEntity.getUserId(), groupEntity.getGroupId());

        if(memberSearchResult.isPresent()) {//멤버 검색 결과 확인
            memberEntity = memberSearchResult.get();
        }else {
            System.out.println("member Serach Result Error\n");
            System.out.println(memberSearchResult);
            System.out.println(userEntity.getUserId());
            System.out.println(groupEntity.getGroupId());
            throw new NoGroupException("There is No Group", ErrorCode.NO_GROUP_ERROR);
        }

        List<CategoryClosureEntity> categoryClosureEntityList = wonbinDao.findClosuresByRootId(groupEntity.getRootCategoryId().getCategoryId());
        List<CategoryDto> categoryDtoList = new ArrayList<CategoryDto>();
        for(CategoryClosureEntity categoryClosureEntity : categoryClosureEntityList){
            CategoryEntity categoryEntity = new CategoryEntity();
            Optional<CategoryEntity> categorySerachResult = wonbinDao.findCategoryById(categoryClosureEntity.getChild().getCategoryId());
            if(categorySerachResult.isPresent()) {//멤버 검색 결과 확인
                categoryEntity = categorySerachResult.get();
            }else {
                throw new NoGroupException("There is No Group", ErrorCode.NO_GROUP_ERROR);
            }
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setCategoryId(categoryEntity.getCategoryId());
            categoryDto.setCategoryName(categoryEntity.getCategoryName());
            categoryDto.setDepth(categoryClosureEntity.getDepth());
            categoryDtoList.add(categoryDto);
        }

        dto.setGroupName(groupEntity.getGroupName());
        dto.setCategoryList(categoryDtoList);
        dto.setIsAdmin(memberEntity.getIsAdmin());
        dto.setIsMember(memberEntity.getIsMember());
        return dto;
    }

    public AdminResponseDto admin(Integer groupId) {
        //사용자 정보 확인
        //groupId로 groupName, root_category_Id 찾기
        //root_category_ID로 자식 카테고리 Id, name 다 받아오기 -> 쿼리짜기
        //root_category_ID가 parent_ID인 모든 category_closure 받아옴. 받아온 child_id로 같은 일 반복해서 한 리스트에 모아줌

        UserEntity userEntity = new UserEntity();//일단 임의로 userdata 생성 -> 원래는 토큰으로 알아내야 됨
        userEntity.setUserId(1);
        userEntity.setUserName("Kyle");
        userEntity.setUserEmail("gogo4261@naver.com");//isAdmin에 따라?

        AdminResponseDto dto = new AdminResponseDto();

        GroupEntity groupEntity = new GroupEntity();
        Optional<GroupEntity> groupSearchResult = wonbinDao.findGroupById(groupId);

        if(groupSearchResult.isPresent()) {
            groupEntity = groupSearchResult.get();
        }else {
            throw new NoGroupException("There is No Group", ErrorCode.NO_GROUP_ERROR);
        }

        List<CategoryClosureEntity> categoryClosureEntityList = wonbinDao.findClosuresByRootId(groupEntity.getRootCategoryId().getCategoryId());
        List<CategoryIdDto> categoryIdDtoList = new ArrayList<CategoryIdDto>();
        for(CategoryClosureEntity categoryClosureEntity : categoryClosureEntityList){
            CategoryEntity categoryEntity = new CategoryEntity();
            Optional<CategoryEntity> categorySerachResult = wonbinDao.findCategoryById(categoryClosureEntity.getChild().getCategoryId());
            if(categorySerachResult.isPresent()) {//멤버 검색 결과 확인
                categoryEntity = categorySerachResult.get();
            }else {
                throw new NoGroupException("There is No Group", ErrorCode.NO_GROUP_ERROR);
            }
            CategoryIdDto categoryIdDto = new CategoryIdDto();
            categoryIdDto.setCatergoryId(categoryEntity.getCategoryId());
            categoryIdDto.setCatergoryName(categoryEntity.getCategoryName());
            categoryIdDtoList.add(categoryIdDto);
        }

        dto.setGroupName(groupEntity.getGroupName());
        dto.setCategoryIdList(categoryIdDtoList);

        return dto;
    }

    public AdminMembersResponseDto adminMembers(Integer groupId) {
        //사용자 정보 확인 및 isAdmin
        //groupId로 member_Table에서 user_ID 찾고 is_Member에 따라 다른 리스트에 넣기
        UserEntity myUserEntity = new UserEntity();//일단 임의로 userdata 생성 -> 원래는 토큰으로 알아내야 됨
        myUserEntity.setUserId(1);
        myUserEntity.setUserName("Kyle");
        myUserEntity.setUserEmail("gogo4261@naver.com");//isAdmin에 따라?

        AdminMembersResponseDto dto = new AdminMembersResponseDto();

        GroupEntity groupEntity = new GroupEntity();
        Optional<GroupEntity> searchResult = wonbinDao.findGroupById(groupId);

        if(searchResult.isPresent()) {
            groupEntity = searchResult.get();
        }else {
            throw new NoGroupException("There is No Group", ErrorCode.NO_GROUP_ERROR);
        }

        List<MemberEntity> memberEntityList = wonbinDao.findMembersByGroupId(groupId);

        List<MemberDto> MemberDtoList = new ArrayList<MemberDto>();
        List<UserDto> UserDtoList = new ArrayList<UserDto>();

        for(MemberEntity memberEntity : memberEntityList){
            Optional<UserEntity> optionalUserEntity = wonbinDao.findUserById(memberEntity.getUserEntity().getUserId());
            if(optionalUserEntity.isPresent()){
                if(memberEntity.getIsMember() == 1){
                    MemberDto memberDto = new MemberDto();
                    UserEntity userEntity = optionalUserEntity.get();
                    memberDto.setUserId(userEntity.getUserId());
                    memberDto.setUserName(userEntity.getUserName());
                    memberDto.setIsAdmin(memberEntity.getIsAdmin());
                    MemberDtoList.add(memberDto);
                }
                else{
                    UserDto userDto = new UserDto();
                    UserEntity userEntity = optionalUserEntity.get();
                    userDto.setUserId(userEntity.getUserId());
                    userDto.setUserName(userEntity.getUserName());
                    UserDtoList.add(userDto);
                }
            }
        }

        dto.setMemberList(MemberDtoList);
        dto.setJoinList(UserDtoList);

        return dto;
    }

    public ResponseDto designateAdmin(Integer newAdminId, Integer groupId) {
        //사용자 정보 확인
        //groupId와 newAdminId로 members에서 튜플 찾아서 isAdmin 바꿔주기
        UserEntity myUserEntity = new UserEntity();//일단 임의로 userdata 생성 -> 원래는 토큰으로 알아내야 됨
        myUserEntity.setUserId(1);
        myUserEntity.setUserName("Kyle");
        myUserEntity.setUserEmail("gogo4261@naver.com");//isAdmin에 따라?

        Boolean result = wonbinDao.designateAdmin(newAdminId, groupId);

        ResponseDto dto = new ResponseDto();
        if(result) {
            dto.setMessage("success!");
        }
        else{
            dto.setMessage("failure..");
        }

        return dto;
    }

    public ResponseDto dropMember(Integer userId, Integer groupId) {
        //사용자 정보 확인
        //groupId와 userId로 members에서 튜플 찾아서 제거
        UserEntity myUserEntity = new UserEntity();//일단 임의로 userdata 생성 -> 원래는 토큰으로 알아내야 됨
        myUserEntity.setUserId(1);
        myUserEntity.setUserName("Kyle");
        myUserEntity.setUserEmail("gogo4261@naver.com");//isAdmin에 따라?

        Boolean result = wonbinDao.dropMember(userId, groupId);

        ResponseDto dto = new ResponseDto();
        if(result) {
            dto.setMessage("success!");
        }
        else{
            dto.setMessage("failure..");
        }

        return dto;
    }

    public ResponseDto confirm(Integer userId, Integer groupId) {
        //사용자 정보 확인
        //groupId와 userId로 members에서 튜플 찾아서 isMember 바꿔주기

        UserEntity myUserEntity = new UserEntity();//일단 임의로 userdata 생성 -> 원래는 토큰으로 알아내야 됨
        myUserEntity.setUserId(1);
        myUserEntity.setUserName("Kyle");
        myUserEntity.setUserEmail("gogo4261@naver.com");//isAdmin에 따라?

        Boolean result = wonbinDao.confirm(userId, groupId);

        ResponseDto dto = new ResponseDto();
        if(result) {
            dto.setMessage("success!");
        }
        else{
            dto.setMessage("failure..");
        }

        return dto;
    }

    public ResponseDto deleteGroup(Integer groupId) {
        //사용자 정보 확인
        //groupId로 그룹 찾아서 삭제

        UserEntity myUserEntity = new UserEntity();//일단 임의로 userdata 생성 -> 원래는 토큰으로 알아내야 됨
        myUserEntity.setUserId(1);
        myUserEntity.setUserName("Kyle");
        myUserEntity.setUserEmail("gogo4261@naver.com");//isAdmin에 따라?

        Boolean result = wonbinDao.deleteGroup(groupId);

        ResponseDto dto = new ResponseDto();
        if(result) {
            dto.setMessage("success!");
        }
        else{
            dto.setMessage("failure..");
        }

        return dto;
    }

    public ResponseDto updateGroup(String newGroupName, Integer groupId) {
        //사용자 정보 확인
        //groupId로 그룹 찾아서 새이름을 변경

        UserEntity myUserEntity = new UserEntity();//일단 임의로 userdata 생성 -> 원래는 토큰으로 알아내야 됨
        myUserEntity.setUserId(1);
        myUserEntity.setUserName("Kyle");
        myUserEntity.setUserEmail("gogo4261@naver.com");//isAdmin에 따라?

        Boolean result = wonbinDao.updateGroup(newGroupName, groupId);

        ResponseDto dto = new ResponseDto();
        if(result) {
            dto.setMessage("success!");
        }
        else{
            dto.setMessage("failure..");
        }

        return dto;
    }
}
