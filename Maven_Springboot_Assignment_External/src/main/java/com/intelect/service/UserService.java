package com.intelect.service;


import java.text.ParseException;
import java.util.List;

import com.intelect.model.User;

public interface UserService {
	
	User findById(long id);
	
	User findByName(String name);
	
	void saveUser(User user) throws ParseException;
	
	void updateUser(User user);
	
	void deleteUserById(long id);

	List<User> findAllUsers();
	
	boolean isUserExist(User user);
	
}
