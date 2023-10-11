package com.wl.cfwl;


import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wl.adapter.DataAdapter;
import com.wl.adapter.DataztAdapter;
import com.wl.entry.JZXX;
import com.wl.entry.JZZT;
import com.wl.entry.ResponseData;
import com.wl.entry.ResponseData1;
import com.wl.entry.ResponseData2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Datasum extends Activity {
    private ListView projectgd;
    private DataAdapter dataAdapter;
    private LinearLayout l1, l2, l3, l4, n1, n2;
    private int list_delete;
    private WebView web1, web2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


// 检查权限


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.datasum);
        n1 = (LinearLayout) findViewById(R.id.n1);
        n2 = (LinearLayout) findViewById(R.id.n2);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.heightPixels / 2 + 50;
        Log.e("宽度", "" + screenWidth);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // 宽度设置为与父容器相同
                screenWidth// 高度设置为适应内容
        );
        n1.setLayoutParams(layoutParams);
        n2.setLayoutParams(layoutParams);
        jzzt();
        jzxx();
        Log.e("wedwdaw", "wdawada");
        getData();
        web1 = (WebView) findViewById(R.id.web1);
        web2 = (WebView) findViewById(R.id.web2);
        web2.loadUrl("http://rinotrack.unistrong.com/");
        web2.getSettings().setDomStorageEnabled(true);
        web1.getSettings().setDomStorageEnabled(true);
        web1.loadUrl("http://114.117.161.248:8081/");
        web1.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        web1.getSettings().setJavaScriptEnabled(true);
        web1.getSettings().setSupportZoom(true);

        web1.getSettings().setBuiltInZoomControls(true);
        web2.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        web2.getSettings().setJavaScriptEnabled(true);
        web1.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });
        web2.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });
        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (LinearLayout) findViewById(R.id.l2);
        l3 = (LinearLayout) findViewById(R.id.l3);
        l4 = (LinearLayout) findViewById(R.id.l4);
        layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // 宽度设置为与父容器相同
                screenWidth + 200// 高度设置为适应内容
        );
        l3.setLayoutParams(layoutParams);
        l4.setLayoutParams(layoutParams);
        jzxx = (Button) findViewById(R.id.jzxx);

        jzxx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                l2.setVisibility(View.VISIBLE);
                l1.setVisibility(View.GONE);
                l3.setVisibility(View.GONE);
                l4.setVisibility(View.GONE);
                getData();
                jzxx.setBackgroundResource(R.drawable.ic_button_g);
                jzxx.setTextColor(Color.GREEN);
                jzzt.setBackgroundResource(R.drawable.ic_button_r);
                jzzt.setTextColor(Color.RED);
                cos.setBackgroundResource(R.drawable.ic_button_r);
                cos.setTextColor(Color.RED);
                hn.setBackgroundResource(R.drawable.ic_button_r);
                hn.setTextColor(Color.RED);
            }
        });
        jzzt = (Button) findViewById(R.id.jzzt);

        jzzt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                l1.setVisibility(View.VISIBLE);
                l2.setVisibility(View.GONE);
                l3.setVisibility(View.GONE);
                l4.setVisibility(View.GONE);
                getData1();
                jzzt.setBackgroundResource(R.drawable.ic_button_g);
                jzzt.setTextColor(Color.GREEN);
                jzxx.setBackgroundResource(R.drawable.ic_button_r);
                jzxx.setTextColor(Color.RED);
                cos.setBackgroundResource(R.drawable.ic_button_r);
                cos.setTextColor(Color.RED);
                hn.setBackgroundResource(R.drawable.ic_button_r);
                hn.setTextColor(Color.RED);

            }
        });
        cos = (Button) findViewById(R.id.cos);

        cos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l1.setVisibility(View.GONE);
                l2.setVisibility(View.GONE);
                l3.setVisibility(View.VISIBLE);
                l4.setVisibility(View.GONE);
                cos.setBackgroundResource(R.drawable.ic_button_g);
                cos.setTextColor(Color.GREEN);
                jzxx.setBackgroundResource(R.drawable.ic_button_r);
                jzxx.setTextColor(Color.RED);
                jzzt.setBackgroundResource(R.drawable.ic_button_r);
                jzzt.setTextColor(Color.RED);
                hn.setBackgroundResource(R.drawable.ic_button_r);
                hn.setTextColor(Color.RED);

            }
        });
        hn = (Button) findViewById(R.id.hn);

        hn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l1.setVisibility(View.GONE);
                l2.setVisibility(View.GONE);
                l3.setVisibility(View.GONE);
                l4.setVisibility(View.VISIBLE);
                hn.setBackgroundResource(R.drawable.ic_button_g);
                hn.setTextColor(Color.GREEN);
                jzxx.setBackgroundResource(R.drawable.ic_button_r);
                jzxx.setTextColor(Color.RED);
                jzzt.setBackgroundResource(R.drawable.ic_button_r);
                jzzt.setTextColor(Color.RED);
                cos.setBackgroundResource(R.drawable.ic_button_r);
                cos.setTextColor(Color.RED);

            }
        });
    }

    public ArrayList<JZXX> infoList;

    public void getData() {
        list = new ArrayList<Object>();

        dataAdapter = new DataAdapter(Datasum.this, list);

        projectgd.setAdapter(dataAdapter);

        dataAdapter.setSelectedItem(list_delete);
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputParams1 inputParams = new InputParams1();
                inputParams.setorgid("00000000-0000-0000-0000-000000000000");

                // 将输入参数序列化为JSON字符串
                String json = new Gson().toJson(inputParams);
                Log.e("複製", json);

                long time = System.currentTimeMillis();
                try {
                    URL url = new URL("http://rinotrack.unistrong.com/v1.0/webapi/generateSign2?accessKeyId=75a7759acd1c4a228f188846b3dade5a&accessKeySecret=k0Yac7Cu7wy1BGr9D9gujDrtoCh7p621ak4zE1YTzWI=&timestamp=" + time);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // 设置请求方法为POST
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // 发送请求
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(json.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    // 获取响应
                    int responseCode = connection.getResponseCode();
                    // 可选：设置请求头、设置超时时间等

                    // 获取响应码

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // 读取响应内容
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        reader.close();

                        String response = stringBuilder.toString();
                        Gson gson = new Gson();
                        ResponseData2 responseDataArray = gson.fromJson(response, ResponseData2.class);


                        Log.e("複製", response);
                        Log.e("複製", responseDataArray.data);
                        url = new URL("http://rinotrack.unistrong.com/v1.0/webapi/site/getSiteByOrgid?sign=" + responseDataArray.data + "&accessKeyId=75a7759acd1c4a228f188846b3dade5a&timestamp=" + time);
                        connection = (HttpURLConnection) url.openConnection();

                        // 设置请求方法为POST
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setDoOutput(true);

                        // 发送请求
                        outputStream = connection.getOutputStream();
                        outputStream.write(json.getBytes());
                        outputStream.flush();
                        outputStream.close();
                        responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            // 读取响应内容
                            inputStream = connection.getInputStream();
                            reader = new BufferedReader(new InputStreamReader(inputStream));
                            stringBuilder = new StringBuilder();

                            while ((line = reader.readLine()) != null) {
                                stringBuilder.append(line);
                            }
                            reader.close();

                            response = stringBuilder.toString();
                            Log.e("複製", response);

                            // 在UI线程中处理响应内容
                            String finalResponse = response;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // TODO: 处理响应内容
                                    Gson gson = new Gson();
                                    ResponseData responseData = gson.fromJson(finalResponse, ResponseData.class);
                                    List<ResponseData.Site> sites = responseData.data;
                                    infoList = new ArrayList<JZXX>();
                                    int i_c = 1;
                                    for (int i = 0; i < sites.size(); i++) {

                                        JZXX abc = new JZXX();
                                        abc.id = i_c++;
                                        abc.name = sites.get(i).sitename;
                                        abc.b = sites.get(i).longitude;
                                        abc.l = sites.get(i).latitude;
                                        abc.dtbtl = sites.get(i).radiofrequency;
                                        abc.ip = sites.get(i).ip;
                                        abc.port = sites.get(i).port;
                                        abc.gz = sites.get(i).mountpoint;
                                        abc.mm = sites.get(i).passward;
                                        abc.qy = sites.get(i).mregion;
                                        if (abc.qy == null) abc.qy = "";
                                        else {
                                            String[] qqqq = sites.get(i).mregion.split(":");
                                            if (qqqq.length == 2) {
                                                String[] qqqq1 = qqqq[0].split("-");
                                                if (qqqq1.length == 2) {
                                                    abc.qy = qqqq1[1];
                                                    abc.dqy = qqqq1[0];
                                                } else
                                                    abc.qy = qqqq[0];
                                                abc.user = qqqq[1];
                                            } else {
                                                String[] qqqq1 = qqqq[0].split("-");
                                                if (qqqq1.length == 2) {
                                                    abc.qy = qqqq1[1];
                                                    abc.dqy = qqqq1[0];
                                                } else
                                                    abc.qy = qqqq[0];
                                            }
                                        }
                                        infoList.add(abc);

                                    }
                                    Collections.sort(infoList, new Comparator<JZXX>() {
                                        @Override
                                        public int compare(JZXX jzxx1, JZXX jzxx2) {
                                            return jzxx1.name.compareTo(jzxx2.name);
                                        }
                                    });
                                    list = new ArrayList<Object>();
                                    int i_cc = 1;
                                    for (JZXX info : infoList) {
                                        info.id = i_cc++;
                                        list.add(info);
                                    }
                                    dataAdapter = new DataAdapter(Datasum.this, list);

                                    projectgd.setAdapter(dataAdapter);

                                    dataAdapter.setSelectedItem(list_delete);


                                }
                            });
                        } else {
                            // 处理请求失败
                        }
                        connection.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    // 处理异常
                }
            }
        }).start();

    }

    private ImageView iback, irefesh, imageview, isearch;
    private EditText mEditText;
    private Button jzxx, jzzt, cos, hn;

    public void jzzt() {
        isearch = (ImageView) findViewById(R.id.isearch);

        //设置删除图片的点击事件
        isearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < infoList.size(); i++) {

                    if (infoList.get(i).name.contains(mEditText.getText() + "") || infoList.get(i).gz.contains(mEditText.getText() + "")) {
                        Log.e("qqqq111", "" + i);
                        projectgd.smoothScrollToPosition(i);
                        dataAdapter.setSelectedItem(i);
                        dataAdapter.notifyDataSetChanged();
                        Log.e("qqqq", "" + i);
                        return;
                    }

                }

                setDilog("没有搜索到相关内容");

            }
        });
        mEditText = (EditText) findViewById(R.id.edittext);
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        //EditText添加监听
        mEditText.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }//文本改变之前执行

            @Override
            //文本改变的时候执行
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果长度为0
                if (s.length() == 0) {
                    //隐藏“删除”图片
                    imageview.setVisibility(View.GONE);
                } else {//长度不为0
                    //显示“删除图片”
                    imageview.setVisibility(View.VISIBLE);
                    //显示ListView

                }
            }

            public void afterTextChanged(Editable s) {
            }//文本改变之后执行
        });
        imageview = (ImageView) findViewById(R.id.imageview);

        //设置删除图片的点击事件
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");

            }
        });
        irefesh = (ImageView) findViewById(R.id.irefesh);

        //设置删除图片的点击事件
        irefesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();

            }
        });
        iback = (ImageView) findViewById(R.id.iback);

        //设置删除图片的点击事件
        iback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        projectgd = (ListView) findViewById(R.id.projectgd);

        projectgd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                list_delete = position;
                dataAdapter.setSelectedItem(position);

                dataAdapter.notifyDataSetChanged();

                JZXX info = (JZXX) projectgd.getItemAtPosition(position);

                //获取内容


            }
        });

    }

    private List<Object> list;

    public void setDilog(String abc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Datasum.this);

