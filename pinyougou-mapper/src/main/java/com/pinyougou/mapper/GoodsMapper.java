package com.pinyougou.mapper;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import com.pinyougou.pojo.Goods;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * GoodsMapper 数据访问接口
 * @date 2019-03-29 17:13:32
 * @version 1.0
 */
public interface GoodsMapper extends Mapper<Goods>{

    //多条件查询商品
    List<Map<String,Object>> findAll(Goods goods);

    //修改删除审核通用方法,添加列名参数
    void updateStatus(
            @Param("columnName")String columnName,
            @Param("ids") Long[] ids,
            @Param("status") String status);
}