package com.oruit.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.ClientProtocolException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @version1.0.0
 * 
 * @author HJF
 * @date 2016/12/01
 * @description 对HttpRequestUtil.java 进行精简，并加入国际化功能，所有请求会加入设定语言的参数，<br>
 *              语言的选择顺序：当前线程中request中设定的语言-->项目设定的默认语言(classes目录先conf.properties
 *              文件中project.default.lenguage值)<br>
 *              -->浏览器语言-->简体中文
 *
 */
public class SimpleRequestUtil {

    @SuppressWarnings("unchecked")
    public static StandardResult getStandardResult(String url, Map<String, Object> params, Map<String, String> headers,
            String charset) throws IOException {
        return HttpRequestUtil.getStandardResult(url, setParamLanguage(params), headers, charset);
    }

    public static StandardResult getStandardResult(String url, Map<String, Object> params, Map<String, String> headers)
            throws IOException {
        return getStandardResult(url, params, headers, "UTF-8");
    }

    public static StandardResult getStandardResult(String url, Map<String, Object> params) throws IOException {
        return getStandardResult(url, params, null);
    }

    public static StandardResult getStandardResult(String url) throws IOException {
        return getStandardResult(url, null);
    }

    public static String getResponseText(String url) throws IOException {
        return getResponseText(url, null, "UTF-8");
    }

    public static String getResponseText(String url, Map<String, Object> params) throws IOException {
        return getResponseText(url, params, "UTF-8");
    }

    public static String getResponseText(String url, Map<String, Object> params, Map<String, String> headers) throws IOException {
        return getResponseText(url, params, headers, "UTF-8");
    }

    public static String getResponseText(String url, Map<String, Object> params, String charset) throws IOException {
        return getResponseText(url, params, null, charset);
    }

    @SuppressWarnings("unchecked")
    public static String getResponseText(String url, Map<String, Object> params, Map<String, String> headers, String charset)
            throws IOException {
        return HttpRequestUtil.getResponseText(url, setParamLanguage(params), headers, charset);
    }

    public static String postResponseText(String url, Map<String, String> params) throws IOException {
        return postResponseText(url, params, "UTF-8");
    }

    public static String postResponseText(String url, Map<String, String> params, Map<String, String> headers)
            throws IOException {
        return postResponseText(url, params, headers, "UTF-8");
    }

    public static String postResponseText(String url, Map<String, String> params, String charset) throws IOException {
        return postResponseText(url, params, null, charset);
    }
    
    public static String postRequestBody(String url, Object requestBody) throws ClientProtocolException, IOException {
    	return HttpRequestUtil.postRequestBody(url, requestBody);
    }
    
    public static StandardResult postRequestBodyForStandardResult(String url, Object requestBody) throws ClientProtocolException, IOException {
    	String result = HttpRequestUtil.postRequestBody(url, requestBody);
    	return JSONObject.parseObject(result, StandardResult.class);
    }

    @SuppressWarnings("unchecked")
    public static String postResponseText(String url, Map<String, String> params, Map<String, String> headers, String charset)
            throws IOException {
        return HttpRequestUtil.postResponseText(url, setParamLanguage(params), headers, charset);
    }

    @SuppressWarnings("unchecked")
    public static StandardResult postStandardResult(String url, Map<String, String> params, Map<String, String> headers,
            String charset) throws IOException {
        return HttpRequestUtil.postStandardResult(url, setParamLanguage(params), headers, charset);
    }

    public static StandardResult postStandardResult(String url, Map<String, String> params, Map<String, String> headers)
            throws IOException {
        return postStandardResult(url, params, headers,"UTF-8");
    }

    public static StandardResult postStandardResult(String url, Map<String, String> params) throws IOException {
        return postStandardResult(url, params, null);
    }

    public static StandardResult postStandardResult(String url) throws IOException {
        return postStandardResult(url, null);
    }

    @SuppressWarnings("unchecked")
    public static String deleteResponseText(String url, Map<String, Object> params, Map<String, String> headers, String charset)
            throws IOException {
        return HttpRequestUtil.deleteResponseText(url, setParamLanguage(params), headers, charset);
    }

    public static String deleteResponseText(String url, Map<String, Object> params, Map<String, String> headers)
            throws IOException {
        return deleteResponseText(url, params, headers, "UTF-8");
    }

    public static String deleteResponseText(String url, Map<String, Object> params) throws IOException {
        return deleteResponseText(url, params, null);
    }

    public static String deleteResponseText(String url) throws IOException {
        return deleteResponseText(url, null);
    }

    @SuppressWarnings("unchecked")
    public static StandardResult deleteStandardResult(String url, Map<String, Object> params, Map<String, String> headers,
            String charset) throws IOException {
        return HttpRequestUtil.deleteStandardResult(url, setParamLanguage(params), headers);
    }

    public static StandardResult deleteStandardResult(String url, Map<String, Object> params, Map<String, String> headers)
            throws IOException {
        return deleteStandardResult(url, params, headers, null);
    }

    public static StandardResult deleteStandardResult(String url, Map<String, Object> params) throws IOException {
        return deleteStandardResult(url, params, null);
    }

    public static StandardResult deleteStandardResult(String url) throws IOException {
        return deleteStandardResult(url, null);
    }

