package com.mongodb.mode;

import com.entity.BaseEntity;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

/**
 * @Classname PageInfo
 * @Description 分页mode
 * @Date 2020/1/3 11:22
 * @Created by 125937
 */
public class PageInfo<T extends BaseEntity> implements Serializable {

    private int pageIndex=1;

    private int pageSize=50;

    private int rowTotal;

    private int pageTotal;

    private List<T> rows;

    private List<String> selectFields;

    private T queryCondition;

    private List<Sort.Order> orderCondition;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getRowTotal() {
        return rowTotal;
    }

    public void setRowTotal(int rowTotal) {
        this.rowTotal = rowTotal;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public List<String> getSelectFields() {
        return selectFields;
    }

    public void setSelectFields(List<String> selectFields) {
        this.selectFields = selectFields;
    }

    public T getQueryCondition() {
        return queryCondition;
    }

    public void setQueryCondition(T queryCondition) {
        this.queryCondition = queryCondition;
    }

    public List<Sort.Order> getOrderCondition() {
        return orderCondition;
    }

    public void setOrderCondition(List<Sort.Order> orderCondition) {
        this.orderCondition = orderCondition;
    }
}
