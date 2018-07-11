package com.zlwon.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@RequestMapping("job")
public class JobController {
	
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

	/**
	 * 物性表添加到文档中
	 * @return
	 */
	@RequestMapping("addSpecificationDocument")
	public   String    addSpecificationDocument(){
		specificationService.addSpecificationDocument();
		return  "success";
	}
	
	/**
	 * 案例添加到文档中
	 * @return
	 */
	@RequestMapping("addApplicationCaseDocument")
	public   String    addApplicationCaseDocument(){
		applicationCaseService.addApplicationCaseDocument();
		return  "success";
	}
	
	/**
	 * 添加案例提问到文档中
	 */
	@RequestMapping("addApplicationCaseQuestionsDocument")
	public   String    addApplicationCaseQuestionsDocument(){
		applicationCaseQuestionsService.addApplicationCaseQuestionsDocument(null);
		return  "success";
	}
	
	/**
	 * 添加物性提问到文档中
	 * @return
	 */
	@RequestMapping("addSpecificationQuestionsDocument")
	public   String    addSpecificationQuestionsDocument(){
		specificationQuestionsService.addSpecificationQuestionsDocument(null);
		return  "success";
	}
	
	/**
	 * 添加报价单到文档中
	 */
	@RequestMapping("addSpecificationQuotationDocument")
	public   String   addSpecificationQuotationDocument(){
		specificationQuotationService.addSpecificationQuotationDocument(null);
		return  "success";
	}
	
	/**
	 * 添加物性标签到文档中
	 * @return
	 */
	@RequestMapping("addSpecificationCharacteristicDocument")
	public  String   addSpecificationCharacteristicDocument(){
		specificationCharacteristicService.addSpecificationCharacteristicDocument(null);
		return  "success";
	}
	
	/**
	 * 添加用户到文档中
	 * @return
	 */
	@RequestMapping("addCustomerDocument")
	public  String   addCustomerDocument(){
		customerService.addCustomerDocument();
		return  "success";
	}
	
	/**
	 * 回答添加到文档中
	 * @return
	 */
	@RequestMapping("addAnswerDocument")
	public  String   addAnswerDocument(){
		answerService.addAnswerDocument();
		return  "success";
	}
	
	
}
