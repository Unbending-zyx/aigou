package com.yuxiao.aigou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuxiao.aigou.common.entry.IdWorker;
import com.yuxiao.aigou.goods.dao.BrandMapper;
import com.yuxiao.aigou.goods.dao.CategoryMapper;
import com.yuxiao.aigou.goods.dao.SkuMapper;
import com.yuxiao.aigou.goods.dao.SpuMapper;
import com.yuxiao.aigou.goods.dto.Goods;
import com.yuxiao.aigou.goods.pojo.Brand;
import com.yuxiao.aigou.goods.pojo.Category;
import com.yuxiao.aigou.goods.pojo.Sku;
import com.yuxiao.aigou.goods.pojo.Spu;
import com.yuxiao.aigou.goods.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private IdWorker idWorker; //id生成器  雪花算法
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BrandMapper brandMapper;

    /**
     * 恢复数据
     * @param spuId
     */
    @Override
    public void restore(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        //检查是否删除的商品
        if(!spu.getIsDelete().equals("1")){
            throw new RuntimeException("此商品未删除！");
        }
        //未删除
        spu.setIsDelete("0");
        //未审核
        spu.setStatus("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 逻辑删除商品 将isDelete 修改为1
     */
    @Transactional
    @Override
    public void logicDelete(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        if(!spu.getIsMarketable().equals("0")){
            throw new RuntimeException("必须先下架再删除！");
        }
        //删除
        spu.setIsDelete("1");
        //未审核
        spu.setStatus("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 批量下架商品
     */
    @Override
    public int pullMany(Long[] ids) {
        Spu spu = new Spu();
        spu.setIsMarketable("0");//下架
        //批量修改
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //传入的id
        criteria.andIn("id", Arrays.asList(ids));
        //上架商品
        criteria.andEqualTo("isMarketable","1");
        //审核通过的商品
        criteria.andEqualTo("status","1");
        //非删除商品
        criteria.andEqualTo("isDelete","0");
        return spuMapper.updateByExampleSelective(spu,example);
    }


    /**
     * 批量上架商品
     */
    @Override
    public int putMany(Long[] ids) {
        Spu spu = new Spu();
        spu.setIsMarketable("1");//上架
        //批量修改
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //传入的id
        criteria.andIn("id", Arrays.asList(ids));
        //下架商品
        criteria.andEqualTo("isMarketable","0");
        //审核通过的商品
        criteria.andEqualTo("status","1");
        //非删除商品
        criteria.andEqualTo("isDelete","0");
        return spuMapper.updateByExampleSelective(spu,example);
    }

    /**
     * 商品上架
     */
    @Override
    public void put(Long spuId) throws Exception {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        if (spu.getIsDelete().equals("1")){
            throw new Exception("该商品已被删除");
        }
        if(!spu.getStatus().equals("1")){
            throw new Exception("未通过审核的商品不能上架！");
        }
        spu.setIsMarketable("1");//上架状态
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 商品下架
     */
    @Override
    public void pull(Long spuId) throws Exception {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        if (spu.getIsDelete().equals("1")){
            throw new Exception("该商品已被删除");
        }
        spu.setIsMarketable("0");//商品下架
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 商品审核方法
     */
    @Override
    public void audit(Long spuId) throws Exception {
        //流程 1.根据spuid查询 spu
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        //2. 修改spu相关状态
        if (spu.getIsDelete().equals("1")){
            throw new Exception("该商品已被删除");
        }
        spu.setStatus("1");//审核通过
        spu.setIsMarketable("1");//审核通过 商品自动上架
        //3.保存spu
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 根据id查询goods  根据spu id  查询goods
     * @param id spu id
     */
    @Override
    public Goods findGoodsById(Long id) {
        //两步
        //1.查询spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        Goods goods = new Goods();
        goods.setSpu(spu);
        //2.查询List<sku> 根据spu id  查询sku列表
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skus = skuMapper.select(sku);
        goods.setSkuList(skus);
        return goods;
    }

    /**
     * 添加商品信息  spu sku
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveGoods(Goods goods) {
//        System.out.println("传入的 goods 为："+JSON.toJSONString(goods));
        //spu -> 一个
        Spu spu = goods.getSpu();

        //判断spu 的id是否为空  为空则为新增  不为空则为修改
        if (spu.getId()==null){
            //为空  增加
            spu.setId(idWorker.nextId());
            spuMapper.insertSelective(spu);
        }else{
            //不为空  修改
            //修改需要两步  1.修改spu  2.删除之前的sku list
            spuMapper.updateByPrimaryKeySelective(spu);
            //删除之前的sku list
            Sku sku = new Sku();
            sku.setSpuId(spu.getId());
            skuMapper.delete(sku);
        }


        //sku -> list集合
        Date date = new Date();
        //查询三级分类
        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
        //查询品牌
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());

        for (Sku sku:goods.getSkuList()){
            sku.setId(idWorker.nextId()); //设置id

            //防止sku的spec为空  如果为空  设置空值  防止空指针异常
            if (StringUtils.isEmpty(sku.getSpec())){
                sku.setSpec("{}");
            }

            //设置sku名  名字生成规则：spu+规格信息（sku表中的spec字段）
            StringBuilder name=new StringBuilder(spu.getName());
            Map specMap = JSON.parseObject(sku.getSpec(), Map.class);
            for (Object e : specMap.entrySet()) {
                 name.append(" "+((Map.Entry) e).getValue());
            }
            sku.setName(name.toString());
            sku.setCreateTime(date);//设置创建时间
            sku.setUpdateTime(date);//设置更新时间
            sku.setSpuId(spu.getId());//设置关联的spuid
            sku.setCategoryId(category.getId());//设置类目id -> 三级分类id
            sku.setCategoryName(category.getName());//设置类目名称 -> 三级分类名称
            sku.setBrandName(brand.getName());//设置品牌名称

            //将sku添加到数据库中
            skuMapper.insertSelective(sku);
        }
    }

    /**
     * Spu条件+分页查询
     * @param spu 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Spu> findPage(Spu spu, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(spu);
        //执行搜索
        return new PageInfo<Spu>(spuMapper.selectByExample(example));
    }

    /**
     * Spu分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Spu> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Spu>(spuMapper.selectAll());
    }

    /**
     * Spu条件查询
     * @param spu
     * @return
     */
    @Override
    public List<Spu> findList(Spu spu){
        //构建查询条件
        Example example = createExample(spu);
        //根据构建的条件查询数据
        return spuMapper.selectByExample(example);
    }


    /**
     * Spu构建查询对象
     * @param spu
     * @return
     */
    public Example createExample(Spu spu){
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(spu!=null){
            // 主键
            if(!StringUtils.isEmpty(spu.getId())){
                    criteria.andEqualTo("id",spu.getId());
            }
            // 货号
            if(!StringUtils.isEmpty(spu.getSn())){
                    criteria.andEqualTo("sn",spu.getSn());
            }
            // SPU名
            if(!StringUtils.isEmpty(spu.getName())){
                    criteria.andLike("name","%"+spu.getName()+"%");
            }
            // 副标题
            if(!StringUtils.isEmpty(spu.getCaption())){
                    criteria.andEqualTo("caption",spu.getCaption());
            }
            // 品牌ID
            if(!StringUtils.isEmpty(spu.getBrandId())){
                    criteria.andEqualTo("brandId",spu.getBrandId());
            }
            // 一级分类
            if(!StringUtils.isEmpty(spu.getCategory1Id())){
                    criteria.andEqualTo("category1Id",spu.getCategory1Id());
            }
            // 二级分类
            if(!StringUtils.isEmpty(spu.getCategory2Id())){
                    criteria.andEqualTo("category2Id",spu.getCategory2Id());
            }
            // 三级分类
            if(!StringUtils.isEmpty(spu.getCategory3Id())){
                    criteria.andEqualTo("category3Id",spu.getCategory3Id());
            }
            // 模板ID
            if(!StringUtils.isEmpty(spu.getTemplateId())){
                    criteria.andEqualTo("templateId",spu.getTemplateId());
            }
            // 运费模板id
            if(!StringUtils.isEmpty(spu.getFreightId())){
                    criteria.andEqualTo("freightId",spu.getFreightId());
            }
            // 图片
            if(!StringUtils.isEmpty(spu.getImage())){
                    criteria.andEqualTo("image",spu.getImage());
            }
            // 图片列表
            if(!StringUtils.isEmpty(spu.getImages())){
                    criteria.andEqualTo("images",spu.getImages());
            }
            // 售后服务
            if(!StringUtils.isEmpty(spu.getSaleService())){
                    criteria.andEqualTo("saleService",spu.getSaleService());
            }
            // 介绍
            if(!StringUtils.isEmpty(spu.getIntroduction())){
                    criteria.andEqualTo("introduction",spu.getIntroduction());
            }
            // 规格列表
            if(!StringUtils.isEmpty(spu.getSpecItems())){
                    criteria.andEqualTo("specItems",spu.getSpecItems());
            }
            // 参数列表
            if(!StringUtils.isEmpty(spu.getParaItems())){
                    criteria.andEqualTo("paraItems",spu.getParaItems());
            }
            // 销量
            if(!StringUtils.isEmpty(spu.getSaleNum())){
                    criteria.andEqualTo("saleNum",spu.getSaleNum());
            }
            // 评论数
            if(!StringUtils.isEmpty(spu.getCommentNum())){
                    criteria.andEqualTo("commentNum",spu.getCommentNum());
            }
            // 是否上架,0已下架，1已上架
            if(!StringUtils.isEmpty(spu.getIsMarketable())){
                    criteria.andEqualTo("isMarketable",spu.getIsMarketable());
            }
            // 是否启用规格
            if(!StringUtils.isEmpty(spu.getIsEnableSpec())){
                    criteria.andEqualTo("isEnableSpec",spu.getIsEnableSpec());
            }
            // 是否删除,0:未删除，1：已删除
            if(!StringUtils.isEmpty(spu.getIsDelete())){
                    criteria.andEqualTo("isDelete",spu.getIsDelete());
            }
            // 审核状态，0：未审核，1：已审核，2：审核不通过
            if(!StringUtils.isEmpty(spu.getStatus())){
                    criteria.andEqualTo("status",spu.getStatus());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //检查是否被逻辑删除  ,必须先逻辑删除后才能物理删除
        if(!spu.getIsDelete().equals("1")){
            throw new RuntimeException("此商品不能删除！");
        }
        spuMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Spu
     * @param spu
     */
    @Override
    public void update(Spu spu){
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 增加Spu
     * @param spu
     */
    @Override
    public void add(Spu spu){
        spuMapper.insert(spu);
    }

    /**
     * 根据ID查询Spu
     * @param id
     * @return
     */
    @Override
    public Spu findById(Long id){
        return  spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Spu全部数据
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }
}
