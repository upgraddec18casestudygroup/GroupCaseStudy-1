package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UsersDao {
    @PersistenceContext
    private EntityManager entityManager;
    public UsersEntity CreateUser(UsersEntity userEntity){
        entityManager.persist(userEntity);
        return userEntity;
    }
    public UsersEntity getUser(final String userUuid){
        try{
            return entityManager.createNamedQuery("userByUuid",UsersEntity.class).setParameter("uuid",userUuid).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

    public UsersEntity getUserByEmail(final String email){
        try{
            return entityManager.createNamedQuery("userByEmail",UsersEntity.class).setParameter("email",email).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }
    public UsersEntity getUserByUsername(final String username){
        try{
            return entityManager.createNamedQuery("userByUsername",UsersEntity.class).setParameter("username",username).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

    public void updateUserEntity(final UserAuthTokenEntity userAuthEntity){
        entityManager.merge(userAuthEntity);
    }

    public UsersEntity getUserByUuid(final String uuid){
        try{
            return entityManager.createNamedQuery("userByUuid",UsersEntity.class).setParameter("uuid",uuid).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    public void updateUser(final UsersEntity updatedUserEntity){
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

    public String deleteUser(final UsersEntity userEntity){
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
