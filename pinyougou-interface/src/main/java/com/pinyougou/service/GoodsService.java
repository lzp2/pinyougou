package com.pinyougou.service;

import com.pinyougou.common.pojo.PageResult;
import com.pinyougou.pojo.Goods;
import java.util.List;
import java.io.Serializable;
/**
 * GoodsService 服务接口
 * @date 2019-03-29 17:20:44
 * @version 1.0
 */
public interface GoodsService {

	/** 添加方法 */
	void save(Goods goods);

	/** 修改方法 */
	void update(Goods goods);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	Goods findOne(Serializable id);

	/** 查询全部 */
	List<Goods> findAll();

	/** 多条件分页查询 */
	/**
	 *
	 * @param goods 商品对象
	 * @param page	商品页码
	 * @param rows	每页显示的记录数
	 * @return	商品分页数据
	 */
	PageResult findByPage(Goods goods, int page, int rows);

	//批量修改商品审核状态
    void updateStatus(String columnName,Long[] ids, String status);

    //修改商品可销售状态
	//void updateMarketable(Long[] ids, String status);
}