// 设置对话框的标题和消息
        builder.setTitle("提示");
        builder.setMessage(Html.fromHtml(abc));

// 设置对话框的按钮及其点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 处理确定按钮的点击事件
                dialog.dismiss();
            }
        });


// 创建并显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private ListView projectgd1;
    private DataztAdapter dataAdapter1;
    private int list_delete1;
    public ArrayList<JZZT> infoList1;

    public class InputParams {
        private String[] infoType;

        public String[] getInfoType() {
            return infoType;
        }

        public void setInfoType(String[] infoType) {
            this.infoType = infoType;
        }
    }

    public class InputParams1 {
        private String orgid;

        public String getorgid() {
            return orgid;
        }

        public void setorgid(String orgid) {
            this.orgid = orgid;
        }
    }

    public void getData1() {
        list1 = new ArrayList<Object>();

        dataAdapter1 = new DataztAdapter(Datasum.this, list1);

        projectgd1.setAdapter(dataAdapter1);

        dataAdapter1.setSelectedItem(list_delete1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputParams inputParams = new InputParams();
                inputParams.setInfoType(new String[]{"AccessPoint4500", "AccessPoint8208"});

                // 将输入参数序列化为JSON字符串
                String json = new Gson().toJson(inputParams);

                try {
                    URL url = new URL("http://114.117.161.248:8081/api/basicInfo");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // 设置请求方法为POST
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // 发送请求
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(json.getBytes());
                    outputStream.flush();
                    outputStream.close();

                    // 获取响应
                    int responseCode = connection.getResponseCode();
                    // 可选：设置请求头、设置超时时间等

                    // 获取响应码

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // 读取响应内容
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        reader.close();

                        String response = stringBuilder.toString();
                        Log.e("複製", response);

                        // 在UI线程中处理响应内容
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO: 处理响应内容
                                Gson gson = new Gson();

                                ResponseData1[] responseDataArray = gson.fromJson(response, ResponseData1[].class);


                                infoList1 = new ArrayList<JZZT>();
                                int i_c = 1;
                                for (int i = 0; i < responseDataArray[0].accessPoint.size(); i++) {

                                    JZZT abc = new JZZT();

                                    abc.id = i_c++;
                                    abc.jxd = responseDataArray[0].accessPoint.get(i);
                                    abc.jzmc = responseDataArray[0].accessPoint.get(i);
                                    abc.ljs = responseDataArray[0].number.get(i);
                                    abc.gdj = responseDataArray[0].fixedSolutionNum.get(i);
                                    abc.fdj = responseDataArray[0].floatSolutionNum.get(i);
                                    abc.cfj = responseDataArray[0].diffrentialPosNum.get(i);
                                    abc.fcfdw = responseDataArray[0].notDiffrentialPosNum.get(i);
                                    abc.wdd = responseDataArray[0].notAlignedNum.get(i);
                                    infoList1.add(abc);

                                }
                                Collections.sort(infoList, new Comparator<JZXX>() {
                                    @Override
                                    public int compare(JZXX jzxx1, JZXX jzxx2) {
                                        return jzxx1.name.compareTo(jzxx2.name);
                                    }
                                });
                                list1 = new ArrayList<Object>();
                                int i_cc = 1;
                                for (JZZT info : infoList1) {
                                    info.id = i_cc++;
                                    list1.add(info);
                                }

                                dataAdapter1 = new DataztAdapter(Datasum.this, list1);

                                projectgd1.setAdapter(dataAdapter1);

                                dataAdapter1.setSelectedItem(list_delete1);


                            }
                        });
                    } else {
                        // 处理请求失败
                    }

                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    // 处理异常
                }
            }
        }).start();

    }

    private ImageView iback1, irefesh1, imageview1, isearch1;
    private EditText mEditText1;
    private TextView qy, zdname;

    public void jzxx() {
        isearch1 = (ImageView) findViewById(R.id.isearch1);
        qy = (TextView) findViewById(R.id.qy);
        zdname = (TextView) findViewById(R.id.zdname);
        zdname.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Collections.sort(infoList, new Comparator<JZXX>() {
                    @Override
                    public int compare(JZXX jzxx1, JZXX jzxx2) {
                        return jzxx1.name.compareTo(jzxx2.name);
                    }
                });
                list = new ArrayList<Object>();
                int i_cc = 1;
                for (JZXX info : infoList) {
                    info.id = i_cc++;
                    list.add(info);
                }
                dataAdapter = new DataAdapter(Datasum.this, list);

                projectgd.setAdapter(dataAdapter);

                dataAdapter.setSelectedItem(list_delete);

            }
        });
        qy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Collections.sort(infoList, new Comparator<JZXX>() {
                    @Override
                    public int compare(JZXX jzxx1, JZXX jzxx2) {
                        return jzxx1.qy.compareTo(jzxx2.qy);
                    }
                });
                list = new ArrayList<Object>();
                int i_cc = 1;
                for (JZXX info : infoList) {
                    info.id = i_cc++;
                    list.add(info);
                }
                dataAdapter = new DataAdapter(Datasum.this, list);

                projectgd.setAdapter(dataAdapter);

                dataAdapter.setSelectedItem(list_delete);


            }
        });
        //设置删除图片的点击事件
        isearch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < infoList1.size(); i++) {

                    if (infoList1.get(i).jxd.contains(mEditText1.getText() + "") || infoList1.get(i).jzmc.contains(mEditText1.getText() + "")) {
                        Log.e("qqqq111", "" + i);
                        projectgd1.smoothScrollToPosition(i);
                        dataAdapter1.setSelectedItem(i);
                        dataAdapter1.notifyDataSetChanged();
                        Log.e("qqqq", "" + i);
                        return;
                    }

                }

                setDilog("没有搜索到相关内容");

            }
        });
        mEditText1 = (EditText) findViewById(R.id.edittext1);
        mEditText1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        //EditText添加监听
        mEditText1.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }//文本改变之前执行

            @Override
            //文本改变的时候执行
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //如果长度为0
                if (s.length() == 0) {
                    //隐藏“删除”图片
                    imageview1.setVisibility(View.GONE);
                } else {//长度不为0
                    //显示“删除图片”
                    imageview1.setVisibility(View.VISIBLE);
                    //显示ListView

                }
            }

            public void afterTextChanged(Editable s) {
            }//文本改变之后执行
        });
        imageview1 = (ImageView) findViewById(R.id.imageview1);

        //设置删除图片的点击事件
        imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText1.setText("");

            }
        });
        irefesh1 = (ImageView) findViewById(R.id.irefesh1);

        //设置删除图片的点击事件
        irefesh1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData1();

            }
        });
        iback1 = (ImageView) findViewById(R.id.iback1);

        //设置删除图片的点击事件
        iback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        projectgd1 = (ListView) findViewById(R.id.projectgd1);

        projectgd1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                list_delete1 = position;
                dataAdapter1.setSelectedItem(position);

                dataAdapter1.notifyDataSetChanged();

                JZZT info = (JZZT) projectgd1.getItemAtPosition(position);

                //获取内容


            }
        });

    }

    private List<Object> list1;


}