package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonUserBusinessService;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class CommonController {

    @Autowired
    private CommonUserBusinessService userAdminBusinessService;

    @RequestMapping(method = RequestMethod.GET, value = "/userProfile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
public ResponseEntity <UserDetailsResponse> getUserProfileById(@PathVariable("userId") final String userUuid, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        String[] bearerToken= authorization.split("Basic ");
        final UsersEntity userEntity=userAdminBusinessService.getUser(userUuid,bearerToken[1]);
        UserDetailsResponse userDetailsResponse=new UserDetailsResponse().firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName()).userName(userEntity.getUserName())
                .emailAddress(userEntity.getEmail()).contactNumber(userEntity.getContactnumber())
                .dob(userEntity.getDOB()).country(userEntity.getCountry()).aboutMe(userEntity.getAboutMe());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);


    }
}
