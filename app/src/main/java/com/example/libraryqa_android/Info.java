package com.example.libraryqa_android;

import java.util.List;

public class Info {
    String graph_answer;//知识库回答
    private List<SearchAsw> search_answer;//信息检索回答

    public class SearchAsw {
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

    public void setGraph_answer(String graph_answer) {
        this.graph_answer = graph_answer;
    }
    public String getGraph_answer(){
        return graph_answer;
    }
    public void setSearch_answer(List<SearchAsw> search_answer){
        this.search_answer = search_answer;
    }
    public List<SearchAsw> getSearch_answer(){
        return search_answer;
    }
    public void setGraph_answerNull(){
        this.graph_answer = null;
    }
    public void setSearch_answerNull(){
        this.search_answer = null;
    }
}
