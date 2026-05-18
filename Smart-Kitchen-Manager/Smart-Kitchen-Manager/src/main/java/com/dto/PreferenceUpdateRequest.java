package com.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户偏好更新请求DTO
 * 
 * @author 
 * @date 2023-04-25
 */
public class PreferenceUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID（必填）
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 健康偏好设置（JSON格式字符串）
     */
    private String healthPreference;

    /**
     * 过敏信息（逗号分隔）
     */
    private String allergyInfo;

    public PreferenceUpdateRequest() {
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
