package com.zlwon.mapper;

import java.util.List;

import com.zlwon.pojo.es.ApplicationCaseES;

/**
 * 应用案例Mapper
 * @author yangy
 *
 */

public interface ApplicationCaseMapper {

	/**
	 * 得到所有案例
	 * @return
	 */
	List<ApplicationCaseES> selectAllApplicationCase();

	/**
	 * 根据案例id，得到案例信息
	 * @param id
	 * @return
	 */
	ApplicationCaseES selectApplicationCaseById(Integer id);

}
