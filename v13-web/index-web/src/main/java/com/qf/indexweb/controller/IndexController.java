package com.qf.indexweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IProducTypeService;
import com.qf.v13.api.IProductService;
import com.qf.v13.entity.TProductType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author blxf
 * @Date ${Date}
 */
@Controller
public class IndexController {
    @Reference
    private IProducTypeService typeService;

    @RequestMapping(value = {"/", "index", "home"})
    public String toIndex(Model model) {
        List<TProductType> typeList = typeService.selectListByCache();
        model.addAttribute("typeList", typeList);
        return "home";
    }
}
