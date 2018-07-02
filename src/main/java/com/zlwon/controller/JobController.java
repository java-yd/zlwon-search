package com.zlwon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zlwon.service.ApplicationCaseQuestionsService;
import com.zlwon.service.ApplicationCaseService;
import com.zlwon.service.SpecificationCharacteristicService;
import com.zlwon.service.SpecificationQuestionsService;
import com.zlwon.service.SpecificationQuotationService;
import com.zlwon.service.SpecificationService;

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
		applicationCaseQuestionsService.addApplicationCaseQuestionsDocument();
		return  "success";
	}
	
	/**
	 * 添加物性提问到文档中
	 * @return
	 */
	@RequestMapping("addSpecificationQuestionsDocument")
	public   String    addSpecificationQuestionsDocument(){
		specificationQuestionsService.addSpecificationQuestionsDocument();
		return  "success";
	}
	
	/**
	 * 添加报价单到文档中
	 */
	@RequestMapping("addSpecificationQuotationDocument")
	public   String   addSpecificationQuotationDocument(){
		specificationQuotationService.addSpecificationQuotationDocument();
		return  "success";
	}
	
	/**
	 * 添加物性标签到文档中
	 * @return
	 */
	@RequestMapping("addSpecificationCharacteristicDocument")
	public  String   addSpecificationCharacteristicDocument(){
		specificationCharacteristicService.addSpecificationCharacteristicDocument();
		return  "success";
	}
}
