package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @Author: 孙毅
 * @Date: 2025/5/22 14:31
 * @Description:
 */
public interface CategoryService {
    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 新增菜品、新增套餐分类
     * @param categoryDTO
     * @return
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * 启用禁用功能
     * @param status
     * @param id
     * @return
     */
    void startOrStop(Integer status, Long id);

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    void updateCategory(CategoryDTO categoryDTO);

    /**
     * 删除分类
     * @param id
     * @return
     */
    void deleteCategoryById(Long id);

    /**
     * 查询分类情况
     * @return
     */
    List<Category> listCategoryType(String type);
}
