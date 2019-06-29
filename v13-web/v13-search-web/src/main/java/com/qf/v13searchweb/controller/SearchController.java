package com.qf.v13searchweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.ISearchService;
import com.qf.v13.common.pojo.PageResultBean;
import com.qf.v13.common.pojo.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author blxf
 * @Date ${Date}
 */
@Controller
@RequestMapping("search")
public class SearchController {
    @Reference
    private ISearchService searchService;


    @RequestMapping("product/{keywords}/{currentPage}")
    public String queryByKeywords(@PathVariable String keywords, @PathVariable int currentPage, Model model, HttpServletRequest request) {
        //获取用户信息


        ResultBean resultBean = searchService.queryByKeywords(keywords, currentPage);
        PageResultBean page = (PageResultBean) resultBean.getData();
        model.addAttribute("page", page);
        return "search-list";
    }
}
