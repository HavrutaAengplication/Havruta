package com.example.havruta.service;

import com.example.havruta.data.dao.HavrutaDao;
import com.example.havruta.data.dto.*;
import com.example.havruta.data.entity.*;
import com.example.havruta.data.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        ResponseDto responseDto = new ResponseDto();

        Optional<GroupEntity> groupEntity = groupRepository.findById(groupId);

        if (groupEntity.isEmpty()) {
            responseDto.setMessage("Invalid groupId");
        } else {
            /*
            if(token is not group admin){
                responseDto.setMessage("Not group administrator");
            }
            else{
            */
                Integer parentCategoryId = categoryInfoDto.getParentCategoryId();
                String categoryName = categoryInfoDto.getCategoryName();

                /* check if categoryName already exists as a child of parentCategoryId (if true, flag is true) */
                List<CategoryClosureEntity> tmpList = categoryClosureRepository.findById_ParentId(parentCategoryId);

                boolean flag = false;

                for(CategoryClosureEntity c : tmpList) {
                    Optional<CategoryEntity> tmpCategoryEntity = categoryRepository.findById(c.getChild().getCategoryId());
                    if(tmpCategoryEntity.isPresent()) {
                        if (tmpCategoryEntity.get().getCategoryName().equals(categoryInfoDto.getCategoryName())) {
                            flag = true;
                            break;
                        }
                    }
                }

                if(flag) {
                    responseDto.setMessage("Same category name already exists");
                }
                else{
                    CategoryEntity categoryEntity = new CategoryEntity();
                    categoryEntity.setCategoryName(categoryInfoDto.getCategoryName());

                    CategoryEntity newCategoryEntity = categoryRepository.save(categoryEntity);
                    newCategoryEntity.getCategoryId();
                    categoryInfoDto.getParentCategoryId();
                    categoryClosureRepository.createCategory(newCategoryEntity.getCategoryId(), categoryInfoDto.getParentCategoryId());

                    responseDto.setMessage("SUCCESS");
                }
                /*
            }
            */
        }

        return responseDto;
    }

    public ResponseDto deleteCategory(String token, Integer groupId, Integer categoryId) {
        ResponseDto responseDto = new ResponseDto();

        Optional<GroupEntity> groupEntity = groupRepository.findById(groupId);

        if (groupEntity.isEmpty()) {
            responseDto.setMessage("Invalid groupId");
        } else {
            /*
            if(token is not group admin){
                responseDto.setMessage("Not group administrator");
            }
            else{
                */
            Optional<CategoryEntity> target = categoryRepository.findById(categoryId);
            if (target.isEmpty()) {
                    /*
                    For synchronization cases, such as two administrators are deleting the same category.
                    One delete request would be successful, but the later request would have invalid category id.
                     */
                responseDto.setMessage("Invalid categoryId");
            } else {
                categoryRepository.delete(target.get());
                categoryClosureRepository.removeCategory(target.get().getCategoryId());
                responseDto.setMessage("SUCCESS");
            }
                /*
            }
            */
        }

        return responseDto;
    }

    public ResponseDto updateCategory(String token, Integer groupId, CategoryInfoDto categoryInfoDto, Integer categoryId) {
        ResponseDto responseDto = new ResponseDto();

        Optional<GroupEntity> groupEntity = groupRepository.findById(groupId);

        if (groupEntity.isEmpty()) {
            responseDto.setMessage("Invalid groupId");
        } else {
            /*
            if(token is not group admin){
                responseDto.setMessage("Not group administrator");
            }
            else{
                */
            Optional<CategoryEntity> target = categoryRepository.findById(categoryId);
            if (target.isEmpty()) {
                    /*
                    For synchronization cases, such as two administrators are deleting the same category.
                    One delete request would be successful, but the later request would have invalid category id.
                     */
                responseDto.setMessage("Invalid categoryId");
            } else {
                /* Update category name */
                target.get().setCategoryName(categoryInfoDto.getCategoryName());
                categoryRepository.save(target.get());
                /* update categoryClosureRepository */
                categoryClosureRepository.updateCategory(categoryId, categoryInfoDto.getParentCategoryId(), groupEntity.get().getRootCategoryId().getCategoryId());
                responseDto.setMessage("SUCCESS");
            }
                /*
            }
            */
        }

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

    public CategoryListDto makeCategory(String token, Integer groupId) {
        CategoryListDto responseDto = new CategoryListDto();

        return responseDto;
    }

    public ResponseDto makeNewProblem(String token, ProblemDto problemDto, Integer groupId) {
        ResponseDto responseDto = new ResponseDto();

        return responseDto;
    }
}
