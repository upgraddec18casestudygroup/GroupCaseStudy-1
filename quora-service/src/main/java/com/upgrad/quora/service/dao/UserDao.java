package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
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
        try{
            return entityManager.createNamedQuery("userByUuid",UserEntity.class).setParameter("uuid",userUuid).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

    public UserEntity getUserByEmail(final String email){
        try{
            return entityManager.createNamedQuery("userByEmail",UserEntity.class).setParameter("email",email).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }
    public UserEntity getUserByUsername(final String username){
        try{
            return entityManager.createNamedQuery("userByUsername",UserEntity.class).setParameter("username",username).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

    public void updateUserEntity(final UserAuthTokenEntity userAuthEntity){
        entityManager.merge(userAuthEntity);
    }

    public UserEntity getUserByUuid(final String uuid){
        try{
            return entityManager.createNamedQuery("userByUuid",UserEntity.class).setParameter("uuid",uuid).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    public void updateUser(final UserEntity updatedUserEntity){
        entityManager.merge(updatedUserEntity);
    }

    public UserAuthTokenEntity getUserAuthToken(final String access_token){
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken",UserAuthTokenEntity.class)
                                .setParameter("access_token",access_token).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

    public String deleteUser(final UserEntity userEntity){
        String uuid=userEntity.getUuid();
        entityManager.remove(userEntity);
        return uuid;

    }
    public UserAuthTokenEntity getUserAuthTokenByUuid(final String uuid){
        try {
            return entityManager.createNamedQuery("userAuthTokenByUuid",UserAuthTokenEntity.class).setParameter("uuid",uuid).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }
}
