package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;
    public UserEntity CreateUser(UserEntity userEntity){
         entityManager.persist(userEntity);
        return userEntity;
    }
    public UserEntity getUser(final String userUuid){
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", userUuid)
                    .getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }
}
