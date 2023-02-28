package com.example.havruta.data.dao.impl;

import com.example.havruta.data.dao.WonbinDao;
import com.example.havruta.data.entity.*;
import com.example.havruta.data.entity.serializable.MemberId;
import com.example.havruta.data.repository.*;
import com.example.havruta.errorAndException.ErrorCode;
import com.example.havruta.errorAndException.NoGroupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WonbinDaoImpl implements WonbinDao {
    private CategoryRepository categoryRepository;
    private CategoryClosureRepository categoryClosureRepository;
    private GroupRepository groupRepository;
    private MemberRepository memberRepository;
    private ProblemRepository problemRepository;
    private UserRepository userRepository;

    @Autowired
    public WonbinDaoImpl(CategoryRepository categoryRepository,
                         CategoryClosureRepository categoryClosureRepository,
                         GroupRepository groupRepository,
                         MemberRepository memberRepository,
                         ProblemRepository problemRepository,
                         UserRepository userRepository
                         ) {
        this.categoryRepository = categoryRepository;
        this.categoryClosureRepository = categoryClosureRepository;
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<CategoryEntity> findAllCategory(){return categoryRepository.findAll();}
    @Override
    public Optional<GroupEntity> findGroupById(Integer groupId){return groupRepository.findById(groupId); }
    @Override
    public Optional<UserEntity> findUserById(Integer userId){return userRepository.findById(userId);}
    @Override
    public Optional<MemberEntity> findMemberById(Integer userId, Integer groupId){
        MemberId memberId = new MemberId(userId, groupId);
        return memberRepository.findById(memberId);
    }
    @Override
    public List<CategoryClosureEntity> findClosuresByRootId(Integer rootId){
        return null;
    }
    @Override
    public Optional<CategoryEntity> findCategoryById(Integer categoryId){
        return categoryRepository.findById(categoryId);
    }
    @Override
    public List<MemberEntity> findMembersByGroupId(Integer groupId){
        return memberRepository.findById_GroupId(groupId);
    }
    @Override
    public Boolean designateAdmin(Integer newAdminId, Integer groupId){
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(new MemberId(newAdminId, groupId));
        MemberEntity memberEntity = new MemberEntity();
        if(optionalMemberEntity.isPresent()){
            System.out.println(optionalMemberEntity);
            memberEntity = optionalMemberEntity.get();
            memberEntity.setIsAdmin(1);
            memberRepository.save(memberEntity);
            return true;
        }
        else{
            System.out.println(newAdminId);
            System.out.println(groupId);
            throw new NoGroupException("There is No Group", ErrorCode.NO_GROUP_ERROR);
        }
    }
    @Override
    public Boolean dropMember(Integer userId, Integer groupId){
        memberRepository.deleteById(new MemberId(userId, groupId));
        return true;
    }
    @Override
    public Boolean confirm(Integer userId, Integer groupId){
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(new MemberId(userId, groupId));
        MemberEntity memberEntity = new MemberEntity();
        if(optionalMemberEntity.isPresent()){
            System.out.println(optionalMemberEntity);
            memberEntity = optionalMemberEntity.get();
            memberEntity.setIsMember(1);
            memberRepository.save(memberEntity);
            return true;
        }
        else{
            System.out.println(userId);
            System.out.println(groupId);
            throw new NoGroupException("There is No Group", ErrorCode.NO_GROUP_ERROR);
        }
    }
    @Override
    public Boolean deleteGroup(Integer groupId){
        //그룹 삭제, 관련 멤버 삭제, 관련 카테고리 삭제?
        groupRepository.deleteById(groupId);
        memberRepository.deleteById_groupId(groupId);
        return true;
    }
    @Override
    public Boolean updateGroup(String newGroupName, Integer groupId){
        Optional<GroupEntity> optionalGroupEntity = groupRepository.findById(groupId);
        GroupEntity groupEntity = new GroupEntity();
        if(optionalGroupEntity.isPresent()){
            System.out.println(optionalGroupEntity);
            groupEntity = optionalGroupEntity.get();
            groupEntity.setGroupName(newGroupName);
            groupRepository.save((groupEntity));
            return true;
        }
        else{
            System.out.println(groupId);
            throw new NoGroupException("There is No Group", ErrorCode.NO_GROUP_ERROR);
        }
    }
}
