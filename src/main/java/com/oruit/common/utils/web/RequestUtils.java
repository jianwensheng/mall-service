package com.oruit.common.utils.web;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RequestUtils {

    private static final Logger logger = LoggerFactory.getLogger(RequestUtils.class);

    private static final SerializerFeature[] features = {SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteMapNullValue};
    public static String UID = "1487642975";
    public static String INFVERSION = "5.3";
    public static String KEY = "XyVW5Td+f7QT10yMqmj36mlQsnJQs5kDxYsJLbIRJw6OTbNNhlHsiw==";

    public static String getBasePath(HttpServletRequest request) {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + path;
        return basePath;
    }

    /**
     * 获取项目的全路径
     *
     * @param request
     * @return
     */
    public static String getPath(HttpServletRequest request) {
        String realPath = request.getSession().getServletContext().getRealPath("/");
        return realPath;
    }

    /**
     * 获取temp目录的全路径
     *
     * @param request
     * @return
     */
    public static String getTempPath(HttpServletRequest request) {
        String path = getPath(request) + "temp/";
        return path;
    }

    /**
     * 获取设备信息
     *
     * @param request
     * @return
     */
    public static String getDevCode(HttpServletRequest request) {
        //获取设备信息
        String devCode = null;
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null) {
            String[] str = userAgent.split("/");
            if (str.length > 8) {
                devCode = str[7];
            }
        }
        return devCode;
    }

    /**
     * 获取request请求的参数内容
     *
     * @param request
     * @param paramName
     * @return
     */
    public static String getRequestParam(HttpServletRequest request, String paramName) throws UnsupportedEncodingException {
        if (!StringUtils.isBlank(request.getParameter(paramName))) {
            return request.getParameter(paramName);
        } else {
            return "";
        }
    }

    /**
     * 获取完整路径
     *
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getRequestFullUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        // 完整链接路径
        String strURL = request.getRequestURL().toString();
        if (!StringUtils.isBlank(request.getQueryString())) {
            strURL += "?" + request.getQueryString();
        }
        return strURL;
    }

    public static void requestPost(String urlPath, Map<String, String> mapParam) throws MalformedURLException, IOException {
        baserequestPost(urlPath, mapParam);
    }

    public static String baserequestPost(String urlPath, Map<String, String> mapParam) {
        String strParams = "";
        for (Map.Entry<String, String> entry : mapParam.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            strParams += key + "=" + value + "&";
        }
        return baseRequestPostStringParams(urlPath, strParams);
    }

    public static String baseRequestPostStringParams(String urlPath, String strParams) {
        HttpURLConnection con = null;

        try {
            URL url = new URL(urlPath);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(1000);
            // post请求必须设置下面两项
            con.setDoOutput(true);
            con.setDoInput(true);
            // 不使用缓存
            con.setUseCaches(false);

            // 设置自定义的请求头，也可以用这个方法得到发送数据
            // 这句是打开链接
            OutputStream out = con.getOutputStream();

            // 把数据写到报文
            out.write(strParams.getBytes());

            if(logger.isDebugEnabled()){ logger.debug("mapParam:" + strParams); }

            out.flush();
            out.close();
            // 这句才是真正发送请求
            InputStream in = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String response = "";
            String readLine = null;
            while ((readLine = br.readLine()) != null) {
                response = response + readLine;
            }
            in.close();
            br.close();
            if(logger.isDebugEnabled()){ logger.debug("response:" + response); }
            return response;
        } catch (Exception e) {
            return "";
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    public static String baseRequestPostJSONParams(String urlPath, String Params) {
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        String response = "";
        try {
            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(urlPath);
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //POST请求
            out = new OutputStreamWriter(
                    conn.getOutputStream());
            out.write(Params);
            out.flush();
            //读取响应
            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                response += lines;
            }
            reader.close();
            // 断开连接
            conn.disconnect();

        } catch (Exception e) {
            if(logger.isDebugEnabled()){ logger.debug("发送 POST 请求出现异常！" + e); }
            e.printStackTrace();
        } //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return response;
    }

    public static String baseRequestPostJSONParamsNoReader(String urlPath, String Params) {
        OutputStreamWriter out = null;
        String response = "";
        try {
            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(urlPath);
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setConnectTimeout(2000);
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //POST请求
            out = new OutputStreamWriter(
                    conn.getOutputStream());
            // 向对象输出流写出数据，这些数据将存到内存缓冲区中
            out.write(Params);
            // 刷新对象输出流，将任何字节都写入潜在的流中（些处为ObjectOutputStream）
            out.flush();
            InputStream xx = conn.getInputStream();

            // 断开连接
            conn.disconnect();

        } catch (Exception e) {
            if(logger.isDebugEnabled()){ logger.debug("发送 POST 请求出现异常！" + e); }
            e.printStackTrace();
        } //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    // 关闭流对象。此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中,
                    // 在调用下边的getInputStream()函数时才把准备好的http请求正式发送到服务器
                    out.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return response;
    }

    public static String baseRequestGet(String url) {

        StringBuffer resultBuffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;

        try {
            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(url);
            //建立连接
            HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();

            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setConnectTimeout(3000);


            String tempLine = null;

            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }

            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);

            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }

        } catch (Exception e) {
            if(logger.isDebugEnabled()){ logger.debug("发送 baseRequestGet 请求出现异常！" + e); }
            e.printStackTrace();
        } //使用finally块来关闭输出流、输入流
        finally {

            try {
                if (reader != null) {
                    reader.close();
                }

                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        return resultBuffer.toString();
    }

    public static void main(String[] args) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("userid", "632");
        params.put("rewardtype", "4");
        params.put("fromuser", "637");
        params.put("gold", "0");
        params.put("Infversion", "6.2");
        params.put("Key", KEY);
        params.put("Method", "RewardCommon");
        params.put("UID", "1500724505");
        if(logger.isDebugEnabled()){ logger.debug(RequestUtils.baseRequestPostJSONParams("http://ggzservice.oruit.net/app-http/api", JSONObject.toJSONString(params, features))); }
    }


}
