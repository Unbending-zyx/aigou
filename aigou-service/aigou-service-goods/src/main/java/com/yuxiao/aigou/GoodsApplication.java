package com.yuxiao.aigou;

import com.yuxiao.aigou.common.entry.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient //开启eureka客户端
@MapperScan(basePackages = "com.yuxiao.aigou.goods.dao") //开启通用Mapper的包扫描
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class,args);
    }

    /**
     * 将idWorker放入容器
     */
    @Bean
    public IdWorker getIdWorker(){
        return new IdWorker(0,0);
    }
}
