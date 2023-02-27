package com.example.havruta.service;

import com.example.havruta.data.dto.*;
import com.example.havruta.data.entity.*;
import com.example.havruta.data.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class SoominServiceImpl implements SoominService{
    private final GroupRepository groupRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryClosureRepository categoryClosureRepository;
    private final ProblemRepository problemRepository;
    private final CategoryProblemRepository categoryProblemRepository;

    @Autowired
    public SoominServiceImpl(
            GroupRepository groupRepository,
            CategoryRepository categoryRepository,
            CategoryClosureRepository categoryClosureRepository,
            ProblemRepository problemRepository,
            CategoryProblemRepository categoryProblemRepository
    ) {
        this.groupRepository = groupRepository;
        this.categoryRepository = categoryRepository;
        this.categoryClosureRepository = categoryClosureRepository;
        this.problemRepository = problemRepository;
        this.categoryProblemRepository = categoryProblemRepository;
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
        4. repository tasks
         */
        ResponseDto responseDto = new ResponseDto();

        /* 1. check if group id is valid */
        checkGroupIdValid(groupId);
        /* 2. check if token is group admin */
        /* TODO: token */
        //checkUserGroupAdmin(userId, groupId);
        /* 3. check if category id is descendant of group root category */
        checkCategoryInGroup(categoryId, groupId);
        /* 4. repository tasks */
        categoryClosureRepository.removeCategory_dropTempClosureTable();
        categoryClosureRepository.removeCategory_createTempClosureTable(categoryId); /* maybe need some lock or semaphore (if this was a multithreaded program) */
        categoryClosureRepository.removeCategory_deleteFromCategoryClosure();
        categoryClosureRepository.removeCategory_deleteFromCategories();
        categoryClosureRepository.removeCategory_deleteFromCategoryProblems();
        categoryClosureRepository.removeCategory_dropTempClosureTable();

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
        CategoryProblemListDto responseDto = new CategoryProblemListDto();

        Optional<GroupEntity> groupEntity = groupRepository.findById(groupId);

        if (groupEntity.isEmpty()) {
            /*
            TODO: exception handling
             */
        } else {
            /*
            if(token is not group member){
                TODO: exception handling
            }
            else{
                */
            Optional<CategoryEntity> target = categoryRepository.findById(categoryId);
            if (target.isEmpty()) {
                    /*
                    For synchronization cases, such as two administrators are deleting the same category.
                    One delete request would be successful, but the later request would have invalid category id.
                     */
                /*
                TODO: exception handling
                 */
            } else {
                List<CategoryProblemEntity> categoryProblemEntityList = categoryProblemRepository.findAllByCategoryEntity_CategoryId(target.get());
/*
                List<Integer> problemIdList = new ArrayList<>();
                for(CategoryProblemEntity c : categoryProblemEntityList){
                    problemIdList.add(c.getProblemId().getProblemId());
                }

                List<ProblemEntity> problemEntityList = problemRepository.findAllById(problemIdList);
                List<CategoryProblemDto> categoryProblemDtoList = new ArrayList<>();

                for(ProblemEntity e : problemEntityList){
                    List<ItemDto> itemList = new ArrayList<>();
                    List<ImageDto> imageList = new ArrayList<>();

                    for(ItemEntity i : e.getProblemCandidate()){
                        itemList.add(new ItemDto(i.getItem()));
                    }

                    for(ImageEntity m : e.getProblemImage()){
                        imageList.add(new ImageDto(m.getImage()));
                    }

                    CategoryProblemDto categoryProblemDto = new CategoryProblemDto(
                            e.getProblemId(),
                            e.getProblemType(),
                            e.getProblemQuestion(),
                            itemList,
                            e.getProblemAnswer(),
                            imageList
                    );

                    categoryProblemDtoList.add(categoryProblemDto);
                }

                responseDto.setCategoryProblemList(categoryProblemDtoList);
            }*/

            }

        }

        return responseDto;
    }

    public ResponseDto registerGroup(String token, Integer groupId) {
        ResponseDto responseDto = new ResponseDto();

        return responseDto;
    }

    public CategoryListDto getGroupCategory(Integer groupId) {
        CategoryListDto responseDto = new CategoryListDto();

        return responseDto;
    }

    public ResponseDto makeNewProblem(String token, ProblemDto problemDto, Integer groupId) {
        ResponseDto responseDto = new ResponseDto();

        return responseDto;
    }

    /* throws exception if groupId is invalid */
    protected void checkGroupIdValid(Integer groupId){
        Optional<GroupEntity> groupEntity = groupRepository.findById(groupId);

        if (groupEntity.isEmpty()) {
            /* TODO: exception handling */
        }
    }

    /* throws exception if userId is not group admin */
    protected void checkUserGroupAdmin(Integer userId, Integer groupId){
        /*
        if(token is not group admin){
            TODO: exception handling
        }
        */
    }

    /* throws exception if categoryId isn't category of groupId */
    protected void checkCategoryInGroup(Integer categoryId, Integer groupId){
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
}
