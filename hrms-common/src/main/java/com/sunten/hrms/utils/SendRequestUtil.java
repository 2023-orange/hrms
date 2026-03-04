package com.sunten.hrms.utils;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;

public class SendRequestUtil {
    public static String sendGetRequest(URI tagetUri, String payload, String JSessionId) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        StringEntity stringEntity = new StringEntity(payload, "UTF-8");
        stringEntity.setContentType("application/json");

        HttpGetWithEntity get =  new HttpGetWithEntity();
        get.setEntity(stringEntity);
//        "Cookie", "JSESSIONID=" + sessionId
        if (null != JSessionId) {
            get.setHeader("Cookie", "JSESSIONID=" + JSessionId);
        }
        System.out.println("entity=======" + get.getEntity().getContent());
        get.setURI(tagetUri);
        CloseableHttpResponse response = null;
        String resultString;
        try {
            // 执行http get请求
            response = httpClient.execute(get);
            // 判断返回状态是否为200
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
        return resultString;

    }

    public static String sendPostRequest(URI tagetUri, String payload, String JSessionId) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        StringEntity stringEntity = new StringEntity(payload, "UTF-8");
        stringEntity.setContentType("application/json");
        HttpPost post = new HttpPost();
        post.setEntity(stringEntity);
        if (null != JSessionId) {
            post.setHeader("Cookie", "JSESSIONID=" + JSessionId);
        }
        post.setURI(tagetUri);
        CloseableHttpResponse response = null;
        String resultString;
        try {
            // 执行http get请求
            response = httpClient.execute(post);
            // 判断返回状态是否为200
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
        return resultString;
    }
}
