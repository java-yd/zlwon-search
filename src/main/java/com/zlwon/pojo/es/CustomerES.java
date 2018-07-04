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
 * 用户文档
 * 不和别的文档关联，别的文档如果需要用户信息，通过用户id来获取用户信息
 *
 */
@Data
@Document(indexName=EsConstant.ES_INDEXNAME,type="customerES")
public class CustomerES {
	
	@Id
	private  Integer   id;//用户id，也是文档id
	
	@Field(type=FieldType.Text,index=false)
	private  String    nickname;//提问用户昵称
	
	@Field(type=FieldType.Text,index=false)
	private  String    headerimg;//提问用户头像
	
	@Field(type=FieldType.Text,index=false)
	private  String    intro;//提问用户一句话介绍
}
