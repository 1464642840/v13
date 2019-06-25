package com.qf.v13productservice;

import com.github.pagehelper.PageInfo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.qf.v13.api.IProductService;
import com.qf.v13.common.utils.HttpClientUtil;
import com.qf.v13.common.utils.HttpClientUtils;
import com.qf.v13.common.utils.QCloudUtil;
import com.qf.v13.entity.TProduct;
import com.qf.v13.pojo.TProductVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13ProductServiceApplicationTests {


    @Autowired
    private IProductService service;

    @Autowired
    private DataSource dataSource;

    @Test
    public void contextLoads() {

        List<Long> list = new ArrayList<>();
        list.add(16L);
        list.add(17L);
        System.out.println(service.batchDelete(list));
    }

    @Test
    public void dataSourceTest() throws SQLException {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + dataSource.getConnection());
    }

    @Test
    public void testUploadCOS() {

        String s = QCloudUtil.uploadFile(new File("C:\\Users\\" +
                "Administrator\\Desktop\\文件\\词典.png"), "11.png");
        System.out.println(s);
    }
    @Test
    public  void testCreateHtml(){
        List<Long> ids = new ArrayList<>();
        List<TProduct> tProducts = service.selectList();
        for (TProduct tProduct : tProducts) {
            ids.add(tProduct.getId());
            try {
                HttpClientUtils.doGet("http://localhost:9094/detail/createHTMLById/"+tProduct.getId(),"utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
