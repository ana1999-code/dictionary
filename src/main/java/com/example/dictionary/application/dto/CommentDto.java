package com.example.dictionary.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CommentDto {

    private Integer id;

    @NotBlank
    private String text;

    @NotNull
    private UserDto commenter;

    @NotNull
    private LocalDate commentedAt;

    @NotNull
    private WordDto word;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserDto getCommenter() {
        return commenter;
    }

    public void setCommenter(UserDto commenter) {
        this.commenter = commenter;
    }

    public LocalDate getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(LocalDate commentedAt) {
        this.commentedAt = commentedAt;
    }

    public WordDto getWord() {
        return word;
    }

    public void setWord(WordDto word) {
        this.word = word;
    }
}
