package com.gzc.springbootes;


import com.gzc.springbootes.dao.GoodsDao;
import com.gzc.springbootes.pojo.Goods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DocTest {

    @Autowired
    private GoodsDao goodsDao;

    /**
     * 添加文档
     * */
    @Test
    public void saveTest(){
        Goods goods = new Goods();
        goods.setId("1");
        goods.setGoodsName("华为手机");
        goods.setStore(100);
        goods.setPrice(5000);
        goodsDao.save(goods);
        System.out.println("添加成功...");
    }

    /**
     * 根据ID查询文档
     * */
    @Test
    public void findById(){
        Goods goods = goodsDao.findById("1").get();
        System.out.println(goods);
    }

    //查询所有
    @Test
    public void testfindall(){
        Iterable<Goods> all = goodsDao.findAll();
        for (Goods goods : all) {
            System.out.println("goods = " + goods);
        }
    }

    //更新
    @Test
    public void testupdata(){
        Goods goods = new Goods();
        goods.setId("1");
        goods.setGoodsName("三星手机");
        goods.setStore(1200);
        goods.setPrice(52000);
        goodsDao.save(goods);
    }

    //删除
    @Test
    public void testdelete(){
        goodsDao.deleteById("1");
    }
}
