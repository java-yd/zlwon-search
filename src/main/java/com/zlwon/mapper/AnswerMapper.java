package com.zlwon.mapper;

import java.util.List;

import com.zlwon.pojo.es.AnswerES;

/**
 * 提问回答Mapper
 * @author yangy
 *
 */

public interface AnswerMapper {

	/**
	 * 得到所有提问回答
	 * @return
	 */
	List<AnswerES> selectAllAnswer();

}
