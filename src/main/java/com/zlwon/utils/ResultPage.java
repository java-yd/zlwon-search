package com.zlwon.utils;

import java.util.List;

public class ResultPage<T> extends AbstractResult {

	private List<T> dataList; //列表数据
    private Integer totalPage; //总页数
    private Integer totalNumber;//总条数
    private Integer pageIndex; //当前页
    private Integer pageSize;  //当前页条数

    public static <T> ResultPage<T> error() {
        return new ResultPage(StatusCode.SYS_ERROR);
    }

    @SuppressWarnings(value = "unchecked")
    public static <T> ResultPage<T> error(StatusCode statusCode) {
        return new ResultPage(statusCode);
    }

    @SuppressWarnings(value = "unchecked")
    public static <T> ResultPage<T> list(List<T> dataList,Integer totalPage,Integer totalNumber,Integer pageIndex,Integer pageSize) {
    	ResultPage<T> res =  new ResultPage(StatusCode.SUCCESS);
        res.dataList = dataList;
        res.totalPage = totalPage;
        res.totalNumber = totalNumber;
        res.pageIndex = pageIndex;
        res.pageSize = pageSize;
        return res;
    }

    public ResultPage(StatusCode statusCode) {
        super(statusCode);
    }

    public ResultPage(String code, String message) {
        super(code, message);
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
