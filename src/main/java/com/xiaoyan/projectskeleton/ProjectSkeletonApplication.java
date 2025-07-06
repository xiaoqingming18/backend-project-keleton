package com.xiaoyan.projectskeleton;

import com.xiaoyan.projectskeleton.common.config.JwtConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan("com.xiaoyan.projectskeleton.mapper")
@EnableConfigurationProperties({JwtConfig.class})
public class ProjectSkeletonApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectSkeletonApplication.class, args);
    }

}
