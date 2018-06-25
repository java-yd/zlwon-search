package com.zlwon.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import com.zlwon.mapper.ApplicationCaseMapper;
import com.zlwon.pojo.constant.EsConstant;
import com.zlwon.pojo.es.ApplicationCaseES;
import com.zlwon.pojo.es.document.ApplicationCaseDocument;
import com.zlwon.service.ApplicationCaseService;
import com.zlwon.utils.JsonUtils;
import com.zlwon.utils.es.ElasticsearchTemplateUtils;

@Service
public class ApplicationCaseServiceImpl implements ApplicationCaseService {

	@Autowired
	private  ApplicationCaseMapper  applicationCaseMapper;
	@Autowired
	private  ElasticsearchTemplate   elasticsearchTemplate;
	
	/**
	 * 添加案例到文档中
	 */
	public void addApplicationCaseDocument() {
		List<ApplicationCaseES> list = applicationCaseMapper.selectAllApplicationCase();
		if(list != null  &&  list.size() > 0){
			ElasticsearchTemplateUtils.addDocuments(elasticsearchTemplate, getApplicationCaseIndexQuerys(list));
		}
	}
	
	private   List<IndexQuery>  getApplicationCaseIndexQuerys(List<ApplicationCaseES> list){
		List<IndexQuery> indexQuerys = new  ArrayList<>();
		IndexQuery query = null;
		
		for (ApplicationCaseES applicationCaseES : list) {
			query = new  IndexQuery();
			query.setId(applicationCaseES.getId().toString());
			query.setIndexName(EsConstant.ES_INDEXNAME);
			query.setType("applicationCaseES");
			query.setParentId(applicationCaseES.getSid());
			query.setSource(getApplicationCaseSource(applicationCaseES));
			indexQuerys.add(query);
		}
		
		return  indexQuerys;
	}

	private String getApplicationCaseSource(ApplicationCaseES applicationCaseES) {
		ApplicationCaseDocument  document = new  ApplicationCaseDocument();
		BeanUtils.copyProperties(applicationCaseES, document);
		return JsonUtils.objectToJson(document);
	}
	
}
