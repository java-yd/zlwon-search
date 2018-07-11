package com.zlwon.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import com.zlwon.mapper.DealerdQuotationMapper;
import com.zlwon.pojo.constant.EsConstant;
import com.zlwon.pojo.es.ApplicationCaseES;
import com.zlwon.pojo.es.SpecificationQuotationES;
import com.zlwon.pojo.es.document.SpecificationQuotationDocument;
import com.zlwon.service.SpecificationQuotationService;
import com.zlwon.utils.JsonUtils;
import com.zlwon.utils.es.ElasticsearchTemplateUtils;

@Service
public class SpecificationQuotationServiceImpl implements SpecificationQuotationService {

	@Autowired
	private  DealerdQuotationMapper  dealerdQuotationMapper;
	@Autowired
	private  ElasticsearchTemplate   elasticsearchTemplate;
	
	/**
	 * 添加报价单到文档中
	 * @param id:报价单id，null就是添加所有
	 */
	@Override
	public void addSpecificationQuotationDocument(Integer id) {
		List<SpecificationQuotationES> list = null;
		if(id == null){
			list = dealerdQuotationMapper.selectAllDealerdQuotation();
		}else{
			list = new ArrayList<>();
			SpecificationQuotationES specificationQuotationES = dealerdQuotationMapper.selectById(id);
			if(specificationQuotationES != null){
				list.add(specificationQuotationES);
			}
		}
		if(list != null  &&  list.size() > 0){
			ElasticsearchTemplateUtils.addDocuments(elasticsearchTemplate, getSpecificationQuotationIndexQuerys(list));
		}
	}

	private List<IndexQuery> getSpecificationQuotationIndexQuerys(List<SpecificationQuotationES> list) {
		List<IndexQuery> indexQuerys = new  ArrayList<>();
		IndexQuery query = null;
		
		for (SpecificationQuotationES specificationQuotationES : list) {
			query = new  IndexQuery();
			query.setId(specificationQuotationES.getId().toString());
			query.setIndexName(EsConstant.ES_INDEXNAME);
			query.setType("specificationQuotationES");
			query.setParentId(specificationQuotationES.getSpecificationId());
			query.setSource(getSpecificationQuotationSource(specificationQuotationES));
			indexQuerys.add(query);
		}
		
		return  indexQuerys;
	}

	private String getSpecificationQuotationSource(SpecificationQuotationES specificationQuotationES) {
		SpecificationQuotationDocument  document = new SpecificationQuotationDocument();
		BeanUtils.copyProperties(specificationQuotationES, document);
		document.setSid(specificationQuotationES.getSpecificationId());
		return JsonUtils.objectToJson(document);
	}

}
