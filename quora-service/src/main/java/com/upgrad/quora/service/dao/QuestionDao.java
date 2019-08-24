package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
   private EntityManager entityManager;

    public QuestionEntity createQuestion(QuestionEntity questionEntity){
        entityManager.persist(questionEntity);
        return questionEntity;
    }
    public List< QuestionEntity > getAllQuestions() {
        try {
            return entityManager.createNamedQuery("getAllQuestions", QuestionEntity.class).getResultList();
        } catch (NoResultException nre) {

            return null;
        }
    }
    public QuestionEntity getQuestionByQUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("getQuestionByQUuid", QuestionEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    public QuestionEntity updateQuestion(final QuestionEntity questionEntity) {
        return entityManager.merge(questionEntity);
    }

    public List < QuestionEntity > getAllQuestionsByUserId(final UsersEntity user) {
        try {
            return entityManager.createNamedQuery("getAllQuestionsByUserId", QuestionEntity.class).setParameter("user", user).getResultList();
        } catch (NoResultException nre) {

            return null;
        }
    }

    public QuestionEntity getQuestionByQuestionId(String questionId) {
        try {
            return entityManager.createNamedQuery("questionById", QuestionEntity.class).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public String deleteQuestion(final QuestionEntity questionEntity) {
        String uuid = questionEntity.getUuid();
        entityManager.remove(questionEntity);
        return uuid;
    }
}
