package com.coffeeorderproject.spring.service;

import java.util.ArrayList;

import com.coffeeorderproject.spring.dto.BoardDto;

public interface UserBoardService {

	ArrayList<BoardDto> getUserBoard(int pageNum, String userId);

	int getUserBoardCount(String userId);

}