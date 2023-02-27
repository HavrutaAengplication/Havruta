package com.example.havruta.data.dao;

import com.example.havruta.data.entity.*;

import java.util.List;
import java.util.Optional;

public interface WonbinDao {
    public List<CategoryEntity> findAllCategory();
    public Optional<GroupEntity> findGroupById(Integer groupId);
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
