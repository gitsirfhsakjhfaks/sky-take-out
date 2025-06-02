package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

/**
 * @Author: 孙毅
 * @Date: 2025/5/31 19:13
 * @Description:
 */
public interface SetmealService {

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    void save(SetmealDTO setmealDTO);

    /**
     * 套餐起售、停售
     * @param id
     * @param status
     * @return
     */
    void startOrStop(Long id, Integer status);

    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetmealVO getById(Long id);
}
