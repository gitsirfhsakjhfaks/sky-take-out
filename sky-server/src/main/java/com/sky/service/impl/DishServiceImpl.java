package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @Author: 孙毅
 * @Date: 2025/5/27 22:27
 * @Description:
 */
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @Override
    @Transactional// 启用事务，同时成功，同时失败
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        // Dish实体类中还有更新时间更新人等字段，因此需要进行对象转换
        BeanUtils.copyProperties(dishDTO,dish);

        // 向菜品表插入1条数据
        dishMapper.insert(dish);

        // 获取insert语句生成的主键值
        Long dishId = dish.getId();

        // 向口味表插入n条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size()>0){
            // 设置口味实体中的id
            flavors.forEach(fl->{
                fl.setDishId(dishId);
            });

            dishFlavorMapper.insertBatch(flavors);
        }


    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        // 使用PageHelper插件
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());// 自动在接下来的sql查询语句追加limit...
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        PageResult pageResult = new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }

    /**
     * 菜品批量删除
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 判断当前菜品是否能被删除？--是否存在起售中的商品
        List<Dish> dishes = dishMapper.getByIds(ids);// 得到所有菜品的集合
        boolean anyOnSale = dishes.stream()
                .filter(Objects::nonNull)// Objects类的一个静态方法nonNull
                .anyMatch(dish -> dish.getStatus() == StatusConstant.ENABLE);
        if (anyOnSale){
            // 当前菜品处于起售状态不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }
        // 判断当前菜品是否能被删除？--是否存在和套餐关联
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0){
            // 当前菜品和套餐关联，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }

        // 删除菜品表中的菜品数据
        dishMapper.deletByIds(ids);
        // 删除菜品对应的味道表数据
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * 菜品起售、停售
     * @param status
     * @param id
     * @return
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        dishMapper.startOrStop(status,id);
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Override
    @Transactional
    public DishVO getByIdWithFlavor(Long id) {
        // 根据id查询dish表得到dish数据
        Dish dish = dishMapper.getById(id);
        // 根据dish_id查询dish_flavor表得到flavor数据
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
        // 封装dish数据和flavor数据，返回VO对象
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        // 更新dish表的数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);// 传递的是dish对象，这样更具通用性
        dishMapper.update(dish);

        // 先删除原有的口味，再添加要新的前端传递过来的口味

        // 删除原有的口味
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        // 插入后生成的主键值赋值给对象
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
        }
        // 重新插入口味数据
        dishFlavorMapper.insertBatch(flavors);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> getByCategoryId(Long categoryId) {
        List<Dish> dishes = dishMapper.getByCategoryId(categoryId);
        return dishes;
    }
}
