package com.coffeeorderproject.spring.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coffeeorderproject.spring.dao.UserBoardDao;
import com.coffeeorderproject.spring.dto.BoardDto;

import lombok.Setter;

@Service
public class UserBoardServiceImpl implements UserBoardService {
	
	@Setter(onMethod_ = { @Autowired})
	private UserBoardDao userBoardDao;
	
	@Override
	public ArrayList<BoardDto> getUserBoard(int pageNum, String userId) {
		ArrayList<BoardDto> boardArr = userBoardDao.selectUserBoard(pageNum,userId);
		return boardArr;
	}

	@Override
	public int getUserBoardCount(String userId) {
		int boardCount = userBoardDao.getBoardCount(userId);
		return boardCount;
	}

}
