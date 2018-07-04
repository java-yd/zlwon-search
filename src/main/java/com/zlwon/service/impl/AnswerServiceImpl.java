package com.zlwon.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import com.zlwon.mapper.AnswerMapper;
import com.zlwon.pojo.constant.EsConstant;
import com.zlwon.pojo.es.AnswerES;
import com.zlwon.pojo.es.ApplicationCaseQuestionsAnswerES;
import com.zlwon.pojo.es.SpecificationQuestionsAnswerES;
import com.zlwon.pojo.es.document.ApplicationCaseQuestionsAnswerDocument;
import com.zlwon.pojo.es.document.SpecificationQuestionsAnswerDocument;
import com.zlwon.service.AnswerService;
import com.zlwon.utils.JsonUtils;
import com.zlwon.utils.es.ElasticsearchTemplateUtils;

@Service
public class AnswerServiceImpl implements AnswerService {

	@Autowired
	private  AnswerMapper  answerMapper;
	@Autowired
	private  ElasticsearchTemplate   elasticsearchTemplate;
	
	/**
	 * 回答添加到文档中
	 * @return
	 */
	public void addAnswerDocument() {
		//得到所有回答信息，保存到文档中(案例提问回答文档，物性提问回答文档，数据一模一样，但是无法指定多个父类型，所以弄了俩个)
		List<AnswerES> list = answerMapper.selectAllAnswer();
		if(list != null  &&  list.size() > 0){
			ElasticsearchTemplateUtils.addDocuments(elasticsearchTemplate, getApplicationCaseQuestionsAnswerIndexQuerys(list));
			ElasticsearchTemplateUtils.addDocuments(elasticsearchTemplate, getSpecificationQuestionsAnswerIndexQuerys(list));
		}
	}
	
	//创建要插入案例提问回答的文档对象
	private   List<IndexQuery>  getApplicationCaseQuestionsAnswerIndexQuerys(List<AnswerES> list){
		List<IndexQuery> indexQuerys = new  ArrayList<>();
		IndexQuery query = null;
		
		for (AnswerES answerEs : list) {
			query = new  IndexQuery();
			query.setId(answerEs.getId().toString());
			query.setIndexName(EsConstant.ES_INDEXNAME);
			query.setType("applicationCaseQuestionsAnswerES");
			query.setParentId(answerEs.getQid());
			query.setSource(getApplicationCaseQuestionsAnswerSource(answerEs));
			indexQuerys.add(query);
		}
		return  indexQuerys;
	}
	private String getApplicationCaseQuestionsAnswerSource(AnswerES answerEs) {
		ApplicationCaseQuestionsAnswerDocument  document = new  ApplicationCaseQuestionsAnswerDocument();
		BeanUtils.copyProperties(answerEs, document);
		return JsonUtils.objectToJson(document);
	}
	
	//创建要插入物性提问回答的文档对象
	private   List<IndexQuery>  getSpecificationQuestionsAnswerIndexQuerys(List<AnswerES> list){
		List<IndexQuery> indexQuerys = new  ArrayList<>();
		IndexQuery query = null;
		
		for (AnswerES answerEs : list) {
			query = new  IndexQuery();
			query.setId(answerEs.getId().toString());
			query.setIndexName(EsConstant.ES_INDEXNAME);
			query.setType("specificationQuestionsAnswerES");
			query.setParentId(answerEs.getQid());
			query.setSource(getSpecificationQuestionsAnswerSource(answerEs));
			indexQuerys.add(query);
		}
		return  indexQuerys;
	}
	private String getSpecificationQuestionsAnswerSource(AnswerES answerEs) {
		SpecificationQuestionsAnswerDocument  document = new  SpecificationQuestionsAnswerDocument();
		BeanUtils.copyProperties(answerEs, document);
		return JsonUtils.objectToJson(document);
	}

}
