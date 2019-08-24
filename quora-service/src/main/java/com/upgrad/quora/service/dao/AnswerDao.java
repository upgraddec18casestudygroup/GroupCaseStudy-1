package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {
    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity createAnswer(final AnswerEntity answerEntity){
        entityManager.persist(answerEntity);
        return answerEntity;
    }
    public AnswerEntity getAnswerByAnswerId(final String answerID) {
        try {
            return entityManager.createNamedQuery("answerByUuid", AnswerEntity.class).setParameter("uuid", answerID).getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }
    }

    public AnswerEntity updateAnswer(final AnswerEntity answerEntity) {
        entityManager.merge(answerEntity);
        return answerEntity;
    }

    public String deleteAnswer(final AnswerEntity answerEntity) {
        String uuid = answerEntity.getUuid();
        entityManager.remove(answerEntity);
        return uuid;
    }
    public List<AnswerEntity> getAnswerByAnswerID(final AnswerEntity answerEntity) throws NoResultException {

        return entityManager.createNamedQuery("getAnswerByAnswerID", AnswerEntity.class).setParameter("answerid", answerEntity).getResultList();
    }
}
