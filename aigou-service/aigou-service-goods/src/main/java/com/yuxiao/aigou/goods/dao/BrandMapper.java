package com.yuxiao.aigou.goods.dao;
import com.yuxiao.aigou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface BrandMapper extends Mapper<Brand> {

    /**
     * 根据分类id查询品牌集合
     * @param categoryid
     * @return
     */
    @Select("select * from tb_brand b,tb_category_brand cb WHERE b.id=cb.brand_id AND cb.category_id=#{categoryid}")
    List<Brand> findByCategory(Integer categoryid);
}
