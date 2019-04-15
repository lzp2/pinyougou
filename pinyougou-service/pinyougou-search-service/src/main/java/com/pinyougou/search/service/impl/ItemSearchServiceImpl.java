package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.service.ItemSearchService;
import com.pinyougou.solr.SolrItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//商品搜索服务接口实现类
@Service(interfaceName = "com.pinyougou.service.ItemSearchService")
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    //搜索方法
    @Override
    public Map<String, Object> search(Map<String, Object> params) {
        try {
            //获取检索关键字
            String keywords = (String) params.get("keywords");
            //判断检索关键字
            if (StringUtils.isNoneBlank(keywords)) {//高亮查询
                //创建高亮查询对象
                HighlightQuery highlightQuery = new SimpleHighlightQuery();
                //创建查询条件对象
                Criteria criteria = new Criteria("keywords").is(keywords);//分词
                //添加查询条件(关键字)
                highlightQuery.addCriteria(criteria);

                //创建高亮选项对象
                HighlightOptions highlightOptions = new HighlightOptions();
                //设置高亮域
                highlightOptions.addField("title");
                //设置高亮前缀
                highlightOptions.setSimplePrefix("<font color='red'>");
                //设置高亮后缀
                highlightOptions.setSimplePostfix("</font>");
                //设置高亮选项
                highlightQuery.setHighlightOptions(highlightOptions);

                //1.按商品分类过滤
                String category = (String) params.get("category");
                if (StringUtils.isNoneBlank(category)){
                    //创建条件对象
                    Criteria criteria1 = new Criteria("category").is(category);
                    //添加过滤查询
                    highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                }
                //2.按商品品牌过滤
                String brand = (String) params.get("brand");
                if (StringUtils.isNoneBlank(category)){
                    //创建条件对象
                    Criteria criteria1 = new Criteria("brand").is(brand);
                    //添加过滤查询
                    highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                    System.out.println("============================");
                }
                //3.按商品规格过滤
                //spec:{"网络":"联通4G","机身内存":"64G"}
                Map<String,String> specMap = (Map<String, String>) params.get("spec");
                //"spec_网络":"联通4G"
                //"spec_机身内存":"64G"
                if (specMap != null && specMap.size() > 0){
                    //迭代规格,产生多个过滤条件
                    for (String key : specMap.keySet()) {
                        //创建条件对象
                        Criteria criteria1 = new Criteria("spec_" + key).is(specMap.get(key));
                        //添加过滤查询
                        highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                    }
                }
                //4.按商品价格过滤
                String price = (String) params.get("price");
                if (StringUtils.isNoneBlank(price)){
                    //0-500 1000-1500 3000-*
                    String[] priceArr = price.split("-");
                    //价格起始不是0(greaterThanEqual 大于等于)
                    if (!"0".equals(priceArr[0])){
                        //创建条件对象
                        Criteria criteria1 = new Criteria("price").greaterThanEqual(priceArr[0]);
                        highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                    }
                    //价格终点不是*(lessThanEqual 小于等于)
                    if (!"0".equals(priceArr[1])){
                        //创建条件对象
                        Criteria criteria1 = new Criteria("price").lessThanEqual(priceArr[1]);
                        highlightQuery.addFilterQuery(new SimpleFilterQuery(criteria1));
                    }
                }

                //分页查询,得到高亮分页查询对象
                HighlightPage<SolrItem> highlightPage = solrTemplate.
                        queryForHighlightPage(highlightQuery, SolrItem.class);
                //循环高亮项集合
                for (HighlightEntry<SolrItem> sihe : highlightPage.getHighlighted()) {
                    //获取检索到的原实体
                    SolrItem solrItem = sihe.getEntity();
                    //获取高亮内容集合
                    List<HighlightEntry.Highlight> highlights = sihe.getHighlights();
                    //判断高亮集合及集合中第一个Field的高亮内容
                    if (highlights != null && highlights.size() > 0){
                        String title = highlights.get(0).getSnipplets().get(0).toString();
                        //为标题设置高亮内容
                        solrItem.setTitle(title);
                    }
                }
                //创建Map集合封装返回数据
                Map<String, Object> data = new HashMap<>();
                data.put("rows",highlightPage.getContent());
                return data;
            }else {//简单查询
                //创建查询对象
                SimpleQuery simpleQuery = new SimpleQuery("*:*");
                //分页检索
                ScoredPage<SolrItem> scoredPage = solrTemplate.queryForPage(simpleQuery, SolrItem.class);

                //创建Map集合封装返回数据
                Map<String, Object> data = new HashMap<>();
                //获取内容
                data.put("rows", scoredPage.getContent());
                return data;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
