package com.zlwon.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zlwon.pojo.constant.EsConstant;
import com.zlwon.pojo.es.ApplicationCaseES;
import com.zlwon.pojo.es.ApplicationCaseQuestionsAnswerES;
import com.zlwon.pojo.es.ApplicationCaseQuestionsES;
import com.zlwon.pojo.es.CustomerES;
import com.zlwon.pojo.es.SpecificationCharacteristicES;
import com.zlwon.pojo.es.SpecificationES;
import com.zlwon.pojo.es.SpecificationQuestionsAnswerES;
import com.zlwon.pojo.es.SpecificationQuestionsES;
import com.zlwon.pojo.es.SpecificationQuotationES;
import com.zlwon.service.AnswerService;
import com.zlwon.service.ApplicationCaseQuestionsService;
import com.zlwon.service.ApplicationCaseService;
import com.zlwon.service.CustomerService;
import com.zlwon.service.SpecificationCharacteristicService;
import com.zlwon.service.SpecificationQuestionsService;
import com.zlwon.service.SpecificationQuotationService;
import com.zlwon.service.SpecificationService;
import com.zlwon.utils.es.ElasticsearchTemplateUtils;

@Component
public class ScheduledJob {
	
	@Autowired
	private  SpecificationService  specificationService;
	@Autowired
	private  ApplicationCaseService  applicationCaseService;
	@Autowired
	private  SpecificationQuestionsService  specificationQuestionsService;
	@Autowired
	private  SpecificationQuotationService  specificationQuotationService;
	@Autowired
	private  ApplicationCaseQuestionsService  applicationCaseQuestionsService;
	@Autowired
	private  SpecificationCharacteristicService  specificationCharacteristicService;
	@Autowired
	private  CustomerService  customerService;
	@Autowired
	private  AnswerService  answerService;
	@Autowired
	private  ElasticsearchTemplate   elasticsearchTemplate;

	//每天凌晨3点执行
		@Scheduled(cron = "0 0 3  * * ? ")
	    public void reportCurrentByCron(){
			delIndex();
			createIndex();
			specificationService.addSpecificationDocument();
			applicationCaseService.addApplicationCaseDocument();
			applicationCaseQuestionsService.addApplicationCaseQuestionsDocument(null);
			specificationQuestionsService.addSpecificationQuestionsDocument(null);
			specificationQuotationService.addSpecificationQuotationDocument(null);
			specificationCharacteristicService.addSpecificationCharacteristicDocument(null);
			customerService.addCustomerDocument();
			answerService.addAnswerDocument();
	    }
		
		
		/**
		 * 创建索引
		 */
		private   void   createIndex(){
			List<Class> indexClass = new ArrayList<>();
			indexClass.add(SpecificationES.class);
			indexClass.add(ApplicationCaseES.class);
			indexClass.add(SpecificationQuestionsES.class);
			indexClass.add(SpecificationQuotationES.class);
			indexClass.add(ApplicationCaseQuestionsES.class);
			indexClass.add(SpecificationCharacteristicES.class);
			indexClass.add(CustomerES.class);
			indexClass.add(SpecificationQuestionsAnswerES.class);
			indexClass.add(ApplicationCaseQuestionsAnswerES.class);
			List<Class> mappingClass = new  ArrayList<>();
			mappingClass.add(SpecificationQuestionsAnswerES.class);
			mappingClass.add(ApplicationCaseQuestionsAnswerES.class);
			mappingClass.add(CustomerES.class);
			mappingClass.add(SpecificationCharacteristicES.class);
			mappingClass.add(ApplicationCaseQuestionsES.class);
			mappingClass.add(SpecificationQuotationES.class);
			mappingClass.add(SpecificationQuestionsES.class);
			mappingClass.add(ApplicationCaseES.class);
			mappingClass.add(SpecificationES.class);
			ElasticsearchTemplateUtils.createIndexOrMapping(indexClass, mappingClass, elasticsearchTemplate);
		}
		
		/**
		 * 删除索引库
		 */
		private  void  delIndex(){
			elasticsearchTemplate.deleteIndex(EsConstant.ES_INDEXNAME);
		}
}
