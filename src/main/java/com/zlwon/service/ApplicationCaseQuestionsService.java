package com.zlwon.service;

public interface ApplicationCaseQuestionsService {

	/**
	 * 添加案例提问到文档中
	 * @param 提问id，如果为null，就是添加所有
	 */
	void addApplicationCaseQuestionsDocument(Integer  id);

}
