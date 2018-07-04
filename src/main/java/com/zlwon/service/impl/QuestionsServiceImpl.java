package com.zlwon.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.join.query.HasChildQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Order;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.zlwon.pojo.constant.EsConstant;
import com.zlwon.pojo.es.ApplicationCaseQuestionsAnswerES;
import com.zlwon.pojo.es.CustomerES;
import com.zlwon.pojo.es.SpecificationES;
import com.zlwon.pojo.es.SpecificationQuestionsAnswerES;
import com.zlwon.pojo.vo.QuestionsVo;
import com.zlwon.pojo.vo.SpecificationVo;
import com.zlwon.service.QuestionsService;
import com.zlwon.service.SpecificationService;
import com.zlwon.utils.JsonUtils;
import com.zlwon.utils.ResultPage;
import com.zlwon.utils.es.ElasticsearchTemplateUtils;

@Service
public class QuestionsServiceImpl implements QuestionsService {

	@Autowired
	private  ElasticsearchTemplate   elasticsearchTemplate;
	@Autowired
	private  SpecificationService   specificationService;
	
	/**
	 * 问答搜索
	 */
	@Override
	public Object findQuestions(Integer pageIndex, Integer pageSize, String key) {
		return executeQuery(pageIndex,pageSize,key);
	}

	//问答搜索-执行查询
	private Object executeQuery(Integer pageIndex, Integer pageSize, String key) {
		Object object = elasticsearchTemplate.query(createSearchQuery(key,pageIndex,pageSize), new ResultsExtractor(){
			@Override
			public Object extract(SearchResponse response) {
				SearchHits searchHits = response.getHits();
				long totalNumber = searchHits.totalHits;
				SearchHit[] hits = searchHits.getHits();
				
				QuestionsVo  vo = null;
				List<QuestionsVo>  list = new  ArrayList<>();
				for (int i = 0; i < hits.length; i++) {
					vo = new QuestionsVo();
					Map<String, Object> source = hits[i].getSource();
					
					vo.setId(Integer.valueOf(hits[i].getId()));//问答id
					
					Integer  infoClass = source.get("infoClass") == null? 0:Integer.valueOf(source.get("infoClass")+"");//来源类别：1:物性、2:案例
					vo.setInfoClass(infoClass);//设置来源类别
					vo.setIid(infoClass==0?0:infoClass==1?source.get("sid") == null? 0:Integer.valueOf(source.get("sid")+""):source.get("aid") == null? 0:Integer.valueOf(source.get("aid")+""));//来源id
					
					vo.setSource(source.get("source")==null?"":source.get("source").toString());//来源标题
					
					Integer  uid = source.get("uid") == null? 0:Integer.valueOf(source.get("uid")+"");//得到提问用户id
					vo.setUid(uid);//提问用户id
					//通过提问用户id，补全提问用户信息
					setCustomerInfo(vo,uid);
					//统统计回答个数和点赞最多的回答内容(关键字搜索时高亮显示回答内容)
					count(hits[i],vo);
					
					vo.setTitle(source.get("title") == null?"":source.get("title").toString());//提问标题
					vo.setContent(source.get("content") == null?"":source.get("content").toString());//提问内容
					vo.setCreateTime(new Date(Long.valueOf(source.get("createTime").toString())));//提问时间
					Map<String, HighlightField> highlightFields = hits[i].getHighlightFields();
					for (String name : highlightFields.keySet()) {
						HighlightField highlightField = highlightFields.get(name);
						if(highlightField.getName().equals("title")){
							vo.setTitle(highlightField.fragments()[0].toString());
						}else if (highlightField.getName().equals("content")) {
							vo.setContent(highlightField.fragments()[0].toString());
						}else if (highlightField.getName().equals("source")) {
							vo.setSource(highlightField.fragments()[0].toString());
						}
					}
					list.add(vo);
				}
				ResultPage<QuestionsVo> resultPage = ResultPage.list(list,(int)(totalNumber % pageSize == 0 ? totalNumber / pageSize : totalNumber / pageSize + 1), (int)totalNumber, pageIndex, pageSize);
				return JsonUtils.objectToJson(resultPage);
			}
		});
		return  object;
	}


	//通过提问用户id，补全提问用户信息
	protected void setCustomerInfo(QuestionsVo vo, Integer uid) {
		//通过uid得到用户信息
		CustomerES customerES = ElasticsearchTemplateUtils.queryOneDocumentById(uid+"", CustomerES.class, elasticsearchTemplate);
		if(customerES != null){
			vo.setNickname(customerES.getNickname());//昵称
			vo.setHeaderimg(customerES.getHeaderimg());//头像
			vo.setIntro(customerES.getIntro());//一句话介绍
		}
	}

