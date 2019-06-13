package com.qf.v13productservice;

import com.github.pagehelper.PageInfo;
import com.qf.v13.api.IProductService;
import com.qf.v13.entity.TProduct;
import com.qf.v13.pojo.TProductVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13ProductServiceApplicationTests {

    @Autowired
    private IProductService service;

    @Test
    public void contextLoads() {

        List<Long> list = new ArrayList<>();
        list.add(16L);
        list.add(17L);
        System.out.println(service.batchDelete(list));
    }


}
