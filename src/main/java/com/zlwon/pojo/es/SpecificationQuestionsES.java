package com.zlwon.pojo.es;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Parent;

import com.zlwon.pojo.constant.EsConstant;

import lombok.Data;

/**
 * 物性问题文档
 */
@Data
@Document(indexName=EsConstant.ES_INDEXNAME,type="specificationQuestionsES")
public class SpecificationQuestionsES {

	@Id
	private  Integer   id;//物性问题id，也是文档的id
	
	@Parent(type="specificationES")
	private  String    specificationId;//物性id
	
	@Field(type=FieldType.Text,index=false)
	private  String    nickname;//提问用户昵称
	
	@Field(type=FieldType.Text,index=false)
	private  Integer   uid;//提问用户id
	
	@Field(type=FieldType.Text,index=false)
	private  String    headerimg;//提问用户头像
	
	@Field(type=FieldType.Text,index=false)
	private  String    intro;//提问用户一句话介绍
	
	@Field(type=FieldType.Date)
	private  Date      createTime;//提问时间
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private  String    title;//提问标题
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private  String    content;//提问内容
	
}
