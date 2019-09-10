package com.stylefeng.guns.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class StaticController extends WebMvcConfigurerAdapter {
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.err.println("-----------------");
        System.err.println("映射静态资源访问");
        System.err.println("-----------------");
        try {
//            registry.addResourceHandler((FSS.xgt +"**")).addResourceLocations(("classpath:/static"+FSS.xgt));
            registry.addResourceHandler("/MP_verify_MLvDTY0UZCw4LduU.txt").addResourceLocations(("file:"+(System.getProperty("os.name").toLowerCase().startsWith("win")? FSS.FILE_UPLOAD_PATH_LOC:FSS.FILE_UPLOAD_PATH_ONLINE)+"MP_verify_MLvDTY0UZCw4LduU.txt"));
//            registry.addResourceHandler("/image/**").addResourceLocations(("file:"+(System.getProperty("os.name").toLowerCase().startsWith("win")?FSS.FILE_UPLOAD_PATH_LOC:FSS.FILE_UPLOAD_PATH_ONLINE)+"image/"));
//            registry.addResourceHandler("/fileupload/image/**").addResourceLocations(("file:"+(System.getProperty("os.name").toLowerCase().startsWith("win")?FSS.FILE_UPLOAD_PATH_LOC:FSS.FILE_UPLOAD_PATH_ONLINE)+"image/"));
//            registry.addResourceHandler("/protalApi/image/**").addResourceLocations(("file:"+(System.getProperty("os.name").toLowerCase().startsWith("win")?FSS.FILE_UPLOAD_PATH_LOC:FSS.FILE_UPLOAD_PATH_ONLINE)+"image/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
