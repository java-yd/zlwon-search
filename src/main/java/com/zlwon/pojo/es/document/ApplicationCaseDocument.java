package com.zlwon.pojo.es.document;

import java.util.Date;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;

@Data
public class ApplicationCaseDocument {
	private String title;  //案例名称
	
	private String appProduct;  //应用产品名称
	
	private Integer industryId;  //应用行业ID
	
	private String marketId;  //应用市场ID
	
	private String selectRequirements;  //选材要求
	
	private String selectCause;  //选材原因
	
	private String setting;  //案例背景
	
	private String photo;  //缩略图
	
	private Date createTime;  //创建日期
}
