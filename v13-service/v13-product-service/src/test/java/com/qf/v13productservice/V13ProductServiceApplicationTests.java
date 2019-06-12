package com.qf.v13productservice;

import com.github.pagehelper.PageInfo;
import com.qf.v13.api.IProductService;
import com.qf.v13.entity.TProduct;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13ProductServiceApplicationTests {

    @Autowired
    private IProductService service;
    @Test
    public void contextLoads() {
        PageInfo<TProduct> page = service.page(1, 10);
        System.out.println(page.getList().size());

    }

}
