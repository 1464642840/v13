package com.qf.v13searchservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.qf.v13.api.ISearchService;
import com.qf.v13.common.pojo.PageResultBean;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TProduct;
import com.qf.v13.mapper.TProductMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author blxf
 * @Date ${Date}
 */

@Service
public class SearchServiceImpl implements ISearchService {
    @Autowired
    private TProductMapper productMapper;
    @Autowired
    private SolrClient solrClient;

    @Override
    public ResultBean synAllData() {
        //1.获取数据库的数据
        List<TProduct> list = productMapper.selectList();
        //2.将数据库中的数据导入到数据索引中
        for (TProduct tProduct : list) {
            SolrInputDocument document = new SolrInputDocument();
            document.setField("id", tProduct.getId());
            document.setField("product_name", tProduct.getName());
            document.setField("product_price", tProduct.getPrice().toString());
            document.setField("product_sale_price", tProduct.getSalePrice().toString());
            document.setField("product_sale_point", tProduct.getSalePoint());
            document.setField("product_images", tProduct.getImage());
            try {
                solrClient.add(document);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
                return new ResultBean("404", "数据同步不成功");
            }
        }
        try {
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean("404", "数据提交到索引库不成功");
        }
        return new ResultBean("200", "数据同步成功");
    }

    @Override
    public ResultBean queryByKeywords(String keywords, int currentPage) {
        //创建一个分页对象
        PageResultBean<TProduct> page = new PageResultBean<>();
        //创建一个solr的查询条件对象
        SolrQuery solrQuery = new SolrQuery();
        //分页大小设置为12 ,当前页为传进的参数
        page.setPageSize(12);
        page.setPageNum(currentPage);

        //给查询条件对象赋查询坐标和查询条数
        solrQuery.setStart((page.getPageNum() - 1) * page.getPageSize());
        solrQuery.setRows(page.getPageSize());

        //判断传进来的关键字是否为空.如果为空则查询所有
        if (StringUtils.isAnyEmpty(keywords)) {
            solrQuery.setQuery("*:*");
        } else {
            solrQuery.setQuery("product_name:" + keywords);
        }

        //设置高亮信息
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("product_name");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        try {
            //进行solr查询得到查询结果
            QueryResponse query = solrClient.query(solrQuery);
            //获取高亮信息
            Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
            //获得总结果的总条数,不是当前页数的条数
            long numFound = query.getResults().getNumFound();
            page.setTotal(numFound);
            //计算得出总页数
            Long pages = page.getTotal() % page.getPageSize() == 0 ? page.getTotal() /
                    page.getPageSize() : page.getTotal() / page.getPageSize() + 1;
            page.setPages(pages.intValue());

            //得到数据集
            SolrDocumentList results = query.getResults();
            ArrayList<TProduct> tProducts = new ArrayList<>(results.size());
            //遍历数据集 将数据集中的数据赋给list集合
            for (SolrDocument result : results) {
                TProduct t = new TProduct();
                t.setId(Long.parseLong(result.getFieldValue("id").toString()));
                // t.setName(result.getFieldValue("product_name").toString());
                //针对高亮做设置
                Map<String, List<String>> map = highlighting.get(result.getFieldValue("id"));
                List<String> product_name = map.get("product_name");
                if (product_name != null && product_name.size() != 0) {
                    t.setName(product_name.get(0));
                } else {
                    t.setName(result.getFieldValue("product_name").toString());
                }
                if (result.getFieldValue("product_price") != null) {
                    t.setPrice(BigDecimal.valueOf(Double.parseDouble(result.getFieldValue("product_price").toString())));
                }
                if (result.getFieldValue("product_sale_price") != null) {
                    t.setSalePrice(BigDecimal.valueOf(Double.parseDouble(result.getFieldValue("product_sale_price").toString())));

                }
                t.setSalePoint((result.getFieldValue("product_sale_point").toString()));
                if (result.getFieldValue("product_images") != null) {
                    t.setImage(result.getFieldValue("product_images").toString());
                }
                tProducts.add(t);
            }
            page.setList(tProducts);
            page.setNavigatePages(6);
            page.setKeyword(keywords);
            return new ResultBean("200", page);

        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean("404", "执行查询失败");
        }
    }

    @Override
    public ResultBean synAddOrUpdateById(Long id) {
        TProduct tProduct = productMapper.selectByPrimaryKey(id);

        SolrInputDocument document = new SolrInputDocument();
        document.setField("id", tProduct.getId());
        document.setField("product_name", tProduct.getName());
        document.setField("product_price", tProduct.getPrice().toString());
        document.setField("product_sale_price", tProduct.getSalePrice().toString());
        document.setField("product_sale_point", tProduct.getSalePoint());
        document.setField("product_images", tProduct.getImage());
        try {
            solrClient.add(document);
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean("404", "数据同步不成功");
        }
        return new ResultBean("200", "数据同步成功");

    }

    @Override
    public ResultBean synDeleteById(Long id) {
        System.out.println(id);
        try {
            solrClient.deleteById(id.toString());
            solrClient.commit();


        } catch (SolrServerException e) {
            e.printStackTrace();
            return new ResultBean("404", "删除失败");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResultBean("200", "删除成功");
    }


}
