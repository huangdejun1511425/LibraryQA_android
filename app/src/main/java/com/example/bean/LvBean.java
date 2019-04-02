package com.example.bean;

/**
 * Create
 */

public class LvBean {
    public LvBean(String text, int isAsk, int imgId) {
        this.text = text;
        this.isAsk = isAsk;//-1:graph_answer; 0:greet; 1:ask; 2:search_answer1; 3:search_answer2; 4:search_answer3
        this.imgId = imgId;
    }

    public String text;
    public int isAsk;
    public int imgId;
}
