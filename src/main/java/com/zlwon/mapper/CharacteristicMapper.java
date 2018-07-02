package com.zlwon.mapper;

import java.util.List;

import com.zlwon.pojo.es.SpecificationCharacteristicES;

/**
 * 物性表主要特性标签Mapper
 * @author yangy
 *
 */

public interface CharacteristicMapper {

    /**
     * 得到所有标签
     * @return
     */
	List<SpecificationCharacteristicES> selectAllCharacteristic();
	
}