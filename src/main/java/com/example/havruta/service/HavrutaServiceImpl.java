package com.example.havruta.service;

import com.example.havruta.data.dao.HavrutaDao;
import com.example.havruta.data.dto.*;
import com.example.havruta.data.entity.*;
import com.example.havruta.data.entity.serializable.CategoryProblemId;
import com.example.havruta.data.entity.serializable.ClosureId;
import com.example.havruta.data.entity.serializable.MemberId;
import com.example.havruta.data.repository.*;
import com.example.havruta.errorAndException.*;
import com.example.havruta.security.JwtUtil;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HavrutaServiceImpl implements HavrutaService {
    private final HavrutaDao havrutaDao;
    private final JwtUtil jwtUtil;
    private final GroupRepository groupRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryClosureRepository categoryClosureRepository;
    private final ProblemRepository problemRepository;
    private final CategoryProblemRepository categoryProblemRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    @Autowired
    public HavrutaServiceImpl(
            HavrutaDao havrutaDao,
            JwtUtil jwtUtil,
            GroupRepository groupRepository,
            CategoryRepository categoryRepository,
            CategoryClosureRepository categoryClosureRepository,
            ProblemRepository problemRepository,
            CategoryProblemRepository categoryProblemRepository,
            MemberRepository memberRepository,
            UserRepository userRepository
    ) {
        this.havrutaDao = havrutaDao;
        this.jwtUtil = jwtUtil;
        this.groupRepository = groupRepository;
        this.categoryRepository = categoryRepository;
        this.categoryClosureRepository = categoryClosureRepository;
        this.problemRepository = problemRepository;
        this.categoryProblemRepository = categoryProblemRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
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
        Integer userID = jwtUtil.extractUserId(token);
        //checkUserIdValid?
        SpecificGroupResponseDto dto = new SpecificGroupResponseDto();

        GroupEntity groupEntity = new GroupEntity();
        Optional<GroupEntity> groupSearchResult = havrutaDao.findGroupById(groupId);

        if(groupSearchResult.isPresent()) {//그룹 검색 결과 확인
            groupEntity = groupSearchResult.get();
        }else {
            throw new NoGroupException("No Group", ErrorCode.NO_GROUP_ERROR);
        }

        MemberEntity memberEntity = new MemberEntity();
        Optional<MemberEntity> memberSearchResult = havrutaDao.findMemberById(userID, groupEntity.getGroupId());
        if(memberSearchResult.isPresent()) {//멤버 검색 결과 확인
            memberEntity = memberSearchResult.get();
            dto.setIsAdmin(memberEntity.getIsAdmin());
            dto.setIsMember(memberEntity.getIsMember());
        }else {
            dto.setIsAdmin(0);
            dto.setIsMember(0);
        }

        List<CategoryClosureEntity> categoryClosureEntityList = havrutaDao.findClosuresByRootId(groupEntity.getRootCategoryId().getCategoryId());
        if(categoryClosureEntityList.isEmpty()) {
            throw new NoCategoryException("No Category Closure", ErrorCode.NO_CATEGORY_ERROR);
        }
        List<CategoryDto> categoryDtoList = new ArrayList<CategoryDto>();
        for(CategoryClosureEntity categoryClosureEntity : categoryClosureEntityList){
            CategoryEntity categoryEntity = new CategoryEntity();
            Optional<CategoryEntity> categorySerachResult = havrutaDao.findCategoryById(categoryClosureEntity.getChild().getCategoryId());
            if(categorySerachResult.isPresent()) {
                categoryEntity = categorySerachResult.get();
            }else {
                throw new NoCategoryException("No Category", ErrorCode.NO_CATEGORY_ERROR);
            }
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setCategoryId(categoryEntity.getCategoryId());
            categoryDto.setCategoryName(categoryEntity.getCategoryName());
            categoryDto.setDepth(categoryClosureEntity.getDepth());
            categoryDtoList.add(categoryDto);
        }

        dto.setGroupName(groupEntity.getGroupName());
        dto.setCategoryList(categoryDtoList);

        return dto;
    }

    public AdminResponseDto admin(String token, Integer groupId) {
        Integer userID = jwtUtil.extractUserId(token);
        checkUserGroupAdmin(userID, groupId);

        AdminResponseDto dto = new AdminResponseDto();

        GroupEntity groupEntity = new GroupEntity();
        Optional<GroupEntity> groupSearchResult = havrutaDao.findGroupById(groupId);

        if(groupSearchResult.isPresent()) {//그룹 검색 결과 확인
            groupEntity = groupSearchResult.get();
        }else {
            throw new NoGroupException("No Group", ErrorCode.NO_GROUP_ERROR);
        }

        List<CategoryClosureEntity> categoryClosureEntityList = havrutaDao.findClosuresByRootId(groupEntity.getRootCategoryId().getCategoryId());
        if(categoryClosureEntityList.isEmpty()) {
            throw new NoCategoryException("No Category Closure", ErrorCode.NO_CATEGORY_ERROR);
        }
        List<CategoryIdDto> categoryIdDtoList = new ArrayList<CategoryIdDto>();
        for(CategoryClosureEntity categoryClosureEntity : categoryClosureEntityList){
            CategoryEntity categoryEntity = new CategoryEntity();
            Optional<CategoryEntity> categorySerachResult = havrutaDao.findCategoryById(categoryClosureEntity.getChild().getCategoryId());
            if(categorySerachResult.isPresent()) {//멤버 검색 결과 확인
                categoryEntity = categorySerachResult.get();
            }else {
                throw new NoCategoryException("No Category", ErrorCode.NO_CATEGORY_ERROR);
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
        Integer userID = jwtUtil.extractUserId(token);
        checkUserGroupAdmin(userID, groupId);

        AdminMembersResponseDto dto = new AdminMembersResponseDto();

        GroupEntity groupEntity = new GroupEntity();
        Optional<GroupEntity> searchResult = havrutaDao.findGroupById(groupId);

        if(searchResult.isPresent()) {//그룹 검색 결과 확인
            groupEntity = searchResult.get();
        }else {
            throw new NoGroupException("No Group", ErrorCode.NO_GROUP_ERROR);
        }

        List<MemberDto> MemberDtoList = new ArrayList<MemberDto>();
        List<UserDto> UserDtoList = new ArrayList<UserDto>();
        List<MemberEntity> memberEntityList = havrutaDao.findMembersByGroupId(groupId);
        if(memberEntityList.isEmpty()){
            dto.setMemberList(MemberDtoList);
            dto.setJoinList(UserDtoList);

            return dto;
        }

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
            else{
                throw new NoUserException("No Such Memeber in User", ErrorCode.NO_USER_ERROR);
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
        checkUserGroupAdmin(userID, groupId);

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
        checkUserGroupAdmin(myUserID, groupId);

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
        checkUserGroupAdmin(myUserID, groupId);

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
        checkUserGroupAdmin(myUserID, groupId);

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
        checkUserGroupAdmin(myUserID, groupId);

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


    public ResponseDto newCategory(String token, Integer groupId, CategoryInfoDto categoryInfoDto) {
        /*
        1. check if group id is valid
        2. check if token is group admin
        3. check if parent category id is descendant of group root category
        4. check if same category name exists as a child of parent category
        5. repository tasks
         */

        ResponseDto responseDto = new ResponseDto();

        /* 1. check if group id is valid */
        checkGroupIdValid(groupId);
        /* 2. check if token is group admin */
        Integer userId = jwtUtil.extractUserId(token);
        checkUserGroupAdmin(userId, groupId);
        /* 3. check if parent category id is descendant of group root category */
        checkCategoryInGroup(categoryInfoDto.getParentCategoryId(), groupId);
        /* 4. check if categoryName already exists as a child of parentCategoryId (if exists, flag is true) */
        Integer parentCategoryId = categoryInfoDto.getParentCategoryId();
        String categoryName = categoryInfoDto.getCategoryName();

        List<CategoryClosureEntity> tmpList = categoryClosureRepository.findById_ParentId(parentCategoryId);

        boolean flag = false;

        for (CategoryClosureEntity c : tmpList) {
            Optional<CategoryEntity> tmpCategoryEntity = categoryRepository.findById(c.getChild().getCategoryId());
            if (tmpCategoryEntity.isPresent()) {
                if (tmpCategoryEntity.get().getCategoryName().equals(categoryInfoDto.getCategoryName())) {
                    flag = true;
                    break;
                }
            }
        }

        if (flag) {
            throw new SameCategoryNameException("Same category name already exists", ErrorCode.CATEGORY_NAME_ERROR);
        }

        /* 5. repository tasks */
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryName(categoryInfoDto.getCategoryName());

        CategoryEntity newCategoryEntity = categoryRepository.save(categoryEntity);
        categoryClosureRepository.createCategory(newCategoryEntity.getCategoryId(), categoryInfoDto.getParentCategoryId());

        responseDto.setMessage("SUCCESS");

        return responseDto;
    }

    public ResponseDto deleteCategory(String token, Integer groupId, Integer categoryId) {
        /*
        1. check if group id is valid
        2. check if token is group admin
        3. check if category id is descendant of group root category
        4. check if category id is root category
        5. repository tasks
         */
        ResponseDto responseDto = new ResponseDto();

        /* 1. check if group id is valid */
        checkGroupIdValid(groupId);
        /* 2. check if token is group admin */
        Integer userId = jwtUtil.extractUserId(token);
        checkUserGroupAdmin(userId, groupId);
        /* 3. check if category id is descendant of group root category */
        checkCategoryInGroup(categoryId, groupId);
        /* 4. check if category id is root category */
        checkCategoryRoot(categoryId, groupId);
        /* 5. repository tasks */
        /* get category id list */
        List<CategoryClosureEntity> categoryClosureEntityList = categoryClosureRepository.findAllById_ParentId(categoryId);
        List<Integer> categoryIdList = new ArrayList<>();
        for(CategoryClosureEntity c : categoryClosureEntityList){
            categoryIdList.add(c.getId().getChildId());
        }

        /* remove from category closure */
        for(Integer i : categoryIdList) {
            categoryClosureRepository.deleteAllByChild_CategoryId_OrParent_CategoryId(i, i);
        }

        /* get problem id list */
        List<CategoryProblemEntity> categoryProblemEntityList = new ArrayList<>();
        for(Integer i : categoryIdList) {
            categoryProblemEntityList.addAll(categoryProblemRepository.findById_CategoryId(i));
        }

        List<Integer> problemIdList = new ArrayList<>();
        boolean flag;
        for(CategoryProblemEntity c : categoryProblemEntityList){
            flag = true;
            List<CategoryProblemEntity> tmpList = categoryProblemRepository.findAllById_ProblemId(c.getId().getProblemId());
            for(CategoryProblemEntity e : tmpList){
                /*
                if problem is in a category which won't be deleted,
                don't have to move to root. exclude it
                 */
                if(!categoryProblemEntityList.contains(e)){
                    flag = false;
                    break;
                }
            }
            if(flag) {
                problemIdList.add(c.getId().getProblemId());
            }
        }

        /* remove from category problem */
        /* remove from categories */
        for(Integer i : categoryIdList) {
            categoryProblemRepository.deleteAllById_CategoryId(i);
            categoryRepository.deleteAllByCategoryId(i);
        }

        /* add to category problem */
        for(Integer i : problemIdList) {
            Optional<GroupEntity> optionalGroupEntity = groupRepository.findById(groupId);
            Optional<ProblemEntity> optionalProblemEntity = problemRepository.findById(i);
            if (optionalGroupEntity.isEmpty()) {
                throw new NoGroupException("No Group", ErrorCode.NO_GROUP_ERROR);
            }
            if (optionalProblemEntity.isEmpty()) {
                throw new NoProblemException("No Problem", ErrorCode.NO_PROBLEM_ERROR);
            }
            categoryProblemRepository.save(
                    new CategoryProblemEntity(
                            new CategoryProblemId(
                                    optionalGroupEntity.get().getRootCategoryId().getCategoryId(),
                                    i
                            ),
                            optionalGroupEntity.get().getRootCategoryId(),
                            optionalProblemEntity.get()

                    )
            );
        }

        responseDto.setMessage("SUCCESS");

        return responseDto;
    }

    public ResponseDto updateCategory(String token, Integer groupId, CategoryInfoDto categoryInfoDto, Integer categoryId) {
        /*
        1. check if group id is valid
        2. check if token is group admin
        3. check if both category id & parent id are descendants of group root category
        4. repository tasks
         */
        ResponseDto responseDto = new ResponseDto();

        /* 1. check if group id is valid */
        checkGroupIdValid(groupId);
        /* 2. check if token is group admin */
        Integer userId = jwtUtil.extractUserId(token);
        checkUserGroupAdmin(userId, groupId);
        /* 3. check if both category id & parent id are descendants of group root category */
        checkCategoryInGroup(categoryId, groupId);
        checkCategoryInGroup(categoryInfoDto.getParentCategoryId(), groupId);
        /* 4. repository tasks */
        /* Update category name */
        Optional<CategoryEntity> target = categoryRepository.findById(categoryId);
        if(target.isEmpty()){
            throw new NoCategoryException("No Category", ErrorCode.NO_CATEGORY_ERROR);
        }
        target.get().setCategoryName(categoryInfoDto.getCategoryName());
        categoryRepository.save(target.get());
        /* update categoryClosureRepository */

        categoryClosureRepository.updateCategory_dropTempFromTable();
        categoryClosureRepository.updateCategory_dropTempToTable();
        categoryClosureRepository.updateCategory_createTempFromTable(categoryId);
        categoryClosureRepository.updateCategory_createTempToTable(categoryInfoDto.getParentCategoryId());
        categoryClosureRepository.updateCategory_deleteFromCategoryClosure();
        categoryClosureRepository.updateCategory_insertIntoCategoryClosure();
        categoryClosureRepository.updateCategory_dropTempFromTable();
        categoryClosureRepository.updateCategory_dropTempToTable();

        responseDto.setMessage("SUCCESS");

        return responseDto;
    }
    public CategoryProblemListDto getCategoryProblem(String token, Integer groupId, Integer categoryId) {
        /*
        1. check if group id is valid
        2. check if token is group member
        3. check if category id is descendant of root id
        4. repository tasks
         */
        CategoryProblemListDto responseDto = new CategoryProblemListDto();

        /* 1. check if group id is valid */
        checkGroupIdValid(groupId);
        /* 2. check if token is group member */
        Integer userId = jwtUtil.extractUserId(token);
        checkUserGroupMember(userId, groupId, true);
        /* 3. check if category id is descendant of root id */
        checkCategoryInGroup(categoryId, groupId);
        /* 4. repository tasks */
        /* find all descendant categories of categoryId */
        List<CategoryClosureEntity> categoryClosureEntityList = categoryClosureRepository.findById_ParentId(categoryId);

        List<Integer> categoryIdList = new ArrayList<>();

        for(CategoryClosureEntity c : categoryClosureEntityList){
            categoryIdList.add(c.getId().getChildId());
        }

        /* find all problems of the categories */
        List<CategoryProblemEntity> categoryProblemEntityList = new ArrayList<>();

        for(Integer i : categoryIdList){
            categoryProblemEntityList.addAll(categoryProblemRepository.findById_CategoryId(i));
        }

        List<ProblemEntity> duplicateProblemEntityList = new ArrayList<>();
        for (CategoryProblemEntity c : categoryProblemEntityList) {
            duplicateProblemEntityList.add(c.getProblemEntity());
        }

        /* remove duplicates */
        Set<ProblemEntity> problemEntitySet = new HashSet<>(duplicateProblemEntityList);
        List<ProblemEntity> problemEntityList = new ArrayList<>(problemEntitySet);

        /* for response */
        List<CategoryProblemDto> categoryProblemDtoList = new ArrayList<>();

        for (ProblemEntity e : problemEntityList) {
            CategoryProblemDto categoryProblemDto = new CategoryProblemDto();

            categoryProblemDto.setProblemId(e.getProblemId());
            categoryProblemDto.setProblemType(e.getProblemType());
            categoryProblemDto.setProblemAnswer(e.getProblemAnswer());
            categoryProblemDto.setProblemQuestion(e.getProblemQuestion());

            List<ItemDto> itemDtoList = new ArrayList<>();
            List<ImageDto> imageDtoList = new ArrayList<>();

            for(Map.Entry<Integer, String> mapEntry : e.getProblemCandidate().entrySet()){
                itemDtoList.add(new ItemDto(mapEntry.getValue()));
            }

            for(Map.Entry<Integer, String> mapEntry : e.getProblemImage().entrySet()){
                imageDtoList.add(new ImageDto(mapEntry.getValue()));
            }

            categoryProblemDto.setProblemCandidate(itemDtoList);
            categoryProblemDto.setProblemImage(imageDtoList);

            categoryProblemDtoList.add(categoryProblemDto);
        }

        responseDto.setCategoryProblemList(categoryProblemDtoList);

        return responseDto;
    }

    public ResponseDto registerGroup(String token, Integer groupId) {
        /*
        1. check if group id is valid
        2. check if token isn't group member
        3. repository tasks
         */
        ResponseDto responseDto = new ResponseDto();

        /* 1. check if group id is valid */
        checkGroupIdValid(groupId);
        /* 2. check if token isn't group member */
        Integer userId = jwtUtil.extractUserId(token);
        checkUserGroupMember(userId, groupId, false);
        /* 3. repository tasks */

        Optional<GroupEntity> optionalGroupEntity = groupRepository.findById(groupId);

        if(optionalGroupEntity.isEmpty()){
            throw new NoGroupException("No Group", ErrorCode.NO_GROUP_ERROR);
        }

        memberRepository.save(new MemberEntity(new MemberId(userId, groupId), userRepository.findById(userId).get(), optionalGroupEntity.get(), 0, 0));

        return responseDto;
    }

    public CategoryListDto getGroupCategory(Integer groupId) {
        CategoryListDto responseDto = new CategoryListDto();

        checkGroupIdValid(groupId);
        /* same task on group page */
        /* from wonbin */
        GroupEntity groupEntity = groupRepository.findById(groupId).get();

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
        responseDto.setCategoryList(categoryDtoList);

        return responseDto;
    }

    public ResponseDto makeNewProblem(String token, ProblemRequestDto problemRequestDto, Integer groupId) {
        /*
        1. check if group id is valid
        2. check if token is group member
        3. check if categories are descendant of groupId
        4. repository tasks
         */
        ResponseDto responseDto = new ResponseDto();

        /* 1. check if group id is valid */
        checkGroupIdValid(groupId);
        /* 2. check if token is group member */
        Integer userId = jwtUtil.extractUserId(token);
        checkUserGroupMember(userId, groupId, true);
        /* 3. check if categories are descendant of groupId */
        List<CategoryClosureEntity> categoryClosureEntityList = categoryClosureRepository.findById_ParentId(groupRepository.findById(groupId).get().getRootCategoryId().getCategoryId());

        for(CategoryClosureEntity c : categoryClosureEntityList){
            checkCategoryInGroup(c.getId().getChildId(), groupId);
        }
        /* 4. repository tasks */
        /* 4-1. problem repo */
        ProblemEntity problemEntity = new ProblemEntity();

        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);

        if(optionalUserEntity.isEmpty()){
            throw new NoUserException("No User", ErrorCode.NO_USER_ERROR);
        }

        problemEntity.setUserId(optionalUserEntity.get());
        problemEntity.setProblemType(problemRequestDto.getProblemType());
        problemEntity.setProblemQuestion(problemRequestDto.getProblemQuestion());
        problemEntity.setProblemAnswer(problemRequestDto.getProblemAnswer());

        Map<Integer, String> candidateMap = new HashMap<>();
        int cnt = 0;
        for(ItemDto itemDto : problemRequestDto.getProblemCandidate()){
            candidateMap.put(cnt++, itemDto.getItem());
        }
        problemEntity.setProblemCandidate(candidateMap);

        Map<Integer, String> imageMap = new HashMap<>();
        cnt = 0;
        for(ImageDto imageDto : problemRequestDto.getProblemImage()){
            imageMap.put(cnt++, imageDto.getImage());
        }
        problemEntity.setProblemImage(imageMap);

        ProblemEntity problemEntityResult = problemRepository.save(problemEntity);

        /* 4-2. categoryProblem repo */
        for(CategoryIdRequestDto c : problemRequestDto.getCategoryIdList()){
            CategoryProblemEntity categoryProblemEntity = new CategoryProblemEntity();

            /* TODO: if error here, several ways to handle it.
                1: undo the tasks done above, which means that none of the categories added
                2: set a flag that error occured, and go on. After loop ended, throw an exception (would be better if we can send the failed categories to client)
                3: throw exception right away. (not a good way)
             */
            CategoryEntity categoryEntity = categoryRepository.findById(c.getCategoryId()).get();

            categoryProblemRepository.save(new CategoryProblemEntity(new CategoryProblemId(), categoryEntity, problemEntityResult));
        }

        responseDto.setMessage("SUCCESS");

        return responseDto;
    }

    /* throws exception if groupId is invalid */
    private void checkGroupIdValid(Integer groupId){
        Optional<GroupEntity> groupEntity = groupRepository.findById(groupId);

        if (groupEntity.isEmpty()) {
            throw new NoGroupException("No Group", ErrorCode.NO_GROUP_ERROR);
        }
    }

    /* throws exception if userId is not group admin */
    private void checkUserGroupAdmin(Integer userId, Integer groupId){
        Optional<GroupEntity> groupEntity = groupRepository.findById(groupId);

        Optional<MemberEntity> memberEntity = memberRepository.findById(new MemberId(userId, groupId));

        if(memberEntity.isEmpty()){
            throw new NotMemberException("Not Member", ErrorCode.NOT_MEMBER_ERROR);
        }
        else if(memberEntity.get().getIsMember().equals(0)){
            throw new NotMemberException("Not Member", ErrorCode.NOT_MEMBER_ERROR);
        }
        else if(memberEntity.get().getIsAdmin().equals(0)){
            throw new NotAdminException("Not Admin", ErrorCode.NOT_ADMIN_ERROR);
        }
    }

    /* throws exception if categoryId isn't category of groupId */
    private void checkCategoryInGroup(Integer categoryId, Integer groupId){
        Optional<GroupEntity> groupEntity = groupRepository.findById(groupId);

        /* get list of descendants of root */
        List<CategoryClosureEntity> descendantList = categoryClosureRepository.findById_ParentId(groupEntity.get().getRootCategoryId().getCategoryId());

        /* check if category exists in the list */
        boolean found = false;

        for (CategoryClosureEntity c : descendantList) {
            if (c.getChild().getCategoryId().equals(categoryId)) {
                found = true;
                break;
            }
        }

        if (!found) {
            throw new NoCategoryException("No Category", ErrorCode.NO_CATEGORY_ERROR);
        }
    }

    /* throws exception if userId isn't member of groupId when flag is true */
    private void checkUserGroupMember(Integer userId, Integer groupId, Boolean flag){
        Optional<GroupEntity> groupEntity = groupRepository.findById(groupId);

        Optional<MemberEntity> memberEntity = memberRepository.findById(new MemberId(userId, groupId));

        if(flag) {
            if (memberEntity.isEmpty()) {
                throw new NotMemberException("Not Member", ErrorCode.NOT_MEMBER_ERROR);
            } else if (memberEntity.get().getIsMember().equals(0)) {
                throw new NotMemberException("Not Member", ErrorCode.NOT_MEMBER_ERROR);
            }
        }
        else{
            if (memberEntity.isPresent()) {
                if (memberEntity.get().getIsMember().equals(1)) {
                    /* already a member */
                    throw new AlreadyMemberException("Already Member", ErrorCode.ALREADY_MEMBER_ERROR);
                }
            }
        }
    }

    /* throws exception if categoryId is rootCategoryId of groupId */
    private void checkCategoryRoot(Integer categoryId, Integer groupId){
        Optional<GroupEntity> groupEntity = groupRepository.findById(groupId);

        if(groupEntity.get().getRootCategoryId().getCategoryId().equals(categoryId)){
            throw new RootCategoryException("Root Category", ErrorCode.ROOT_CATEGORY_ERROR);
        }
    }
}
