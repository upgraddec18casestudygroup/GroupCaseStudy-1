package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerBusinessService {
    @Autowired
    private AnswerDao answerDao;
    @Autowired
    private UsersDao userDao;
    @Autowired
    private AuthorizationService authorizationService;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(final AnswerEntity answerEntity) {
        return answerDao.createAnswer(answerEntity);
    }
//Get Answer with check of owner / admin user
    public AnswerEntity getAnswerbyQUuid(final String answerId,final String accessToken, final String Task)
            throws  AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        AnswerEntity existingAnswerEntity = answerDao.getAnswerByAnswerId(answerId);

        if (existingAnswerEntity.getUser().getUserName().equalsIgnoreCase(userAuthTokenEntity.getUser().getUserName())) {
            return existingAnswerEntity;
        } else {
            if(Task=="editanswer")
                throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
            else
                throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");
        }
    }
    //To update Answer
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity updateAnswer(final AnswerEntity updatedAnswerEntity){
        return answerDao.updateAnswer(updatedAnswerEntity);
    }

    //To delete Answer
    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteAnswer(final AnswerEntity answerEntityToDelete, final UsersEntity signedinUserEntity )
            throws AuthorizationFailedException {

        if (signedinUserEntity.getRole().equalsIgnoreCase("admin")
                ||(answerEntityToDelete.getUser().getUserName()==signedinUserEntity.getUserName())) {
            return answerDao.deleteAnswer(answerEntityToDelete);
        }
        else{
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }
    }
}
