package com.example.havruta.service;

import com.example.havruta.data.dto.*;
import com.example.havruta.data.entity.*;
import com.example.havruta.data.entity.serializable.CategoryProblemId;
import com.example.havruta.data.entity.serializable.MemberId;
import com.example.havruta.data.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SoominServiceImpl implements SoominService{
    private final GroupRepository groupRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryClosureRepository categoryClosureRepository;
    private final ProblemRepository problemRepository;
    private final CategoryProblemRepository categoryProblemRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    @Autowired
    public SoominServiceImpl(
            GroupRepository groupRepository,
            CategoryRepository categoryRepository,
            CategoryClosureRepository categoryClosureRepository,
            ProblemRepository problemRepository,
            CategoryProblemRepository categoryProblemRepository,
            MemberRepository memberRepository,
            UserRepository userRepository
    ) {
        this.groupRepository = groupRepository;
        this.categoryRepository = categoryRepository;
        this.categoryClosureRepository = categoryClosureRepository;
        this.problemRepository = problemRepository;
        this.categoryProblemRepository = categoryProblemRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
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
        /* TODO: token */
        //checkUserGroupAdmin(userId, groupId);
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
            /* TODO: exception handling */
            responseDto.setMessage("Same category name already exists");
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
        /* TODO: token */
        //checkUserGroupAdmin(userId, groupId);
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
        for(Integer i : problemIdList){
            categoryProblemRepository.save(
                    new CategoryProblemEntity(
                            new CategoryProblemId(
                                    groupRepository.findById(groupId).get().getRootCategoryId().getCategoryId(),
                                    i
                            ),
                            groupRepository.findById(groupId).get().getRootCategoryId(),
                            problemRepository.findById(i).get()

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
        /* TODO: token */
        //checkUserGroupAdmin(userId, groupId);
        /* 3. check if both category id & parent id are descendants of group root category */
        checkCategoryInGroup(categoryId, groupId);
        checkCategoryInGroup(categoryInfoDto.getParentCategoryId(), groupId);
        /* 4. repository tasks */
        /* Update category name */
        Optional<CategoryEntity> target = categoryRepository.findById(categoryId);
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
        /* TODO: token */
        //checkUserGroupMember(userId, groupId);
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

        Set<ProblemEntity> problemEntitySet = new HashSet<>(duplicateProblemEntityList);
        List<ProblemEntity> problemEntityList = new ArrayList<>(problemEntitySet);

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


        //test code need to be deleted
        UserEntity userEntity = userRepository.findById(1).get();
        Integer userId = userEntity.getUserId();

        /* 1. check if group id is valid */
        checkGroupIdValid(groupId);
        /* 2. check if token isn't group member */
        /* TODO: token */
        checkUserGroupMember(userId, groupId, false);
        /* 3. repository tasks */

        GroupEntity groupEntity = groupRepository.findById(groupId).get();

        memberRepository.save(new MemberEntity(new MemberId(userId, groupId), userRepository.findById(userId).get(), groupEntity, 0, 0));

        return responseDto;
    }

    public CategoryListDto getGroupCategory(Integer groupId) {
        CategoryListDto responseDto = new CategoryListDto();

        /* same task on group page */

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
        /* TODO: token */
        //checkUserGroupMember(userId, groupId, true);
        /* 3. check if categories are descendant of groupId */
        List<CategoryClosureEntity> categoryClosureEntityList = categoryClosureRepository.findById_ParentId(groupRepository.findById(groupId).get().getRootCategoryId().getCategoryId());

        for(CategoryClosureEntity c : categoryClosureEntityList){
            checkCategoryInGroup(c.getId().getChildId(), groupId);
        }
        /* 4. repository tasks */
        /* 4-1. problem repo */
        ProblemEntity problemEntity = new ProblemEntity();
        /* TODO: userId */
        UserEntity userEntity = userRepository.findById(1).get(); //test code need to be deleted
        problemEntity.setUserId(userEntity);
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
            /* TODO: exception handling */
        }
    }

    /* throws exception if userId is not group admin */
    private void checkUserGroupAdmin(Integer userId, Integer groupId){
        Optional<GroupEntity> groupEntity = groupRepository.findById(groupId);

        Optional<MemberEntity> memberEntity = memberRepository.findById(new MemberId(userId, groupId));

        if(memberEntity.isEmpty()){
            /* TODO: exception handling */
        }
        else if(memberEntity.get().getIsAdmin().equals(0)){
            /* TODO: exception handling */
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
            /* TODO: exception handling */
        }
    }

    /* throws exception if userId isn't member of groupId when flag is true */
    private void checkUserGroupMember(Integer userId, Integer groupId, Boolean flag){
        Optional<GroupEntity> groupEntity = groupRepository.findById(groupId);

        Optional<MemberEntity> memberEntity = memberRepository.findById(new MemberId(userId, groupId));

        if(flag) {
            if (memberEntity.isEmpty()) {
                /* TODO: exception handling */
            } else if (memberEntity.get().getIsMember().equals(0)) {
                /* TODO: exception handling */
            }
        }
        else{
            if (memberEntity.isPresent()) {
                if (memberEntity.get().getIsMember().equals(1)) {
                    /* TODO: exception handling */
                    /* already a member */
                }
            }
        }
    }

    /* throws exception if categoryId is rootCategoryId of groupId */
    private void checkCategoryRoot(Integer categoryId, Integer groupId){
        Optional<GroupEntity> groupEntity = groupRepository.findById(groupId);

        if(groupEntity.get().getRootCategoryId().getCategoryId().equals(categoryId)){
            /* TODO: exception handling */
        }
    }
}
