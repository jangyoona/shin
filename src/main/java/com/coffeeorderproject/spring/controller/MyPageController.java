package com.coffeeorderproject.spring.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.coffeeorderproject.spring.dto.BoardDto;
import com.coffeeorderproject.spring.dto.UserCouponDto;
import com.coffeeorderproject.spring.dto.UserDto;
import com.coffeeorderproject.spring.dto.UserOrderDto;
import com.coffeeorderproject.spring.service.MyPageService;
import com.coffeeorderproject.spring.service.UserBoardService;

import jakarta.servlet.http.HttpSession;
import lombok.Setter;
@Controller
@RequestMapping("/userMyPage")
public class MyPageController {

	@Setter(onMethod_ = { @Autowired})
	private MyPageService mypageService;
	
	@Setter(onMethod_ = { @Autowired})
	private UserBoardService userBoardService;
	
	@GetMapping("/mypage")
	public String MyPageForm(HttpSession session, Model model) {
		
		UserDto user = (UserDto)session.getAttribute("loginUser");
		String userId = user.getUserId();
		
		ArrayList<UserOrderDto> orderList = mypageService.findMyPageUserorder(userId);
		model.addAttribute("userOrders", orderList);
		
		ArrayList<UserCouponDto> coupon = mypageService.UserCoupon(userId);
		model.addAttribute("userCoupon", coupon);
		
		ArrayList<BoardDto> userBoardList = mypageService.UserBoard(userId);
		model.addAttribute("board", userBoardList);
		
		
		return "/userMyPage/mypage";
	}
	
	@GetMapping("/order-list")
	public String orderListForm(HttpSession session, String page, Model model) {
        
		UserDto user = (UserDto)session.getAttribute("loginUser");
		String userId = user.getUserId();
		int pageNum = Integer.parseInt(page);
		
		ArrayList<UserOrderDto> orderArr = mypageService.getUserOrder(pageNum, userId);
		int orderCount = mypageService.getUserOrderCount(userId);
		
		int allPageNum = orderCount / 15 + (orderCount % 15 > 0 ? 1 : 0);
		int pagerBlock = (pageNum - 1) / 2;
		int start = (pagerBlock * 2) + 1;
		int end = start + 2;
		
		end = end > allPageNum ? allPageNum : end;
		
		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("allPageNum", allPageNum);
		
		model.addAttribute("orders", orderArr);
		model.addAttribute("orderCount", orderCount);
		
		return "/userMyPage/order-list";
	}
	
	@GetMapping("user-review")
	public String reviewForm(HttpSession session, String page, Model model) {
        
        UserDto user = (UserDto)session.getAttribute("loginUser");
		String userId = user.getUserId();
        int pageNum = Integer.parseInt(page);
        
        
		ArrayList<BoardDto> boardArr = userBoardService.getUserBoard(pageNum, userId);
		int boardCount = userBoardService.getUserBoardCount(userId);
		
		int allPageNum = boardCount / 15 + (boardCount % 15 > 0 ? 1 : 0);
		int pagerBlock = (pageNum - 1) / 2;
		int start = (pagerBlock * 2) + 1;
		int end = start + 2;
		
		end = end > allPageNum ? allPageNum : end;
		
		model.addAttribute("start", start);
		model.addAttribute("end", end);
		model.addAttribute("allPageNum", allPageNum);
		
		model.addAttribute("boardArr", boardArr);
		model.addAttribute("boardCount", boardCount);
		
		
		return "userMyPage/user-review";
	}
	
}
