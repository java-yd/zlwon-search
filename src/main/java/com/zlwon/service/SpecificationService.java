package com.zlwon.service;

import com.zlwon.pojo.es.dto.SpecificationDTO;

public interface SpecificationService {
	
	/**
	 * 物性表添加到文档中
	 */
	void addSpecificationDocument();

	/**
	 * 物性搜索
	 * @return
	 */
	Object findSpecification(Integer pageIndex, Integer pageSize, SpecificationDTO specificationDTO);

}
