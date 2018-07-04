package com.zlwon.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import com.zlwon.mapper.CustomerMapper;
import com.zlwon.pojo.constant.EsConstant;
import com.zlwon.pojo.es.CustomerES;
import com.zlwon.pojo.es.document.CustomerDocument;
import com.zlwon.service.CustomerService;
import com.zlwon.utils.JsonUtils;
import com.zlwon.utils.es.ElasticsearchTemplateUtils;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private  CustomerMapper  customerMapper;
	@Autowired
	private  ElasticsearchTemplate   elasticsearchTemplate;
	
	/**
	 * 添加用户到文档中
	 * @return
	 */
	public void addCustomerDocument() {
		//得到所有用户信息，保存到文档中
		List<CustomerES> list = customerMapper.selectAllCustomer();
		if(list != null  &&  list.size() > 0){
			ElasticsearchTemplateUtils.addDocuments(elasticsearchTemplate, getCustomerIndexQuerys(list));
		}
	}
	
	//创建要插入的文档对象
	private   List<IndexQuery>  getCustomerIndexQuerys(List<CustomerES> list){
		List<IndexQuery> indexQuerys = new  ArrayList<>();
		IndexQuery query = null;
		
		for (CustomerES customerES : list) {
			query = new  IndexQuery();
			query.setId(customerES.getId().toString());
			query.setIndexName(EsConstant.ES_INDEXNAME);
			query.setType("customerES");
			query.setSource(getCustomerSource(customerES));
			indexQuerys.add(query);
		}
		return  indexQuerys;
	}

	
	private String getCustomerSource(CustomerES customerES) {
		CustomerDocument  document = new  CustomerDocument();
		BeanUtils.copyProperties(customerES, document);
		return JsonUtils.objectToJson(document);
	}

}
