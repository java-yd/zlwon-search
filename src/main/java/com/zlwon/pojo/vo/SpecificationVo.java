package com.zlwon.pojo.vo;

import java.util.Date;

import lombok.Data;

@Data
public class SpecificationVo {
	
	private  Integer  id;

	private  String   name;//物性规格名称，页面显示
	
	private  String   brandName;//商标名，页面显示
	
	private  String   baseMaterial;//基材名称，页面显示
	
	private  String   manufacturer;//生产商名称，页面显示
	
	private  String   content;//物性描述，页面显示
	
	private  Integer  caseCount;//关联的案例个数
	
	private  Integer  questionCount;//提问个数
	
	private  Integer  quotationCount;//报价单个数
	
}
