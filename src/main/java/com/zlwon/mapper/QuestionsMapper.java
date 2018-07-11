package com.zlwon.mapper;

import java.util.List;

import com.zlwon.pojo.es.ApplicationCaseQuestionsES;
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
	/**
	 * 得到案例所有提问
	 * @return
	 */
	List<ApplicationCaseQuestionsES> selectAllApplicationCaseQuestions();
	
	/**
	 * 根据物性提问id，得到物性提问信息
	 * @param id
	 * @return
	 */
	SpecificationQuestionsES selectSpecificationQuestionsById(Integer id);
	
	/**
	 * 根据案例提问id，得到案例提问信息
	 * @param id
	 * @return
	 */
	ApplicationCaseQuestionsES selectApplicationCaseQuestionsById(Integer id);

}
