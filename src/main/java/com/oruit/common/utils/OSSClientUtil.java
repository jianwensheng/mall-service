package com.oruit.common.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Configuration
public class OSSClientUtil {
	
	  Log log = LogFactory.getLog(OSSClientUtil.class);
	   
	  private String endpoint = "oss-cn-beijing.aliyuncs.com";

	  private String accessKeyId = "LTAIpnTPzobdnbMW";
	   
	  private String accessKeySecret = "jzHVzvVw28Yk0wkMtLDpouA9ZxMhEG";
 
	  private String bucketName = "dida-test";
	  
	  String domainUrl = "https://dida-test.oss-cn-beijing.aliyuncs.com";
	  
	  //文件存储目录
	  private String filedir = "image/";
	 
	  private OSSClient ossClient;
	  
	  public OSSClientUtil() { 

			ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	  }   
	  
	  public void init() {
		  ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	  }
	  
	  public void init(String endpoint,String accessKeyId,String accessKeySecret,String bucketName,String domainUrl) { 
		    this.endpoint = endpoint;
		    this.accessKeyId = accessKeyId;
		    this.accessKeySecret = accessKeySecret;
		    this.bucketName = bucketName;
		    this.domainUrl =  domainUrl; 
		    ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
	  }
	   
	  /**
	   * 销毁
	   */
	  public void destory() {
	    ossClient.shutdown();
	  }
	 
	  /**
	   * 上传图片
	   *
	   * @param url
	   */
	  public void uploadImg2Oss(String url) {
	    File fileOnServer = new File(url);
	    FileInputStream fin;
	    try {
	      fin = new FileInputStream(fileOnServer);
	      String[] split = url.split("/");
	      this.uploadFile2OSS(fin, split[split.length - 1]);
	    } catch (FileNotFoundException e) {
	    	log.debug("图片上传失败");
	    }
	  }
	 
	 
	  public String uploadImg2Oss(MultipartFile file) {
	    if (file.getSize() > 1024 * 1024) {
	    	log.debug("上传图片大小不能超过1M！");
	    }
	    String originalFilename = file.getOriginalFilename();
	    String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
	    Random random = new Random();
	    String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
	    try {
	      InputStream inputStream = file.getInputStream();
	      return uploadFile2OSS(inputStream, name); 
	    } catch (Exception e) {
	    	log.debug("图片上传失败");
	    }
	    return null;
	  }
	 
	  /**
	   * 获得图片路径
	   *
	   * @param fileUrl
	   * @return
	   */
	  public String getImgUrl(String fileUrl) {
	    if (!StringUtils.isEmpty(fileUrl)) {
	      String[] split = fileUrl.split("/");
	      return this.getUrl(this.filedir + split[split.length - 1]);
	    }
	    return null;
	  }
	 
	  /**
	   * 上传到OSS服务器  如果同名文件会覆盖服务器上的
	   *
	   * @param instream 文件流
	   * @param fileName 文件名称 包括后缀名
	   * @return 出错返回"" ,唯一MD5数字签名
	   */
	  public String uploadFile2OSS(InputStream instream, String fileName) {
	    String path = "";
	    String ossImageDomain = domainUrl;
	    try {
	      //创建上传Object的Metadata 
	      ObjectMetadata objectMetadata = new ObjectMetadata();
	      objectMetadata.setContentLength(instream.available());
	      objectMetadata.setCacheControl("no-cache");
	      objectMetadata.setHeader("Pragma", "no-cache");
	      objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
	      objectMetadata.setContentDisposition("inline;filename=" + fileName); 
		  String dPath = "";
		  Calendar a = Calendar.getInstance();
		  dPath += String.valueOf(a.get(Calendar.YEAR));
		  dPath += "/";
		  dPath += String.valueOf(a.get(Calendar.MONTH) + 1);
		  dPath += "/";
		  dPath += String.valueOf(a.get(Calendar.DAY_OF_MONTH));
		  dPath += "/";
		String oospath = dPath.substring(0, dPath.length() - 1);
		String remoteFilePath = oospath.substring(0, oospath.length()).replaceAll("\\\\", "/") + "/";
	      //上传文件
		  path = filedir+remoteFilePath  + fileName;
	      PutObjectResult putResult = ossClient.putObject(bucketName, path, instream, objectMetadata); 
	    } catch (IOException e) {
	      log.error(e.getMessage(), e);
	    } finally {
	      try {
	        if (instream != null) {
	          instream.close();
	        }
	        if (ossClient != null) {
	        	ossClient.shutdown();
		    }
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    }
	    return ossImageDomain+"/"+path;
	  }
	 
	  /**
	   * Description: 判断OSS服务文件上传时文件的contentType
	   *
	   * @param FilenameExtension 文件后缀
	   * @return String
	   */
	  public static String getcontentType(String FilenameExtension) {
	    if (FilenameExtension.equalsIgnoreCase(".bmp")) {
	      return "image/bmp";
	    }
	    if (FilenameExtension.equalsIgnoreCase(".gif")) {
	      return "image/gif";
	    }
	    if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
	        FilenameExtension.equalsIgnoreCase(".jpg") ||
	        FilenameExtension.equalsIgnoreCase(".png")) {
	      return "image/jpeg";
	    }
	    if (FilenameExtension.equalsIgnoreCase(".html")) {
	      return "text/html";
	    }
	    if (FilenameExtension.equalsIgnoreCase(".txt")) {
	      return "text/plain";
	    }
	    if (FilenameExtension.equalsIgnoreCase(".vsd")) {
	      return "application/vnd.visio";
	    }
	    if (FilenameExtension.equalsIgnoreCase(".pptx") ||
	        FilenameExtension.equalsIgnoreCase(".ppt")) {
	      return "application/vnd.ms-powerpoint";
	    }
	    if (FilenameExtension.equalsIgnoreCase(".docx") ||
	        FilenameExtension.equalsIgnoreCase(".doc")) {
	      return "application/msword";
	    }
	    if (FilenameExtension.equalsIgnoreCase(".xml")) {
	      return "text/xml";
	    }
	    return "image/jpeg";
	  }
	 
	  /**
	   * 获得url链接
	   *
	   * @param key
	   * @return
	   */
	  public String getUrl(String key) {
	    // 设置URL过期时间为10年  3600l* 1000*24*365*10
	    Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
	    // 生成URL
	    URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
	    if (url != null) {
	      return url.toString();
	    }
	    return null;
	  }
	  
	  /**
	   * 删除OSS图片
	   */
	  public void deleteImg(String key) {
		// 创建OSSClient实例。
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		// 删除文件。
		ossClient.deleteObject(bucketName, key);
		// 关闭OSSClient。
		ossClient.shutdown();
	  }
	  
	  /**
	   * 批量删除啊
	   * @param keys
	   */
	  public void deleteKeys(List<String> keys) {
		  // 创建OSSClient实例。
		  OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		  // 删除文件。
		  DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys));
		  // List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
		  // 关闭OSSClient。
		  ossClient.shutdown();
	  }
	  
	  
	  public static void main(String[] args) {
		  OSSClientUtil oss = new OSSClientUtil();
		  String fileName="d:\\test\\1.jpg";
		  File file=new File(fileName);
		try {
			String url = oss.uploadFile2OSS(new FileInputStream(file),"11.png");
			System.out.println(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
