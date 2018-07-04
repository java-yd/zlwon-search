package com.zlwon.pojo.es.document;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Parent;

import com.zlwon.pojo.constant.EsConstant;

import lombok.Data;

@Data
public class SpecificationQuestionsDocument extends QuestionsDocument{

	private  String   sid;//物性id：1查询物性列表，统计物性问题个数，2CMS系统物性规格名称修改时匹配，从而修改该文档物性规格名称
}
