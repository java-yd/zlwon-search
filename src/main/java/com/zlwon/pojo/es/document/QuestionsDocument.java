package com.zlwon.pojo.es.document;

import java.util.Date;

import lombok.Data;


@Data
public class QuestionsDocument {

	private  String    nickname;//提问用户昵称
	
	private  Integer   uid;//提问用户id
	
	private  String    headerimg;//提问用户头像
	
	private  String    intro;//提问用户一句话介绍
	
	private  Date      createTime;//提问时间
	
	private  String    title;//提问标题
	
	private  String    content;//提问内容
}
