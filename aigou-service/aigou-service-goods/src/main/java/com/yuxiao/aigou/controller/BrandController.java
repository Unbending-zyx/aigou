package com.yuxiao.aigou.controller;

import com.github.pagehelper.PageInfo;
import com.yuxiao.aigou.common.entry.Result;
import com.yuxiao.aigou.common.entry.StatusCode;
import com.yuxiao.aigou.goods.pojo.Brand;
import com.yuxiao.aigou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/brand")
@CrossOrigin //允许跨域
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 查询所有品牌  方法
     */

    @GetMapping
    public Result<List<Brand>> findAll(){
        List<Brand> brandAllList = brandService.findAll();
        //响应结果封装
        return new Result<List<Brand>>(true, StatusCode.OK,"查询所有品牌集合成功",brandAllList);
    }

    /**
     * 根据id查询品牌
     */
    @GetMapping(value = "/{id}")
    public Result<Brand> findById(@PathVariable("id") Integer id){
        Brand brand = brandService.findById(id);
        return new Result<Brand>(true,StatusCode.OK,"根据id查询品牌成功",brand);
    }

    /**
     * 插入新的品牌
     */
    @PostMapping
    public Result addBrand(@RequestBody Brand brand){
        System.out.println(brand.getName());
        brandService.add(brand);
        return new Result(true,StatusCode.OK,"品牌插入成功");

    }

    /**
     * 根据id修改品牌数据
     */
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable("id") Integer id,@RequestBody Brand brand){
        brand.setId(id);
        brandService.update(brand);
        return new Result(true,StatusCode.OK,"品牌修改成功");
    }

    /**
     * 根据id删除品牌
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id")Integer id){
        brandService.delete(id);
        return new Result(true,StatusCode.OK,"品牌删除成功");
    }

    /**
     * 根据条件查询品牌列表
     */
    @PostMapping(value = "/search")
    public Result<List<Brand>> findList(@RequestBody Brand brand){
        List<Brand> brandList = brandService.findList(brand);
        return new Result<List<Brand>>(true,StatusCode.OK,"品牌查询成功",brandList);
    }
    /**
     * 条件查询品牌列表
     */
    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(@PathVariable("page") int page,@PathVariable("size") int size){
        PageInfo<Brand> pageInfo=brandService.findPage(page, size);
        return new Result<PageInfo<Brand>>(true,StatusCode.OK,"品牌分页查询成功",pageInfo);
    }

    /**
     * 分页+条件查询品牌列表
     */
    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(@RequestBody Brand brand,@PathVariable("page") int page,@PathVariable("size") int size){
        PageInfo<Brand> pageInfo=brandService.findPage(brand,page, size);
        return new Result<PageInfo<Brand>>(true,StatusCode.OK,"品牌分页查询成功",pageInfo);
    }



}
