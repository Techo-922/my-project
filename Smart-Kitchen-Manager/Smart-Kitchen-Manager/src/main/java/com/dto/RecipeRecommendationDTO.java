package com.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 食谱推荐响应DTO
 * 用于封装智能推荐接口的返回数据
 * 
 * @author 
 * @date 2023-04-25
 */
public class RecipeRecommendationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 食谱ID
     */
    private Long id;

    /**
     * 食谱名称
     */
    private String caipumingcheng;

    /**
     * 食谱封面
     */
    private String caipufengmian;

    /**
     * 菜式类型
     */
    private String caishileixing;

    /**
     * 烹饪方式
     */
    private String pengrenfangshi;

    /**
     * 分数
     */
    private Integer fenshu;

    /**
     * 制作流程
     */
    private String zhizuoliucheng;

    /**
     * 匹配度（0-100）
     */
    private Double matchRate;

    /**
     * 是否高优先级（匹配度≥60%）
     */
    private Boolean highPriority;

    /**
     * 所需食材列表
     */
    private List<String> requiredIngredients;

    /**
     * 缺失食材列表
     */
    private List<String> missingIngredients;

    /**
     * 缺失食材数量
     */
    private Integer missingCount;

    public RecipeRecommendationDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaipumingcheng() {
        return caipumingcheng;
    }

    public void setCaipumingcheng(String caipumingcheng) {
        this.caipumingcheng = caipumingcheng;
    }

    public String getCaipufengmian() {
        return caipufengmian;
    }

    public void setCaipufengmian(String caipufengmian) {
        this.caipufengmian = caipufengmian;
    }

    public String getCaishileixing() {
        return caishileixing;
    }

    public void setCaishileixing(String caishileixing) {
        this.caishileixing = caishileixing;
    }

    public String getPengrenfangshi() {
        return pengrenfangshi;
    }

    public void setPengrenfangshi(String pengrenfangshi) {
        this.pengrenfangshi = pengrenfangshi;
    }

    public Integer getFenshu() {
        return fenshu;
    }

    public void setFenshu(Integer fenshu) {
        this.fenshu = fenshu;
    }

    public String getZhizuoliucheng() {
        return zhizuoliucheng;
    }

    public void setZhizuoliucheng(String zhizuoliucheng) {
        this.zhizuoliucheng = zhizuoliucheng;
    }

    public Double getMatchRate() {
        return matchRate;
    }

    public void setMatchRate(Double matchRate) {
        this.matchRate = matchRate;
    }

    public Boolean getHighPriority() {
        return highPriority;
    }

    public void setHighPriority(Boolean highPriority) {
        this.highPriority = highPriority;
    }

    public List<String> getRequiredIngredients() {
        return requiredIngredients;
    }

    public void setRequiredIngredients(List<String> requiredIngredients) {
        this.requiredIngredients = requiredIngredients;
    }

    public List<String> getMissingIngredients() {
        return missingIngredients;
    }

    public void setMissingIngredients(List<String> missingIngredients) {
        this.missingIngredients = missingIngredients;
    }

    public Integer getMissingCount() {
        return missingCount;
    }

    public void setMissingCount(Integer missingCount) {
        this.missingCount = missingCount;
    }
}
