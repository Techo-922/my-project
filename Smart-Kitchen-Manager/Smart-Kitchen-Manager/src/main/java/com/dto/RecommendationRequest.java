package com.dto;

import java.io.Serializable;

/**
 * 推荐请求参数封装类
 * 用于封装智能推荐接口的请求参数
 * 
 * @author 
 * @date 2023-04-25
 */
public class RecommendationRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID（必传）
     */
    private Long userId;

    /**
     * 页码（默认1）
     */
    private Integer pageNum = 1;

    /**
     * 每页大小（默认10）
     */
    private Integer pageSize = 10;

    /**
     * 排序类型（matchRate-匹配度, popularity-热度）
     */
    private String sortType = "matchRate";

    public RecommendationRequest() {
    }

    public RecommendationRequest(Long userId) {
        this.userId = userId;
    }

    public RecommendationRequest(Long userId, Integer pageNum, Integer pageSize, String sortType) {
        this.userId = userId;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.sortType = sortType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
}
