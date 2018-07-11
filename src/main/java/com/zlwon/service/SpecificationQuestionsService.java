package com.zlwon.service;

import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate.Param;

public interface SpecificationQuestionsService {

	/**
	 * 添加物性提问到文档中
	 * @param 提问id，如果为null，就是添加所有
	 */
	void addSpecificationQuestionsDocument(Integer  id);

}
