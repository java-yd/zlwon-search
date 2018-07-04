package com.zlwon.pojo.es;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Parent;

import com.zlwon.pojo.constant.EsConstant;

import lombok.Data;

/**
 * 案例提问回答文档
 */
@Data
@Document(indexName=EsConstant.ES_INDEXNAME,type="specificationQuestionsAnswerES")
public class SpecificationQuestionsAnswerES extends AnswerES{

	@Parent(type="specificationQuestionsES")
	private  String    specificationQuestionsES;//物性问题id
	
}
