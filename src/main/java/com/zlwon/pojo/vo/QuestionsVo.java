package com.zlwon.pojo.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 问答返回参数
 * @author yuand
 *
 */
@Data
public class QuestionsVo {

	private Integer id;//问答id
	
	private Integer  iid;//来源id(物性id或案例id)
	
	private String source;//来源名称(物性规格名称或案例标题)
	
	private Integer infoClass;//来源类别：1:物性、2:案例
	
	private Integer uid;//提问用户id
	
	private String nickname;//提问用户昵称
	
	private String headerimg;//提问用户头像
	
	private String intro;//提问用户一句话介绍
	
	private String title;//提问标题
	
	private String content;//提问内容
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createTime;//提问时间
	
	private Integer answerCount;//回答统计个数
	
	private String  answerPri;//点赞最多的回答内容
}
