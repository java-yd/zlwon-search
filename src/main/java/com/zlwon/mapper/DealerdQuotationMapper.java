package com.zlwon.mapper;

import java.util.List;

import com.zlwon.pojo.es.SpecificationQuotationES;

/**
 * 物性表材料报价记录Mapper
 * @author yangy
 *
 */

public interface DealerdQuotationMapper {
    
    /**
     * 得到所有审核通过未过期的报价单
     */
    List<SpecificationQuotationES> selectAllDealerdQuotation();
}