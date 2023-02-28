package com.example.havruta.controller;

import com.example.havruta.data.dto.*;
import com.example.havruta.service.SoominService;
import com.example.havruta.service.WonbinService;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/groups/{groupId}")
public class GroupsController {
    private final WonbinService wonbinService;
    private final SoominService soominService;

    @Autowired
    public GroupsController(WonbinService wonbinService, SoominService soominService) {
        this.wonbinService = wonbinService;
        this.soominService = soominService;
    }

    @GetMapping("")
    public ResponseEntity<SpecificGroupResponseDto> specificGroupController(
            @RequestHeader("Authorization") String token,
            @PathVariable("groupId") Integer groupId
    ){
        //넘겨줄 때 사용자 정보도 넘어가야 됨
        SpecificGroupResponseDto dto = wonbinService.specificGroupPage(groupId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                //.location(URI.create("/"))
                .body(dto);
    }

    @GetMapping("/admin")
    public ResponseEntity<AdminResponseDto> adminController(
            @RequestHeader("Authorization") String token,
            @PathVariable("groupId") Integer groupId
    ){
        //넘겨줄 때 사용자 정보도 넘어가야 됨
        AdminResponseDto dto = wonbinService.admin(groupId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                //.location(URI.create("/"))
                .body(dto);
    }

    @GetMapping("/members")
    public ResponseEntity<AdminMembersResponseDto> adminMembersController(
            @RequestHeader("Authorization") String token,
            @PathVariable("groupId") Integer groupId
    ){
        //넘겨줄 때 사용자 정보도 넘어가야 됨
        AdminMembersResponseDto dto = wonbinService.adminMembers(groupId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                //.location(URI.create("/"))
                .body(dto);
    }

    @PutMapping("/members")
    public ResponseEntity<ResponseDto> designateAdminController(
            @RequestHeader("Authorization") String token,
            @RequestBody DesignateAdminRequestDto reqBody,
            @PathVariable("groupId") Integer groupId
    ){
        //넘겨줄 때 사용자 정보도 넘어가야 됨
        ResponseDto dto = wonbinService.designateAdmin(reqBody.getNewAdminId(), groupId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                //.location(URI.create("/"))
                .body(dto);
    }

    @DeleteMapping("/members/{userId}")
    public ResponseEntity<ResponseDto> dropMemberController(
            @RequestHeader("Authorization") String token,
            @PathVariable("groupId") Integer groupId,
            @PathVariable("userId") Integer userId
    ){
        //넘겨줄 때 사용자 정보도 넘어가야 됨
        ResponseDto dto = wonbinService.dropMember(userId, groupId);
        return ResponseEntity
                .status(HttpStatus.OK)
                //.location(URI.create("/"))
                .body(dto);
    }

    @PutMapping("/members/{userId}")
    public ResponseEntity<ResponseDto> confirmController(
            @RequestHeader("Authorization") String token,
            @PathVariable("groupId") Integer groupId,
            @PathVariable("userId") Integer userId
    ){
        //넘겨줄 때 사용자 정보도 넘어가야 됨
        ResponseDto dto = wonbinService.confirm(userId, groupId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                //.location(URI.create("/"))
                .body(dto);
    }

    @DeleteMapping("")
    public ResponseEntity<ResponseDto> deleteGroupController(
            @RequestHeader("Authorization") String token,
            @PathVariable("groupId") Integer groupId
    ){
        //넘겨줄 때 사용자 정보도 넘어가야 됨
        ResponseDto dto = wonbinService.deleteGroup(groupId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                //.location(URI.create("/"))
                .body(dto);
    }

    @PutMapping("")
    public ResponseEntity<ResponseDto> updateGroupController(
            @RequestHeader("Authorization") String token,
            @RequestBody UpdateGroupRequestDto reqBody,
            @PathVariable("groupId") Integer groupId
    ){
        //넘겨줄 때 사용자 정보도 넘어가야 됨
        ResponseDto dto = wonbinService.updateGroup(reqBody.getNewGroupName(), groupId);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                //.location(URI.create("/"))
                .body(dto);
    }


    @PostMapping("/categories")
    public ResponseEntity<ResponseDto> createCategory(
            @PathVariable Integer groupId,
            @RequestHeader("Authorization") String token,
            @RequestBody CategoryInfoDto requestDto
    ){
        ResponseDto responseDto = soominService.newCategory(token, groupId, requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/groups/" + groupId + "/admin"))
                .body(responseDto);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<ResponseDto> deleteCategory(
            @PathVariable Integer groupId,
            @PathVariable Integer categoryId,
            @RequestHeader("Authorization") String token
    ){
        ResponseDto responseDto = soominService.deleteCategory(token, groupId, categoryId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .location(URI.create("/groups/" + groupId + "/admin"))
                .body(responseDto);
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<ResponseDto> updateCategory(
            @PathVariable Integer groupId,
            @PathVariable Integer categoryId,
            @RequestHeader("Authorization") String token,
            @RequestBody CategoryInfoDto requestDto
    ){
        ResponseDto responseDto = soominService.updateCategory(token, groupId, requestDto, categoryId);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/groups/" + groupId + "/admin"))
                .body(responseDto);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryProblemListDto> categoryProblemController(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer groupId,
            @PathVariable Integer categoryId
    ){
        CategoryProblemListDto responseDto = soominService.getCategoryProblem(token, groupId, categoryId);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/groups/" + groupId + "/categories/" + categoryId))
                .body(responseDto);
    }

    @PostMapping("/newbie")
    public ResponseEntity<ResponseDto> newbieController(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer groupId
    ){
        ResponseDto responseDto = soominService.registerGroup(token,groupId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/groups/" + groupId))
                .body(responseDto);
    }

    @GetMapping("/problems")
    public ResponseEntity<CategoryListDto> newProblemPageController(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer groupId
    ){
        CategoryListDto responseDto = soominService.getGroupCategory(groupId);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .location(URI.create("/groups/" + groupId + "/admin"))
                .body(responseDto);
    }

    @PostMapping("/problems")
    public ResponseEntity<ResponseDto> newProblemController(
            @RequestHeader("Authorization") String token,
            @RequestBody ProblemRequestDto reqbody,
            @PathVariable Integer groupId
    ){
        ResponseDto responseDto = soominService.makeNewProblem(token, reqbody, groupId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/groups/" + groupId))
                .body(responseDto);
    }
}
