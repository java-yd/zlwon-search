package com.zlwon.kafka;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.zlwon.pojo.ApplicationCaseMessage;
import com.zlwon.pojo.CharacteristicMessage;
import com.zlwon.pojo.DealerdQuotationMessage;
import com.zlwon.pojo.QuestionsMessage;
import com.zlwon.pojo.SpecificationMessage;
import com.zlwon.pojo.constant.MessageConstant;
import com.zlwon.service.ApplicationCaseQuestionsService;
import com.zlwon.service.ApplicationCaseService;
import com.zlwon.service.SpecificationCharacteristicService;
import com.zlwon.service.SpecificationQuestionsService;
import com.zlwon.service.SpecificationQuotationService;
import com.zlwon.service.SpecificationService;
import com.zlwon.utils.JsonUtils;

/**
 * 消息消费者
 */
@Component
public class MsgConsumer {

	@Autowired
	private  SpecificationService  specificationService;
	@Autowired
	private  ApplicationCaseService  applicationCaseService;
	@Autowired
	private  ApplicationCaseQuestionsService  applicationCaseQuestionsService;
	@Autowired
	private  SpecificationQuestionsService  specificationQuestionsService;
	@Autowired
	private  SpecificationCharacteristicService  specificationCharacteristicService;
	@Autowired
	private  SpecificationQuotationService  specificationQuotationService;
	
	/**
	 * 监听物性添加和修改
	 * @param content
	 */
	@KafkaListener(topics = "${kafka.topic.add.specification}")
    public void addOrUpdateSpecificationMessage(String content) {
		if(StringUtils.isNotBlank(content)){
			SpecificationMessage message = JsonUtils.jsonToPojo(content, SpecificationMessage.class);
			if(message.getType().equals(MessageConstant.ADDORUPDATE_SPECIFICATION_TYPE)){
				specificationService.addOrUpdateSpecification(message.getId());
			}
		}
    }
	
	/**
	 * 监听案例添加和修改
	 * @param content
	 */
	@KafkaListener(topics = "${kafka.topic.add.applicationCase}")
    public void addOrUpdateApplicationCaseMessage(String content) {
		if(StringUtils.isNotBlank(content)){
			ApplicationCaseMessage message = JsonUtils.jsonToPojo(content, ApplicationCaseMessage.class);
			if(message.getType().equals(MessageConstant.ADDORUPDATE_APPLICATIONCASE_TYPE)){
				applicationCaseService.addOrUpdateApplicationCase(message.getId());
			}
		}
    }
	
	/**
	 * 监听添加提问
	 */
	@KafkaListener(topics = "${kafka.topic.add.questions}")
    public void addQuestionsMessage(String content) {
		if(StringUtils.isNotBlank(content)){
			QuestionsMessage message = JsonUtils.jsonToPojo(content, QuestionsMessage.class);
			if(message.getType().equals(MessageConstant.ADD_QUESTIONS_TYPE)){
				if(message.getInfoClass() == 1){//物性
					specificationQuestionsService.addSpecificationQuestionsDocument(message.getId());
				}else if (message.getInfoClass() == 2) {//案例
					applicationCaseQuestionsService.addApplicationCaseQuestionsDocument(message.getId());
				}
			}
		}
    }
	
	
	/**
	 * 监听添加物性标签
	 */
	@KafkaListener(topics = "${kafka.topic.add.characteristic}")
    public void addQuestionsCharacteristic(String content) {
		if(StringUtils.isNotBlank(content)){
			CharacteristicMessage message = JsonUtils.jsonToPojo(content, CharacteristicMessage.class);
			if(message.getType().equals(MessageConstant.ADD_CHARACTERISTIC_TYPE)){
				specificationCharacteristicService.addSpecificationCharacteristicDocument(message.getId());
			}
		}
    }
	
	
	/**
	 * 监听添加物性报价
	 */
	@KafkaListener(topics = "${kafka.topic.add.dealerdQuotation}")
	public void addDealerdQuotation(String content) {
		if(StringUtils.isNotBlank(content)){
			DealerdQuotationMessage message = JsonUtils.jsonToPojo(content, DealerdQuotationMessage.class);
			if(message.getType().equals(MessageConstant.ADD_DEALERDQUOTATION_TYPE)){
				specificationQuotationService.addSpecificationQuotationDocument(message.getId());
			}
		}
	}
}
