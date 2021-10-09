package com.yuxiao.aigou.goods.service;

import com.github.pagehelper.PageInfo;
import com.yuxiao.aigou.goods.dto.Goods;
import com.yuxiao.aigou.goods.pojo.Spu;

import java.util.List;

public interface SpuService {

    /***
     * 还原被删除商品
     * @param spuId
     */
    void restore(Long spuId);

    /**
     * 逻辑删除商品 将isDelete 修改为1
     */
    void logicDelete(Long spuId);

    /**
     * 批量下架商品
     */
    int pullMany(Long[] ids);

    /**
     * 批量上架商品
     */
    int putMany(Long[] ids);

    /**
     * 商品上架
     */
    void put(Long spuId) throws Exception;

    /**
     * 商品下架
     */
    void pull(Long spuId) throws Exception;

    /**
     * 商品审核方法
     */
    void audit(Long spuId) throws Exception;

    /**
     * 根据id查询goods  根据spu id  查询goods
     * @param id spu id
     */
    Goods findGoodsById(Long id);

    /**
     * 添加商品信息  spu sku
     *
     */
    void saveGoods(Goods goods);

    /***
     * Spu多条件分页查询
     * @param spu
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spu> findPage(Spu spu, int page, int size);

    /***
     * Spu分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spu> findPage(int page, int size);

    /***
     * Spu多条件搜索方法
     * @param spu
     * @return
     */
    List<Spu> findList(Spu spu);

    /***
     * 删除Spu
     * @param id
     */
    void delete(Long id);

    /***
     * 修改Spu数据
     * @param spu
     */
    void update(Spu spu);

    /***
     * 新增Spu
     * @param spu
     */
    void add(Spu spu);

    /**
     * 根据ID查询Spu
     * @param id
     * @return
     */
     Spu findById(Long id);

    /***
     * 查询所有Spu
     * @return
     */
    List<Spu> findAll();
}
