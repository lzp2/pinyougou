package com.pinyougou.solr.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.pojo.Item;
import com.pinyougou.solr.SolrItem;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//实现商品数据查询(已审核商品)
@Component
public class SolrUtils {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrTemplate solrTemplate;

    //导入商品数据到索引库
    public void importItemData(){
        //创建Item对象封装查询条件tb_item表
        Item item = new Item();
        //正常的商品
        item.setStatus("1");
        //从数据库表中查询商品数据
        //select * from tb_item where status = 1;
        List<Item> itemList = itemMapper.select(item);
        System.out.println("====商品列表====");

        List<SolrItem> solrItems = new ArrayList<>();
        //迭代SKU数据
        for (Item item1 : itemList) {
            SolrItem solrItem = new SolrItem();
            solrItem.setId(item1.getId());
            solrItem.setTitle(item1.getTitle());
            solrItem.setPrice(item1.getPrice());
            solrItem.setImage(item1.getImage());
            solrItem.setGoodsId(item1.getGoodsId());
            solrItem.setCategory(item1.getCategory());
            solrItem.setBrand(item1.getBrand());
            solrItem.setSeller(item1.getSeller());
            solrItem.setUpdateTime(item1.getUpdateTime());

            //动态域{"网络":"移动4G","机身内存":"64G"}
            String spec = item1.getSpec();
            //将spec字段的JSON字符串转换成map
            Map specMap = JSON.parseObject(spec,Map.class);
            //设置动态域
            solrItem.setSpecMap(specMap);

            solrItems.add(solrItem);
        }
        //保存数据到索引库
        UpdateResponse updateResponse = solrTemplate.saveBeans(solrItems);
        if (updateResponse.getStatus() == 0){
            solrTemplate.commit();
        }else {
            solrTemplate.rollback();
        }
        System.out.println("====结束====");
    }

    public static void main(String[] args) {
       ApplicationContext context = new  ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        SolrUtils contextBean = context.getBean(SolrUtils.class);
        contextBean.importItemData();
    }
}
