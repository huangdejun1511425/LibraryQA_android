package com.example.libraryqa_android;

public class SearchAnswer {
    String answer;
    String question;
    String score;

    public String getQuestion(){
        return question;
    }
    public String getAnswer(){
        return answer;
    }
    public String getScore(){
        return score;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
    public void setAnswer(String answer){
        this.question = answer;
    }
    public void setScore(String score){
        this.score = score;
    }
}
