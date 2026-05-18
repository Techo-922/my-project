package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.utils.BaiduOCRUtil;
import com.utils.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * OCR识别控制器
 */
@RestController
@RequestMapping("/ocr")
public class OCRController {
    
    /**
     * 识别购物小票
     */
    @RequestMapping("/recognizeReceipt")
    public R recognizeReceipt(@RequestParam("file") MultipartFile file) {
        try {
            // 将图片转换为Base64
            byte[] bytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            
            // 调用OCR识别
            JSONObject ocrResult = BaiduOCRUtil.recognizeGeneral(base64Image);
            
            // 解析识别结果
            if (ocrResult.containsKey("error")) {
                return R.error(ocrResult.getString("error"));
            }
            
            // 提取食材信息
            List<BaiduOCRUtil.ReceiptItem> items = BaiduOCRUtil.parseReceiptItems(ocrResult);
            
            return R.ok().put("items", items).put("ocrResult", ocrResult);
            
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("图片处理失败: " + e.getMessage());
        }
    }
    
    /**
     * 通用文字识别
     */
    @RequestMapping("/recognizeText")
    public R recognizeText(@RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            
            JSONObject ocrResult = BaiduOCRUtil.recognizeGeneral(base64Image);
            
            if (ocrResult.containsKey("error")) {
                return R.error(ocrResult.getString("error"));
            }
            
            return R.ok().put("data", ocrResult);
            
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("图片处理失败: " + e.getMessage());
        }
    }
    
    /**
     * Base64图片识别
     */
    @RequestMapping("/recognizeBase64")
    public R recognizeBase64(@RequestBody JSONObject params) {
        try {
            String base64Image = params.getString("image");
            
            // 移除Base64前缀（如果有）
            if (base64Image.contains(",")) {
                base64Image = base64Image.split(",")[1];
            }
            
            JSONObject ocrResult = BaiduOCRUtil.recognizeGeneral(base64Image);
            
            if (ocrResult.containsKey("error")) {
                return R.error(ocrResult.getString("error"));
            }
            
            List<BaiduOCRUtil.ReceiptItem> items = BaiduOCRUtil.parseReceiptItems(ocrResult);
            
            return R.ok().put("items", items).put("ocrResult", ocrResult);
            
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("识别失败: " + e.getMessage());
        }
    }
}
