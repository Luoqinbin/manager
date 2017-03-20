package com.badminton.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

/**
 * http 工具类
 */
public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static String doGet(String url, List<NameValuePair> params) throws Exception {
        logger.info("HttpUtils doGet 方法请求开始");
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        String body = null;
        // Get请求
        HttpGet httpget = new HttpGet(url);
        // 设置参数
        String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
        httpget.setURI(new URI(httpget.getURI().toString() + "?" + str));
        // 发送请求
        CloseableHttpResponse response = httpclient.execute(httpget);
        logger.info("HttpUtils doGet StatusCode -> " + response.getStatusLine().getStatusCode());

        // 获取返回数据
        HttpEntity entity = response.getEntity();
        body = EntityUtils.toString(entity);
        httpget.releaseConnection();

        return body;
    }


    public static void doPost(String url, List<NameValuePair> params) throws Exception {
        logger.info("HttpUtils doPost  开始请求");
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();

        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse response = httpclient.execute(httppost);
        logger.info("HttpUtils doPost StatusCode -> " + response.getStatusLine().getStatusCode());

        HttpEntity entity = response.getEntity();
        String jsonStr = EntityUtils.toString(entity, "utf-8");
        logger.info("HttpUtils doPost 返回结果 -> " + jsonStr);
        httppost.releaseConnection();
    }

    public static String doDelete(String url, String authorization) throws Exception {
        logger.info("HttpUtils doDelete  开始请求 url ==>"+url);
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();

        HttpDelete httpDelete = new HttpDelete(url);
        if(StringUtils.isNotEmpty(authorization)){
            httpDelete.setHeader("Authorization",authorization);
        }
        httpDelete.setHeader("Accept", "application/json");
        CloseableHttpResponse response = httpclient.execute(httpDelete);
        if (response != null) {
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("HttpUtils doPost StatusCode -> " + statusCode);
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(entity, "utf-8");
                logger.info("HttpUtils doPost 返回结果 -> " + jsonStr);
                httpDelete.releaseConnection();
                return jsonStr;
            }
        }
        return null;

    }
    public static String doGet(String url, String authorization) throws Exception {
        logger.info("HttpUtils doGet  开始请求 url ==>"+url);
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        String body = null;
        // Get请求
        HttpGet httpget = new HttpGet(url);
        if(StringUtils.isNotEmpty(authorization)){
            httpget.setHeader("Authorization",authorization);
        }
        httpget.setHeader("Accept", "application/json");
        // 设置参数
        // 发送请求
        CloseableHttpResponse response = httpclient.execute(httpget);
        if (response != null) {
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("HttpUtils doGet StatusCode -> " + statusCode);
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                String jsonStr = EntityUtils.toString(entity, "utf-8");
                logger.info("HttpUtils doGet 返回结果 -> " + jsonStr);
                httpget.releaseConnection();
                return jsonStr;
            }
        }
        return null;
    }


}
