package com.zlwon.mapper;

import java.util.List;

import com.zlwon.pojo.es.SpecificationES;

/**
 * 物性表Mapper
 * @author yangy
 *
 */

public interface SpecificationMapper {

    List<SpecificationES> selectAllSpecification();

    /**
     * 根据物性id，得到物性信息
     * @param id
     * @return
     */
	SpecificationES selectSpecificationById(Integer id);

}
