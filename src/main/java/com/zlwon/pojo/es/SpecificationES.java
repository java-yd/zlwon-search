package com.zlwon.pojo.es;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.web.PageableDefault;

import com.zlwon.pojo.constant.EsConstant;

import lombok.Data;

/**
 * 物性文档
 * @author yuand
 *
 */
@Data
@Document(indexName=EsConstant.ES_INDEXNAME,type="specificationES",createIndex=false)
public class SpecificationES implements Serializable{

	@Id
	private  Integer   id;//物性id，也是文档的id
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private  String   name;//物性规格名称，页面显示
	
	@Field(type=FieldType.Text,index=false)
	private  String   brandName;//商标名，页面显示
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private  String   brandId;//商标id
	
	@Field(type=FieldType.Text,index=false)
	private  String   baseMaterial;//基材名称，页面显示
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private  String  baseId;//基材id
	
	@Field(type=FieldType.Text,index=false)
	private  String   manufacturer;//生产商名称，页面显示
	
	@Field(type=FieldType.Integer)
	private  Integer  manufacturerId;//生产商id
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private  String   content;//物性描述，页面显示
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private  String   finds;//填充物id，多个逗号隔开
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private  String   scids;//安规认证id，多个逗号隔开 
	
	@Field(type=FieldType.Date)
	private  Date  createDate;
	
}
