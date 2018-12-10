package com.xie.common.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @version 1.0
 */
public class Page<T> {

    private int currentPage;

    private int pageSize;

    private int totalPages;

    private int total;

    private transient List<T> instanceList;

    private List<T> items = new ArrayList<>();

    private transient Optional<List<T>> op;

    public Page(List<T> instanceList, int pageSize) {
        this.pageSize = pageSize;
        setInstanceList(instanceList);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage < 1 ? 1 : currentPage > this.totalPages ? this.totalPages : currentPage;
        setItems(currentPageData());
    }


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotal() {
        return total;
    }


    public void setInstanceList(List<T> instanceList) {
        this.op = Optional.ofNullable(instanceList);
        this.instanceList = op.orElse(new ArrayList<T>());
        this.total = this.instanceList.size();
        this.totalPages = (int) Math.ceil(1.0 * this.total / this.pageSize);
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    private List<T> currentPageData() {
        if (this.pageSize == 0 || this.totalPages == 1) {
            return this.instanceList;
        }
        List<T> currentPageData = new ArrayList<T>();
        instanceList.stream().skip((this.currentPage - 1) * this.pageSize).limit(this.pageSize)
            .forEach(e -> currentPageData.add(e));
        return currentPageData;
    }
}
