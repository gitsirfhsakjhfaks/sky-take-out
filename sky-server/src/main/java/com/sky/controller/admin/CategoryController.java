package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 孙毅
 * @Date: 2025/5/22 14:23
 * @Description:
 */
@RestController // 表明返回值会自动变成JSON格式
@RequestMapping(("/admin/category"))
@Slf4j // 日志框架的注解
@Api(tags = "分类管理接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){// 查询参数自动封装到类中
        log.info("分类分页查询,前端传递过来的对象是:{}",categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增菜品、新增套餐分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品、新增套餐分类")
    public Result addCategory(@RequestBody CategoryDTO categoryDTO){
        log.info("新增菜品、新增套餐分类,前端传递的对象是:{}",categoryDTO);
        categoryService.addCategory(categoryDTO);// 可以不接收返回值，成功即可
        return Result.success();
    }

    /**
     * 启用禁用功能
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用功能")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("启用禁用功能");
        categoryService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类,修改的信息为:{}",categoryDTO);
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除分类")
    public Result deleteCategoryById(Long id){
        log.info("删除分类,id是:{}",id);
        categoryService.deleteCategoryById(id);
        return Result.success();
    }
}
