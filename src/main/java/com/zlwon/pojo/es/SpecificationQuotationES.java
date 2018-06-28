package com.zlwon.pojo.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Parent;

import com.zlwon.pojo.constant.EsConstant;

import lombok.Data;

/**
 * 报价单文档，只为统计个数
 * @author yuand
 *
 */
@Data
@Document(indexName=EsConstant.ES_INDEXNAME,type="specificationQuotationES")
public class SpecificationQuotationES {

	@Id
	private  Integer   id;//报价单id，也是文档的id
	
	@Parent(type="specificationES")
	private  String    specificationId;//物性id
	
	@Field(type=FieldType.Text)
	private  String   sid;//物性id，用来查询物性列表，统计报价单个数
}
