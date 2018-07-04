package com.zlwon.pojo.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;

@Data
public class AnswerES {

	@Id
	private  Integer   id;//物性(案例)问题回答id，也是文档的id
	
	@Field(type=FieldType.Text)
	private  String   qid;//物性(案例)问题id，用来获取点赞最多的回答内容
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private  String   content;//回答内容
	
	@Field(type=FieldType.Integer)
	private  Integer  supportNums;//回答点赞个数
	
}
