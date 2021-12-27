package com.xuaninsr.xianyuque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;//新增
//@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})//修改
@SpringBootApplication
public class XianyuqueApplication {
	public static void main(String[] args) {
		SpringApplication.run(XianyuqueApplication.class, args);
	}
}
