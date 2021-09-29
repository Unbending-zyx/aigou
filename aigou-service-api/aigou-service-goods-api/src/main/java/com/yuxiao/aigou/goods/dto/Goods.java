package com.yuxiao.aigou.goods.dto;

import com.yuxiao.aigou.goods.pojo.Sku;
import com.yuxiao.aigou.goods.pojo.Spu;

import java.io.Serializable;
import java.util.List;

/**
 * 商品信息组合对象 （商品添加时用于接收参数的对象）
 * 封装了Spu  和List<Sku>
 */
public class Goods implements Serializable {
    //Spu 信息
    private Spu spu;
    //Sku 集合信息
    private List<Sku> skuList;

    public Spu getSpu() {
        return spu;
    }

    public void setSpu(Spu spu) {
        this.spu = spu;
    }

    public List<Sku> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<Sku> skuList) {
        this.skuList = skuList;
    }
}
