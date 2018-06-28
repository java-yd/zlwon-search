package com.zlwon.mapper;

import java.util.List;

import com.zlwon.pojo.es.SpecificationQuestionsES;

/**
 * 提问Mapper
 * @author yangy
 *
 */
public interface QuestionsMapper {
	
	/**
	 * 得到物性所有提问
	 * @return
	 */
	List<SpecificationQuestionsES> selectAllSpecificationQuestions();

}
