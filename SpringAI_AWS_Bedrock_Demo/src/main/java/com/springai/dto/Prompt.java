package com.springai.dto;

public class Prompt {
    private String question;

    public Prompt() {
    }

    public Prompt(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
