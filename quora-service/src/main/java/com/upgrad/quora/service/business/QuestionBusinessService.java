package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionBusinessService {

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AuthorizationService authorizationService;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(final QuestionEntity questionEntity) {
        return questionDao.createQuestion(questionEntity);
    }
    public List<QuestionEntity> getAllQuestions() {
            List<QuestionEntity> questionEntityList= new ArrayList<>();
            questionEntityList= questionDao.getAllQuestions();
        return questionEntityList;
    }

public QuestionEntity getQuestionbyQUuid(final String questionId,final String accessToken, final String Task)
        throws InvalidQuestionException, AuthorizationFailedException {
    UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        QuestionEntity existingQuestionEntity = questionDao.getQuestionByQUuid(questionId);
    if (existingQuestionEntity == null) {
        throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
    }
    if (existingQuestionEntity.getUser().getUserName().equalsIgnoreCase(userAuthTokenEntity.getUser().getUserName())) {
        return existingQuestionEntity;
    } else {
        if(Task=="edit")
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        else
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
    }
}
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity updateQuestion(final QuestionEntity updatedQuestionEntity){
        return questionDao.updateQuestion(updatedQuestionEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteQuestion(final QuestionEntity questionEntityToDelete, final UserEntity signedinUserEntity ) throws AuthorizationFailedException {

        if (signedinUserEntity.getRole().equalsIgnoreCase("admin")
                ||(questionEntityToDelete.getUser().getUserName()==signedinUserEntity.getUserName())) {
            return questionDao.deleteQuestion(questionEntityToDelete);
        }
        else{
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }
    }
    public List<QuestionEntity> getAllQuestionsByUserId (final UserEntity user){
        return questionDao.getAllQuestionsByUserId(user);
    }
    public UserAuthTokenEntity getUserAuthTokenByUuid(final String userUuid,final String accessToken) throws UserNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        if (userDao.getUserByUuid(userUuid) == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
        return userDao.getUserAuthTokenByUuid(userUuid);
    }
}
