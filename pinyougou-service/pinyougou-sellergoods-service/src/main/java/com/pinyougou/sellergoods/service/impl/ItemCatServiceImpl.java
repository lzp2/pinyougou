package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.ItemCatMapper;
import com.pinyougou.pojo.ItemCat;
import com.pinyougou.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

//商品服务接口实现类
@Service(interfaceName = "com.pinyougou.service.ItemCatService")
@Transactional
public class ItemCatServiceImpl implements ItemCatService{
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Override
    public void save(ItemCat itemCat) {

    }

    @Override
    public void update(ItemCat itemCat) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    @Override
    public ItemCat findOne(Serializable id) {
        return null;
    }

    @Override
    public List<ItemCat> findAll() {
        return null;
    }

    @Override
    public List<ItemCat> findByPage(ItemCat itemCat, int page, int rows) {
        return null;
    }

    //根据父级id查询商品下级分类列表
    @Override
    public List<ItemCat> findItemCatByParentId(Long parentId) {
        try {
            /** 创建ItemCat封装查询条件 */
            /*ItemCat itemCat = new ItemCat();
            itemCat.setParentId(parentId);
            return itemCatMapper.select(itemCat);*/

           return itemCatMapper.findItemCatByParentId(parentId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
