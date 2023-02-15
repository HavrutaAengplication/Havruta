package com.example.havruta.service;

import com.example.havruta.data.dto.*;

public interface SoominService {
    /* API #21 ~ */
    public ResponseDto newCategory(String token, Integer groupId, CategoryInfoDto reqbody);
    public ResponseDto deleteCategory(String token, Integer groupId, Integer categoryId);
    public ResponseDto updateCategory(String token, Integer groupId, CategoryInfoDto reqbody, Integer categoryId);
    public CategoryProblemListDto getCategoryProblem(String token, Integer groupId, Integer categoryId);
    public ResponseDto registerGroup(String token, Integer groupId);
    public CategoryListDto getGroupCategory(Integer groupId);
    public CategoryListDto makeCategory(String token, Integer groupId);
    public ResponseDto makeNewProblem(String token, ProblemDto reqbody, Integer groupId);
}
