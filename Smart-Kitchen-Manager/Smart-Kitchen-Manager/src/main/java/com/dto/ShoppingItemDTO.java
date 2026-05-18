package com.dto;

import java.io.Serializable;

/**
 * 采购清单项DTO
 * 
 * @author 
 * @date 2023-04-25
 */
public class ShoppingItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 食材ID
     */
    private Long shicaiId;

    /**
     * 食材名称
     */
    private String shicaiName;

    /**
     * 食材分类（蔬菜、肉类、调料等）
     */
    private String category;

    /**
     * 建议采购量
     */
    private Double suggestedQuantity;

    /**
     * 单位
     */
    private String unit;

    /**
     * 当前库存量
     */
    private Double currentStock;

    /**
     * 缺口量（需要采购的量）
     */
    private Double gapQuantity;

    /**
     * 保质期天数
     */
    private Integer shelfLifeDays;

    /**
     * 存储方式
     */
    private String storageMethod;

    /**
     * 保质期提示
     */
    private String shelfLifeTip;

    /**
     * 是否必买（true-必买，false-可选）
     */
    private Boolean mustBuy;

    /**
     * 用于食谱列表（哪些食谱需要这个食材）
     */
    private String usedInRecipes;

    /**
     * 优先级（1-高，2-中，3-低）
     */
    private Integer priority;

    public ShoppingItemDTO() {
    }

    public Long getShicaiId() {
        return shicaiId;
    }

    public void setShicaiId(Long shicaiId) {
        this.shicaiId = shicaiId;
    }

    public String getShicaiName() {
        return shicaiName;
    }

    public void setShicaiName(String shicaiName) {
        this.shicaiName = shicaiName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getSuggestedQuantity() {
        return suggestedQuantity;
    }

    public void setSuggestedQuantity(Double suggestedQuantity) {
        this.suggestedQuantity = suggestedQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Double currentStock) {
        this.currentStock = currentStock;
    }

    public Double getGapQuantity() {
        return gapQuantity;
    }

    public void setGapQuantity(Double gapQuantity) {
        this.gapQuantity = gapQuantity;
    }

    public Integer getShelfLifeDays() {
        return shelfLifeDays;
    }

    public void setShelfLifeDays(Integer shelfLifeDays) {
        this.shelfLifeDays = shelfLifeDays;
    }

    public String getStorageMethod() {
        return storageMethod;
    }

    public void setStorageMethod(String storageMethod) {
        this.storageMethod = storageMethod;
    }

    public String getShelfLifeTip() {
        return shelfLifeTip;
    }

    public void setShelfLifeTip(String shelfLifeTip) {
        this.shelfLifeTip = shelfLifeTip;
    }

    public Boolean getMustBuy() {
        return mustBuy;
    }

    public void setMustBuy(Boolean mustBuy) {
        this.mustBuy = mustBuy;
    }

    public String getUsedInRecipes() {
        return usedInRecipes;
    }

    public void setUsedInRecipes(String usedInRecipes) {
        this.usedInRecipes = usedInRecipes;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
