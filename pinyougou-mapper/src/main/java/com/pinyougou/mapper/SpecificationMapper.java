package com.pinyougou.mapper;

import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import com.pinyougou.pojo.Specification;

import java.util.List;
import java.util.Map;

/**
 * SpecificationMapper 数据访问接口
 * @date 2019-03-29 17:13:32
 * @version 1.0
 */
public interface SpecificationMapper extends Mapper<Specification>{

    //多条件查询规格
    List<Specification> findAll(Specification specification);

    //查询所有的规格(id与specName)
    @Select("SELECT id,spec_name AS text FROM tb_specification ORDER BY id ASC")
    List<Map<String,Object>> findByIdAndName();
}