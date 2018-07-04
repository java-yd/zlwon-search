package com.zlwon.pojo.es.document;

import java.util.Date;

import lombok.Data;


@Data
public class QuestionsDocument {
	
	private Integer infoClass;//来源类别：1:物性、2:案例
	
	private  String   source;//来源标题(物性规格名称或案例标题)
	
	private  Integer   uid;//提问用户id
	
	private  Date      createTime;//提问时间
	
	private  String    title;//提问标题
	
	private  String    content;//提问内容
	
	private  Integer    answerCount;//回答个数，只是用来查询问题时，进行排序
}
