package com.yuxiao.aigou.dao;

import com.yuxiao.aigou.goods.pojo.Brand;
import tk.mybatis.mapper.common.Mapper;

/**
 * 品牌dao层
 * DAO使用通用Mapper Dao接口需要继承 tk.mybatis.mapper.common.Mapper接口
 *      增加数据：调用Mapper.insert()
 *      增加数据：调用Mapper.insertSelective()
 *
 *      修改数据：调用Mapper.update(javabean对象)
 *      修改数据：调用Mapper.updateByPrimayKey(javabean对象) 根据主键修改
 *
 *      查询数据：
 *          根据id查询：调用Mapper.selectByPrimaryKey(id)  根据主键id查询
 *          根据条件查询：调用Mapper.select(javabean对象)
 *
 *
 */
public interface BrandMapper extends Mapper<Brand> {

}
