package com.intelect.rest.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.intelect.model.User;
import com.intelect.service.UserService;
import com.intelect.util.CustomErrorType;

@RestController
@RequestMapping("/api")
public class IntelectRestApiController {

	public static final Logger logger = LoggerFactory.getLogger(IntelectRestApiController.class);

	@Autowired
	UserService userService; 
	
	//Testing purpose I writen getAll records
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/user/", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }


	// Create a User

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/user/", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) throws ParseException {
		logger.info("Creating User : {}", user);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		Date date1 = sdf.parse(user.getBirthDate());
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Date date = new Date();
		Date currentDate = sdf.parse(dateFormat.format(date));
		if (date1.compareTo(currentDate) > 0) {
			return new ResponseEntity(new CustomErrorType("Please Enter the Valid Birth Date"),HttpStatus.CONFLICT);
		} 
		try{
			if (userService.isUserExist(user) || user.getEmail().equals(null) || user.getEmail().equals("null")) {
				logger.error("Unable to create. A User with Email Id already exist Or Email Id Should Not be Null", user.getEmail());
				return new ResponseEntity(new CustomErrorType("Unable to create. A User with Email Id already exist Or Email Id Should Not be Null " + 
				user.getEmail()),HttpStatus.CONFLICT);
			}
		}catch(NullPointerException e){
			logger.info("Email should Not be Null", e);
			
		}
		userService.saveUser(user);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity(new CustomErrorType("User Id " + 
				user.getId() + " Created Succesfully"),HttpStatus.CREATED);
	}
	
	// Update a User

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/user/update", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUser(@RequestBody User user) {
		logger.info("Updating User with id ", user.getId());

		User currentUser = userService.findById(user.getId());

		if (currentUser == null) {
			logger.error("Unable to update. User with id not found.", user.getId());
			return new ResponseEntity(new CustomErrorType("Unable to upate. User with id " + user.getId() + " not found."),
					HttpStatus.NOT_FOUND);
		}
		try{
		if(!(user.getBirthDate().equals(null))){
		currentUser.setBirthDate(user.getBirthDate());
		}
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		if(user.getPinCode() != 0){
		currentUser.setPinCode(user.getPinCode());
		}
		userService.updateUser(currentUser);
		return new ResponseEntity(new CustomErrorType("User Update Succesfully"),HttpStatus.OK);
	}

	// Delete a User

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/user/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting User with id {}", id);

		User user = userService.findById(id);
		if (user == null) {
			logger.error("Unable to delete. User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. User with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		user.setActive(false);
		userService.updateUser(user);
		return new ResponseEntity(new CustomErrorType("User " + id + " DeActivated Successfully"),
				HttpStatus.NOT_FOUND);
	}

}