package com.upgrad.quora.service.business;

import org.springframework.stereotype.Service;
import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AuthenticationService {

    @Autowired
    private UsersDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Autowired
    private AuthenticationService authenticationService;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity authenticate(final String username, final String password) throws AuthenticationFailedException {

        UsersEntity userEntity = userDao.getUserByUsername(username);
        if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }

        final String encyptedPassword= passwordCryptographyProvider.encrypt(password,userEntity.getSalt());
        if(encyptedPassword.equals(userEntity.getPassword())){
            JwtTokenProvider jwtTokenProvider=new JwtTokenProvider(encyptedPassword);
            UserAuthTokenEntity userAuthTokenByUuid = userDao.getUserAuthTokenByUuid(userEntity.getUuid());
            UserAuthTokenEntity userAuthToken=new UserAuthTokenEntity();
            userAuthToken.setUser(userEntity);
            final ZonedDateTime now=ZonedDateTime.now();
            final ZonedDateTime expiresAt=now.plusHours(8);
            userAuthToken.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(),now,expiresAt));

            userAuthToken.setExpiresAt(expiresAt);
            userAuthToken.setLoginAt(now);

            if(userAuthTokenByUuid == null) {
                userDao.createAuthToken(userAuthToken);
            }else{
                userDao.updateUserEntity(userAuthToken);
            }

            return userAuthToken;
        }else{
            throw new AuthenticationFailedException("ATH-002","Password failed");
        }
    }
}
