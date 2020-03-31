package com.oruit.util;

import com.alibaba.fastjson.JSONObject;
import com.oruit.weixin.MyX509TrustManager;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HttpRequestUtil {

    private static Set<Header> DEFAULT_HEADERS = new HashSet<Header>();

    static {
        DEFAULT_HEADERS.add(new BasicHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
        DEFAULT_HEADERS
                .add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
        DEFAULT_HEADERS.add(new BasicHeader("Accept-Encoding", "gzip,deflate,sdch"));
        DEFAULT_HEADERS.add(new BasicHeader("Request-From", "Analysys/Java"));
    }
    
    private static final String DETAULT_CHARSET = "UTF-8";

    static class HttpClientHolder {
        static CloseableHttpClient httpClient = create();
    }

    public static CloseableHttpClient getDefaultInstance() {
        return HttpClientHolder.httpClient;
    }

    public static CloseableHttpClient create() {
        return create(false);
    }

    public static CloseableHttpClient create(boolean single) {
        return create(single, 600000);
    }

    @SuppressWarnings("resource")
    public static CloseableHttpClient create(boolean single, int connectTimeout) {
        HttpClientConnectionManager connManager;
        if (single)
            connManager = new BasicHttpClientConnectionManager();
        else {
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(60, TimeUnit.SECONDS);
            cm.setDefaultMaxPerRoute(1000);
            cm.setMaxTotal(1000);
            connManager = cm;
        }
        RequestConfig requestConfig = RequestConfig.custom().setCircularRedirectsAllowed(true).setConnectTimeout(connectTimeout)
                .setExpectContinueEnabled(true).build();
        CloseableHttpClient httpclient = HttpClientBuilder.create().setConnectionManager(connManager)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy()).setDefaultRequestConfig(requestConfig)
                .disableAuthCaching().disableAutomaticRetries().disableConnectionState().disableCookieManagement()
                .setDefaultHeaders(DEFAULT_HEADERS).build();
        return httpclient;
    }

    public static StandardResult getStandardResult(String url, Map<String, Object> params, Map<String, String> headers,
            String charset) throws IOException {
        String str = getResponseText(url, params, headers, charset);
        return JSONObject.parseObject(str, StandardResult.class);
    }

    public static StandardResult getStandardResult(String url, Map<String, Object> params, Map<String, String> headers)
            throws IOException {
        return getStandardResult(url, params, headers, DETAULT_CHARSET);
    }

    public static StandardResult getStandardResult(String url, Map<String, Object> params) throws IOException {
        return getStandardResult(url, params, null);
    }

    public static StandardResult getStandardResult(String url) throws IOException {
        return getStandardResult(url, null);
    }

    public static String getResponseText(String url) throws IOException {
        return getResponseText(url, null, DETAULT_CHARSET);
    }

    public static String getResponseText(String url, Map<String, Object> params) throws IOException {
        return getResponseText(url, params, DETAULT_CHARSET);
    }

    public static String getResponseText(String url, Map<String, Object> params, Map<String, String> headers) throws IOException {
        return getResponseText(url, params, headers, DETAULT_CHARSET);
    }

    public static String getResponseText(String url, Map<String, Object> params, String charset) throws IOException {
        return getResponseText(url, params, null, charset);
    }

    public static String getResponseText(String url, Map<String, Object> params, Map<String, String> headers, String charset)
            throws IOException {
        HttpGet httpRequest = null;
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.size() > 0) {
            sb.append(url.indexOf('?') < 0 ? '?' : '&');
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), charset)).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        httpRequest = new HttpGet(sb.toString());
        if (headers != null && headers.size() > 0)
            for (Map.Entry<String, String> entry : headers.entrySet())
                httpRequest.addHeader(entry.getKey(), entry.getValue());
        try {
            return getDefaultInstance().execute(httpRequest, new BasicResponseHandler(charset));
        } finally {
            httpRequest.releaseConnection();
        }
    }

    public static String postResponseText(String url, Map<String, String> params) throws IOException {
        return postResponseText(url, params, DETAULT_CHARSET);
    }

    public static String postResponseText(String url, Map<String, String> params, Map<String, String> headers)
            throws IOException {
        return postResponseText(url, params, headers, DETAULT_CHARSET);
    }

    public static String postResponseText(String url, Map<String, String> params, String charset) throws IOException {
        return postResponseText(url, params, null, charset);
    }

    public static String postResponseText(String url, Map<String, String> params, Map<String, String> headers, String charset)
            throws IOException {
        HttpPost httpRequest = new HttpPost(url);
        if (params != null && params.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet())
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            httpRequest.setEntity(new UrlEncodedFormEntity(nvps, charset));
        }
        if (headers != null && headers.size() > 0)
            for (Map.Entry<String, String> entry : headers.entrySet())
                httpRequest.addHeader(entry.getKey(), entry.getValue());
        try {
            return getDefaultInstance().execute(httpRequest, new BasicResponseHandler(charset));
        } finally {
            httpRequest.releaseConnection();
        }
    }
    
    public static String postRequestBody(String url, Object requestBody, Map<String, String> headers, String charset) throws ClientProtocolException, IOException {
    	CloseableHttpClient httpclient = HttpClients.createDefault();
    	HttpPost httpPost = new HttpPost(url);
    	StringEntity entity = new StringEntity(JSONObject.toJSONString(requestBody), ContentType.APPLICATION_JSON);
    	if (headers != null && headers.size() > 0) {
    		for (Map.Entry<String, String> entry : headers.entrySet()) {
    			httpPost.addHeader(entry.getKey(), entry.getValue());
    		}
    	}
    	httpPost.setEntity(entity);
    	String result = httpclient.execute(httpPost, new BasicResponseHandler(charset));
    	return result;
    }
    
    public static String postRequestBody(String url, Object requestBody) throws ClientProtocolException, IOException {
    	return postRequestBody(url, requestBody, null, DETAULT_CHARSET);
    }

    public static StandardResult postStandardResult(String url, Map<String, String> params, Map<String, String> headers,
            String charset) throws IOException {
        String jsonstr = postResponseText(url, params, headers, charset);
        if (jsonstr != null) {
            return JSONObject.parseObject(jsonstr, StandardResult.class);
        }
        return null;
    }

    public static StandardResult postStandardResult(String url, Map<String, String> params, Map<String, String> headers)
            throws IOException {
        return postStandardResult(url, params, headers,DETAULT_CHARSET);
    }

    public static StandardResult postStandardResult(String url, Map<String, String> params) throws IOException {
        return postStandardResult(url, params, null);
    }

    public static StandardResult postStandardResult(String url) throws IOException {
        return postStandardResult(url, null);
    }

    public static String postResponseText(String url, String body, Map<String, String> headers, String charset)
            throws IOException {
        HttpPost httpRequest = new HttpPost(url);
        httpRequest.setEntity(new StringEntity(body, charset));
        if (headers != null && headers.size() > 0)
            for (Map.Entry<String, String> entry : headers.entrySet())
                httpRequest.addHeader(entry.getKey(), entry.getValue());
        try {
            return getDefaultInstance().execute(httpRequest, new BasicResponseHandler(charset));
        } finally {
            httpRequest.releaseConnection();
        }
    }

    public static String deleteResponseText(String url, Map<String, Object> params, Map<String, String> headers, String charset)
            throws IOException {
        HttpDelete httpRequest = null;
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.size() > 0) {
            sb.append(url.indexOf('?') < 0 ? '?' : '&');
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), charset)).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        httpRequest = new HttpDelete(sb.toString());
        if (headers != null && headers.size() > 0)
            for (Map.Entry<String, String> entry : headers.entrySet())
                httpRequest.addHeader(entry.getKey(), entry.getValue());
        try {
            return getDefaultInstance().execute(httpRequest, new BasicResponseHandler(charset));
        } finally {
            httpRequest.releaseConnection();
        }
    }

    public static String deleteResponseText(String url, Map<String, Object> params, Map<String, String> headers)
            throws IOException {
        return deleteResponseText(url, params, headers, DETAULT_CHARSET);
    }

    public static String deleteResponseText(String url, Map<String, Object> params) throws IOException {
        return deleteResponseText(url, params, null);
    }

    public static String deleteResponseText(String url) throws IOException {
        return deleteResponseText(url, null);
    }

    public static StandardResult deleteStandardResult(String url, Map<String, Object> params, Map<String, String> headers,
            String charset) throws IOException {
        String jsonstr = deleteResponseText(url, params, headers, charset);
        if (jsonstr != null) {
            return JSONObject.parseObject(jsonstr, StandardResult.class);
        }
        return null;
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

    public static String putResponseText(String url, Map<String, String> params, Map<String, String> headers, String charset)
            throws IOException {
        HttpPut httpRequest = new HttpPut(url);
        if (params != null && params.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet())
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            httpRequest.setEntity(new UrlEncodedFormEntity(nvps, charset));
        }
        if (headers != null && headers.size() > 0)
            for (Map.Entry<String, String> entry : headers.entrySet())
                httpRequest.addHeader(entry.getKey(), entry.getValue());
        try {
            return getDefaultInstance().execute(httpRequest, new BasicResponseHandler(charset));
        } finally {
            httpRequest.releaseConnection();
        }
    }

    public static String putResponseText(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        return putResponseText(url, params, headers, DETAULT_CHARSET);
    }

    public static String putResponseText(String url, Map<String, String> params) throws IOException {
        return putResponseText(url, params, null);
    }

    public static String putResponseText(String url) throws IOException {
        return putResponseText(url, null);
    }

    public static StandardResult putStandardResult(String url, Map<String, String> params, Map<String, String> headers,
            String charset) throws IOException {
        String jsonstr = putResponseText(url, params, headers, charset);
        if (jsonstr != null) {
            return JSONObject.parseObject(jsonstr, StandardResult.class);
        }
        return null;
    }

    public static StandardResult putStandardResult(String url, Map<String, String> params, Map<String, String> headers)
            throws IOException {
        return putStandardResult(url, params, headers, DETAULT_CHARSET);
    }

    public static StandardResult putStandardResult(String url, Map<String, String> params) throws IOException {
        return putStandardResult(url, params, null);
    }

    public static StandardResult putStandardResult(String url) throws IOException {
        return putStandardResult(url, null);
    }

    public static class BasicResponseHandler implements ResponseHandler<String> {

        private String charset;

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public BasicResponseHandler(String charset) {
            this.charset = charset;
        }

        @Override
        public String handleResponse(final HttpResponse response) throws HttpResponseException, IOException {
            final StatusLine statusLine = response.getStatusLine();
            final HttpEntity entity = response.getEntity();
            if (statusLine.getStatusCode() >= 500) {
                EntityUtils.consume(entity);
                throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
            }
            return entity == null ? null : EntityUtils.toString(entity, charset);
        }

    }

    public static String post(String url, String entity) throws IOException {
        return invoke("POST", url, entity);
    }

    public static String post(String url, String entity, String charset) throws IOException {
        return invoke("POST", url, entity, charset);
    }

    public static String put(String url, String entity) throws IOException {
        return invoke("PUT", url, entity);
    }

    public static String put(String url, String entity, String charset) throws IOException {
        return invoke("PUT", url, entity, charset);
    }

    public static String delete(String url) throws IOException {
        return invoke("DELETE", url, null);
    }

    public static String get(String url) throws IOException {
        return invoke("GET", url, null);
    }

    private static String invoke(String method, String url, String entity) throws IOException {
        return invoke(method, url, entity, DETAULT_CHARSET);
    }

    private static String invoke(String method, String url, String entity, String charset) throws IOException {
        HttpRequestBase httpRequest = null;
        if (method.equalsIgnoreCase("GET"))
            httpRequest = new HttpGet(url);
        else if (method.equalsIgnoreCase("POST"))
            httpRequest = new HttpPost(url);
        else if (method.equalsIgnoreCase("PUT"))
            httpRequest = new HttpPut(url);
        else if (method.equalsIgnoreCase("DELETE"))
            httpRequest = new HttpDelete(url);
        if (entity != null)
            ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(new StringEntity(entity, charset));
        try {
            return getDefaultInstance().execute(httpRequest, new BasicResponseHandler(charset));
        } finally {
            if (httpRequest != null)
                httpRequest.releaseConnection();
        }
    }

    public static String httpRequest2(String requestUrl, String requestMethod, byte[] outputStr) {
        StringBuffer buffer = new StringBuffer();
        try {
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new SecureRandom());

            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();

                outputStream.write(outputStr);
                outputStream.close();
            }

            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, DETAULT_CHARSET);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();

            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
        } catch (ConnectException ce) {
            System.out.print("Weixin server connection timed out.");
        } catch (Exception e) {
            System.out.print("https request error:{}" + e.getMessage());
        }
        return buffer.toString();
    }

}
