package com.zlwon.pojo.es.document;

import lombok.Data;

/**
 * 回答
 * @author yuand
 *
 */
@Data
public class AnswerDocument {

	private  String  qid;//问题id
	
	private  String   content;//回答内容
	
	private  Integer  supportNums;//回答点赞个数
}
