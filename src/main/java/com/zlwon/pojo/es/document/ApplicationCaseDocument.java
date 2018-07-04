package com.zlwon.pojo.es.document;

import java.util.Date;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;

@Data
public class ApplicationCaseDocument {
	
	private  String   sid;//物性id，用来查询物性列表，统计案例个数
	
	private String title;  //案例名称
	
	private  String   emptyField;//匹配字符串，查询列表时(因为涉及到父(子)文档查询，使用的should(查询条件必须满足一个)，否则会造成案例数据缺失)
	
	private String appProduct;  //应用产品名称
	
	private String terminal;//终端客户
	
	private Integer industryId;  //应用行业ID
	
	private String marketId;  //应用市场ID
	
	private String selectRequirements;  //选材要求
	
	private String selectCause;  //选材原因
	
	private String setting;  //案例背景
	
	private String photo;  //缩略图
	
	private Date createTime;  //创建日期
}
