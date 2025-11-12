package com.mbpay.util;

import com.mbpay.model.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 工具类
 */
public class HttpUtil {
    private static final Gson gson = new GsonBuilder().create();

    /**
     * SHA256 哈希并转为小写 hex
     *
     * @param input 输入字符串
     * @return 哈希值（小写 hex）
     */
    public static String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA256 hash failed", e);
        }
    }

    /**
     * Base64 编码
     *
     * @param input 输入字符串
     * @return Base64 编码字符串
     */
    public static String base64Encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * URL 编码
     *
     * @param input 输入字符串
     * @return URL 编码字符串
     */
    public static String urlEncode(String input) {
        try {
            return URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            throw new RuntimeException("URL encode failed", e);
        }
    }

    /**
     * 对象转 JSON 字符串
     *
     * @param obj 对象
     * @return JSON 字符串
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * 解析响应 JSON
     *
     * @param jsonStr JSON 字符串
     * @return Response 对象
     */
    public static Response parseResponse(String jsonStr) {
        Map<String, Object> result = gson.fromJson(jsonStr, new TypeToken<Map<String, Object>>() {}.getType());
        int code = ((Double) result.getOrDefault("code", 0.0)).intValue();
        String message = String.valueOf(result.getOrDefault("message", ""));
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) result.getOrDefault("data", new HashMap<>());
        return new Response(code, message, data);
    }

    /**
     * GET 请求
     *
     * @param url 请求 URL
     * @param params 请求参数
     * @param timeout 超时时间（毫秒）
     * @return 响应体
     * @throws Exception
     */
    public static String get(String url, Map<String, String> params, int timeout) throws Exception {
        // 构建查询字符串
        if (params != null && !params.isEmpty()) {
            StringBuilder queryString = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (queryString.length() > 0) {
                    queryString.append("&");
                }
                queryString.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            }
            if (url.contains("?")) {
                url += "&" + queryString.toString();
            } else {
                url += "?" + queryString.toString();
            }
        }

        URL urlObj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        conn.setDoInput(true);

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("HTTP status " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        return response.toString();
    }

    /**
     * POST 请求
     *
     * @param url 请求 URL
     * @param params 请求参数
     * @param timeout 超时时间（毫秒）
     * @return 响应体
     * @throws Exception
     */
    public static String post(String url, Map<String, String> params, int timeout) throws Exception {
        URL urlObj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // 构建请求体
        if (params != null && !params.isEmpty()) {
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (postData.length() > 0) {
                    postData.append("&");
                }
                postData.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            }

            OutputStream os = conn.getOutputStream();
            os.write(postData.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("HTTP status " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        return response.toString();
    }
}






