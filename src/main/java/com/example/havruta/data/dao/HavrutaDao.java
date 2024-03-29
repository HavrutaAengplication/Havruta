package com.example.havruta.data.dao;

import com.example.havruta.data.entity.*;

import java.util.List;
import java.util.Optional;

public interface HavrutaDao {

    Optional<UserEntity> signUp(String email, String userName);
    /* 각 entity 별로 Dao 만드는 것이 좋아보임. GroupDao로 수정하는 것 어떤지? */
    List<GroupEntity> findAllGroup();
    Optional<GroupEntity> findGroupById(Integer id);
    Optional<UserEntity> findByEmail(String email);

    Optional<GroupEntity> saveNewGroup(Integer userId, String groupName);

    Optional<UserEntity> findByUserId(Integer userID);
    List<GroupEntity> findByUserEntity(UserEntity userEntity);

    Optional<UserEntity> changeUserName(Integer userID, String userName);

    void deleteUser(Integer userID);

    List<ProblemEntity> getMyProblemList(UserEntity userEntity);

    Optional<GroupEntity> findGroupByProblemEntity(ProblemEntity problemEntity);

    List<List<CategoryClosureEntity>> findCategoriesListByProblemEntity(ProblemEntity problemEntity);
    void deleteCategoryListByProblemEntity(ProblemEntity problemEntity);

    Optional<ProblemEntity> findProblemEntityByProblemId(Integer problemId);
    Optional<ProblemEntity> changeProblem(ProblemEntity problemEntity);

    Optional<CategoryEntity> findCategoryEntityByCategoryId (Integer categoryId);

    List<CategoryProblemEntity> saveNewCategoryProblem(ProblemEntity problemEntity, List<CategoryEntity> categoryEntityList);

    void deleteProblem(Integer problemId);

    public List<CategoryEntity> findAllCategory();
    public Optional<UserEntity> findUserById(Integer userId);
    public Optional<MemberEntity> findMemberById(Integer userId, Integer groupId);
    public List<CategoryClosureEntity> findClosuresByRootId(Integer rootId);
    public Optional<CategoryEntity> findCategoryById(Integer categoryId);
    public List<MemberEntity> findMembersByGroupId(Integer groupId);
    public Boolean designateAdmin(Integer newAdminId, Integer groupId);
    public Boolean dropMember(Integer userId, Integer groupId);
    public Boolean confirm(Integer userId, Integer groupId);
    public Boolean deleteGroup(Integer groupId);
    public Boolean updateGroup(String newGroupName, Integer groupId);
}
