package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: 孙毅
 * @Date: 2025/5/28 21:09
 * @Description:
 */
@Mapper
public interface SetmealDishMapper {
    /**
     * 根据dishId的集合查询setmealId
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
