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
public class ApplicationCaseQuestionsDocument extends QuestionsDocument{
	private  String   aid;//案例id：CMS系统案例标题修改时匹配，从而修改该文档案例标题,由于父案例继承物性，使用HasParentQueryBuilder会无法匹配
}
