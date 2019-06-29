package com.qf.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author blxf
 * @Date ${Date}
 */
public class CartItem implements Serializable {
    private Long product_id;
    private int count;
    private Date updateTime;

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
