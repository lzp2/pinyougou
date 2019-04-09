package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.mapper.TypeTemplateMapper;
import com.pinyougou.pojo.SpecificationOption;
import com.pinyougou.pojo.TypeTemplate;
import com.pinyougou.service.TypeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service(interfaceName = "com.pinyougou.service.TypeTemplateService")
@Transactional
public class TypeTemplateServiceImpl implements TypeTemplateService{
    //注入数据访问接口代理对象
    @Autowired
    private TypeTemplateMapper typeTemplateMapper;
    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;
    //添加类型模板
    @Override
    public void save(TypeTemplate typeTemplate) {
        try {
            typeTemplateMapper.insertSelective(typeTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //修改类型模板
    @Override
    public void update(TypeTemplate typeTemplate) {
        try {
            typeTemplateMapper.updateByPrimaryKeySelective(typeTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Serializable id) {

    }

    //删除类型模板
    @Override
    public void deleteAll(Serializable[] ids) {
        try {
            typeTemplateMapper.deleteAll(ids);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //根据主键id查询类型模板
    @Override
    public TypeTemplate findOne(Serializable id) {
        return typeTemplateMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TypeTemplate> findAll() {
        return null;
    }

    //多条件查询分页
    @Override
    public PageResult findByPage(TypeTemplate typeTemplate, int page, int rows) {
        try {
            //开始分页
            PageInfo<TypeTemplate> pageInfo = PageHelper.startPage(page, rows).doSelectPageInfo(new ISelect() {
                @Override
                public void doSelect() {
                    typeTemplateMapper.findAll(typeTemplate);
                }
            });
            return new PageResult(pageInfo.getTotal(),pageInfo.getList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //根据模板id查询所有的规格与规格选项
    @Override
    public List<Map> findSpecByTemplateId(Long id) {
        try {
            //根据主键id查询模板
            TypeTemplate typeTemplate = findOne(id);
            //[{"id":33,"text":"电视屏幕尺寸"}]
            //获取模板中所有的规格,转化成List<Map>
            List<Map> specLists = JSON.parseArray(typeTemplate.getSpecIds(), Map.class);
            //迭代模板中所有的规格
            for (Map map : specLists) {
                //创建查询对象
                SpecificationOption so = new SpecificationOption();
                so.setSpecId(Long.valueOf(map.get("id").toString()));
                //通过规格id,查询规格选项数据
                List<SpecificationOption> optionList = specificationOptionMapper.select(so);
                map.put("options",optionList);
            }
            return specLists;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
