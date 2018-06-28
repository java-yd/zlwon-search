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
import org.elasticsearch.join.query.HasParentQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
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

import com.zlwon.mapper.ApplicationCaseMapper;
import com.zlwon.pojo.constant.EsConstant;
import com.zlwon.pojo.dto.ApplicationCaseDTO;
import com.zlwon.pojo.es.ApplicationCaseES;
import com.zlwon.pojo.es.SpecificationES;
import com.zlwon.pojo.es.document.ApplicationCaseDocument;
import com.zlwon.pojo.vo.ApplicationCaseVo;
import com.zlwon.service.ApplicationCaseService;
import com.zlwon.service.SpecificationService;
import com.zlwon.utils.JsonUtils;
import com.zlwon.utils.ResultPage;
import com.zlwon.utils.es.ElasticsearchTemplateUtils;

@Service
public class ApplicationCaseServiceImpl implements ApplicationCaseService {

	@Autowired
	private  ApplicationCaseMapper  applicationCaseMapper;
	@Autowired
	private  ElasticsearchTemplate   elasticsearchTemplate;
	@Autowired
	private  SpecificationService   specificationService;
	
	/**
	 * 案例搜索
	 */
	@Override
	public Object findApplicationCase(Integer pageIndex, Integer pageSize, ApplicationCaseDTO applicationCaseDTO) {
		return  executeQuery(pageIndex,pageSize,applicationCaseDTO);
	}
	
	//案例搜索-创建查询条件
	private  SearchQuery   createSearchQuery(ApplicationCaseDTO applicationCaseDTO,Integer pageIndex,Integer pageSize){
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		BoolQueryBuilder filterBuilder = QueryBuilders.boolQuery();
		BoolQueryBuilder parentFilterBuilder = QueryBuilders.boolQuery();//父过滤条件
		
		
		Integer mids = applicationCaseDTO.getMids();//生产商
		if(mids != null && mids > 0){
			parentFilterBuilder.filter(new HasParentQueryBuilder("specificationES", QueryBuilders.matchQuery("manufacturerId", mids),false).innerHit(new InnerHitBuilder()));//查询结果显示父数据
		}
		String tids = applicationCaseDTO.getTids();//商标
		if(StringUtils.isNotBlank(tids)){
			parentFilterBuilder.filter(new HasParentQueryBuilder("specificationES", QueryBuilders.matchQuery("brandId", tids),false).innerHit(new InnerHitBuilder()));//查询结果显示父数据
		}
		Integer industryId = applicationCaseDTO.getIndustryId();//应用行业
		if(industryId != null && industryId > 0){
			parentFilterBuilder.filter(QueryBuilders.matchQuery("industryId",industryId));
		}
		String marketIds = applicationCaseDTO.getMarketIds();//应用市场
		if(StringUtils.isNotBlank(marketIds)){
			parentFilterBuilder.filter(QueryBuilders.matchQuery("marketId", marketIds));
		}
		
		String searchText = applicationCaseDTO.getKey();//关键字
		if(StringUtils.isNotBlank(searchText)){
			boolQuery
				.should(QueryBuilders.multiMatchQuery(searchText, "title","appProduct","selectRequirements","selectCause","setting"))
				.should(
						new HasParentQueryBuilder("specificationES",QueryBuilders.multiMatchQuery(searchText,"name","content"), false) //false好像是不参与评分
							.innerHit(new InnerHitBuilder().setHighlightBuilder(new HighlightBuilder().field("name"))));//查询结果显示父数据，并设置父数据中name为高亮显示
		}else{
			boolQuery
				.should(QueryBuilders.matchQuery("emptyField", EsConstant.APPLICATIONCASEES_EMPTYFIELD_VALUE))//should是必须要满足一个，所以在ApplicationCaseES设置了一个匹配字符串,否则会造成数据丢失
				.should(new HasChildQueryBuilder("applicationCaseQuestionsES", QueryBuilders.matchAllQuery(), ScoreMode.Total).innerHit(new InnerHitBuilder()))//默认查询，按提问个数排序
				.should(new HasParentQueryBuilder("specificationES",QueryBuilders.matchAllQuery(),false).innerHit(new InnerHitBuilder()));
		}
		
		SearchQuery query = new  NativeSearchQueryBuilder()
				.withIndices(EsConstant.ES_INDEXNAME)//指定查询的索引库
				.withTypes("applicationCaseES")//指定查询的类型
				.withQuery(boolQuery)
				.withFilter(filterBuilder)
				.withFilter(parentFilterBuilder)//父过滤条件
				.withHighlightFields(new Field("title"))
//				.withSort(SortBuilders.fieldSort("name").order(SortOrder.ASC))  //汉子无法排序
				.withPageable(new  PageRequest(pageIndex-1, pageSize))
				.build();
		return   query;
	}
	
