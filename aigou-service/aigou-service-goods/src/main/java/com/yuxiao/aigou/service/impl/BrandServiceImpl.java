package com.yuxiao.aigou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuxiao.aigou.dao.BrandMapper;
import com.yuxiao.aigou.goods.pojo.Brand;
import com.yuxiao.aigou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 查询所有品牌
     * @return
     */
    @Override
    public List<Brand> findAll() {
        //查询所有 返回List<Brand>
        return brandMapper.selectAll();
    }

    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Brand brand) {
        //通用mapper（tk包中的） 但凡含有 Selective 会忽略空值
        brandMapper.insertSelective(brand);
    }

    /**
     * 根据id修改品牌数据
     * @param brand
     */
    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    @Override
    public void delete(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    /**
     * 多条件搜索
     * @param brand
     * @return
     */
    @Override
    public List<Brand> findList(Brand brand) {
        Example example=createExample(brand);
        return brandMapper.selectByExample(example);
    }

    /**
     * 品牌列表分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Brand> findPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return new PageInfo<Brand>(brandMapper.selectAll());
    }

    @Override
    public PageInfo<Brand> findPage(Brand brand, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return new PageInfo<Brand>(brandMapper.selectByExample(createExample(brand)));
    }

    private Example createExample(Brand brand){
        //自定义条件搜索对象 example (tk.mybatis.mapper.entity.Example)
        //构造函数传入 当前对象的class文件
        Example example=new Example(Brand.class);
        //条件构造器
        Example.Criteria criteria=example.createCriteria();
        if (brand!=null){
            //brand.name !=null 根据名字模糊查询  where name like '%name%'
            if (!StringUtils.isEmpty(brand.getName())){
                //andLike 参数介绍：1.Brand属性名 2.搜索条件
                criteria.andLike("name","%"+brand.getName()+"%");
            }
            //brand.letter !=null 根据首字母查询
            if (!StringUtils.isEmpty(brand.getLetter())){
                //andLike 参数介绍：1.Brand属性名 2.搜索条件
                criteria.andEqualTo("letter",brand.getLetter());
            }
        }
        return example;
    }


}
