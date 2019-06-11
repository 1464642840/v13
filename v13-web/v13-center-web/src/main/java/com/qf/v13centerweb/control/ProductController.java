package com.qf.v13centerweb.control;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IProductService;
import com.qf.v13.entity.TProduct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * @author blxf
 * @Date ${Date}
 */
@Controller
@RequestMapping("product")
public class ProductController {
    @Reference
    private IProductService service;

    @RequestMapping("get/{id}")
    @ResponseBody
    public TProduct productController(@PathVariable("id") Long id) {
        return service.selectByPrimaryKey(id);
    }

    @RequestMapping("list")
    public String goList(Model model) {
        List<TProduct> list = service.selectList();
        model.addAttribute("list",list);
        return "product/list";
    }
}
