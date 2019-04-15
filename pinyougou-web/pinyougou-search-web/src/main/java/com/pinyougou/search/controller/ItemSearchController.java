package com.pinyougou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.service.ItemSearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//商品搜索控制器
@RestController
public class ItemSearchController {
    @Reference(timeout = 2000)
    private ItemSearchService itemSearchService;

    //解决搜索高并发
    @PostMapping("/Search")
    public Map<String,Object> search(@RequestBody Map<String,Object> params){
        return itemSearchService.search(params);
    }
}
