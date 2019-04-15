package com.pinyougou.service;

import java.util.Map;

//商品搜索服务接口
public interface ItemSearchService {
    //搜索方法
    Map<String,Object> search(Map<String,Object> params);
}
