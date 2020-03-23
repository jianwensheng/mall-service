package com.oruit.common.utils.web;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by yeqiang on 4/18/19.
 */
@Slf4j
public class FreemarkerUtils {
    public static String getTemplate(String template, Map<String, Object> map) throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        // 根目录取出来的是/WEB-INF/classes, 本项目view目录在/WEB-INF/classes
        cfg.setClassForTemplateLoading(FreemarkerUtils.class, "/templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        // cfg.setTemplateLoader(new FileTemplateLoader(new File(templatePath)));

        cfg.setSetting("template_update_delay", "10");
        cfg.setSetting("locale", "en_US");
        cfg.setSetting("datetime_format", "yyyy-MM-dd HH:mm:ss");
        cfg.setSetting("date_format", "yyyy-MM-dd");
        cfg.setSetting("number_format", "#.##");
        cfg.setSetting("auto_import", "common/common.html as common");
        cfg.setSetting("template_exception_handler", "ignore");

        Template temp = cfg.getTemplate(template + ".html");
        StringWriter stringWriter = new StringWriter();
        temp.process(map, stringWriter);
        return stringWriter.toString();
    }
}
