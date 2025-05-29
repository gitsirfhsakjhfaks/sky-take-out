package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: 孙毅
 * @Date: 2025/5/28 23:56
 * @Description:
 */
@Mapper
public interface DishFlavorMapper {
    /**
     * 向口味表插入n条数据
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品的Id集合删除对应的口味数据项
     * @param dishIds
     */
    void deleteByDishIds(List<Long> dishIds);

    @Select("select * from dish_flavor where dish_id=#{dishId}")
    List<DishFlavor> getByDishId(Long dishId);

    @Delete("DELETE from dish_flavor where dish_id=#{id}")
    void deleteByDishId(Long id);
}