    @SuppressWarnings("unchecked")
    public static String putResponseText(String url, Map<String, String> params, Map<String, String> headers, String charset)
            throws IOException {
        return HttpRequestUtil.putResponseText(url, setParamLanguage(params), headers, charset);
    }

    public static String putResponseText(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        return putResponseText(url, params, headers, "UTF-8");
    }

    public static String putResponseText(String url, Map<String, String> params) throws IOException {
        return putResponseText(url, params, null);
    }

    public static String putResponseText(String url) throws IOException {
        return putResponseText(url, null);
    }

    @SuppressWarnings("unchecked")
    public static StandardResult putStandardResult(String url, Map<String, String> params, Map<String, String> headers,
            String charset) throws IOException {
        return HttpRequestUtil.putStandardResult(url, setParamLanguage(params), headers, charset);
    }

    public static StandardResult putStandardResult(String url, Map<String, String> params, Map<String, String> headers)
            throws IOException {
        return putStandardResult(url, params, headers, "UTF-8");
    }

    public static StandardResult putStandardResult(String url, Map<String, String> params) throws IOException {
        return putStandardResult(url, params, null);
    }

    public static StandardResult putStandardResult(String url) throws IOException {
        return putStandardResult(url, null);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Map setParamLanguage(Map params) {
        if (params == null) {
            params = new HashMap();
        }
        if (params.get("language") == null) {
            params.put("language", ResponseLanguage.getThreadLanguage().language());
            params.put("region", ResponseLanguage.getThreadLanguage().region());
        }
        return params;
    }
    
    
    public static DownloadResult download(String url, String charset, Map<String, String> headers, Map<String, String> params) throws IOException {
    	StringBuffer content = new StringBuffer();
        if(!HttpCommonUtil.isEmptyMap(params)) {
        	Set<String> keys = params.keySet();
        	for(String key : keys) {
        		if(content.length() == 0) {
        			content.append(key + "=" + URLEncoder.encode(params.get(key),"UTF-8"));
        		} else {
        			content.append("&" + key + "=" + URLEncoder.encode(params.get(key),"UTF-8"));
        		}
        	}
        }
    	if(content.length() > 0) {
    		url = url + "?" + content.toString();
    	}
    	URL downurl = new URL(url);
        URLConnection urlConnection = downurl.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
        
        // true -- will setting parameters
        httpURLConnection.setDoOutput(true);
        // true--will allow read in from
        httpURLConnection.setDoInput(true);
        // will not use caches
        httpURLConnection.setUseCaches(false);
        // setting serialized
        httpURLConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");
        // default is GET                        
        httpURLConnection.setRequestMethod("GET");//POST
        httpURLConnection.setRequestProperty("connection", "Keep-Alive");
        httpURLConnection.setRequestProperty("Charsert", "UTF-8");
        // 1 min
        httpURLConnection.setConnectTimeout(60000);
        // 1 min
        httpURLConnection.setReadTimeout(60000);

        if(!HttpCommonUtil.isEmptyMap(headers)) {
        	Set<String> keys = headers.keySet();
        	for(String key : keys) {
        		httpURLConnection.addRequestProperty(key, headers.get(key));
        	}
        }

        // connect to server (tcp)
//        httpURLConnection.connect();
        
        
        DataOutputStream dos=new DataOutputStream(httpURLConnection.getOutputStream());
        dos.writeBytes(content.toString());
        dos.flush();
        dos.close();
        
        
        
        DownloadResult result = new SimpleRequestUtil().new DownloadResult();
        Map<String, List<String>> headerMap = httpURLConnection.getHeaderFields();
        if(!HttpCommonUtil.isEmptyMap(headerMap)) {
        	Map<String, String> resultHeaders = new HashMap<String, String>();
        	Set<String> keys = headerMap.keySet();
        	for(String key : keys) {
        		String value = httpURLConnection.getHeaderField(key);
        		resultHeaders.put(key, value);
        	}
        	result.setHeaders(resultHeaders);
        }
        InputStream is = httpURLConnection.getInputStream();// send request to
        result.setIs(is);
    	return result;
    }

    
    public static void main(String[] args) throws IOException {
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("casId", "1357421");
    	params.put("date", "2017-03-04");
    	DownloadResult result = download("http://192.168.220.224:9292/fangzhou/downLoad/applybase?name=Event", "UTF-8", null, params);
    	File file = new File("F:/tmp/Event.xlsx");
        if(!file.exists()){
            file.createNewFile();
        }

        FileOutputStream out = new FileOutputStream(file);  
        byte[] buffer = new byte[4096];
        int readLength = 0;
        while ((readLength=result.getIs().read(buffer)) > 0) {
            byte[] bytes = new byte[readLength];
            System.arraycopy(buffer, 0, bytes, 0, readLength);
            out.write(bytes);
        }
        out.flush();
        out.close();
    }
    

	public class DownloadResult implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		private Map<String, String> headers;
		
		private InputStream is;

		public Map<String, String> getHeaders() {
			return headers;
		}

		public void setHeaders(Map<String, String> headers) {
			this.headers = headers;
		}

		public InputStream getIs() {
			return is;
		}

		public void setIs(InputStream is) {
			this.is = is;
		}
		
	}

}


