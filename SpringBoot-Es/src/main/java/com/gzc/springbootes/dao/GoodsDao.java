package com.gzc.springbootes.dao;

import com.gzc.springbootes.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsDao extends ElasticsearchRepository<Goods, String> {
}
