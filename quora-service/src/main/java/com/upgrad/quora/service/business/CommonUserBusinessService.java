package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CommonUserBusinessService {
    @Autowired
    private UsersDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UsersEntity getUser(final String userUuid, final String authorizationToken) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthEntity=userDao.getUserAuthToken(authorizationToken);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        } else if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        } else if(userDao.getUserByUuid(userUuid) == null){
            throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
        } else{
            return userDao.getUserByUuid(userAuthEntity.getUuid());
        }

    }
}
