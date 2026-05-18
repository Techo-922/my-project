package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 食材保质期参考库
 * 
 * @author 
 * @date 2023-04-25
 */
@TableName("shicai_shelf_life")
public class ShicaiShelfLifeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 食材ID
     */
    private Long shicaiId;
    
    /**
     * 食材名称（冗余字段，方便查询）
     */
    private String shicaiName;
    
    /**
     * 食材分类（冗余字段，方便查询）
     */
    private String category;

    /**
     * 保质期天数
     */
    private Integer shelfLifeDays;

    /**
     * 存储方式
     */
    private String storageMethod;

    /**
     * 添加时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public ShicaiShelfLifeEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
