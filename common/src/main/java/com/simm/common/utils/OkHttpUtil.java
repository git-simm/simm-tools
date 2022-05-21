package com.simm.common.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.exception.ExceptionUtils;
import okhttp3.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * @author miscr
 */
@Component
@Slf4j
public class OkHttpUtil {
    @Resource
    private OkHttpClient okHttpClient;

    /**
     * get
     *
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @param headers 传入请求头
     * @return
     */
    public String get(String url, Map<String, String> queries, Map<String, String> headers) {
        StringBuffer sb = new StringBuffer(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        Request.Builder builder = new Request
                .Builder()
                .url(sb.toString());
        if (headers != null) {
            builder.headers(Headers.of(headers));
        }
        Request request = builder.build();
        return execute(request);
    }

    /**
     * post
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return
     */
    public String post(String url, Object params,Map<String, String> headers) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(params));
        Request.Builder builder = new Request
                .Builder()
                .url(url)
                .post(requestBody);
        if (headers != null) {
            builder.headers(Headers.of(headers));
        }
        Request request = builder.build();
        return execute(request);
    }

    /**
     * post
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return
     */
    public String put(String url, Object params,Map<String, String> headers) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(params));
        Request.Builder builder = new Request
                .Builder()
                .url(url)
                .put(requestBody);
        if (headers != null) {
            builder.headers(Headers.of(headers));
        }
        Request request = builder.build();
        return execute(request);
    }

    /**
     * post 上传文件
     *
     * @param url
     * @param params
     * @param fileType
     * @return
     */
    public String postFile(String url, Map<String, Object> params, String fileType) {
        String responseBody = "";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                if (params.get(key) instanceof File) {
                    File file = (File) params.get(key);
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(fileType), file));
                    continue;
                }
                builder.addFormDataPart(key, params.get(key).toString());
            }
        }
        Request request = new Request
                .Builder()
                .url(url)
                .post(builder.build())
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            int status = response.code();
            if (status == 200) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error("okhttp postFile error >> ex = {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }

    /**
     * 执行方法
     * @param request 请求
     * @return
     */
    public String execute(Request request) {
        String responseBody = "";
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            int status = response.code();
            if (status == 200) {
                return response.body().string();
            }
        } catch (Exception e) {
            log.error("okhttp post error >> ex = {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }
}