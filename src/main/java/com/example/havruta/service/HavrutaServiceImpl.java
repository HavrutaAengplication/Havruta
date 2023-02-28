package com.example.havruta.service;

import com.example.havruta.data.dao.HavrutaDao;
import com.example.havruta.data.dto.*;
import com.example.havruta.data.entity.*;
import com.example.havruta.data.entity.serializable.CategoryProblemId;
import com.example.havruta.data.entity.serializable.ClosureId;
import com.example.havruta.errorAndException.ErrorCode;
import com.example.havruta.errorAndException.NoGroupException;
import com.example.havruta.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HavrutaServiceImpl implements HavrutaService {
    private final HavrutaDao havrutaDao;
    private final JwtUtil jwtUtil;

    @Autowired
    public HavrutaServiceImpl(HavrutaDao havrutaDao, JwtUtil jwtUtil) {
        this.havrutaDao = havrutaDao;
        this.jwtUtil = jwtUtil;
    }

    public ResponseDto signUp(SignInRequestDto reqbody){
        /*
            1. reqbody.googleToken validation
            2. reqbody.googleToken 에서 Email 추출.
            3. email + reqbody.userName 으로 users에 저장
        */
        ResponseDto dto = new ResponseDto();
        String email = "EmailFromGoogleToken3";
        Optional<UserEntity> userEntity = havrutaDao.signUp(email ,reqbody.getUserName());
        if(userEntity.isPresent()){
            dto.setMessage("success");
        }
        else{
            dto.setMessage("fail");
        }
        return dto;
    }

    public UserNameDto login(LoginRequestDto req){
        String userEmail = "EmailFromGoogleToken";
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

    public ResponseDto newGroup(String token, NewGroupRequestDto reqbody){
        ResponseDto dto = new ResponseDto();
        Integer userID = jwtUtil.extractUserId(token);
        Optional<GroupEntity> groupEntity = havrutaDao.saveNewGroup(userID, reqbody.getGroupName());
        if(groupEntity.isPresent()){
            dto.setMessage("new Group creation success");
        }
        else{
            dto.setMessage("new Group creation failed");
        }
        return dto;
    }
    public MyPageResponseDto myPage(String token){
        MyPageResponseDto dto = new MyPageResponseDto();
        Integer userID = jwtUtil.extractUserId(token);
        Optional<UserEntity> userEntity = havrutaDao.findByUserId(userID);
        if(userEntity.isPresent()) {
            List<GroupEntity> groupEntityList = havrutaDao.findByUserEntity(userEntity.get());
            List<GroupDto> groupDtoList = new ArrayList<>();
            for(GroupEntity groupEntity : groupEntityList){
                GroupDto groupDto = new GroupDto();
                groupDto.setGroupId(groupEntity.getGroupId());
                groupDto.setGroupName(groupEntity.getGroupName());
                groupDtoList.add(groupDto);
            }
            dto.setUserName(userEntity.get().getUserName());
            dto.setGroupList(groupDtoList);
        }
        return dto;
    }

    public ResponseDto changeUserName(String token, String userName){
        ResponseDto dto = new ResponseDto();
        Integer userID = jwtUtil.extractUserId(token);
        if(havrutaDao.changeUserName(userID,userName).isPresent())
            dto.setMessage("name change success");
        else
            dto.setMessage("name change failed");
        return dto;
    }

    public ResponseDto deleteUser(String token){
        // 에러 시?
        ResponseDto dto = new ResponseDto();
        System.out.println("token = " + token);
        Integer userID = jwtUtil.extractUserId(token);
        havrutaDao.deleteUser(userID);
        dto.setMessage("Success");
        return dto;
    }

    public MyProblemListDto getMyProblemList(String token){
        MyProblemListDto myProblemListDto = new MyProblemListDto();
        List<MyProblemDto> myProblemDtoList = new ArrayList<>();
        Integer userID = jwtUtil.extractUserId(token);
        UserEntity userEntity = havrutaDao.findByUserId(userID).get();
        List<ProblemEntity> myProblemList = havrutaDao.getMyProblemList(userEntity);
        for(ProblemEntity problemEntity : myProblemList){
            MyProblemDto myProblemDto = new MyProblemDto();

            GroupEntity groupEntity = havrutaDao.findGroupByProblemEntity(problemEntity).get();
            myProblemDto.setGroupId(groupEntity.getGroupId());
            myProblemDto.setGroupName(groupEntity.getGroupName());

            List<List<CategoryClosureEntity>> categoryIdList = havrutaDao.findCategoriesListByProblemEntity(problemEntity);
            List<PathDto> pathDtoList = new ArrayList<>();
            for(List<CategoryClosureEntity> categoryClosureEntityList : categoryIdList){
                List<CategoryIdDto> categoryIdDtoList = new ArrayList<>();
                for(CategoryClosureEntity categoryClosure : categoryClosureEntityList){
                    Integer parentId = categoryClosure.getParent().getCategoryId();
                    String categoryName = categoryClosure.getParent().getCategoryName();
                    categoryIdDtoList.add(new CategoryIdDto(parentId,categoryName));
                }
                pathDtoList.add(new PathDto(categoryIdDtoList));
            }
            myProblemDto.setCategoryIdList(pathDtoList);

            myProblemDto.setProblemType(problemEntity.getProblemType());

            myProblemDto.setProblemQuestion(problemEntity.getProblemQuestion());

            List<ItemDto> itemDtoList = new ArrayList<>();
            Map<Integer, String> map = problemEntity.getProblemCandidate();
            if(map != null) {
                map.forEach((key, value) -> {
                    ItemDto itemDto = new ItemDto();
                    itemDto.setItem(value);
                    itemDtoList.add(itemDto);
                });
            }
            myProblemDto.setProblemCandidate(itemDtoList);

            myProblemDto.setProblemAnswer(problemEntity.getProblemAnswer());

            List<ImageDto> imageDtoList = new ArrayList<>();
            map = problemEntity.getProblemCandidate();
            if(map != null) {
                map.forEach((key, value) -> {
                    ImageDto imageDto = new ImageDto();
                    imageDto.setImage(value);
                    imageDtoList.add(imageDto);
                });
            }
            myProblemDto.setProblemImage(imageDtoList);

            myProblemDtoList.add(myProblemDto);
        }
        myProblemListDto.setMyProblemDtoList(myProblemDtoList);
        return myProblemListDto;
    }

    @Override
    public ResponseDto updateProblem(String token, Integer problemId, ProblemDto problemDto) {
        ResponseDto dto = new ResponseDto();
        Integer userID = jwtUtil.extractUserId(token);
        //TODO 본인 문제인지 점검
        Optional<ProblemEntity> problemEntity = havrutaDao.findProblemEntityByProblemId(problemId);
        if(problemEntity.isPresent()){
            havrutaDao.deleteCategoryListByProblemEntity(problemEntity.get());

            List<CategoryIdRequestDto> categoryIdRequestDtoList = problemDto.getCategoryIdList();
            List<CategoryEntity> categoryEntityList = new ArrayList<>();
            for(CategoryIdRequestDto categoryIdRequestDto : categoryIdRequestDtoList){
                Integer categoryId = categoryIdRequestDto.getCategoryId();
                CategoryEntity categoryEntity = havrutaDao.findCategoryEntityByCategoryId(categoryId).get();
                categoryEntityList.add(categoryEntity);
            }
            havrutaDao.saveNewCategoryProblem(problemEntity.get(), categoryEntityList);

            problemEntity.get().setProblemType(problemDto.getProblemType());
            problemEntity.get().setProblemQuestion(problemDto.getProblemQuestion());
            //problemEntity.get().setProblemCandidate(problemDto.getProblemCandidate());
            problemEntity.get().setProblemAnswer(problemDto.getProblemAnswer());
            //problemEntity.get().setProblemImage(problemDto.getProblemImage());
            havrutaDao.changeProblem(problemEntity.get());
            dto.setMessage("success");
        }
        else{
            dto.setMessage("fail");
        }
        return dto;
    }

    public ResponseDto deleteProblem(String token, Integer problemId){
        ResponseDto dto = new ResponseDto();
        Integer userID = jwtUtil.extractUserId(token);
        //TODO 본인 문제인지 점검
        Optional<ProblemEntity> problemEntity = havrutaDao.findProblemEntityByProblemId(problemId);
        if(problemEntity.isPresent()){
            havrutaDao.deleteCategoryListByProblemEntity(problemEntity.get());
            havrutaDao.deleteProblem(problemEntity.get().getProblemId());
            dto.setMessage("success");
        }
        else{
            dto.setMessage("fail");
        }
        return dto;
    }

    public SpecificGroupResponseDto specificGroupPage(String token, Integer groupId){
        //사용자 정보 확인 (우리가 발행한 토큰 넘어올 것) 및 isAdmin, isMember 받아오기 -> member entity 생성 필요?
        //groupId로 groupName, root_category_Id 찾기
        //root_category_ID로 자식 카테고리 Id, name, depth 다 받아오기 -> 쿼리 짜야 됨
        //root_category_ID가 parent_ID인 모든 category_closure 받아옴. 받아온 child_id로 같은 일 반복해서 한 리스트에 모아줌

        Integer userID = jwtUtil.extractUserId(token);

        SpecificGroupResponseDto dto = new SpecificGroupResponseDto();

        GroupEntity groupEntity = new GroupEntity();
        Optional<GroupEntity> groupSearchResult = havrutaDao.findGroupById(groupId);

        if(groupSearchResult.isPresent()) {//그룹 검색 결과 확인
            groupEntity = groupSearchResult.get();
        }else {
            throw new NoGroupException("There is No Group", ErrorCode.NO_GROUP_ERROR);
        }

        MemberEntity memberEntity = new MemberEntity();
        Optional<MemberEntity> memberSearchResult = havrutaDao.findMemberById(userID, groupEntity.getGroupId());

        if(memberSearchResult.isPresent()) {//멤버 검색 결과 확인
            memberEntity = memberSearchResult.get();
        }else {
            System.out.println("member Serach Result Error\n");
            System.out.println(memberSearchResult);
            System.out.println(userID);
            System.out.println(groupEntity.getGroupId());
            throw new NoGroupException("There is No Group", ErrorCode.NO_GROUP_ERROR);
        }

        List<CategoryClosureEntity> categoryClosureEntityList = havrutaDao.findClosuresByRootId(groupEntity.getRootCategoryId().getCategoryId());
        List<CategoryDto> categoryDtoList = new ArrayList<CategoryDto>();
        for(CategoryClosureEntity categoryClosureEntity : categoryClosureEntityList){
            CategoryEntity categoryEntity = new CategoryEntity();
            Optional<CategoryEntity> categorySerachResult = havrutaDao.findCategoryById(categoryClosureEntity.getChild().getCategoryId());
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

    public AdminResponseDto admin(String token, Integer groupId) {
        //사용자 정보 확인
        //groupId로 groupName, root_category_Id 찾기
        //root_category_ID로 자식 카테고리 Id, name 다 받아오기 -> 쿼리짜기
        //root_category_ID가 parent_ID인 모든 category_closure 받아옴. 받아온 child_id로 같은 일 반복해서 한 리스트에 모아줌

        Integer userID = jwtUtil.extractUserId(token);

        AdminResponseDto dto = new AdminResponseDto();

        GroupEntity groupEntity = new GroupEntity();
        Optional<GroupEntity> groupSearchResult = havrutaDao.findGroupById(groupId);

        if(groupSearchResult.isPresent()) {
            groupEntity = groupSearchResult.get();
        }else {
            throw new NoGroupException("There is No Group", ErrorCode.NO_GROUP_ERROR);
        }

        List<CategoryClosureEntity> categoryClosureEntityList = havrutaDao.findClosuresByRootId(groupEntity.getRootCategoryId().getCategoryId());
        List<CategoryIdDto> categoryIdDtoList = new ArrayList<CategoryIdDto>();
        for(CategoryClosureEntity categoryClosureEntity : categoryClosureEntityList){
            CategoryEntity categoryEntity = new CategoryEntity();
            Optional<CategoryEntity> categorySerachResult = havrutaDao.findCategoryById(categoryClosureEntity.getChild().getCategoryId());
            if(categorySerachResult.isPresent()) {//멤버 검색 결과 확인
                categoryEntity = categorySerachResult.get();
            }else {
                throw new NoGroupException("There is No Group", ErrorCode.NO_GROUP_ERROR);
            }
            CategoryIdDto categoryIdDto = new CategoryIdDto();
            categoryIdDto.setCategoryId(categoryEntity.getCategoryId());
            categoryIdDto.setCategoryName(categoryEntity.getCategoryName());
            categoryIdDtoList.add(categoryIdDto);
        }

        dto.setGroupName(groupEntity.getGroupName());
        dto.setCategoryIdList(categoryIdDtoList);

        return dto;
    }

    public AdminMembersResponseDto adminMembers(String token, Integer groupId) {
        //사용자 정보 확인 및 isAdmin
        //groupId로 member_Table에서 user_ID 찾고 is_Member에 따라 다른 리스트에 넣기
        Integer userID = jwtUtil.extractUserId(token);

        AdminMembersResponseDto dto = new AdminMembersResponseDto();

        GroupEntity groupEntity = new GroupEntity();
        Optional<GroupEntity> searchResult = havrutaDao.findGroupById(groupId);

        if(searchResult.isPresent()) {
            groupEntity = searchResult.get();
        }else {
            throw new NoGroupException("There is No Group", ErrorCode.NO_GROUP_ERROR);
        }

        List<MemberEntity> memberEntityList = havrutaDao.findMembersByGroupId(groupId);

        List<MemberDto> MemberDtoList = new ArrayList<MemberDto>();
        List<UserDto> UserDtoList = new ArrayList<UserDto>();

        for(MemberEntity memberEntity : memberEntityList){
            Optional<UserEntity> optionalUserEntity = havrutaDao.findUserById(memberEntity.getUserEntity().getUserId());
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

    public ResponseDto designateAdmin(String token, Integer newAdminId, Integer groupId) {
        //사용자 정보 확인
        //groupId와 newAdminId로 members에서 튜플 찾아서 isAdmin 바꿔주기
        Integer userID = jwtUtil.extractUserId(token);

        Boolean result = havrutaDao.designateAdmin(newAdminId, groupId);

        ResponseDto dto = new ResponseDto();
        if(result) {
            dto.setMessage("success!");
        }
        else{
            dto.setMessage("failure..");
        }

        return dto;
    }

    public ResponseDto dropMember(String token, Integer userId, Integer groupId) {
        //사용자 정보 확인
        //groupId와 userId로 members에서 튜플 찾아서 제거
        Integer myUserID = jwtUtil.extractUserId(token);

        Boolean result = havrutaDao.dropMember(userId, groupId);

        ResponseDto dto = new ResponseDto();
        if(result) {
            dto.setMessage("success!");
        }
        else{
            dto.setMessage("failure..");
        }

        return dto;
    }

    public ResponseDto confirm(String token, Integer userId, Integer groupId) {
        //사용자 정보 확인
        //groupId와 userId로 members에서 튜플 찾아서 isMember 바꿔주기

        Integer myUserID = jwtUtil.extractUserId(token);

        Boolean result = havrutaDao.confirm(userId, groupId);

        ResponseDto dto = new ResponseDto();
        if(result) {
            dto.setMessage("success!");
        }
        else{
            dto.setMessage("failure..");
        }

        return dto;
    }

    public ResponseDto deleteGroup(String token, Integer groupId) {
        //사용자 정보 확인
        //groupId로 그룹 찾아서 삭제

        Integer myUserID = jwtUtil.extractUserId(token);

        Boolean result = havrutaDao.deleteGroup(groupId);

        ResponseDto dto = new ResponseDto();
        if(result) {
            dto.setMessage("success!");
        }
        else{
            dto.setMessage("failure..");
        }

        return dto;
    }

    public ResponseDto updateGroup(String token, String newGroupName, Integer groupId) {
        //사용자 정보 확인
        //groupId로 그룹 찾아서 새이름을 변경

        Integer myUserID = jwtUtil.extractUserId(token);

        Boolean result = havrutaDao.updateGroup(newGroupName, groupId);

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
