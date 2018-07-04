package com.zlwon.pojo.es;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Parent;

import com.zlwon.pojo.constant.EsConstant;

import lombok.Data;

/**
 * 案例问题文档
 */
@Data
@Document(indexName=EsConstant.ES_INDEXNAME,type="applicationCaseQuestionsES")
public class ApplicationCaseQuestionsES {

	@Id
	private  Integer   id;//案例问题id，也是文档的id
	
	@Parent(type="applicationCaseES")
	private  String    applicationCaseId;//案例id
	
	@Field(type=FieldType.Integer,index=false)
	private  Integer  infoClass;//来源类别：2:案例
	
	@Field(type=FieldType.Text)
	private  String   aid;//案例id：CMS系统案例标题修改时匹配，从而修改该文档案例标题,由于父案例继承物性，使用HasParentQueryBuilder会无法匹配
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private  String   source;//案例标题，
	
	@Field(type=FieldType.Integer,index=false)
	private  Integer  uid;//提问者id,对应CustomerEs文档的id
	
	@Field(type=FieldType.Date)
	private  Date      createTime;//提问时间
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private  String    title;//提问标题
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private  String    content;//提问内容
	
	@Field(type=FieldType.Integer)
	private  Integer    answerCount;//回答个数，只是用来查询问题时，进行排序
}
