package com.zlwon.pojo.es.document;

import java.util.Date;

import lombok.Data;

@Data
public class SpecificationDocument {

	private  String   name;//物性规格名称，页面显示
	
	private  String   emptyField;//匹配字符串，查询列表时，要不然不是都显示
	
	private  String   brandName;//商标名，页面显示
	
	private  String   brandId;//商标id
	
	private  String   baseMaterial;//基材名称，页面显示
	
	private  String  baseId;//基材id
	
	private  String   manufacturer;//生产商名称，页面显示
	
	private  Integer  manufacturerId;//生产商id
	
	private  String   content;//物性描述，页面显示
	
	private  String   finds;//填充物id，多个逗号隔开
	
	private  String   scids;//安规认证id，多个逗号隔开 
	
	private  Date  createDate;
}
