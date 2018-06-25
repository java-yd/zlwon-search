package com.zlwon.pojo.es.dto;

import lombok.Data;

/**
 * 查询参数
 * @author yuand
 *
 */
@Data
public class SpecificationDTO {

	private  Integer  manufacturerStr;//生产商id
	
	private  String   brandNameStr;//商标id，多个逗号隔开
	
	private  String  baseMaterialStr;//基材id，多个逗号隔开
	
	private  String   fillerStr;//填充物id，多个逗号隔开
	
	private  String   safeCertifyStr;//安规认证id，多个逗号隔开
	
	private  String  searchText;//关键字搜索
}
