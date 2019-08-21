package com.upgrad.quora.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Table(name = "question")
@NamedQueries({

        @NamedQuery(name = "getQuestionByQUuid", query = "select q from QuestionEntity q where q.uuid =:uuid"),
        @NamedQuery(name= "getAllQuestionsByUserId",query = "select q from QuestionEntity q where q.user = :user"),
        @NamedQuery(name= "getAllQuestions",query = "select q from QuestionEntity q "),
        @NamedQuery(name= "getQuestionById",query = "select q from QuestionEntity q where q.uuid = :uuid")

})

public class QuestionEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer question_Id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "content")
    @NotNull
    @Size(max = 500)
    private String content;

    @Column(name = "date")
    @NotNull
    private ZonedDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;

    public Integer getQuestion_id() {return question_Id;}
    public void setQuestion_id(Integer question_id) {this.question_Id = question_id;}

    public String getUuid() {return uuid;}
    public void setUuid(String uuid) {this.uuid = uuid;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public ZonedDateTime getDate() {return date;}
    public void setDate(ZonedDateTime date) {this.date = date;}

    public UserEntity getUser() {
        return user;
    }
    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
