package com.coffeeorderproject.spring.dao;

import com.coffeeorderproject.spring.dto.UserDto;

public interface AccountDao {

	void insertUser(UserDto user);

	Boolean idCheck(UserDto user);

	UserDto selectUser(UserDto user);

	UserDto selectUserEmail(String id);

	void updateUserPw(String userId, String newPw);

	void updateUser(String userId);

}