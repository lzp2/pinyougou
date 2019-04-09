package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Goods;
import com.pinyougou.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Reference(timeout = 2000)
    private GoodsService goodsService;

    //添加商品
    @PostMapping("/save")
    public boolean save(@RequestBody Goods goods){
        try {
            //获取登录用户名
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置商家id
            goods.setSellerId(sellerId);
            goodsService.save(goods);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //多条件查询商品分页
    @GetMapping("/findByPage")
    public PageResult findByPage(Goods goods, Integer page, Integer rows) {
        //获取商家编号
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        //添加查询条件
        goods.setSellerId(sellerId);
        //GET请求中文转码
        if (StringUtils.isNoneBlank(goods.getGoodsName())) {
            try {
                goods.setGoodsName(new String(goods.getGoodsName().getBytes("ISO8859-1"), "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return goodsService.findByPage(goods, page, rows);
    }

    //商家商品上下架(修改可销售状态)
    @GetMapping("/updateMarketable")
    public boolean updateMarketable(Long[] ids,String status){
        try {
            goodsService.updateStatus("is_marketable",ids,status);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
