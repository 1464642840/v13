package com.qf.v13emailweb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13SearchWebApplicationTests {

    @Test
    public void contextLoads() {
        List<Long> id = new ArrayList<>();
        for (Long i = 1L; i < 10; i++) {
            id.add(i);
        }

    }

}
