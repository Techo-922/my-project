package com.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 采购清单响应DTO
 * 
 * @author 
 * @date 2023-04-25
 */
public class ShoppingListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 采购清单项列表
     */
    private List<ShoppingItemDTO> items;

    /**
     * 按分类分组的采购清单
     * key: 分类名称（蔬菜、肉类、调料等）
     * value: 该分类下的采购项列表
     */
    private Map<String, List<ShoppingItemDTO>> itemsByCategory;

    /**
     * 总计项数
     */
    private Integer totalItems;

    /**
     * 必买项数
     */
    private Integer mustBuyItems;

    /**
     * 可选项数
     */
    private Integer optionalItems;

    /**
     * 生成时间
     */
    private String generateTime;

    /**
     * 目标食谱列表
     */
    private List<String> targetRecipes;

    public ShoppingListResponse() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ShoppingItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ShoppingItemDTO> items) {
        this.items = items;
    }

    public Map<String, List<ShoppingItemDTO>> getItemsByCategory() {
        return itemsByCategory;
    }

    public void setItemsByCategory(Map<String, List<ShoppingItemDTO>> itemsByCategory) {
        this.itemsByCategory = itemsByCategory;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Integer getMustBuyItems() {
        return mustBuyItems;
    }

    public void setMustBuyItems(Integer mustBuyItems) {
        this.mustBuyItems = mustBuyItems;
    }

    public Integer getOptionalItems() {
        return optionalItems;
    }

    public void setOptionalItems(Integer optionalItems) {
        this.optionalItems = optionalItems;
    }

    public String getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(String generateTime) {
        this.generateTime = generateTime;
    }

    public List<String> getTargetRecipes() {
        return targetRecipes;
    }

    public void setTargetRecipes(List<String> targetRecipes) {
        this.targetRecipes = targetRecipes;
    }
}
