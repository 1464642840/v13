package com.qf.v13emailweb.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.ISearchService;
import com.qf.v13.common.constant.RabbitMQConstant;
import com.qf.v13.common.pojo.ResultBean;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author blxf
 * @Date ${Date}
 */
@Component
public class ProductHandle {
    @Reference
    private ISearchService searchService;

    @RabbitHandler
    @RabbitListener(queues = RabbitMQConstant.PRODUCT_SEARCH_QUEUE_ADDORUPDATE)
    public void processAddOrUpdate(Long id) {
        System.out.println(id);
        searchService.synAddOrUpdateById(id);
    }

    @RabbitHandler
    @RabbitListener(queues = RabbitMQConstant.PRODUCT_SEARCH_QUEUE_DELETE)
    public void processDelete(Long id) {
        ResultBean result = searchService.synDeleteById(id);
        System.out.println(result.getStatusCode());
    }
}
