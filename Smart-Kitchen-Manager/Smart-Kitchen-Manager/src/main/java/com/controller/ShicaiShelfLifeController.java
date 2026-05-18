package com.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ShicaiShelfLifeEntity;
import com.service.ShicaiShelfLifeService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

/**
 * 食材保质期参考库
 * 后端接口
 * @author 
 * @email 
 * @date 2024-12-30
 */
@RestController
@RequestMapping("/shicaishelflife")
public class ShicaiShelfLifeController {
    
    @Autowired
    private ShicaiShelfLifeService shicaiShelfLifeService;

    /**
     * 分页查询保质期参考库
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ShicaiShelfLifeEntity shicaiShelfLife,
                  HttpServletRequest request) {
        
        // 过滤空字符串参数,避免查询条件错误
        if (shicaiShelfLife.getShicaiName() != null && shicaiShelfLife.getShicaiName().trim().isEmpty()) {
            shicaiShelfLife.setShicaiName(null);
        }
        if (shicaiShelfLife.getCategory() != null && shicaiShelfLife.getCategory().trim().isEmpty()) {
            shicaiShelfLife.setCategory(null);
        }
        
        // 构建查询条件
        EntityWrapper<ShicaiShelfLifeEntity> ew = new EntityWrapper<ShicaiShelfLifeEntity>();
        
        PageUtils page = shicaiShelfLifeService.queryPage(params, 
            MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shicaiShelfLife), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 根据食材ID查询保质期信息
     */
    @RequestMapping("/getByShicaiId/{shicaiId}")
    public R getByShicaiId(@PathVariable("shicaiId") Long shicaiId) {
        if (shicaiId == null) {
            return R.error("食材ID不能为空");
        }
        
        ShicaiShelfLifeEntity shelfLife = shicaiShelfLifeService.getByShicaiId(shicaiId);
        
        if (shelfLife == null) {
            return R.ok().put("data", null);
        }
        
        return R.ok().put("data", shelfLife);
    }

    /**
     * 查询详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        if (id == null) {
            return R.error("ID不能为空");
        }
        
        ShicaiShelfLifeEntity shelfLife = shicaiShelfLifeService.selectById(id);
        
        if (shelfLife == null) {
            return R.error(404, "保质期参考不存在");
        }
        
        return R.ok().put("data", shelfLife);
    }

    /**
     * 添加保质期参考
     */
    @RequestMapping("/save")
    public R save(@RequestBody ShicaiShelfLifeEntity shicaiShelfLife) {
        // 使用带验证的保存方法（如果已存在则更新）
        boolean success = shicaiShelfLifeService.saveWithValidation(shicaiShelfLife);
        
        if (success) {
            return R.ok("保存成功");
        } else {
            return R.error("保存失败，请检查数据是否有效");
        }
    }

    /**
     * 更新保质期参考
     */
    @RequestMapping("/update")
    public R update(@RequestBody ShicaiShelfLifeEntity shicaiShelfLife) {
        if (shicaiShelfLife.getId() == null) {
            return R.error("ID不能为空");
        }
        
        // 检查记录是否存在
        ShicaiShelfLifeEntity existing = shicaiShelfLifeService.selectById(shicaiShelfLife.getId());
        if (existing == null) {
            return R.error(404, "保质期参考不存在");
        }
        
        // 使用带验证的更新方法
        boolean success = shicaiShelfLifeService.updateWithValidation(shicaiShelfLife);
        
        if (success) {
            return R.ok("更新成功");
        } else {
            return R.error("更新失败，请检查数据是否有效");
        }
    }

    /**
     * 删除保质期参考
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        if (ids == null || ids.length == 0) {
            return R.error("请选择要删除的记录");
        }
        
        // 检查所有记录是否存在
        for (Long id : ids) {
            ShicaiShelfLifeEntity shelfLife = shicaiShelfLifeService.selectById(id);
            if (shelfLife == null) {
                return R.error("保质期参考ID " + id + " 不存在");
            }
        }
        
        // 执行删除
        shicaiShelfLifeService.deleteBatchIds(Arrays.asList(ids));
        
        return R.ok("删除成功");
    }

    /**
     * 查询所有保质期参考（不分页）
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        // 构建查询条件
        EntityWrapper<ShicaiShelfLifeEntity> ew = new EntityWrapper<ShicaiShelfLifeEntity>();
        
        // 如果有查询参数，添加查询条件
        if (params.get("shicaiId") != null) {
            ew.eq("shicai_id", params.get("shicaiId"));
        }
        if (params.get("storageMethod") != null) {
            ew.like("storage_method", params.get("storageMethod").toString());
        }
        
        // 按添加时间倒序排列
        ew.orderBy("addtime", false);
        
        List<ShicaiShelfLifeEntity> list = shicaiShelfLifeService.selectList(ew);
        
        return R.ok().put("data", list);
    }
}
