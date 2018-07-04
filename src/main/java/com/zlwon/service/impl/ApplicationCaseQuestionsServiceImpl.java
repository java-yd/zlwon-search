package com.zlwon.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import com.zlwon.mapper.QuestionsMapper;
import com.zlwon.pojo.constant.EsConstant;
import com.zlwon.pojo.es.ApplicationCaseQuestionsES;
import com.zlwon.pojo.es.document.ApplicationCaseQuestionsDocument;
import com.zlwon.service.ApplicationCaseQuestionsService;
import com.zlwon.utils.JsonUtils;
import com.zlwon.utils.es.ElasticsearchTemplateUtils;

@Service
public class ApplicationCaseQuestionsServiceImpl implements ApplicationCaseQuestionsService {

	@Autowired
	private  QuestionsMapper  questionsMapper;
	@Autowired
	private  ElasticsearchTemplate   elasticsearchTemplate;
	
	/**
	 * 添加案例提问到文档中
	 */
	public void addApplicationCaseQuestionsDocument() {
		List<ApplicationCaseQuestionsES> list = questionsMapper.selectAllApplicationCaseQuestions();
		if(list != null  &&  list.size() > 0){
			ElasticsearchTemplateUtils.addDocuments(elasticsearchTemplate, getApplicationCaseQuestionsIndexQuerys(list));
		}
	}

	//创建插入的文档对象
	private List<IndexQuery> getApplicationCaseQuestionsIndexQuerys(List<ApplicationCaseQuestionsES> list) {
		List<IndexQuery> indexQuerys = new  ArrayList<>();
		IndexQuery query = null;
		
		for (ApplicationCaseQuestionsES applicationCaseQuestionsES : list) {
			query = new  IndexQuery();
			query.setId(applicationCaseQuestionsES.getId().toString());
			query.setIndexName(EsConstant.ES_INDEXNAME);
			query.setType("applicationCaseQuestionsES");
			query.setParentId(applicationCaseQuestionsES.getApplicationCaseId());
			query.setSource(getApplicationCaseQuestionSource(applicationCaseQuestionsES));
			indexQuerys.add(query);
		}
		return  indexQuerys;
	}

	private String getApplicationCaseQuestionSource(ApplicationCaseQuestionsES applicationCaseQuestionsES) {
		ApplicationCaseQuestionsDocument  document = new  ApplicationCaseQuestionsDocument();
		BeanUtils.copyProperties(applicationCaseQuestionsES, document);
		document.setAid(applicationCaseQuestionsES.getApplicationCaseId());
		document.setInfoClass(2);
		return JsonUtils.objectToJson(document);
	}

}
