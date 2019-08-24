package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    @Autowired
    private UsersDao userDao;

    /**
     * Method to authorize a user based on the given access token
     *
     * @param accessToken assigned to the User
     * @return UserAuthTokenEntity which has the authorisation details of the user
     * @throws AuthorizationFailedException
     */

    public UserAuthTokenEntity verifyAuthToken(final String accessToken, String endpoinIdentifier) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthTokenEntity.getLogoutAt() != null) {
            String ExceptionMessagepart="";
            switch (endpoinIdentifier){
                case "create":
                    ExceptionMessagepart=" post a question";
                    break;
                case "all":
                    ExceptionMessagepart=" get all questions";
                    break;
                case "edit":
                    ExceptionMessagepart="  edit the question";
                    break;
                case "delete":
                    ExceptionMessagepart="  delete a question";
                    break;
                case "alluser":
                    ExceptionMessagepart="  get all questions posted by a specific user";
                    break;
                case "postAnswer":
                    ExceptionMessagepart=" post an answer";
                    break;
                case "editAnswer":
                    ExceptionMessagepart=" edit an answer";
                    break;
                case "deleteAnswer":
                    ExceptionMessagepart=" delete an answer";
                    break;
            }
            throw new AuthorizationFailedException("ATHR-002", String.format("User is signed out.Sign in first to%s", ExceptionMessagepart));
        }
        //else if(userAuthTokenEntity.getUser()==null){

        //}
        else{
            return userAuthTokenEntity;
        }

    }
}
