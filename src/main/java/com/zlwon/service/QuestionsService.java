package com.zlwon.service;

public interface QuestionsService {

	/**
	 * 问答搜索
	 */
	Object findQuestions(Integer pageIndex, Integer pageSize, String key);

}
