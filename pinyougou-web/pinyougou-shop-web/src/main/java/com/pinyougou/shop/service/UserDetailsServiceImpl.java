package com.pinyougou.shop.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Seller;
import com.pinyougou.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

//自定义用户认证服务类
public class UserDetailsServiceImpl implements UserDetailsService{
    //注入商家服务接口代理对象
    @Reference(timeout = 2000)
    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //创建List集合封装角色
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        //添加角色
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        //根据登录名查询商家
        Seller seller = sellerService.findOne(username);
        //判断商家是否为空,状态码
        if (seller != null && seller.getStatus().equals("1")){
            //返回用户信息对象
            System.out.println(seller.getPassword());
            return new User(username,seller.getPassword(),grantedAuthorities);
        }
        return null;
    }
}
