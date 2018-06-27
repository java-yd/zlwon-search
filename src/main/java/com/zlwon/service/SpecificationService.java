package com.zlwon.service;

import com.zlwon.pojo.dto.SpecificationDTO;
import com.zlwon.pojo.es.SpecificationES;

public interface SpecificationService {
	
	/**
	 * 物性表添加到文档中
	 */
	void addSpecificationDocument();

	/**
	 * 物性搜索-分页获取
	 * @return
	 */
	Object findSpecification(Integer pageIndex, Integer pageSize, SpecificationDTO specificationDTO);

	
	/**
	 * 根据id搜索物性文档信息
	 * @return
	 */
	SpecificationES findOneSpecificationById(String   id);
}
