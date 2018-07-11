package com.zlwon.service;

public interface SpecificationQuotationService {

	/**
	 * 添加报价单到文档中
	 * @param id:报价单id，null就是添加所有
	 */
	void addSpecificationQuotationDocument(Integer id);

}
