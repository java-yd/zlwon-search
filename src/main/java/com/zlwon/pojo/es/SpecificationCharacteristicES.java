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
 * 物性标签
 * @author yuand
 *
 */
@Data
@Document(indexName=EsConstant.ES_INDEXNAME,type="specificationCharacteristicES")
public class SpecificationCharacteristicES {

	@Id
	private  Integer   id;//物性标签id，也是文档的id
	
	@Parent(type="specificationES")
	private  String    specificationId;//物性id
	
	@Field(type=FieldType.Text,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
	private  String    labelName;//标签名称
}
