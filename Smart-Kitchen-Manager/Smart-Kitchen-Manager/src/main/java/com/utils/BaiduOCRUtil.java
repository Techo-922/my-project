package com.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * 百度OCR工具类 - 识别购物小票
 */
public class BaiduOCRUtil {
    
    // 百度OCR API配置（需要在百度AI开放平台申请）
    private static final String API_KEY = "k9wMFIoeUIboO6rgi7D2Fdq1";  // 替换为你的API Key
    private static final String SECRET_KEY = "dvwT3Iqz0dvHcmP4tjv05BwPcG22DJzU";  // 替换为你的Secret Key
    
    // 获取access_token
    private static String getAccessToken() throws IOException {
        String url = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials"
                + "&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY;
        
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        
        HttpResponse response = httpClient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = JSONObject.parseObject(result);
        
        httpClient.close();
        return jsonObject.getString("access_token");
    }
    
    /**
     * 识别购物小票
     * @param imageBase64 图片的Base64编码
     * @return 识别结果
     */
    public static JSONObject recognizeReceipt(String imageBase64) {
        try {
            String accessToken = getAccessToken();
            String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/receipt?access_token=" + accessToken;
            
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            
            // 构建请求参数
            String params = "image=" + URLEncoder.encode(imageBase64, "UTF-8");
            httpPost.setEntity(new StringEntity(params));
            
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            
            httpClient.close();
            return JSONObject.parseObject(result);
            
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject error = new JSONObject();
            error.put("error", "OCR识别失败: " + e.getMessage());
            return error;
        }
    }
    
    /**
     * 通用文字识别（用于识别普通小票）
     * @param imageBase64 图片的Base64编码
     * @return 识别结果
     */
    public static JSONObject recognizeGeneral(String imageBase64) {
        try {
            String accessToken = getAccessToken();
            String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic?access_token=" + accessToken;
            
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            
            String params = "image=" + URLEncoder.encode(imageBase64, "UTF-8");
            httpPost.setEntity(new StringEntity(params));
            
            HttpResponse response = httpClient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            
            httpClient.close();
            return JSONObject.parseObject(result);
            
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject error = new JSONObject();
            error.put("error", "OCR识别失败: " + e.getMessage());
            return error;
        }
    }
    
    /**
     * 解析小票文本，提取食材信息
     * @param ocrResult OCR识别结果
     * @return 食材列表
     */
    public static List<ReceiptItem> parseReceiptItems(JSONObject ocrResult) {
        List<ReceiptItem> items = new ArrayList<>();
        
        if (ocrResult.containsKey("words_result")) {
            JSONArray wordsResult = ocrResult.getJSONArray("words_result");
            
            for (int i = 0; i < wordsResult.size(); i++) {
                JSONObject line = wordsResult.getJSONObject(i);
                String text = line.getString("words");
                
                // 解析每一行，尝试提取商品名称、数量、价格
                ReceiptItem item = parseLineToItem(text);
                if (item != null) {
                    items.add(item);
                }
            }
        }
        
        return items;
    }
    
    /**
     * 解析单行文本为食材项
     */
    private static ReceiptItem parseLineToItem(String line) {
        // 简单的解析逻辑，可以根据实际小票格式优化
        // 示例格式: "西红柿 500g 12.50"
        
        // 跳过非商品行
        if (line.contains("合计") || line.contains("小计") || 
            line.contains("找零") || line.contains("收款") ||
            line.length() < 3) {
            return null;
        }
        
        ReceiptItem item = new ReceiptItem();
        
        // 使用正则表达式提取信息
        // 提取数字（可能是数量或价格）
        String[] parts = line.split("\\s+");
        
        if (parts.length >= 1) {
            item.setName(parts[0]);  // 第一部分作为商品名
            
            // 尝试提取数量和单位
            for (String part : parts) {
                if (part.matches("\\d+(\\.\\d+)?[克千斤g].*")) {
                    item.setQuantityText(part);
                } else if (part.matches("\\d+(\\.\\d+)?")) {
                    // 可能是价格
                    try {
                        item.setPrice(Double.parseDouble(part));
                    } catch (Exception e) {
                        // 忽略
                    }
                }
            }
            
            return item;
        }
        
        return null;
    }
    
    /**
     * 小票商品项
     */
    public static class ReceiptItem {
        private String name;           // 商品名称
        private String quantityText;   // 数量文本（如"500g"）
        private Integer quantity;      // 数量数值
        private String unit;           // 单位
        private Double price;          // 价格
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getQuantityText() {
            return quantityText;
        }
        
        public void setQuantityText(String quantityText) {
            this.quantityText = quantityText;
            parseQuantity(quantityText);
        }
        
        public Integer getQuantity() {
            return quantity;
        }
        
        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
        
        public String getUnit() {
            return unit;
        }
        
        public void setUnit(String unit) {
            this.unit = unit;
        }
        
        public Double getPrice() {
            return price;
        }
        
        public void setPrice(Double price) {
            this.price = price;
        }
        
        // 解析数量文本
        private void parseQuantity(String text) {
            if (text == null) return;
            
            // 提取数字
            String numStr = text.replaceAll("[^0-9.]", "");
            try {
                double num = Double.parseDouble(numStr);
                
                // 判断单位
                if (text.contains("kg") || text.contains("千克") || text.contains("公斤")) {
                    this.quantity = (int)(num * 1000);  // 转换为克
                    this.unit = "克";
                } else if (text.contains("g") || text.contains("克")) {
                    this.quantity = (int)num;
                    this.unit = "克";
                } else if (text.contains("斤")) {
                    this.quantity = (int)(num * 500);  // 1斤=500克
                    this.unit = "克";
                } else {
                    this.quantity = (int)num;
                    this.unit = "个";
                }
            } catch (Exception e) {
                this.quantity = 1;
                this.unit = "个";
            }
        }
    }
}
