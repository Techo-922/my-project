package com.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户偏好初始化请求DTO
 * 用于向导式设置用户偏好
 * 
 * @author 
 * @date 2023-04-25
 */
public class PreferenceInitRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID（必填）
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 饮食类型
     * 可选值：normal(普通)、vegetarian(素食)、vegan(纯素)、low-carb(低碳水)
     */
    private String dietType;

    /**
     * 蛋白质目标（克/天）
     */
    private Integer proteinGoal;

    /**
     * 碳水化合物目标（克/天）
     */
    private Integer carbGoal;

    /**
     * 脂肪目标（克/天）
     */
    private Integer fatGoal;

    /**
     * 口味偏好
     * 可选值：mild(清淡)、spicy(辛辣)、sweet(甜味)、salty(咸味)
     */
    private String flavor;

    /**
     * 过敏信息（逗号分隔）
     */
    private String allergyInfo;

    public PreferenceInitRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDietType() {
        return dietType;
    }

    public void setDietType(String dietType) {
        this.dietType = dietType;
    }

    public Integer getProteinGoal() {
        return proteinGoal;
    }

    public void setProteinGoal(Integer proteinGoal) {
        this.proteinGoal = proteinGoal;
    }

    public Integer getCarbGoal() {
        return carbGoal;
    }

    public void setCarbGoal(Integer carbGoal) {
        this.carbGoal = carbGoal;
    }

    public Integer getFatGoal() {
        return fatGoal;
    }

    public void setFatGoal(Integer fatGoal) {
        this.fatGoal = fatGoal;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getAllergyInfo() {
        return allergyInfo;
    }

    public void setAllergyInfo(String allergyInfo) {
        this.allergyInfo = allergyInfo;
    }
}
