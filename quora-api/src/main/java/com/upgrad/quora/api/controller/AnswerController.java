package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.business.AuthorizationService;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.dao.UsersDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UsersEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AnswerController {
    @Autowired
    private UsersDao userDao;
    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private AnswerBusinessService answerBusinessService;
    @Autowired
    private QuestionBusinessService questionBusinessService;

    //Endpoint to post an answer of a Question
    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@RequestHeader("accessToken")
                                                           final String AcccessToken,
                                                         final AnswerRequest answerRequest,
                                                       @PathVariable("questionId") final String questionId)
            throws AuthorizationFailedException, InvalidQuestionException {

        String[] bearerToken = AcccessToken.split("Bearer ");
        UserAuthTokenEntity userAuthTokenEntity =authorizationService.verifyAuthToken(bearerToken[1],"postAnswer");
        QuestionEntity questionEntity = questionBusinessService.getQuestionbyQUuid(questionId, bearerToken[1],"postAnswer");
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setUser(userAuthTokenEntity.getUser());
        answerEntity.setAnswer(answerRequest.getAnswer());
        final ZonedDateTime now = ZonedDateTime.now();
        answerEntity.setDate(now);
        final AnswerEntity createdAnsEntity = answerBusinessService.createAnswer(answerEntity);

        AnswerResponse answerResponse = new AnswerResponse().id(createdAnsEntity.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

    //Endpoint to edit answer by answerId
    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(final AnswerEditRequest answerEditRequest,
                                                                  @PathVariable("answerId") final String answerUuId,
                                                                  @RequestHeader("accessToken") final String accessToken)
            throws AuthorizationFailedException {

        String[] bearerToken = accessToken.split("Bearer ");
        UserAuthTokenEntity userAuthTokenEntity = authorizationService.verifyAuthToken(bearerToken[1],"editAnswer");
        AnswerEntity answerEntity = answerBusinessService.getAnswerbyQUuid(answerUuId, bearerToken[1],"editAnswer");
        answerEntity.setAnswer(answerEditRequest.getContent());
        AnswerEntity updatedAnswerEntity = answerBusinessService.updateAnswer(answerEntity);
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(updatedAnswerEntity.getAnswer()).status("QUESTION EDITED");

        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
    }

    //Endpoint to delete question by questionid
    @RequestMapping(method= RequestMethod.DELETE,path="/answer/delete/{answerId}",produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable("answerId") final String answerUuid,
                                                               @RequestHeader("accessToken") final String accessToken)
            throws AuthorizationFailedException, InvalidQuestionException {

        String [] bearerToken = accessToken.split("Bearer ");
        UserAuthTokenEntity userAuthTokenEntity = authorizationService.verifyAuthToken(bearerToken[1],"deleteAnswer");
        AnswerEntity answerEntityToDelete = answerBusinessService.getAnswerbyQUuid(answerUuid, bearerToken[1],"deleteAnswer");

        UsersEntity signedinUserEntity =userDao.getUserByUuid(userAuthTokenEntity.getUuid());
        final String Uuid = answerBusinessService.deleteAnswer(answerEntityToDelete,signedinUserEntity);

        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(Uuid).status("ANSWER DELETED");

        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }
}
