package com.yuxiao.aigou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

//项目启动时 会要求配置数据库信息（原因是引入的common包中的依赖有可能需要配置数据库）
//此处使用exclude = DataSourceAutoConfiguration.class 告诉springboot不进行数据库的自动装载 （排除数据库自动装载）
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
public class FileApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class,args);
    }
}