	//案例搜索-执行搜索
	private  Object      executeQuery(Integer pageIndex, Integer pageSize, ApplicationCaseDTO applicationCaseDTO){
		Object object = elasticsearchTemplate.query(createSearchQuery(applicationCaseDTO,pageIndex,pageSize), new ResultsExtractor(){
			@Override
			public Object extract(SearchResponse response) {
				SearchHits totalHits = response.getHits();
				long totalNumber = totalHits.totalHits;
				SearchHit[] hits = totalHits.getHits();
				ApplicationCaseVo  vo = null;
				List<ApplicationCaseVo>  list = new  ArrayList<>();
				for (int i = 0; i < hits.length; i++) {
					vo = new ApplicationCaseVo();
					getSpecificationName(hits[i],vo);//赋值物性名称
					
					String id = hits[i].getId();
					vo.setId(Integer.valueOf(id));
					Map<String, Object> source = hits[i].getSource();
					vo.setTitle(source.get("title") == null?"":source.get("title").toString());
					vo.setPhoto(source.get("photo") == null?"":source.get("photo").toString());
					Map<String, HighlightField> highlightFields = hits[i].getHighlightFields();
					for (String name : highlightFields.keySet()) {
						HighlightField highlightField = highlightFields.get(name);
						if(highlightField.getName().equals("title")){
							vo.setTitle(highlightField.fragments()[0].toString());
						}
					}
					list.add(vo);
				}
				ResultPage<ApplicationCaseVo> resultPage = ResultPage.list(list,(int)(totalNumber % pageSize == 0 ? totalNumber / pageSize : totalNumber / pageSize + 1), (int)totalNumber, pageIndex, pageSize);
				return JsonUtils.objectToJson(resultPage);
			}
		});
		return  object;
	}
	
	//案例搜索-获取物性规格名称
	private   void   getSpecificationName(SearchHit hit,ApplicationCaseVo  vo){
		Map<String, SearchHits> innerHits = hit.getInnerHits();
		for (Entry<String, SearchHits> entry : innerHits.entrySet()) {
			if(entry.getKey().equals("specificationES")){
				SearchHits hits2 = entry.getValue();//关键字搜索不到物性规格昵称，这里会返回null，所以要单独获取物性规格昵称
				if(hits2.getTotalHits() == 0){
					//需要根据物性id，得到物性规格名称
					Map<String, SearchHitField> fields = hit.getFields();
					for (Entry<String, SearchHitField> searchHitField : fields.entrySet()) {
						Object parentId= searchHitField.getValue().getValues().get(0);
						SpecificationES specificationES = specificationService.findOneSpecificationById(parentId+"");
						vo.setName(specificationES == null ? "" : specificationES.getName());
						break;
					}
					break;
				}
				SearchHit searchHit = hits2.getHits()[0];
				Map<String, Object> source = searchHit.getSource();
				vo.setName(source.get("name") == null?"":source.get("name").toString());
				Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
				for (Entry<String, HighlightField> nameEntry : highlightFields.entrySet()) {
					HighlightField highlightField = nameEntry.getValue();
					if(highlightField.getName().equals("name")){
						vo.setName(highlightField.fragments()[0].toString());
					}
				}
			}
		}
	}
	
	/**
	 * 添加案例到文档中
	 */
	public void addApplicationCaseDocument() {
		List<ApplicationCaseES> list = applicationCaseMapper.selectAllApplicationCase();
		if(list != null  &&  list.size() > 0){
			ElasticsearchTemplateUtils.addDocuments(elasticsearchTemplate, getApplicationCaseIndexQuerys(list));
		}
	}
	
	private   List<IndexQuery>  getApplicationCaseIndexQuerys(List<ApplicationCaseES> list){
		List<IndexQuery> indexQuerys = new  ArrayList<>();
		IndexQuery query = null;
		
		for (ApplicationCaseES applicationCaseES : list) {
			query = new  IndexQuery();
			query.setId(applicationCaseES.getId().toString());
			query.setIndexName(EsConstant.ES_INDEXNAME);
			query.setType("applicationCaseES");
			query.setParentId(applicationCaseES.getSpecificationId());
			query.setSource(getApplicationCaseSource(applicationCaseES));
			indexQuerys.add(query);
		}
		
		return  indexQuerys;
	}

	private String getApplicationCaseSource(ApplicationCaseES applicationCaseES) {
		ApplicationCaseDocument  document = new  ApplicationCaseDocument();
		BeanUtils.copyProperties(applicationCaseES, document);
		document.setEmptyField(EsConstant.APPLICATIONCASEES_EMPTYFIELD_VALUE);
		document.setSid(applicationCaseES.getSpecificationId());
		return JsonUtils.objectToJson(document);
	}
}
