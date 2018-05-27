package com.example.demo.qiantai;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.service.SignInService;

@Configuration
public class OnClassService {
	@Autowired
	private SignInService signInService;
	
	@Bean
	public String qiantai() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<>();
		list.add("小明");
		list.add("小红");
		list.add("小强");
		Scanner scanner = new Scanner(System.in);
		List<String> us = new ArrayList<>();
		int sc;
	//	SignInService qianDao = new SignInService();
		while (true) {
			System.out.println("1.签到");
			System.out.println("2.查询");
			System.out.println("3.统计");
			System.out.println("请输入你要选择服务的数字");
			sc = scanner.nextInt();
			while (sc == 1) {
				System.out.println("请输入签到姓名：");
				String name = scanner.next();
				String na=signInService.signIn(name, list, us);
				System.out.println("输入n退出签到");
				String what = scanner.next();
				if (what.equals("n")) {
					break;
				}
			}
			while (sc == 2) {
				System.out.println("请输入要查询的学生姓名：");
				String name1 = scanner.next();
				String na1=signInService.query(name1,us,list);
				System.out.println("输入n退出查询");
				String what = scanner.next();
				if (what.equals("n")) {
					break;
				}
			}
			if (sc == 3) {
				int i=signInService.statistics(us);
				System.out.println("已签到人数："+i);
			}
		}
	}


}