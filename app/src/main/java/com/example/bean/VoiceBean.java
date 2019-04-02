package com.example.bean;

import java.util.ArrayList;

/**
 * Created by sakamichi on 16/10/26.
 */

public class VoiceBean {
    public ArrayList<STT> ws;

    public class STT {
        public ArrayList<Text> cw;
    }

    public class Text {
        public String w;
    }
}
