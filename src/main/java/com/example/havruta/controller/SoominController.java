package com.example.havruta.controller;

import com.example.havruta.data.dto.*;
import com.example.havruta.service.SoominService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/groups")
public class SoominController {
    private final SoominService soominService;

    @Autowired
    public SoominController(SoominService soominService){
        this.soominService = soominService;
    }

    @PostMapping("/{groupId}/categories")
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

    @DeleteMapping("/{groupId}/categories/{categoryId}")
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

    @PutMapping("/{groupId}/categories/{categoryId}")
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

    @GetMapping("/{groupId}/categories/{categoryId}")
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

    @PostMapping("/{groupId}/newbie")
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

    @GetMapping("/{groupId}/problems")
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

    @PostMapping("/{groupId}/problems")
    public ResponseEntity<ResponseDto> newProblemController(
            @RequestHeader("Authorization") String token,
            @RequestBody ProblemDto reqbody,
            @PathVariable Integer groupId
    ){
        ResponseDto responseDto = soominService.makeNewProblem(token, reqbody, groupId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/groups/" + groupId))
                .body(responseDto);
    }
}