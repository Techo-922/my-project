package com.dto;

import java.io.Serializable;

/**
 * 用户偏好设置DTO
 * 用于封装用户的健康偏好和过敏信息
 * 
 * @author 
 * @date 2023-04-25
 */
public class UserPreferenceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 健康偏好设置（JSON格式字符串）
     * 示例：{"dietType":"vegetarian","nutrientGoal":{"protein":50,"carb":150},"flavor":"spicy"}
     */
    private String healthPreference;

    /**
     * 过敏信息（逗号分隔）
     * 示例：花生,牛奶,海鲜
     */
    private String allergyInfo;

    public UserPreferenceDTO() {
    }

    public UserPreferenceDTO(Long userId, String healthPreference, String allergyInfo) {
        this.userId = userId;
        this.healthPreference = healthPreference;
        this.allergyInfo = allergyInfo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getHealthPreference() {
        return healthPreference;
    }

    public void setHealthPreference(String healthPreference) {
        this.healthPreference = healthPreference;
    }

    public String getAllergyInfo() {
        return allergyInfo;
    }

    public void setAllergyInfo(String allergyInfo) {
        this.allergyInfo = allergyInfo;
    }
}
