package com.zlwon.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import com.zlwon.mapper.CharacteristicMapper;
import com.zlwon.pojo.constant.EsConstant;
import com.zlwon.pojo.es.SpecificationCharacteristicES;
import com.zlwon.pojo.es.document.SpecificationCharacteristicDocument;
import com.zlwon.service.SpecificationCharacteristicService;
import com.zlwon.utils.JsonUtils;
import com.zlwon.utils.es.ElasticsearchTemplateUtils;

@Service
public class SpecificationCharacteristicServiceImpl implements SpecificationCharacteristicService {

	@Autowired
	private  CharacteristicMapper  characteristicMapper;
	@Autowired
	private  ElasticsearchTemplate   elasticsearchTemplate;
	
	/**
	 * 添加物性标签到文档中
	 * @return
	 */
	public void addSpecificationCharacteristicDocument() {
		List<SpecificationCharacteristicES> list = characteristicMapper.selectAllCharacteristic();
		if(list != null  &&  list.size() > 0){
			ElasticsearchTemplateUtils.addDocuments(elasticsearchTemplate, getSpecificationCharacteristicIndexQuerys(list));
		}
	}

	private List<IndexQuery> getSpecificationCharacteristicIndexQuerys(List<SpecificationCharacteristicES> list) {
		List<IndexQuery> indexQuerys = new  ArrayList<>();
		IndexQuery query = null;
		
		for (SpecificationCharacteristicES specificationCharacteristicES : list) {
			query = new  IndexQuery();
			query.setId(specificationCharacteristicES.getId().toString());
			query.setIndexName(EsConstant.ES_INDEXNAME);
			query.setType("specificationCharacteristicES");
			query.setParentId(specificationCharacteristicES.getSpecificationId());
			query.setSource(getSpecificationCharacteristicSource(specificationCharacteristicES));
			indexQuerys.add(query);
		}
		
		return  indexQuerys;
	}

	private String getSpecificationCharacteristicSource(SpecificationCharacteristicES specificationCharacteristicES) {
		SpecificationCharacteristicDocument  document = new SpecificationCharacteristicDocument();
		BeanUtils.copyProperties(specificationCharacteristicES, document);
		return JsonUtils.objectToJson(document);
	}

}
