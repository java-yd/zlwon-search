package com.zlwon.service.impl;

import java.util.ArrayList;
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
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.zlwon.mapper.SpecificationMapper;
import com.zlwon.pojo.constant.EsConstant;
import com.zlwon.pojo.dto.SpecificationDTO;
import com.zlwon.pojo.es.ApplicationCaseES;
import com.zlwon.pojo.es.SpecificationES;
import com.zlwon.pojo.es.SpecificationQuestionsES;
import com.zlwon.pojo.es.SpecificationQuotationES;
import com.zlwon.pojo.es.document.SpecificationDocument;
import com.zlwon.pojo.vo.SpecificationVo;
import com.zlwon.service.SpecificationService;
import com.zlwon.utils.JsonUtils;
import com.zlwon.utils.ResultPage;
import com.zlwon.utils.es.ElasticsearchTemplateUtils;

@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private  SpecificationMapper  specificationMapper;
	@Autowired
	private  ElasticsearchTemplate   elasticsearchTemplate;
	
	/**
	 * 物性表添加到文档中
	 */
	public   void    addSpecificationDocument(){
		//得到所有物性表信息，保存到文档中
		List<SpecificationES> list = specificationMapper.selectAllSpecification();
		if(list != null  &&  list.size() > 0){
			ElasticsearchTemplateUtils.addDocuments(elasticsearchTemplate, getSpecificatioIndexQuerys(list));
		}
	}
	
	/**
	 * 根据id搜索物性文档信息
	 * @return
	 */
	public SpecificationES findOneSpecificationById(String   id) {
		return  ElasticsearchTemplateUtils.queryOneDocumentById(id,SpecificationES.class,elasticsearchTemplate);
	}
	
	/**
	 * 物性搜索-分页获取
	 * @return
	 */
	public Object findSpecification(Integer pageIndex, Integer pageSize, SpecificationDTO specificationDTO) {
		return  executeQuery(pageIndex,pageSize,specificationDTO);
	}
	
	//物性搜索-创建查询条件
	private  SearchQuery   createSearchQuery(SpecificationDTO specificationDTO,Integer pageIndex,Integer pageSize){
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		BoolQueryBuilder filterBuilder = QueryBuilders.boolQuery();
		String baseMaterialStr = specificationDTO.getBaseMaterialStr();//基材
		if(StringUtils.isNotBlank(baseMaterialStr)){
			filterBuilder.filter(QueryBuilders.matchQuery("baseId", baseMaterialStr));
		}
		String brandNameStr = specificationDTO.getBrandNameStr();//商标
		if(StringUtils.isNotBlank(brandNameStr)){
			filterBuilder.filter(QueryBuilders.matchQuery("brandId", brandNameStr));
		}
		String fillerStr = specificationDTO.getFillerStr();//填充物
		if(StringUtils.isNotBlank(fillerStr)){
			filterBuilder.filter(QueryBuilders.matchQuery("finds", fillerStr));
		}
		Integer manufacturerStr = specificationDTO.getManufacturerStr();//生产商
		if(manufacturerStr != null && manufacturerStr > 0){
			filterBuilder.filter(QueryBuilders.matchQuery("manufacturerId", manufacturerStr));
		}
		String safeCertifyStr = specificationDTO.getSafeCertifyStr();//安规认证
		if(StringUtils.isNotBlank(safeCertifyStr)){
			filterBuilder.filter(QueryBuilders.matchQuery("scids", safeCertifyStr));
		}
		String searchText = specificationDTO.getSearchText();//关键字
		if(StringUtils.isNotBlank(searchText)){
			boolQuery
				.should(QueryBuilders.multiMatchQuery(searchText, "name","content").boost(2))//增加评分
				.should(new HasChildQueryBuilder("specificationCharacteristicES", QueryBuilders.matchQuery("labelName", searchText), ScoreMode.None))
				.should(new HasChildQueryBuilder("applicationCaseES", QueryBuilders.multiMatchQuery(searchText,"appProduct","title"), ScoreMode.None));//子个数是查询条件匹配的个数，不是总个数，坑
//				.should(new HasChildQueryBuilder("specificationQuestionsES", QueryBuilders.boolQuery(), ScoreMode.None).innerHit(new InnerHitBuilder()))
//				.should(new HasChildQueryBuilder("specificationQuotationES", QueryBuilders.disMaxQuery(), ScoreMode.None).innerHit(new InnerHitBuilder()))
		}else {
			boolQuery
				.should(QueryBuilders.matchPhraseQuery("emptyField", EsConstant.SPECIFICATIONES_EMPTYFIELD_VALUE))//should是必须要满足一个，所以在SpecificationES设置了一个匹配字符串,否则会造成数据丢失
				.should(new HasChildQueryBuilder("applicationCaseES", QueryBuilders.matchAllQuery(), ScoreMode.Total).innerHit(new InnerHitBuilder()))
				.should(new HasChildQueryBuilder("specificationQuestionsES", QueryBuilders.matchAllQuery(), ScoreMode.Total).innerHit(new InnerHitBuilder()))
				.should(new HasChildQueryBuilder("specificationQuotationES", QueryBuilders.matchAllQuery(), ScoreMode.Total).innerHit(new InnerHitBuilder()));
		}
		
		SearchQuery query = new  NativeSearchQueryBuilder()
				.withIndices(EsConstant.ES_INDEXNAME)//指定查询的索引库
				.withTypes("specificationES")//指定查询的类型
				.withQuery(boolQuery)
				.withFilter(filterBuilder)
				.withHighlightFields(new Field("name"),new  Field("content"))
//				.withSort(SortBuilders.fieldSort("sortName").order(SortOrder.ASC))  
				.withPageable(new  PageRequest(pageIndex-1, pageSize))
				.build();
		return   query;
	}
	
	//物性搜索-执行搜索
	private  Object      executeQuery(Integer pageIndex, Integer pageSize, SpecificationDTO specificationDTO){
		Object object = elasticsearchTemplate.query(createSearchQuery(specificationDTO,pageIndex,pageSize), new ResultsExtractor(){
			@Override
			public Object extract(SearchResponse response) {
				SearchHits searchHits = response.getHits();
				long totalNumber = searchHits.totalHits;
				SearchHit[] hits = searchHits.getHits();
				
				SpecificationVo  vo = null;
				List<SpecificationVo>  list = new  ArrayList<>();
				for (int i = 0; i < hits.length; i++) {
					vo = new SpecificationVo();
					
					//统计案例（提问，报价单）个数
					count(hits[i],vo);
					
					String id = hits[i].getId();
					vo.setId(Integer.valueOf(id));
					Map<String, Object> source = hits[i].getSource();
					vo.setBaseMaterial(source.get("baseMaterial") == null?"":source.get("baseMaterial").toString());
					vo.setBrandName(source.get("brandName") == null?"":source.get("brandName").toString());
					vo.setManufacturer(source.get("manufacturer") == null?"":source.get("manufacturer").toString());
					vo.setName(source.get("name") == null?"":source.get("name").toString());
					vo.setContent(source.get("content") == null?"":source.get("content").toString());
					Map<String, HighlightField> highlightFields = hits[i].getHighlightFields();
					for (String name : highlightFields.keySet()) {
						HighlightField highlightField = highlightFields.get(name);
						if(highlightField.getName().equals("name")){
							vo.setName(highlightField.fragments()[0].toString());
						}else if (highlightField.getName().equals("content")) {
							vo.setContent(highlightField.fragments()[0].toString());
						}
					}
					list.add(vo);
				}
				ResultPage<SpecificationVo> resultPage = ResultPage.list(list,(int)(totalNumber % pageSize == 0 ? totalNumber / pageSize : totalNumber / pageSize + 1), (int)totalNumber, pageIndex, pageSize);
				return JsonUtils.objectToJson(resultPage);
			}
		});
		return  object;
	}
	
	//统计案例（提问，报价单）个数
	private void count(SearchHit  hits,SpecificationVo  vo) {
		Map<String, SearchHits> innerHits = hits.getInnerHits();//只有没有查询条件时，才有，因为有查询添加时，统计的个数不是总个数，所以有查询条件时，不返回innerHits了
		if(innerHits != null && innerHits.size() > 0){
			for (Entry<String, SearchHits> innerHit : innerHits.entrySet()) {
				if(innerHit.getKey().equals("specificationQuestionsES")){
					vo.setQuestionCount(Integer.valueOf(innerHit.getValue().getTotalHits()+""));
				}else if (innerHit.getKey().equals("applicationCaseES")) {
					vo.setCaseCount(Integer.valueOf(innerHit.getValue().getTotalHits()+""));
				}else if (innerHit.getKey().equals("specificationQuotationES")) {
					vo.setQuotationCount(Integer.valueOf(innerHit.getValue().getTotalHits()+""));
				}
			}
		}else {
			//案例统计个数
			SearchQuery appQuery = new  NativeSearchQueryBuilder()
					.withIndices(EsConstant.ES_INDEXNAME)//指定查询的索引库
					.withTypes("applicationCaseES")//指定查询的类型
					.withQuery(QueryBuilders.termQuery("sid", hits.getId()))
					.build();
			List<ApplicationCaseES> appList = elasticsearchTemplate.queryForList(appQuery, ApplicationCaseES.class);
			vo.setCaseCount(appList == null?0:appList.size());
			//物性提问统计个数
			SearchQuery questQuery = new  NativeSearchQueryBuilder()
					.withIndices(EsConstant.ES_INDEXNAME)//指定查询的索引库
					.withTypes("specificationQuestionsES")//指定查询的类型
					.withQuery(QueryBuilders.termQuery("sid", hits.getId()))
					.build();
			List<SpecificationQuestionsES> questList = elasticsearchTemplate.queryForList(questQuery, SpecificationQuestionsES.class);
			vo.setQuestionCount(questList == null ? 0 : questList.size());
			//物性报价单统计个数
			SearchQuery quotaQuery = new  NativeSearchQueryBuilder()
					.withIndices(EsConstant.ES_INDEXNAME)//指定查询的索引库
					.withTypes("specificationQuotationES")//指定查询的类型
					.withQuery(QueryBuilders.termQuery("sid", hits.getId()))
					.build();
			List<SpecificationQuotationES> quotaList = elasticsearchTemplate.queryForList(quotaQuery, SpecificationQuotationES.class);
			vo.setQuotationCount(quotaList == null ? 0 : quotaList.size());
		}
	}
	

	//创建要插入的文档对象
	private   List<IndexQuery>  getSpecificatioIndexQuerys(List<SpecificationES> list){
		List<IndexQuery> indexQuerys = new  ArrayList<>();
		IndexQuery query = null;
		
		for (SpecificationES specificationES : list) {
			query = new  IndexQuery();
			query.setId(specificationES.getId().toString());
			query.setIndexName(EsConstant.ES_INDEXNAME);
			query.setType("specificationES");
			query.setSource(getSpecificatioSource(specificationES));
			indexQuerys.add(query);
		}
		return  indexQuerys;
	}

	
	private String getSpecificatioSource(SpecificationES specificationES) {
		SpecificationDocument  document = new  SpecificationDocument();
		BeanUtils.copyProperties(specificationES, document);
		document.setEmptyField(EsConstant.SPECIFICATIONES_EMPTYFIELD_VALUE);
		return JsonUtils.objectToJson(document);
	}

}
