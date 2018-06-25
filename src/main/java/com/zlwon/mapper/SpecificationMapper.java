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

}
