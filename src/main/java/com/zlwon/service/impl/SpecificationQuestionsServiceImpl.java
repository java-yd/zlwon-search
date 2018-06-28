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
import com.zlwon.pojo.es.SpecificationES;
import com.zlwon.pojo.es.SpecificationQuestionsES;
import com.zlwon.pojo.es.document.SpecificationQuestionsDocument;
import com.zlwon.service.SpecificationQuestionsService;
import com.zlwon.utils.JsonUtils;
import com.zlwon.utils.es.ElasticsearchTemplateUtils;

@Service
public class SpecificationQuestionsServiceImpl implements SpecificationQuestionsService {

	@Autowired
	private  QuestionsMapper  questionsMapper;
	@Autowired
	private  ElasticsearchTemplate   elasticsearchTemplate;
	
	/**
	 * 添加物性提问到文档中
	 */
	@Override
	public void addSpecificationQuestionsDocument() {
		List<SpecificationQuestionsES> list = questionsMapper.selectAllSpecificationQuestions();
		if(list != null  &&  list.size() > 0){
			ElasticsearchTemplateUtils.addDocuments(elasticsearchTemplate, getSpecificationQuestionsIndexQuerys(list));
		}
	}

	//创建插入的文档对象
	private List<IndexQuery> getSpecificationQuestionsIndexQuerys(List<SpecificationQuestionsES> list) {
		List<IndexQuery> indexQuerys = new  ArrayList<>();
		IndexQuery query = null;
		
		for (SpecificationQuestionsES specificationQuestionsES : list) {
			query = new  IndexQuery();
			query.setId(specificationQuestionsES.getId().toString());
			query.setIndexName(EsConstant.ES_INDEXNAME);
			query.setType("specificationQuestionsES");
			query.setParentId(specificationQuestionsES.getSpecificationId());
			query.setSource(getSpecificatioQuestionSource(specificationQuestionsES));
			indexQuerys.add(query);
		}
		return  indexQuerys;
	}

	private String getSpecificatioQuestionSource(SpecificationQuestionsES specificationQuestionsES) {
		SpecificationQuestionsDocument  document = new  SpecificationQuestionsDocument();
		BeanUtils.copyProperties(specificationQuestionsES, document);
		document.setSid(specificationQuestionsES.getSpecificationId());
		return JsonUtils.objectToJson(document);
	}

}
