package com.gzc.springbootes.pojo;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "goods")//指定索引库名字
public class Goods implements Serializable {

    @Field(type = FieldType.Keyword)//设置映射,Keyword不分词
    private String id;
    @Field(type = FieldType.Text)
    private String goodsName;
    @Field(type = FieldType.Integer)
    private Integer store;
    @Field(type = FieldType.Double)
    private double price;

}
