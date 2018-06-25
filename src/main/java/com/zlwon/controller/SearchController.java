package com.zlwon.controller;

import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.support.replication.ReplicationRequestBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zlwon.pojo.es.dto.ApplicationCaseDTO;
import com.zlwon.pojo.es.dto.SpecificationDTO;
import com.zlwon.service.ApplicationCaseService;
import com.zlwon.service.SpecificationService;

@RestController
@RequestMapping("search")
public class SearchController {
	
	@Autowired
	private  ElasticsearchTemplate  elasticsearchTemplate;
	@Autowired
	private  SpecificationService  specificationService;
	@Autowired
	private  ApplicationCaseService  applicationCaseService;

	/**
	 * 物性搜索
	 * @return
	 */
	@RequestMapping(value="querySpecification",method=RequestMethod.GET)
	public   Object   querySpecification(@RequestParam(defaultValue="1")Integer  pageIndex,
			@RequestParam(defaultValue="10")Integer  pageSize,SpecificationDTO specificationDTO){
		Object object = specificationService.findSpecification(pageIndex,pageSize,specificationDTO);
		return  object;
	}
	
	/**
	 * 案例搜索
	 * @return
	 */
	@RequestMapping(value="queryApplicationCase",method=RequestMethod.GET)
	public   Object   queryApplicationCase(@RequestParam(defaultValue="1")Integer  pageIndex,
			@RequestParam(defaultValue="10")Integer  pageSize,ApplicationCaseDTO applicationCaseDTO){
		Object object = applicationCaseService.findApplicationCase(pageIndex,pageSize,applicationCaseDTO);
		return   object;
	}
}
