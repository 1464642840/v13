package com.qf.v13centerweb.control;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.qf.v13.api.IProducTypeService;
import com.qf.v13.api.IProductService;
import com.qf.v13.api.ISearchService;
import com.qf.v13.common.constant.RabbitMQConstant;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.common.utils.HttpClientUtil;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TProductType;
import com.qf.v13.pojo.TProductVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private RabbitTemplate rabbitTemplate;


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
        PageInfo<TProduct> page = service.page(currePage, 5);
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
        Long id = service.sava(vo);
        System.out.println(id);
        //发送一个消息到交换机
        rabbitTemplate.convertAndSend(RabbitMQConstant.CENTER_PRODUCT_EXCHANGE, "product.addOrUpdate", id);
        String s = HttpClientUtil.doGet("http://localhost:9094/detail/createHTMLById/" + id);
        return "redirect:/product/page/1";
    }

    @PostMapping("delete/{id}")
    @ResponseBody
    public ResultBean deleteProduct(@PathVariable("id") Long id) {
        int i = service.deleteByPrimaryKey(id);
        if (i > 0) {
            //发送一个消息到交换机,更新索引
            rabbitTemplate.convertAndSend(RabbitMQConstant.CENTER_PRODUCT_EXCHANGE, "product.delete", id);
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
