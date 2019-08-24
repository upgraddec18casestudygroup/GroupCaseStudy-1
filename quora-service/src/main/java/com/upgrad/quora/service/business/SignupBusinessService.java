package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupBusinessService {

    @Autowired
    private UsersDao userDao;
    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UsersEntity signup(UsersEntity userEntity) throws SignUpRestrictedException{
        String[] encryptedText = cryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);
        if(userDao.getUserByUsername(userEntity.getUserName())!=null)
        {
            throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");
        }
        else if(userDao.getUserByEmail(userEntity.getEmail())!=null){
            throw new SignUpRestrictedException("SGR-002","This user has already been registered, try with any other emailId");
        }
        else
        return  userDao.CreateUser(userEntity);
    }
}