	//问答搜索-创建查询条件
	private SearchQuery createSearchQuery(String key, Integer pageIndex, Integer pageSize) {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		
		if(StringUtils.isNotBlank(key)){
			boolQuery
				.should(QueryBuilders.multiMatchQuery(key, "title","content","source"))
				.should(new HasChildQueryBuilder("specificationQuestionsAnswerES", QueryBuilders.matchQuery("content", key), ScoreMode.Max).innerHit(new InnerHitBuilder())
						.innerHit(new InnerHitBuilder().setHighlightBuilder(new HighlightBuilder().field("content"))))
				.should(new HasChildQueryBuilder("applicationCaseQuestionsAnswerES", QueryBuilders.matchQuery("content", key), ScoreMode.Max).innerHit(new InnerHitBuilder())
						.innerHit(new InnerHitBuilder().setHighlightBuilder(new HighlightBuilder().field("content"))));
		}
		
		SearchQuery query = new  NativeSearchQueryBuilder()
				.withIndices(EsConstant.ES_INDEXNAME)//指定查询的索引库
				.withTypes("specificationQuestionsES","applicationCaseQuestionsES")//指定查询的类型
				.withQuery(boolQuery)
				.withHighlightFields(new Field("title"),new  Field("content"),new  Field("source"))
				.withSort(SortBuilders.fieldSort("answerCount").order(SortOrder.DESC))
				.withPageable(new  PageRequest(pageIndex-1, pageSize))//
				.build();
		return   query;
	}

	//统计回答个数和点赞最多的回答内容(关键字搜索时高亮显示回答内容)
	private void count(SearchHit  hits,QuestionsVo  vo) {
		String type = hits.getType();//当前文档类型，统计回答个数要按照当前文档类型获取
		
		//1如果innerHits不为null，说明有关键字搜索，需要查看
		Map<String, SearchHits> innerHits = hits.getInnerHits();
		for (Entry<String, SearchHits> entry : innerHits.entrySet()) {
			if(entry.getKey().equals("specificationQuestionsAnswerES") && type.equals("specificationQuestionsES")){
				SearchHits hits2 = entry.getValue();
				SearchHit[] searchHits = hits2.getHits();
				if(searchHits == null || searchHits.length <= 0)
					break;
				SearchHit searchHit = searchHits[0];
				Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
				for (Entry<String, HighlightField> nameEntry : highlightFields.entrySet()) {
					HighlightField highlightField = nameEntry.getValue();
					if(highlightField.getName().equals("content")){
						vo.setAnswerPri(highlightField.fragments()[0].toString());
					}
				}
			}else if(entry.getKey().equals("applicationCaseQuestionsAnswerES") && type.equals("applicationCaseQuestionsES")){
				SearchHits hits2 = entry.getValue();
				SearchHit[] searchHits = hits2.getHits();
				if(searchHits == null || searchHits.length <= 0)
					break;
				SearchHit searchHit = searchHits[0];
				Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
				for (Entry<String, HighlightField> nameEntry : highlightFields.entrySet()) {
					HighlightField highlightField = nameEntry.getValue();
					if(highlightField.getName().equals("content")){
						vo.setAnswerPri(highlightField.fragments()[0].toString());
					}
				}
			}
		}
		
		//2统计回答个数和点赞最多的回答内容(如果回答内容不为空，就不覆盖了，因为有可能是关键字搜索的高亮信息)
		if(type.equals("applicationCaseQuestionsES")){
			//案例回答统计个数
			SearchQuery appQuery = new  NativeSearchQueryBuilder()
					.withIndices(EsConstant.ES_INDEXNAME)//指定查询的索引库
					.withTypes("applicationCaseQuestionsAnswerES")//指定查询的类型
					.withQuery(QueryBuilders.termQuery("qid", hits.getId()))
					.withSort(SortBuilders.fieldSort("supportNums").order(SortOrder.DESC))
					.build();
			List<ApplicationCaseQuestionsAnswerES> appAnswerList = elasticsearchTemplate.queryForList(appQuery, ApplicationCaseQuestionsAnswerES.class);
			if(appAnswerList != null  &&  appAnswerList.size() > 0){
				if(StringUtils.isBlank(vo.getAnswerPri()))
					vo.setAnswerPri(appAnswerList.get(0).getContent() == null?"":appAnswerList.get(0).getContent());
				vo.setAnswerCount(appAnswerList.size());
			}else{
				if(StringUtils.isBlank(vo.getAnswerPri()))
					vo.setAnswerPri("");
				vo.setAnswerCount(0);
			}
			vo.setAnswerCount(appAnswerList == null?0:appAnswerList.size());
		}else if (type.equals("specificationQuestionsES")) {
			//物性回答统计个数
			SearchQuery questQuery = new  NativeSearchQueryBuilder()
					.withIndices(EsConstant.ES_INDEXNAME)//指定查询的索引库
					.withTypes("specificationQuestionsAnswerES")//指定查询的类型
					.withQuery(QueryBuilders.termQuery("qid", hits.getId()))
					.withSort(SortBuilders.fieldSort("supportNums").order(SortOrder.DESC))
					.build();
			List<SpecificationQuestionsAnswerES> speAnswerList = elasticsearchTemplate.queryForList(questQuery, SpecificationQuestionsAnswerES.class);
			if(speAnswerList != null  &&  speAnswerList.size() > 0){
				if(StringUtils.isBlank(vo.getAnswerPri()))
					vo.setAnswerPri(speAnswerList.get(0).getContent() == null?"":speAnswerList.get(0).getContent());
				vo.setAnswerCount(speAnswerList.size());
			}else {
				if(StringUtils.isBlank(vo.getAnswerPri()))
					vo.setAnswerPri("");
				vo.setAnswerCount(0);
			}
		}
		
	}
}
