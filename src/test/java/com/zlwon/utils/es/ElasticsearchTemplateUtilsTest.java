package com.zlwon.utils.es;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.join.query.HasChildQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import com.zlwon.pojo.constant.EsConstant;
import com.zlwon.pojo.es.ApplicationCaseES;
import com.zlwon.pojo.es.ApplicationCaseQuestionsES;
import com.zlwon.pojo.es.SpecificationCharacteristicES;
import com.zlwon.pojo.es.SpecificationES;
import com.zlwon.pojo.es.SpecificationQuestionsES;
import com.zlwon.pojo.es.SpecificationQuotationES;
import com.zlwon.pojo.es.document.ApplicationCaseDocument;
import com.zlwon.pojo.es.document.SpecificationDocument;
import com.zlwon.pojo.vo.SpecificationVo;
import com.zlwon.utils.JsonUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchTemplateUtilsTest {
	
	@Autowired
	private  ElasticsearchTemplate   elasticsearchTemplate;

	@Test
	public void testAddOneDocument() {
		fail("Not yet implemented");
	}

	
	//创建索引
	@Test
	public void testCreateIndexOrMapping() {
		List<Class> indexClass = new ArrayList<>();
		indexClass.add(SpecificationES.class);
		indexClass.add(ApplicationCaseES.class);
		indexClass.add(SpecificationQuestionsES.class);
		indexClass.add(SpecificationQuotationES.class);
		indexClass.add(ApplicationCaseQuestionsES.class);
		indexClass.add(SpecificationCharacteristicES.class);
		List<Class> mappingClass = new  ArrayList<>();
		mappingClass.add(SpecificationCharacteristicES.class);
		mappingClass.add(ApplicationCaseQuestionsES.class);
		mappingClass.add(SpecificationQuotationES.class);
		mappingClass.add(SpecificationQuestionsES.class);
		mappingClass.add(ApplicationCaseES.class);
		mappingClass.add(SpecificationES.class);
		ElasticsearchTemplateUtils.createIndexOrMapping(indexClass, mappingClass, elasticsearchTemplate);
	}

	//物性添加文档（测试单个）
	@Test
	public void  testAddSpecificationDocument(){
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setIndexName(EsConstant.ES_INDEXNAME);
		indexQuery.setType("specificationES");
		indexQuery.setId("166544");
		indexQuery.setSource(getSource());
		ElasticsearchTemplateUtils.addOneDocument(elasticsearchTemplate, indexQuery);
	}
	//案例添加文档（测试单个）
	@Test
	public void  testAddApplicationCaseDocument(){
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setIndexName(EsConstant.ES_INDEXNAME);
		indexQuery.setType("applicationCaseES");
		indexQuery.setId("888888");
		indexQuery.setParentId("1665499");
		indexQuery.setSource(getApplicationCaseSource());
		ElasticsearchTemplateUtils.addOneDocument(elasticsearchTemplate, indexQuery);
	}
	
	
	
	@Test
	public  void  testQueryDocument(){
		SearchQuery q2 = new  NativeSearchQueryBuilder()
				.withQuery(
						QueryBuilders.boolQuery()
							.should(new HasChildQueryBuilder("applicationCaseES", QueryBuilders.matchPhraseQuery("title","奶瓶"), ScoreMode.None))
//							.should(QueryBuilders.matchQuery("name", "55"))
//							.should(QueryBuilders.matchQuery("content", "描述东西"))
//							.filter(QueryBuilders.matchQuery("finds", "221"))
						)
//				.withFilter(QueryBuilders.matchQuery("finds", "221"))
				.withPageable(new  PageRequest(0, 10))
				.withHighlightFields(new Field("name"),new  Field("content"))
				.build();
		Object object = elasticsearchTemplate.query(q2, new ResultsExtractor(){
			@Override
			public Object extract(SearchResponse response) {
				long totalHits = response.getHits().totalHits;
				SearchHit[] hits = response.getHits().getHits();
				SpecificationVo  vo = null;
				List<SpecificationVo>  list = new  ArrayList<>();
				for (int i = 0; i < hits.length; i++) {
					vo = new SpecificationVo();
					String id = hits[i].getId();
					vo.setId(Integer.valueOf(id));
					Map<String, Object> source = hits[i].getSource();
					vo.setBaseMaterial(source.get("baseMaterial").toString());
					vo.setBrandName(source.get("brandName").toString());
					vo.setManufacturer(source.get("manufacturer").toString());
					vo.setName(source.get("name").toString());
					vo.setContent(source.get("content").toString());
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
				return JsonUtils.objectToJson(list);
			}
		});
		System.out.println(object);
	}
	
	/**
	 * 根据文档id，得到文档信息
	 */
	@Test
	public  void   testQueryOneSpecificationById(){
		SpecificationES specificationES = ElasticsearchTemplateUtils.queryOneSpecificationById(4078+"", SpecificationES.class, elasticsearchTemplate);
		System.out.println(specificationES);
	}
	
	/**
	 * 如果有路径，不指定路由，还是删不掉，但是不报错
	 */
	@Test
	public   void   deleteDocument(){
		DeleteQuery deleteQuery = new DeleteQuery();
		deleteQuery.setIndex("zlwon");
		deleteQuery.setType("applicationCaseES");
		deleteQuery.setQuery(QueryBuilders.idsQuery().addIds("123"));
		elasticsearchTemplate.delete(deleteQuery);
	}
	
	
	private  String   getSource(){
		SpecificationDocument  specificationES = new  SpecificationDocument();
		specificationES.setBaseId(2+"");
		specificationES.setBaseMaterial("基材id为2");
		specificationES.setBrandId(2+"");
		specificationES.setBrandName("商标id为2");
		specificationES.setContent("Tr 58物性描述了一大堆东西");
		specificationES.setCreateDate(new  Date());
		specificationES.setFinds("11,12,211,1221");
		specificationES.setManufacturer("艾曼斯2");
		specificationES.setManufacturerId(1);
		specificationES.setName("TR 58");
		specificationES.setScids("1,21,33");
		return  JsonUtils.objectToJson(specificationES);
	}
	
	private  String   getApplicationCaseSource(){
		ApplicationCaseDocument  applicationCase = new  ApplicationCaseDocument();
		applicationCase.setAppProduct("测试");
		applicationCase.setTitle("哈哈");
		applicationCase.setEmptyField(EsConstant.APPLICATIONCASEES_EMPTYFIELD_VALUE);
		return  JsonUtils.objectToJson(applicationCase);
	}
	
	
	@Test
	public  void   testCount(){
		SearchQuery query = new  NativeSearchQueryBuilder()
				.withIndices(EsConstant.ES_INDEXNAME)//指定查询的索引库
				.withTypes("applicationCaseES")//指定查询的类型
				.withQuery(QueryBuilders.matchPhraseQuery("title", "奶瓶"))
				.build();
		List<ApplicationCaseES> queryForList = elasticsearchTemplate.queryForList(query, ApplicationCaseES.class);
		System.out.println("案例个数:"+queryForList.size());
	}
}
