package com.zlwon.service;

import com.zlwon.pojo.es.dto.ApplicationCaseDTO;

public interface ApplicationCaseService {

	/**
	 * 添加案例到文档中
	 */
	void  addApplicationCaseDocument();

	/**
	 * 案例搜索
	 * @param pageIndex
	 * @param pageSize
	 * @param applicationCaseDTO
	 * @return
	 */
	Object findApplicationCase(Integer pageIndex, Integer pageSize, ApplicationCaseDTO applicationCaseDTO);
}
