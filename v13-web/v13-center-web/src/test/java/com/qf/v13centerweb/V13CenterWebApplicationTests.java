package com.qf.v13centerweb;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.activation.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13CenterWebApplicationTests {
    @Autowired
    private FastFileStorageClient client;

    @Test
    public void contextLoads() throws FileNotFoundException {
        File file = new File("C:\\Users\\Administrator\\Desktop\\文件/6af89bc8gw1f8nymj7lfhj20ce0bi0tp.jpg");
        FileInputStream inputStream  = new FileInputStream(file);
        StorePath jpg = client.uploadFile(inputStream, file.length(), "jpg", null);
        System.out.println(jpg.getFullPath());

    }



}
