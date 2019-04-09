package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//商家控制器
@RestController
@RequestMapping("/seller")
public class sellerController {
    //注入商家服务接口代理对象
    @Reference(timeout = 2000)
    private SellerService sellerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //添加商家(入驻商家)
    @PostMapping("/save")
    public boolean save(@RequestBody Seller seller){
        try {
            //密码加密
            //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String password = passwordEncoder.encode(seller.getPassword());
            seller.setPassword(password);
            sellerService.save(seller);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
