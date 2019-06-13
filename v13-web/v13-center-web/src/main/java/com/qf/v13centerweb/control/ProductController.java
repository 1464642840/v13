package com.qf.v13centerweb.control;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.qf.v13.api.IProducTypeService;
import com.qf.v13.api.IProductService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TProductType;
import com.qf.v13.pojo.TProductVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
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
    @Reference
    private IProducTypeService typeService;


    @RequestMapping("get/{id}")
    @ResponseBody
    public TProduct productController(@PathVariable("id") Long id) {
        return service.selectByPrimaryKey(id);
    }

    @RequestMapping("list")
    public String goList() {

        return "redirect:/product/page/1";
    }

    @RequestMapping("page/{currePage}")
    public String goPage(@PathVariable("currePage") Integer currePage, Model model) {
        //获取分页数据
        PageInfo<TProduct> page = service.page(currePage, 10);
        //获取类别数据
        List<TProductType> typeList = typeService.selectList();
        System.out.println(typeList.size());
        model.addAttribute("page", page);
        model.addAttribute("typeList", typeList);
        return "product/list";
    }

    @RequestMapping("admin")
    public String toAdmin() {

        return "admin/index";
    }

    @PostMapping("add")
    public String addProduct(TProductVo vo) {
        service.sava(vo);
        return "redirect:/product/page/1";
    }

    @PostMapping("delete/{id}")
    @ResponseBody
    public ResultBean deleteProduct(@PathVariable("id") Long id) {
        int i = service.deleteByPrimaryKey(id);
        if (i > 0) {
            return new ResultBean("200", "删除成功");
        }
        return new ResultBean("404", "删除失败!");
    }

    @PostMapping("batchDelete/{ids}")
    @ResponseBody
    public ResultBean deleteProduct(@PathVariable("ids") List<Long> ids) {
        Long i = service.batchDelete(ids);
        if (i > 0) {
            return new ResultBean("200", "删除成功");
        }
        return new ResultBean("404", "删除失败!");
    }


}
