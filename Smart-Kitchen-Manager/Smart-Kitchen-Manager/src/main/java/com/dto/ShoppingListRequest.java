package com.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 采购清单生成请求DTO
 * 
 * @author 
 * @date 2023-04-25
 */
public class ShoppingListRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID（必填）
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 目标食谱ID列表（可选，用户已选食谱）
     */
    private List<Long> recipeIds;

    /**
     * 补充库存阈值（默认30%，即剩余量<30%需补充）
     */
    private Double threshold = 0.3;

    /**
     * 是否包含安全库存（默认true）
     */
    private Boolean includeSafetyStock = true;

    /**
     * 安全库存系数（默认1.2，即在食谱用量基础上增加20%）
     */
    private Double safetyStockFactor = 1.2;

    public ShoppingListRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getRecipeIds() {
        return recipeIds;
    }

    public void setRecipeIds(List<Long> recipeIds) {
        this.recipeIds = recipeIds;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public Boolean getIncludeSafetyStock() {
        return includeSafetyStock;
    }

    public void setIncludeSafetyStock(Boolean includeSafetyStock) {
        this.includeSafetyStock = includeSafetyStock;
    }

    public Double getSafetyStockFactor() {
        return safetyStockFactor;
    }

    public void setSafetyStockFactor(Double safetyStockFactor) {
        this.safetyStockFactor = safetyStockFactor;
    }
}
