package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: 孙毅
 * @Date: 2025/5/31 19:13
 * @Description:
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        // 使用PageHelper插件自动分页
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        // 将分号的页传递到下一条的数据库操作
        // 并把查询集合封装到page对象里，page对象提供了计数分页的方法，例如统计分页中的总数
        Page<SetmealVO> page= setmealMapper.pageQuery(setmealPageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @Override
    public void save(SetmealDTO setmealDTO) {

        // 保存到setmeal表里(注意获取数据库生成的主键)
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.save(setmeal);

        // 保存到setmeal_dish表里,保存套餐和菜品的关联关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        // 获取生成的套餐id
        Long setmealId = setmeal.getId();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });// 一个setmeal_id可以有多个dish_id
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 套餐起售、停售
     * @param id
     * @param status
     * @return
     */
    @Override
    public void startOrStop(Long id, Integer status) {
        // 如果套餐中包含已经停售的菜品，则不能开启状态为起售
        if (status== StatusConstant.DISABLE){
            // 根据id找出对应菜品的列表，再看菜品中是否有已经停售的状态
            //select * from setmeal a left join setmeal_dish b on a.id = b.setmeal_id
            // where id=#{id}
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if (dishList!=null && dishList.size()>0){
                dishList.forEach(dish -> {
                    if (StatusConstant.DISABLE == dish.getStatus()){// 如果存在套餐下的某个菜品停售，抛出异常
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        // 更新状态（传递一个实体对象而非单个属性，更具扩展性，未来要更新别的字段也能用这个mapper接口）
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }
}
