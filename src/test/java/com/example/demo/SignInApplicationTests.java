package com.example.demo;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.service.SignInService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SignInApplicationTests {
	@Autowired
	private SignInService signInService;
	
	@Test
	public void contextLoads() {
		
	}
	@Test
	public void testSignIn() {
		List<String> list = new ArrayList<>();
		list.add("小明");
		list.add("小红");
		list.add("小强");
		List<String> us = new ArrayList<>();
		us.add("小明");
		assertEquals("小明", signInService.signIn("小明",list,us));
	}
	@Test
	public void testQuery() {
		List<String> list = new ArrayList<>();
		list.add("小明");
		list.add("小红");
		list.add("小强");
		List<String> us = new ArrayList<>();
		us.add("小明");
		assertEquals("小明", signInService.query("小明",list,us));
	}
	@Test
	public void testTong() {
		List<String> list = new ArrayList<>();
		list.add("小明");
		list.add("小红");
		list.add("小强");
		List<String> us = new ArrayList<>();
		us.add("小明");
		assertEquals("1", signInService.statistics(list));
	}

}
