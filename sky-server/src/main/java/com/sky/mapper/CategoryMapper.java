package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: 孙毅
 * @Date: 2025/5/22 20:10
 * @Description:
 */
@Mapper
public interface CategoryMapper {

    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    @AutoFill(value = OperationType.INSERT)
    void addCategory(Category category);

    /**
     * 启用禁用功能
     * @param category
     */
    @Update("update category set status=#{status} where id=#{id}")
    @AutoFill(value = OperationType.UPDATE)
    void startOrStop(Category category);

    /**
     * 修改分类
     * @param category
     * @return
     */
    @Update("update category set name=#{name},sort=#{sort},update_time=#{updateTime} where id=#{id}")
    @AutoFill(value = OperationType.UPDATE)
    void updateCategory(Category category);

    @Delete("delete from category where id=#{id}")
    void deleteCategoryById(Long id);

    /**
     * 查询分类情况
     * @return
     */
    List<Category> listCategoryType(String type);
}
