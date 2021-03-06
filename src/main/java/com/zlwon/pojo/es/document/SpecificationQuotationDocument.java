package com.zlwon.pojo.es.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Parent;

import com.zlwon.pojo.constant.EsConstant;

import lombok.Data;

/**
 * 报价单文档，只为统计个数
 * @author yuand
 *
 */
@Data
public class SpecificationQuotationDocument {
	private  String   sid;//物性id，用来查询物性列表，统计报价单个数
}
