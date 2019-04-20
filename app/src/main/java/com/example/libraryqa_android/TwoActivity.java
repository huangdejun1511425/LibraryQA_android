package com.example.libraryqa_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bean.LvBean;
import com.example.bean.VoiceBean;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class TwoActivity extends Activity {

    private ArrayList<LvBean> dataList = new ArrayList<>();
    private HttpPost httpPost;
    private Info info = new Info();
    private static Handler handler = new Handler();
    //显示回答列表
    private ListView mLv_chat;
    //存储回答列表
    private TwoActivity.MyAdapter mAdapter;
    //显示二维码
    private ImageView mImageView;
    //显示用户问题
    private TextView tvAsk;
    //显示二维码"扫一扫"
    private TextView swTextview;
    //用户问题字符串
    private String kgAsk;
    //判断是否有动作
    private boolean isTouch = true;
    //存储post返回的search_answer
    private List<Info.SearchAsw> searchAnswers;
    //post返回的search_answer中前三条问题
    private SpannableString searchAsk1, searchAsk2, searchAsk3;
    //计时
    private CountTimer countTimerView;
    //显示gif动画
    private GifImageView gifImageView;
    //二维码中间logo
    private Bitmap logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏显示
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
        getWindow().setAttributes(params);
        //固定横屏显示
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(NavUtils.getParentActivityName(TwoActivity.this)!=null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.two_activity);
        //创建讯飞语音实例
        initVoice();
        //获取需要的view
        initView();
    }

    private void initVoice() {
        // 将“12345678”替换成您申请的APPID，申请地址：http://open.voicecloud.cn
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5c88a03c");
    }

    private void initView() {
        //获取gifImageView
        gifImageView = (GifImageView) findViewById(R.id.gifImageView);
        //设置gif动画
        gifImageView.setImageResource(R.drawable.picture1);
        //获取lv_chat
        mLv_chat = (ListView) findViewById(R.id.lv_chat);
        //获取tv_ask1
        tvAsk = (TextView) findViewById(R.id.tv_ask1);
        //获取sw_tv
        swTextview = (TextView) findViewById(R.id.sw_tv);
        mImageView = (ImageView) findViewById(R.id.imageView);
        //获取二维码logo
        logo= BitmapFactory.decodeResource(super.getResources(),R.drawable.lib_logo);
        mAdapter = new TwoActivity.MyAdapter();
        //设置计时器为一分钟
        countTimerView = new CountTimer(60000,1000,
                TwoActivity.this);
        //刷新mLv_chat
        mLv_chat.setAdapter(mAdapter);
        //设置问候语
        String greet = "您好，有什么能为您服务？";
        LvBean greetBean = new LvBean(greet, -1, -1);
        //问候语加入会话列表
        dataList.add(greetBean);
        //刷新lv
        mAdapter.notifyDataSetChanged();
        //lv滚动到最后
        mLv_chat.setSelection(dataList.size() - 1);
        //语音问候
        readAsw(greet);
    }

    public void startVoice(View view) {
        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        final RecognizerDialog iatDialog = new RecognizerDialog(this, null);
        //2.设置听写参数
        iatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        iatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //把解析返回的文本拼接起来
        final StringBuilder sb = new StringBuilder();
        //3.设置回调接口
        iatDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                //使用gson处理语音识别结果并返回字符串
                String voiceStr = parseData(recognizerResult.getResultString());
                //在录音完成时拼接
                sb.append(voiceStr);
                if(isLast) {
                    kgAsk = sb.toString();
                    //显示用户问题
                    tvAsk.setVisibility(View.VISIBLE);
                    tvAsk.setText(kgAsk);
                    httpPost = new HttpPost();
                    //向服务器发送post请求
                    httpPost.hPost(kgAsk, info);
                    //等待服务器返回结果
                    while (info.getGraph_answer() == null) {
                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    dataList.clear();
                    //获取服务器返回的graph_answer
                    String asw = info.getGraph_answer();
                    if(asw.length() == 0){
                        //graph_answer为空，返回search_answer
                        asw += "您说的小图不太明白，您是想问以下问题吗？";
                        searchAnswers = info.getSearch_answer();
                        int id = -1;
                        LvBean aswBean = new LvBean(asw, -1, id);
                        LvBean searchBean1  = new LvBean(searchAnswers.get(0).getQuestion(), 2, -1);
                        LvBean searchBean2  = new LvBean(searchAnswers.get(1).getQuestion(), 3, -1);
                        LvBean searchBean3 = new LvBean(searchAnswers.get(2).getQuestion(), 4, -1);
                        dataList.add(aswBean);
                        dataList.add(searchBean1);
                        dataList.add(searchBean2);
                        dataList.add(searchBean3);
                        searchAsk1 = new SpannableString(searchAnswers.get(0).getQuestion());
                        searchAsk2 = new SpannableString(searchAnswers.get(1).getQuestion());
                        searchAsk3 = new SpannableString(searchAnswers.get(2).getQuestion());
                    }
                    else {
                        LvBean aswBean = new LvBean(asw, -1, -1);
                        dataList.add(aswBean);
                    }
                    info.setGraph_answerNull();
                    //dataList.add(askBean);
                    //刷新lv
                    mAdapter.notifyDataSetChanged();
                    //lv滚动到最后
                    mLv_chat.setSelection(dataList.size() - 1);
                    //合成出回答语音
                    gifImageView.setImageResource(R.drawable.picture3);
                    Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(kgAsk + "\n" + asw, 230, "UTF-8", "H", "0", Color.RED, Color.WHITE, null, logo, 0.2F);
                    mImageView.setImageBitmap(mBitmap);
                    swTextview.setVisibility(View.VISIBLE);
                    readAsw(asw);

                }
            }

            @Override
            public void onError(SpeechError speechError) {
            }
        });
        //4.开始听写
        iatDialog.show();
    }

    public void readAsw(String text) {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(this, null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "70");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        //3.开始合成
        mTts.startSpeaking(text, null);
    }

    private String parseData(String resultString) {
        Gson gson = new Gson();
        VoiceBean voiceBean = gson.fromJson(resultString, VoiceBean.class);
        ArrayList<VoiceBean.STT> ws = voiceBean.ws;

        StringBuilder stringBuilder = new StringBuilder();
        for (VoiceBean.STT stt : ws) {
            String voice = stt.cw.get(0).w;
            stringBuilder.append(voice);
        }
        return stringBuilder.toString();
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView = View.inflate(TwoActivity.this, R.layout.item_layout, null);
                vh = new ViewHolder();
                vh.tv_ask = (TextView) convertView.findViewById(R.id.tv_ask);
                vh.ll_asw = (LinearLayout) convertView.findViewById(R.id.ll_asw);
                vh.tv_asw = (TextView) convertView.findViewById(R.id.tv_asw);
                vh.ll_ask = (LinearLayout) convertView.findViewById(R.id.ll_ask);
                vh.tv_search1 = (TextView) convertView.findViewById(R.id.tv_search1);
                vh.tv_search2 = (TextView) convertView.findViewById(R.id.tv_search2);
                vh.tv_search3 = (TextView) convertView.findViewById(R.id.tv_search3);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            LvBean bean = dataList.get(position);
            if (bean.isAsk == 1) {
                vh.ll_ask.setVisibility(View.VISIBLE);
                vh.ll_asw.setVisibility(View.GONE);
                vh.tv_ask.setText(bean.text);
            }
            if(bean.isAsk == 0){
                //vh.ll_greet.setVisibility(View.VISIBLE);
                vh.ll_ask.setVisibility(View.GONE);
                vh.ll_asw.setVisibility(View.GONE);
                //vh.tv_greet.setText(bean.text);
            }
            if(bean.isAsk == -1){
                vh.ll_asw.setVisibility(View.VISIBLE);
                vh.ll_ask.setVisibility(View.GONE);
                vh.tv_asw.setText(bean.text);
            }
            if(bean.isAsk == 2) {
                vh.ll_asw.setVisibility(View.VISIBLE);
                vh.ll_ask.setVisibility(View.GONE);
                vh.tv_asw.setVisibility(View.GONE);
                vh.tv_search1.setVisibility(View.VISIBLE);
                vh.tv_search2.setVisibility(View.GONE);
                vh.tv_search3.setVisibility(View.GONE);
                //search_answer添加点击事件，可点击查看答案
                searchAsk1 = new SpannableString(bean.text);
                searchAsk1.setSpan(new NoLineClickSpan() {
                    @Override
                    public void onClick(View view) {
                        LvBean askBean  = new LvBean(searchAnswers.get(0).getQuestion(), -1, -1);
                        LvBean aswBean = new LvBean(searchAnswers.get(0).getAnswer(), 1, -1);
                        //dataList.clear();
                        if(dataList.size() > 4){
                            dataList.remove(dataList.size() - 1);
                            dataList.remove(dataList.size() - 1);
                        }
                        dataList.add(askBean);
                        dataList.add(aswBean);
                        mAdapter.notifyDataSetChanged();
                        mLv_chat.setSelection(dataList.size() - 1);
                        mLv_chat.setAdapter(mAdapter);
                    }
                }, 0, bean.text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                //设置问题字体颜色
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
                searchAsk1.setSpan(foregroundColorSpan, 0, bean.text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                vh.tv_search1.setText(searchAsk1);
                vh.tv_search1.setMovementMethod(LinkMovementMethod.getInstance());
            }
            if(bean.isAsk == 3) {
                vh.ll_asw.setVisibility(View.VISIBLE);
                //vh.ll_greet.setVisibility(View.GONE);
                vh.ll_ask.setVisibility(View.GONE);
                vh.tv_asw.setVisibility(View.GONE);
                vh.tv_search1.setVisibility(View.GONE);
                vh.tv_search2.setVisibility(View.VISIBLE);
                vh.tv_search3.setVisibility(View.GONE);
                searchAsk2 = new SpannableString(bean.text);
                searchAsk2.setSpan(new NoLineClickSpan() {
                    @Override
                    public void onClick(View view) {
                        LvBean aswBean  = new LvBean(searchAnswers.get(1).getQuestion(), -1, -1);
                        LvBean askBean = new LvBean(searchAnswers.get(1).getAnswer(), 1, -1);
                        //dataList.clear();
                        if(dataList.size() > 4){
                            dataList.remove(dataList.size() - 1);
                            dataList.remove(dataList.size() - 1);
                        }
                        dataList.add(aswBean);
                        dataList.add(askBean);
                        mAdapter.notifyDataSetChanged();
                        mLv_chat.setSelection(dataList.size() - 1);
                        mLv_chat.setAdapter(mAdapter);
                    }
                }, 0, bean.text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
                searchAsk2.setSpan(foregroundColorSpan, 0, bean.text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                vh.tv_search2.setText(searchAsk2);
                vh.tv_search2.setMovementMethod(LinkMovementMethod.getInstance());
            }
            if(bean.isAsk == 4) {
                vh.ll_asw.setVisibility(View.VISIBLE);
                //vh.ll_greet.setVisibility(View.GONE);
                vh.ll_ask.setVisibility(View.GONE);
                vh.tv_asw.setVisibility(View.GONE);
                vh.tv_search1.setVisibility(View.GONE);
                vh.tv_search2.setVisibility(View.GONE);
                vh.tv_search3.setVisibility(View.VISIBLE);
                searchAsk3 = new SpannableString(bean.text);
                searchAsk3.setSpan(new NoLineClickSpan() {
                    @Override
                    public void onClick(View view) {
                        LvBean aswBean  = new LvBean(searchAnswers.get(2).getQuestion(), -1, -1);
                        LvBean askBean = new LvBean(searchAnswers.get(2).getAnswer(), 1, -1);
                        //dataList.clear();
                        if(dataList.size() > 4){
                            dataList.remove(dataList.size() - 1);
                            dataList.remove(dataList.size() - 1);
                        }
                        dataList.add(aswBean);
                        dataList.add(askBean);
                        mAdapter.notifyDataSetChanged();
                        mLv_chat.setSelection(dataList.size() - 1);
                        mLv_chat.setAdapter(mAdapter);
                    }
                }, 0, bean.text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
                searchAsk3.setSpan(foregroundColorSpan, 0, bean.text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                vh.tv_search3.setText(searchAsk3);
                vh.tv_search3.setMovementMethod(LinkMovementMethod.getInstance());

            }
            return convertView;
        }
    }

    //**********************无操作一段时间后显示屏保***********************//
    private void timeStart(){
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                countTimerView.start();

            }
        });
    }

    //主要的方法，重写dispatchTouchEvent
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            //获取触摸动作，如果ACTION_UP，计时开始。
            case MotionEvent.ACTION_UP:
                isTouch = true;
                countTimerView.start();
                break;
            //否则其他动作计时取消
            default:
                isTouch = false;
                countTimerView.cancel();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    protected void onPause() {
        super.onPause();
        countTimerView.cancel();
    }
    @Override
    protected void onResume() {

        super.onResume();
        timeStart();
    }

    public class CountTimer extends CountDownTimer {
        private Context context;

        /**
         * 参数 millisInFuture       倒计时总时间（如60S，120s等）
         * 参数 countDownInterval    渐变时间（每次倒计1s）
         */
        public CountTimer(long millisInFuture, long countDownInterval,Context context) {
            super(millisInFuture, countDownInterval);
            this.context=context;
        }
        // 计时完毕时触发
        @Override
        public void onFinish() {
            if (isTouch) {
                isTouch = false;
                Intent intent = new Intent(TwoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        // 计时过程显示
        @Override
        public void onTick(long millisUntilFinished) {
        }
    }
}
