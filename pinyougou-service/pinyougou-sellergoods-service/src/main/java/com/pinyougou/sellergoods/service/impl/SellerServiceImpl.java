package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.SellerMapper;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

//商家服务接口实现类
@Service(interfaceName = "com.pinyougou.service.SellerService")
@Transactional
public class SellerServiceImpl implements SellerService{
    @Autowired
    private SellerMapper sellserMapper;
    //添加商家
    @Override
    public void save(Seller seller) {
        try {
            //审核状态,未审核
            seller.setStatus("0");
            //审核创建时间
            seller.setCreateTime(new Date());
            sellserMapper.insertSelective(seller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void delete(Serializable id) {

    }

    @Override
    public void deleteAll(Serializable[] ids) {

    }

    //根据主键查询商家
    @Override
    public Seller findOne(Serializable id) {
        return sellserMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }

    //多条件分页查询商家
    @Override
    public PageResult findByPage(Seller seller, int page, int rows) {

        try {
            //开始分页
            PageInfo<Seller> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    sellserMapper.findAll(seller);
                }
            });
            return new PageResult(pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //审核商家(修改商家状态)
    @Override
    public void updateStatus(String sellerId, String status) {
        try {
            Seller seller = new Seller();
            seller.setSellerId(sellerId);
            seller.setStatus(status);
            sellserMapper.updateByPrimaryKeySelective(seller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
