package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

//商家管理控制器
@RestController
@RequestMapping("/seller")
public class SellerController {
    @Reference(timeout = 2000)
    private SellerService sellerService;

    //多条件查询商家
    @GetMapping("/findByPage")
    public PageResult findByPage(Seller seller,Integer page,Integer rows){
        try {
            if (seller != null && StringUtils.isNoneBlank(seller.getName())){
                seller.setName(new String(seller.getName().getBytes("ISO8859-1"),"UTF-8"));
            }
            if (seller != null && StringUtils.isNoneBlank(seller.getNickName())){
                seller.setName(new String(seller.getName().getBytes("ISO8859-1"),"UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sellerService.findByPage(seller,page,rows);
    }

    //审核商家(修改商家状态)
    @GetMapping("/updateStatus")
    public boolean updateStatus(String sellerId,String status){
        try {
            sellerService.updateStatus(sellerId,status);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
