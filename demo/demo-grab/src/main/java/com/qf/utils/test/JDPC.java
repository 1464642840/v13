package com.qf.utils.test;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qf.utils.GetCOnnection;
import com.qf.utils.HttpClientUtils;
import com.qf.v13.api.IProductService;
import com.qf.v13.common.base.BaseServiceImpl;
import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TProductType;
import com.qf.v13.mapper.TProductMapper;
import com.qf.v13productservice.service.ProductServiceImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author QuietHR
 * @create 2018/9/22
 **/
public class JDPC {
    private static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1000);
    // private static LinkedBlockingQueue queue = new LinkedBlockingQueue<Object>(100);
    private static ExecutorService executorService = Executors.newFixedThreadPool(50);
    //  private static ThreadPoolExecutor executorService = new ThreadPoolExecutor(10, 100, 50, TimeUnit.SECONDS, queue);
    public static Connection connection = null;
    public static Set<Long> typeId = new HashSet();

    public static void main(String[] args) throws Exception {
        connection = GetCOnnection.getConnection();
        for (int i = 0; i < 30; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String pid = null;
                        try {
                            pid = queue.take().toString();
                            TProduct product = parsePid(pid);

                            synchronized (JDPC.class) {
                                GetCOnnection.save(connection, product);
                            }

                        } catch (Exception e) {
                            try {
                                queue.put(pid);
                            } catch (InterruptedException e1) {
                            }
                        }
                    }
                }
            });
        }
        page();

    }

    private static void page() throws Exception {
        for (int i = 1; i <= 100; i++) {
            String url = "https://search.jd.com/Search?keyword=%E9%A3%9F%E5%93%81&enc=utf-8&qrst=1&rt=1&stop=1&vt=1&stock=1&page="+i+"&s=53&click=0";
            String html = HttpClientUtils.doGet(url, "utf-8");
            parseIndex(html);
        }
    }

    private static void parseIndex(String html) throws InterruptedException {
        Document document = Jsoup.parse(html);
        Elements liEl = document.select("[class=gl-warp clearfix]>li");
        for (Element li : liEl) {
            queue.put(li.attr("data-sku"));
        }
    }

    private static TProduct parsePid(String pid) throws Exception {
        System.out.println("当前商品的id" + pid);
        String url = "https://item.jd.com/" + pid + ".html";
        String html = HttpClientUtils.doGet(url, "utf-8");
        Document document = Jsoup.parse(html);
        TProduct product = new TProduct();
        product.setId(Long.parseLong(pid));
        Elements titleEl = document.select("[class=sku-name]");
        product.setName(titleEl.text());
        Elements brandEl = document.select("#parameter-brand>li");
        product.setSalePoint(brandEl.attr("title"));
        Elements pnameEl = document.select("[class=parameter2 p-parameter-list]>li:first-child");
        product.setName(pnameEl.attr("title"));
        //获得价格
        String productUrl = "https://p.3.cn/prices/mgets?pduid=" + Math.random() + "&skuIds=J_" + pid;
        String json = HttpClientUtils.doGet(productUrl, "utf-8");
        List<Map<String, String>> list = (List<Map<String, String>>) JSONArray.parse(json);
        String newprice = list.get(0).get("p");
        String oldprice = list.get(0).get("m");

        BigDecimal nP = BigDecimal.valueOf(Double.parseDouble(newprice));
        BigDecimal oP = BigDecimal.valueOf(Double.parseDouble(oldprice));
        product.setPrice(nP);
        product.setSalePrice(oP);
        //获得图片地址
        String attr = document.select("div[class=p-img]").select("a").select("img").attr("src");

        String imgSrc = "https:" + attr;
        product.setImage(imgSrc);
        //获得卖点
        String pointUrl = "https://cd.jd.com/promotion/v2?pduid=" + Math.random() + "&skuId=" + pid + "&area=19_1600007_4773_0&cat=1320%2C1583%2C1592";
        String pointJson = HttpClientUtils.doGet(pointUrl, "GBK");
        Map<String, Map<String, String>> pointMap = (Map<String, Map<String, String>>) JSONObject.parse(pointJson);
        Map<String, String> quan = pointMap.get("quan");
        String salePoint = quan.get("title");
        product.setSalePoint(salePoint);

        //爬取商品类别
        String bigTypename = document.select("div[class=crumb fl clearfix]>div[class=item first]>a").text();
        String smallTypename = document.select("a[clstag*='mbNav-2']").text();
        String href = document.select("a[clstag*='mbNav-2']").attr("href");
        Long smallNum = Long.parseLong(href.substring(href.lastIndexOf(',') + 1));
        Long bigNum = Long.parseLong(href.substring(href.lastIndexOf('=') + 1, href.lastIndexOf(',')));

        if (typeId.add(bigNum)) {
            TProductType tProductType = new TProductType();
            tProductType.setId(bigNum);
            tProductType.setPid(0L);
            tProductType.setName(bigTypename);
            GetCOnnection.save(connection, tProductType);
        }
        if (typeId.add(smallNum)) {
            TProductType tProductType2 = new TProductType();
            tProductType2.setId(smallNum);
            tProductType2.setPid(bigNum);
            tProductType2.setName(smallTypename);
            GetCOnnection.save(connection, tProductType2);
        }

        product.setTypeId(smallNum);
        product.setTypeName(smallTypename);
        product.setFlag(true);
        product.setCreateTime(new Date());
        return product;
    }
}
