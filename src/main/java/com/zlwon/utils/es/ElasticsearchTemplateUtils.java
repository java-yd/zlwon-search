package com.zlwon.utils.es;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

public class ElasticsearchTemplateUtils {
	
	
	/**
	 * 根据文档id，得到文档信息
	 * @param id
	 * @param clazz
	 * @param elasticsearchTemplat
	 * @return
	 */
	public static <T> T queryOneSpecificationById(String   id,Class<T>  clazz,ElasticsearchTemplate  elasticsearchTemplat) {
		GetQuery query = new GetQuery();
		query.setId(id);
		return   elasticsearchTemplat.queryForObject(query , clazz);
	}
	
	/**
	 * 添加(更新)文档，批量添加(更新)，
	 * 	id有值：
	 * 		文档数据存在：更新文档
	 * 		文档数据不存在：添加文档
	 * 	id没值：
	 * 		添加文档，id自动生成
	 * @param elasticsearchTemplat
	 * @param IndexQuerys
	 */
	public  static void  addDocuments(ElasticsearchTemplate  elasticsearchTemplat,List<IndexQuery> indexQuerys){
		elasticsearchTemplat.bulkIndex(indexQuerys);
	}
	
	/**
	 * 添加文档，只添加一个
	 * @param elasticsearchTemplat
	 * @param IndexQuery
	 */
	public  static  void  addOneDocument(ElasticsearchTemplate  elasticsearchTemplat,IndexQuery indexQuery){
		List<IndexQuery> indexQuerys = new  ArrayList<IndexQuery>();
		indexQuerys.add(indexQuery);
		addDocuments(elasticsearchTemplat,indexQuerys);
	}
	
	/**
	 * 创建索引或创建(更新)mapping
	 * @param indexClass 索引类，没有穿null
	 * @param mappingClass mapping类，没有穿null
	 * @param elasticsearchTemplat 必传
	 */
	public  static   void  createIndexOrMapping(List<Class>  indexClass,List<Class>  mappingClass,ElasticsearchTemplate  elasticsearchTemplat){
		if(indexClass != null  && indexClass.size() > 0){
			createIndex(indexClass,elasticsearchTemplat);
		}
		if(mappingClass != null  && mappingClass.size() > 0){
			createMapping(mappingClass,elasticsearchTemplat);
		}
	}
	
	/**
	 * 删除索引
	 * @param indexClass 索引类,必传
	 * @param elasticsearchTemplat 必传
	 */
	public  static  void  deleteIndex(List<Class>  indexClass,ElasticsearchTemplate  elasticsearchTemplat){
		for (Class clazz : indexClass) {
			elasticsearchTemplat.deleteIndex(clazz);
		}
	}
	
	//创建mapping
	private  static  void  createMapping(List<Class>  mappingClass,ElasticsearchTemplate  elasticsearchTemplat){
		for (Class clazz : mappingClass) {
			elasticsearchTemplat.putMapping(clazz);
		}
	}
	//创建索引
	private   static  void   createIndex(List<Class>  indexClass,ElasticsearchTemplate  elasticsearchTemplat){
		for (Class clazz : indexClass) {
			elasticsearchTemplat.createIndex(clazz);
		}
	}
}
