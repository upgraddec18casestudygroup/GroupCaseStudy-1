package com.upgrad.quora.api.Controller;


import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.AuthorizationService;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private QuestionBusinessService questionBusinessService;

    //Endpoint to create Question
    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
                                                                          , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("accessToken")
                                        final String AcccessToken,
                                        final QuestionRequest questionRequest)throws AuthorizationFailedException {

    String[] bearerToken = AcccessToken.split("Bearer ");
    UserAuthTokenEntity userAuthTokenEntity =authorizationService.verifyAuthToken(bearerToken[1],"create");

    QuestionEntity questionEntity = new QuestionEntity();
    questionEntity.setUuid(UUID.randomUUID().toString());
    questionEntity.setUser(userAuthTokenEntity.getUser());
    questionEntity.setContent(questionRequest.getContent());
    final ZonedDateTime now = ZonedDateTime.now();
    questionEntity.setDate(now);
    final QuestionEntity createdQuestionEntity = questionBusinessService.createQuestion(questionEntity);
    QuestionResponse questionResponse = new QuestionResponse().id(createdQuestionEntity.getUuid()).status("QUESTION CREATED");
    return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
}

//Enpoint to get list of all quetions
    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("accessToken") final String accessToken)
            throws AuthorizationFailedException {
        String[] bearerToken = accessToken.split("Bearer ");

        //checking for exception error by calling authorization service
        UserAuthTokenEntity userAuthTokenEntity = authorizationService.verifyAuthToken(bearerToken[1],"all");
        //declaring list for containing all the question entities
        List<QuestionEntity> questionEntityList =questionBusinessService.getAllQuestions();
        List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<QuestionDetailsResponse>();
        if (!questionEntityList.isEmpty()) {
            for (QuestionEntity questionEntity : questionEntityList) {
                QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
                questionDetailsResponse.setId(questionEntity.getUuid());
                questionDetailsResponse.setContent(questionEntity.getContent());
                questionDetailsResponseList.add(questionDetailsResponse);
            }
        }
        return new ResponseEntity<>(questionDetailsResponseList, HttpStatus.OK);
    }

    //Endpoint to edit question by questionId
    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}",
                    consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent(final QuestionEditRequest questionEditRequest,
                                                                    @PathVariable("questionId") final String questionId,
                                                                    @RequestHeader("accessToken") final String accessToken)
            throws AuthorizationFailedException, InvalidQuestionException {

        String[] bearerToken = accessToken.split("Bearer ");
        UserAuthTokenEntity userAuthTokenEntity = authorizationService.verifyAuthToken(bearerToken[1],"edit");
        QuestionEntity questionEntity = questionBusinessService.getQuestionbyQUuid(questionId, bearerToken[1],"edit");
        questionEntity.setContent(questionEditRequest.getContent());
        QuestionEntity updatedQuestionEntity = questionBusinessService.updateQuestion(questionEntity);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(updatedQuestionEntity.getUuid()).status("QUESTION EDITED");

        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
    }

    //Endpoint to delete question by questionid
    @RequestMapping(method= RequestMethod.DELETE,path="/question/delete/{questionId}",produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") final String questionUuid,
                                                                 @RequestHeader("accessToken") final String accessToken)
            throws AuthorizationFailedException, InvalidQuestionException {

        String [] bearerToken = accessToken.split("Bearer ");
        UserAuthTokenEntity userAuthTokenEntity = authorizationService.verifyAuthToken(bearerToken[1],"delete");
        QuestionEntity questionEntityToDelete = questionBusinessService.getQuestionbyQUuid(questionUuid, bearerToken[1],"delete");

        UserEntity signedinUserEntity =userDao.getUserByUuid(userAuthTokenEntity.getUuid());
        final String Uuid = questionBusinessService.deleteQuestion(questionEntityToDelete,signedinUserEntity);

        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(Uuid).status("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
    }
//Endpoint to get all questions posted by a User
    @RequestMapping(method = RequestMethod.GET, path = "question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@PathVariable("userId")final String userId,
                                                                               @RequestHeader("accessToken") final String accessToken)
            throws AuthorizationFailedException, UserNotFoundException {
        String[] bearerToken = accessToken.split("Bearer ");
        UserAuthTokenEntity userAuthTokenEntity = authorizationService.verifyAuthToken(bearerToken[1],"alluser");
        UserAuthTokenEntity verifyuserAuthTokenEntity= questionBusinessService.getUserAuthTokenByUuid(userId,bearerToken[1]);

        List<QuestionEntity> allQuestionsByUser = new ArrayList<QuestionEntity>();
        allQuestionsByUser.addAll(questionBusinessService.getAllQuestionsByUserId((verifyuserAuthTokenEntity.getUser())));
        List<QuestionDetailsResponse> questionDetailsResponses = new ArrayList<QuestionDetailsResponse>();

        for (QuestionEntity question : allQuestionsByUser) {
            QuestionDetailsResponse questionDetailsResponse=new QuestionDetailsResponse();
            questionDetailsResponses.add(questionDetailsResponse.id(question.getUuid()).content(question.getContent()));
        }

        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponses,HttpStatus.OK);

    }
}


