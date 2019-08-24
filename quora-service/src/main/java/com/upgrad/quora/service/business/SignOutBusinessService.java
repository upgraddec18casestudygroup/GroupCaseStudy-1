package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class SignOutBusinessService {
    @Autowired
    private UsersDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UsersEntity signout(final String authorizationToken) throws SignOutRestrictedException {

        UserAuthTokenEntity userAuthToken=userDao.getUserAuthToken(authorizationToken);
        if(userAuthToken == null){
            throw new SignOutRestrictedException("SGR-001","User is not Signed in");
        }
        else{
            userAuthToken.setLogoutAt(ZonedDateTime.now());
            userDao.updateUserEntity(userAuthToken);
        }
        return userAuthToken.getUser();
    }
}
