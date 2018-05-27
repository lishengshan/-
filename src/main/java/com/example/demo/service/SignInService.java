package com.example.demo.service;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class SignInService {

	public String signIn(String name,List<String> list,List<String> usernames) {
		if(name!=null) {
			boolean flag=list.contains(name);
			if(flag) {
				boolean b=usernames.contains(name);
				if(b) {
					System.out.println("您已签到,无需重复签到!");
				}else {
					usernames.add(name);
					System.out.println("签到成功");
				}
			}else {
				System.out.println("你不是本班学生");
			}
		}
		return name;
		
	}
	public String query(String name,List<String> usernames,List<String> list) {
		if(name!=null) {
			boolean flag=list.contains(name);
			if(flag) {
				boolean b=usernames.contains(name);
				if(b) {
					System.out.println(name+"已签到!");
				}else {
					System.out.println("未签到");
				}
			}else {
				System.out.println("你不是本班学生");
			}
		}
		return name;
	}

	public int statistics(List<String> usernames) {
			return usernames.size();
		}
}

