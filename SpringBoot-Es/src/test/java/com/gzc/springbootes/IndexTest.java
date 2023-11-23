package com.gzc.springbootes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexTest {

    @Test
    public void createIndex(){
        System.out.println("系统自动创建索引");
    }

}
