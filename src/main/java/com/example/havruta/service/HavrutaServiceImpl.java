package com.example.havruta.service;

import com.example.havruta.data.dao.HavrutaDao;
import com.example.havruta.data.dto.*;
import com.example.havruta.data.entity.*;
import com.example.havruta.data.entity.serializable.CategoryProblemId;
import com.example.havruta.data.entity.serializable.ClosureId;
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
}
