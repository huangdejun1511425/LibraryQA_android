package com.example.libraryqa_android;

public class SearchAnswer {
    /**
     * 获取信息检索结果
     *
     * @param answer 相似问题答案
     * @param question 相似问题
     * @param score 问题相似度权重
     */
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
