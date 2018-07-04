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
	
	@Field(type=FieldType.Integer,index=false)
	private  Integer  infoClass;//来源类别：1:物性
	
	@Field(type=FieldType.Text)
	private  String   sid;//物性id：1查询物性列表，统计物性问题个数，2CMS系统物性规格名称修改时匹配，从而修改该文档物性规格名称
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private  String   source;//物性规格名称，
	
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
