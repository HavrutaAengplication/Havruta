package com.example.havruta.data.dao.impl;

import com.example.havruta.data.dao.HavrutaDao;
import com.example.havruta.data.entity.*;
import com.example.havruta.data.entity.serializable.CategoryProblemId;
import com.example.havruta.data.entity.serializable.ClosureId;
import com.example.havruta.data.entity.serializable.MemberId;
import com.example.havruta.data.repository.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HavrutaDaoImpl implements HavrutaDao {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final CategoryClosureRepository categoryClosureRepository;
    private final ProblemRepository problemRepository;
    private final CategoryProblemRepository categoryProblemRepository;

    public HavrutaDaoImpl(GroupRepository groupRepository, UserRepository userRepository, CategoryRepository categoryRepository, MemberRepository memberRepository, CategoryClosureRepository categoryClosureRepository, ProblemRepository problemRepository, CategoryProblemRepository categoryProblemRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
        this.categoryClosureRepository = categoryClosureRepository;
        this.problemRepository = problemRepository;
        this.categoryProblemRepository = categoryProblemRepository;
    }

    @Override
    public Optional<UserEntity> signUp(String email, String userName) {
        Optional<UserEntity> userEntity = userRepository.findByUserEmail(email);
        if(userEntity.isEmpty()){
            UserEntity newUserEntity = new UserEntity();
            newUserEntity.setUserName(userName);
            newUserEntity.setUserEmail(email);
            userRepository.save(newUserEntity);
            return Optional.of(newUserEntity);
        }
        return Optional.ofNullable(null);
    }

    @Override
    public List<GroupEntity> findAllGroup() {
        return groupRepository.findAll();
    }

    public Optional<GroupEntity> findGroupById(Integer id){return groupRepository.findById(id);}

    public Optional<UserEntity> findByEmail(String email){return userRepository.findByUserEmail(email);}

    public Optional<UserEntity> findByUserId(Integer userID){return userRepository.findById(userID);}
    public Optional<GroupEntity> saveNewGroup(Integer userId, String groupName){
        GroupEntity groupEntity = new GroupEntity();
        CategoryEntity categoryEntity = new CategoryEntity();
        CategoryClosureEntity categoryClosureEntity = new CategoryClosureEntity();
        MemberEntity memberEntity = new MemberEntity();

        categoryEntity.setCategoryName(groupName);
        CategoryEntity savedCategoryEntity = categoryRepository.save(categoryEntity);

        groupEntity.setGroupName(groupName);
        groupEntity.setRootCategoryId(categoryEntity);
        GroupEntity savedGroupEntity = groupRepository.save(groupEntity);

        categoryClosureEntity.setId(new ClosureId());
        categoryClosureEntity.setParent(savedCategoryEntity);
        categoryClosureEntity.setChild(savedCategoryEntity);
        categoryClosureEntity.setDepth(0);
        categoryClosureRepository.save(categoryClosureEntity);

        System.out.println("userRepository.findById(userId).get() = " + userId);
        memberEntity.setId(new MemberId());
        memberEntity.setUserEntity(userRepository.findById(userId).get());
        memberEntity.setGroupEntity(savedGroupEntity);
        memberEntity.setIsAdmin(1);
        memberEntity.setIsMember(1);
        memberRepository.save(memberEntity);

        return Optional.of(savedGroupEntity);
    }

    public List<GroupEntity> findByUserEntity(UserEntity userEntity){
        List<GroupEntity> groupEntityList = new ArrayList<>();
        List<MemberEntity> memberEntityList = new ArrayList<>();
        memberEntityList = memberRepository.findByUserEntity(userEntity);
        for (MemberEntity memberEntity : memberEntityList) {
            groupEntityList.add(memberEntity.getGroupEntity());
        }
        return groupEntityList;
    }

    @Override
    public Optional<UserEntity> changeUserName(Integer userID, String userName) {
        UserEntity userEntity = userRepository.findById(userID).get();
        userEntity.setUserName(userName);
        return Optional.of(userRepository.save(userEntity));
    }

    public void deleteUser(Integer userID){
        userRepository.deleteById(userID);
    }

    @Override
    public List<ProblemEntity> getMyProblemList(UserEntity userEntity) {
        System.out.println("userEntity.getUserId() = " + userEntity.getUserId());
        return problemRepository.findAllByUserId(userEntity);
    }

    @Override
    public Optional<GroupEntity> findGroupByProblemEntity(ProblemEntity problemEntity) {
        Optional<CategoryProblemEntity> categoryProblemEntity
                = categoryProblemRepository.findFirstByProblemEntity(problemEntity);
        Integer categoryId = categoryProblemEntity.get().getCategoryEntity().getCategoryId();
        List<CategoryClosureEntity> categoryList = categoryClosureRepository.findById_ChildId(categoryId);
        Integer ans = -1;
        CategoryEntity parentCategoryEntity = new CategoryEntity();
        for(CategoryClosureEntity categoryClosureEntity : categoryList){
            if(ans < categoryClosureEntity.getDepth()){
                parentCategoryEntity = categoryClosureEntity.getParent();
                ans = categoryClosureEntity.getDepth();
            }
        }
        return groupRepository.findByRootCategoryId(parentCategoryEntity);

    }

    @Override
    public List<List<CategoryClosureEntity>> findCategoriesListByProblemEntity(ProblemEntity problemEntity) {
        List<CategoryProblemEntity> categoryProblemEntityList
                = categoryProblemRepository.findAllByProblemEntity(problemEntity);
        List<List<CategoryClosureEntity>> categoryIdList = new ArrayList<>();
        for(CategoryProblemEntity categoryProblemEntity : categoryProblemEntityList) {
            Integer categoryId = categoryProblemEntity.getCategoryEntity().getCategoryId();
            System.out.println("categoryId = " + categoryId);
            List<CategoryClosureEntity> categoryList = categoryClosureRepository.findById_ChildId(categoryId);
            categoryList.sort((o1, o2) -> Integer.compare(o2.getDepth(), o1.getDepth()));
            System.out.println("categoryList = " + categoryList);
            categoryIdList.add(categoryList);
        }
        return categoryIdList;
    }

    @Override
    public void deleteCategoryListByProblemEntity(ProblemEntity problemEntity) {
        List<CategoryProblemEntity> categoryProblemEntityList
                = categoryProblemRepository.findAllByProblemEntity(problemEntity);
        for(CategoryProblemEntity categoryProblemEntity : categoryProblemEntityList){
            categoryProblemRepository.delete(categoryProblemEntity);
        }
    }

    @Override
    public Optional<ProblemEntity> findProblemEntityByProblemId(Integer problemId) {
        return problemRepository.findById(problemId);
    }

    @Override
    public Optional<ProblemEntity> changeProblem(ProblemEntity problemEntity) {
        return Optional.of(problemRepository.save(problemEntity));
    }

    @Override
    public Optional<CategoryEntity> findCategoryEntityByCategoryId(Integer categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public List<CategoryProblemEntity> saveNewCategoryProblem(ProblemEntity problemEntity, List<CategoryEntity> categoryEntityList) {
        List<CategoryProblemEntity> categoryProblemEntityList = new ArrayList<>();
        for(CategoryEntity categoryEntity :categoryEntityList){
            CategoryProblemEntity categoryProblemEntity = new CategoryProblemEntity();
            categoryProblemEntity.setId(new CategoryProblemId());
            categoryProblemEntity.setProblemEntity(problemEntity);
            categoryProblemEntity.setCategoryEntity(categoryEntity);
            categoryProblemRepository.save(categoryProblemEntity);
            categoryProblemEntityList.add(categoryProblemEntity);
        }

        return categoryProblemEntityList;
    }

    @Override
    public void deleteProblem(Integer problemId) {
        problemRepository.deleteById(problemId);
    }


}
