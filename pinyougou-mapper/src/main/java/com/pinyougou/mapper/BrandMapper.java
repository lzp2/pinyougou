package com.pinyougou.mapper;

import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import com.pinyougou.pojo.Brand;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * BrandMapper 数据访问接口
 * @date 2019-03-29 17:13:32
 * @version 1.0
 */
public interface BrandMapper extends Mapper<Brand>{
    //多条件查询品牌
    List<Brand> findAll(Brand brand);

    //批量删除
    void deleteAll(Serializable[] ids);
    //新建时查询所有品牌(id与name)
    @Select("SELECT id,name AS text FROM tb_brand ORDER BY id ASC ")
    List<Map<String,Object>> findByIdAndName();
}