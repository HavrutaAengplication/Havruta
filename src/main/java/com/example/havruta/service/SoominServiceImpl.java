package com.example.havruta.service;

import com.example.havruta.data.dao.HavrutaDao;
import com.example.havruta.data.dto.*;
import com.example.havruta.data.entity.CategoryEntity;
import com.example.havruta.data.entity.GroupEntity;
import com.example.havruta.data.repository.CategoryClosureRepository;
import com.example.havruta.data.repository.CategoryRepository;
import com.example.havruta.data.repository.GroupRepository;
import com.example.havruta.data.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SoominServiceImpl implements SoominService{
    private final GroupRepository groupRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryClosureRepository categoryClosureRepository;

    private final ProblemRepository problemRepository;

    @Autowired
    public SoominServiceImpl(
            GroupRepository groupRepository,
            CategoryRepository categoryRepository,
            CategoryClosureRepository categoryClosureRepository,
            ProblemRepository problemRepository
    ) {
        this.groupRepository = groupRepository;
        this.categoryRepository = categoryRepository;
        this.categoryClosureRepository = categoryClosureRepository;
        this.problemRepository = problemRepository;
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
                Integer parentCategoryId = categoryInfoDto.getParentCategoryId();
                String categoryName = categoryInfoDto.getCategoryName();

                find if categoryName already exists as a child of parentCategoryId
                if true
                    responseDto.setMessage("Same category name already exists");

                => 이 작업은 FE에서 해주는 것이 나을 수도. (redirect 없이 FE에서 경고 표시만)
                else
                */
            CategoryEntity newCategoryEntity = categoryRepository.save(new CategoryEntity(0, categoryInfoDto.getCategoryName()));
            categoryClosureRepository.createCategory(newCategoryEntity.getCategoryId(), categoryInfoDto.getParentCategoryId());

            responseDto.setMessage("SUCCESS");
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
                /* TODO */
            }
                /*
            }
            */
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
