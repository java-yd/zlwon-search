package com.zlwon.service;

public interface SpecificationCharacteristicService {

	/**
	 * 添加物性标签到文档中
	 * @param id：物性标签id，如果为null，就是添加所有
	 * @return
	 */
	void addSpecificationCharacteristicDocument(Integer  id);

}
