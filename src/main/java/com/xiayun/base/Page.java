package com.xiayun.base;

import java.util.List;

/**
 * Package: com.xy.entity
 * <p>
 * File: Page.java
 * <p>
 * Author: xy   Date: 2018年4月4日 下午3:19:37
 */

public class Page {
    /**
     * 当前页
     */
    private Long currentPage = 1L;
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 总页数
     */
    private Long totalPage;
    /**
     * 每页显示记录数
     */
    private int pageSize = 10;

    /**
     * 查询到的数据
     */

    private List<?> pageList;

    private String sEcho;

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<?> getPageList() {
        return pageList;
    }

    public void setPageList(List<?> pageList) {
        this.pageList = pageList;
    }


    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }
}
