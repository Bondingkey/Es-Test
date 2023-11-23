package com.gzc.estest;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;

import java.io.IOException;


/**
 * @author: 拿破仑
 * @Date&Time: 2023/11/22  09:51  周三
 * @Project: EsTest
 * @Write software: IntelliJ IDEA
 * @Purpose: 在此处编辑
 */
public class EsTest {

    RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

    private static final String MY_INDEX = "my_index";

    //创建索引
    @Test
    public void createIndex(){
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("students");
        try {
            //设置索引的映射
            createIndexRequest.mapping("{\n" +
                    "    \"properties\": {\n" +
                    "      \"name\": {\n" +
                    "        \"type\": \"keyword\",\n" +
                    "        \"index\": true,\n" +
                    "        \"store\": true\n" +
                    "      },\n" +
                    "      \"age\": {\n" +
                    "        \"type\": \"integer\",\n" +
                    "        \"index\": true,\n" +
                    "        \"store\": true\n" +
                    "      },\n" +
                    "      \"remark\": {\n" +
                    "        \"type\": \"text\",\n" +
                    "        \"index\": true,\n" +
                    "        \"store\": true,\n" +
                    "        \"analyzer\": \"ik_max_word\",\n" +
                    "        \"search_analyzer\": \"ik_max_word\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }", XContentType.JSON);
            //创建索引
            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            //获取ack
            System.out.println(createIndexResponse.isAcknowledged());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查看索引
    @Test
    public void getIndex(){
        GetIndexRequest request = new GetIndexRequest("students");
        try {
            //查询索引库
            GetIndexResponse getIndexResponse = client.indices().get(request, RequestOptions.DEFAULT);
            //返回查到的结果
            System.out.println(getIndexResponse.getMappings());
            System.out.println(getIndexResponse.getSettings());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //删除索引
    @Test
    public void deleteIndex(){
        DeleteIndexRequest request = new DeleteIndexRequest("book","test");
        try {
            AcknowledgedResponse acknowledgedResponse = client.indices().delete(request, RequestOptions.DEFAULT);
            System.out.println(acknowledgedResponse.isAcknowledged());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //添加文档
    @Test
    public void createDocument(){
        IndexRequest request = new IndexRequest("students");
        //设置文档id
        request.id("1");
        Student student = new Student();
        student.setAge(18);
        student.setName("robin");
        student.setRemark("good man");
        request.source(JSONObject.toJSONString(student), XContentType.JSON);
        try {
            IndexResponse index = client.index(request, RequestOptions.DEFAULT);
            System.out.println(index.getResult());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //修改文档
    @Test
    public void updateDocuemnt(){
        UpdateRequest request = new UpdateRequest("students","1");
        try {
            Student student = new Student();
            student.setRemark("very good man");
            request.doc(JSONObject.toJSONString(student), XContentType.JSON);
            UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
            System.out.println(response.getResult());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //根据ID查询
    @Test
    public void getDocument(){
        GetRequest request = new GetRequest("students","1");
        try {
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            System.out.println(response.getSourceAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //批量操作
    @Test
    public void bulkDocument(){
        BulkRequest request = new BulkRequest();
        Student student = new Student();
        for(int i=0;i<10;i++){
            student.setAge(18 + i);
            student.setName("robin" + i);
            student.setRemark("good man " + i);
            request.add(new IndexRequest("students").id(String.valueOf(10 + i)).source(JSONObject.toJSONString(student), XContentType.JSON));
        }
        try {
            BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
            for(BulkItemResponse itemResponse : response.getItems()){
                System.out.println(itemResponse.isFailed());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //删除文档
    @Test
    public void deleteDocument(){
        DeleteRequest request = new DeleteRequest("students","1");
        try {
            DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
            System.out.println(response.getResult());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * dsl查询文档:
     * {
     *   "query": {
     *     "match": {
     *       "title": "华为智能手机"
     *     }
     *   }
     * }
     * */
    @Test
    public void search(){
        SearchRequest request = new SearchRequest(MY_INDEX);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("title","华为智能手机"));
        request.source(builder);
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            for(SearchHit hit : response.getHits().getHits()){
                System.out.println(hit.getSourceAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 高亮查询
     * */
    @Test
    public void highlightSearch(){
        SearchRequest request = new SearchRequest(MY_INDEX);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("title","华为智能手机"));
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.preTags("<b style='color:red'>");
        highlightBuilder.postTags("</b>");
        builder.highlighter(highlightBuilder);
        request.source(builder);
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            for(SearchHit hit : response.getHits().getHits()){
                System.out.println(hit.getSourceAsMap().get("title") + ":" +hit.getHighlightFields().get("title").fragments()[0].string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 聚合查询
     * */
//    @Test
//    public void aggsSearch(){
//        SearchRequest request = new SearchRequest(MY_INDEX);
//        SearchSourceBuilder builder = new SearchSourceBuilder();
//        builder.query(QueryBuilders.matchAllQuery());
//        AggregationBuilder aggregationBuilder = AggregationBuilders
//                .terms("groupby_category").field("category");
//        aggregationBuilder.subAggregation(AggregationBuilders.avg("avg_price").field("price"));
//        builder.aggregation(aggregationBuilder);
//        request.source(builder);
//        try {
//            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
//            Aggregations aggregations = response.getAggregations();
//            Terms terms = aggregations.get("groupby_category");
//            terms.getBuckets().forEach(bucket -> {
//                Avg avg = bucket.getAggregations().get("avg_price");
//                System.out.println(bucket.getKeyAsString() + ":" + bucket.getDocCount() + "," + avg.getValue());
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}
