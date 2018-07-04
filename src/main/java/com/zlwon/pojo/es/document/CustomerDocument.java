package com.zlwon.pojo.es.document;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Parent;

import com.zlwon.pojo.constant.EsConstant;

import lombok.Data;

@Data
public class CustomerDocument {
	
	private  String    nickname;//提问用户昵称
	
	private  String    headerimg;//提问用户头像
	
	private  String    intro;//提问用户一句话介绍
}
