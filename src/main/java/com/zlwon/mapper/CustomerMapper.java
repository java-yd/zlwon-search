package com.zlwon.mapper;

import java.util.List;

import com.zlwon.pojo.es.CustomerES;

/**
 * 用户Mapper
 * @author yangy
 *
 */

public interface CustomerMapper {

	/**
	 * 根据用户ID查询用户
	 * @param id
	 * @return
	 */
	List<CustomerES> selectAllCustomer();

}
