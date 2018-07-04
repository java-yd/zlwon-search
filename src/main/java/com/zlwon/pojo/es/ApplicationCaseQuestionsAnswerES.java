package com.zlwon.pojo.es;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Parent;

import com.zlwon.pojo.constant.EsConstant;

import lombok.Data;

/**
 * 案例提问回答文档
 */
@Data
@Document(indexName=EsConstant.ES_INDEXNAME,type="applicationCaseQuestionsAnswerES")
public class ApplicationCaseQuestionsAnswerES extends AnswerES{

	@Parent(type="applicationCaseQuestionsES")
	private  String    applicationCaseQuestionsES;//案例问题id
	
}
