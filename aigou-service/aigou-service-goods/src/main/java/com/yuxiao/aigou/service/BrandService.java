package com.yuxiao.aigou.service;

import com.github.pagehelper.PageInfo;
import com.yuxiao.aigou.goods.pojo.Brand;

import java.util.List;

public interface BrandService {
    /**
     * 查询所有品牌列表
     */
    public List<Brand> findAll();

    public Brand findById(Integer id);

    public void add(Brand brand);

    /**
     * 修改品牌操作
     */
    public void update(Brand brand);

    /**
     * 根据id删除品牌
     * @param id
     */
    public void delete(Integer id);

    /**
     * 根据品牌信息多条件搜索
     */
    public List<Brand> findList(Brand brand);
    /**
     * 品牌列表分页查询
     */
    public PageInfo<Brand> findPage(Integer page, Integer size);

    /**
     * 条件+分页查询品牌列表
     * @param brand
     * @param page
     * @param size
     * @return
     */
    public PageInfo<Brand> findPage(Brand brand,Integer page,Integer size);
}
