package com.zlwon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zlwon.service.ApplicationCaseService;
import com.zlwon.service.SpecificationService;

@RestController
@RequestMapping("job")
public class JobController {
	
	@Autowired
	private  SpecificationService  specificationService;
	@Autowired
	private  ApplicationCaseService  applicationCaseService;

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
}
