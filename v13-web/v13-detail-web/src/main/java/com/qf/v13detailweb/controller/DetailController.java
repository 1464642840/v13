package com.qf.v13detailweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IProductService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TProduct;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.FormatFlagsConversionMismatchException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author blxf
 * @Date ${Date}
 */
@Controller
@RequestMapping("detail")
public class DetailController {
    @Reference
    private IProductService productService;

    @Autowired
    private Configuration configuration;

    @RequestMapping("createHTMLById/{id}")
    @ResponseBody
    public ResultBean createHTMLById(@PathVariable Long id) {
        try {
            Template template = configuration.getTemplate("item.ftl");
            Map<String, Object> map = new HashMap<>();
            map.put("product", productService.selectByPrimaryKey(id));
            String path = ResourceUtils.getURL("classpath:static").getPath();
            String filePath = new StringBuilder(path).append(File.separator).append(id).append(".html").toString();
            FileWriter fw = new FileWriter(filePath);
            template.process(map, fw);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultBean("404,", "获取模板失败");
        } catch (TemplateException e) {
            e.printStackTrace();
            return new ResultBean("404,", "生成模板失败");
        }
        return new ResultBean("200,", "生成静态页成功");
    }

    @RequestMapping("batchCreateHTML")
    @ResponseBody
    public ResultBean batchCreateHTML(@RequestParam List<Long> ids) {
        int cpus = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor pool = new ThreadPoolExecutor(cpus, 2 * cpus, 10, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(100));
        for (Long id : ids) {
            pool.submit(new CreateHTMLTask(id));
        }

        return new ResultBean("200,", "批量生成静态页成功");
    }

    class CreateHTMLTask implements Runnable {
        private Long id;

        public CreateHTMLTask(Long id) {
            this.id = id;
        }

        @Override
        public void run() {
            createHTMLById(id);
        }
    }

}


