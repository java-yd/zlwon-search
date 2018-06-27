package com.zlwon.pojo.dto;

import lombok.Data;

@Data
public class ApplicationCaseDTO {

	private  Integer   mids;//生产商id
	
	private  String   tids;//商标id，多个逗号隔开
	
	private  String   marketIds;//应用市场id，多个逗号隔开
	
	private  Integer  industryId;//应用行业id
	
	private  String   key;//关键字
	
}
