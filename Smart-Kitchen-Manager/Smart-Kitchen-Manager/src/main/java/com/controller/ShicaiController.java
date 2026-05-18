package com.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utils.R;

/**
 * 食材基础信息
 * 后端接口
 */
@RestController
@RequestMapping("/shicai")
public class ShicaiController {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 查询所有食材列表
     */
    @RequestMapping("/list")
    public R list() {
        try {
            String sql = "SELECT id, shicai_name as shicaiName, category FROM shicai ORDER BY id";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
            return R.ok().put("data", list);
        } catch (Exception e) {
            return R.error("查询食材列表失败");
        }
    }
}
