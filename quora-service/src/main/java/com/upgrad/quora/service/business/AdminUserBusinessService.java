package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminUserBusinessService {

    @Autowired
    private UsersDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteUser(final UsersEntity userEntity, final UsersEntity signedInUser) throws AuthorizationFailedException {
        if(signedInUser.getRole().equalsIgnoreCase("admin")){
            return userDao.deleteUser(userEntity);
        }
        else {
            throw new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UsersEntity getUserByUuid(final String userUuid){
        return userDao.getUserByUuid(userUuid);
    }

}
