package com.oruit.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

public class SimpleResponseUtil {

    public static StandardResult getStandardResultByCode(ResponseCode responseCode, Object datas) {
        return HttpResponseUtil.getStandardResultByCode(responseCode, datas, ResponseLanguage.getThreadLanguage());
    }

    public static StandardResult getStandardResultByCode(ResponseCode responseCode) {
        return getStandardResultByCode(responseCode, null);
    }

    public static Map<String, Object> getResponseMapByCode(ResponseCode responseCode, Object datas) {
        return HttpResponseUtil.getResponseMapByCode(responseCode, datas, ResponseLanguage.getThreadLanguage());
    }

    public static Map<String, Object> getResponseMapByCode(ResponseCode responseCode) {
        return getResponseMapByCode(responseCode, null);
    }

    public static void sendJson(String content) {
        HttpResponseUtil.sendJson(HttpCommonUtil.getThreadResponse(), content);
    }

    public static void sendJsonByCode(ResponseCode responseCode, Object datas) {
        HttpResponseUtil.sendJsonByCode(HttpCommonUtil.getThreadResponse(), responseCode, datas,
                ResponseLanguage.getThreadLanguage());
    }

    public static void sendJsonByCode(ResponseCode responseCode) {
        sendJsonByCode(responseCode, null);
    }

    public static void sendSuccessJson(Object datas) {
        sendJsonByCode(ResponseCode.SUCCESS_0, datas);
    }

    public static void sendSuccessJson() {
        sendSuccessJson(null);
    }

    /**
     * <b>注意:paramNames必须与paramValues一一对应</b>
     *
     * @param paramNames
     * @param paramValues
     * @return Map<String,Object>
     * @throws Exception
     */
    public static Map<String, Object> getMapForNecessaryParams(String[] paramNames, Object... paramValues) throws Exception {
        return ParamCheckResponseUtil.getMapForNecessaryParams(paramNames, paramValues);
    }

    public static Map<String, Object> getMapForNecessaryParams(String... paramNames) throws Exception {
        return ParamCheckResponseUtil.getMapForNecessaryParams(paramNames);
    }

    public static Map<String, Object> getMapForWrongParam(String... paramNames) throws IOException {
        return ParamCheckResponseUtil.getMapForWrongParam(paramNames);
    }

    public static void sendForParamWrong(String... paramNames) throws Exception {
        Map<String, Object> map = getMapForWrongParam(paramNames);
        if (map == null) {
            throw new Exception("无错误参数");
        }
        sendJson(JSONObject.toJSONString(map));
    }

    public static void sendStream(Map<String, String> headers, String contentType, InputStream is) {
        HttpServletResponse response = HttpCommonUtil.getThreadResponse();
        if (!HttpCommonUtil.isEmptyMap(headers)) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                response.addHeader(key, headers.get(key));
            }
        }
        if (!HttpCommonUtil.isEmpty(contentType)) {
            response.setContentType(contentType);
        }

        if (headers != null && headers.get("Content-Type") != null) {
            response.setContentType(headers.get("Content-Type"));
        }
        if (is != null) {
            /*
             * try { File file = new File("F:/tmp/Event.xlsx");
             * if(!file.exists()){ file.createNewFile(); }
             * 
             * FileOutputStream out = new FileOutputStream(file); byte[] buffer
             * = new byte[4096]; int readLength = 0; while
             * ((readLength=is.read(buffer)) > 0) { byte[] bytes = new
             * byte[readLength]; System.arraycopy(buffer, 0, bytes, 0,
             * readLength); out.write(bytes); } out.flush(); out.close(); }catch
             * (Exception e) { e.printStackTrace(); }
             */

            try {
                OutputStream out = response.getOutputStream();
                // 创建缓冲区
                byte buffer[] = new byte[1024];
                int len = 0;
                // 循环将输入流中的内容读取到缓冲区当中
                while ((len = is.read(buffer)) > 0) {
                    // 输出缓冲区的内容到浏览器，实现文件下载
                    out.write(buffer, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
    }

}
