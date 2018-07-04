package com.zlwon.pojo.es;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Parent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zlwon.pojo.constant.EsConstant;

import lombok.Data;

@Data
@Document(indexName=EsConstant.ES_INDEXNAME,type="applicationCaseES")
public class ApplicationCaseES {

	@Id
	private Integer id;  //案例id，也是文档的id
	
	@Parent(type="specificationES")
	private String specificationId;  //物性ID
	
	@Field(type=FieldType.Text)
	private  String   sid;//物性id，用来查询物性列表，统计案例个数
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private String title;  //案例名称
	
	@Field(type=FieldType.Text)
	private  String   emptyField;//匹配字符串，查询列表时(因为涉及到父(子)文档查询，使用的should(查询条件必须满足一个)，否则会造成案例数据缺失)，只用于无条件查询时匹配
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private String appProduct;  //应用产品名称
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private String terminal;//终端客户
	
	@Field(type=FieldType.Integer)
	private Integer industryId;  //应用行业ID
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private String marketId;  //应用市场ID
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private String selectRequirements;  //选材要求
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private String selectCause;  //选材原因
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private String setting;  //案例背景
	
	@Field(type=FieldType.Text,index=false)
	private String photo;  //缩略图
	
	@Field(type=FieldType.Date)
	private Date createTime;  //创建日期
	
	
}
