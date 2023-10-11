package com.wl.cfwl;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.gson.Gson;
import com.wl.CF.BackgroundWork;
import com.wl.adapter.DataztAdapter;
import com.wl.comom.CommonEnum;
import com.wl.comom.NetHelper;
import com.wl.entry.JZXX;
import com.wl.entry.JZZT;
import com.wl.entry.ResponseData1;
import com.wl.entry.SourceNode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import jxl.Sheet;
import jxl.Workbook;

public class Login extends Activity {

    private AppBarConfiguration appBarConfiguration;
    private EditText username,password;
    private Button login;
    private TextView txt;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            // 如果没有定位权限，则向用户请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}, REQUEST_LOCATION_PERMISSION);
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

            String usernametext = sharedPreferences.getString("username", "");
            String passwordtext = sharedPreferences.getString("password", "");
            username.setText(usernametext);
            password.setText(passwordtext);
            Intent intent = getIntent();
            String  abc = intent.getStringExtra("message");

            if (abc!=null) {

            } else {
                new MyAsyncTask().execute();

            }

            // 如果已经有定位权限，则执行相应操作
            // 这里可以调用启动地图定位的代码
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (!allPermissionsGranted) {
                // 如果有任何一个权限被拒绝，立即退出应用程序
                finishAffinity();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

                String usernametext = sharedPreferences.getString("username", "");
                String passwordtext = sharedPreferences.getString("password", "");
                username.setText(usernametext);
                password.setText(passwordtext);
                Intent intent = getIntent();
                String  abc = intent.getStringExtra("message");

                if (abc!=null) {

                } else {
                    new MyAsyncTask().execute();

                }

                // 用户授予了所有权限，执行相应操作
                // 这里可以调用启动地图定位的代码
            }
        }
    }

    private void login()
   {

       txt.setText("");

       new Thread(new Runnable() {
           @Override
           public void run() {

               try {
                   URL url = new URL("http://114.117.161.248:4502/SQL_Date/Equipment_link.asmx"+ "/getQY");
                   HttpURLConnection connection = null;

                   connection=(HttpURLConnection)url.openConnection();

                   //请求方式为POST
                   connection.setRequestMethod("POST");
                   //设置请求超时时间
                   connection.setConnectTimeout(5000);
                   //定义输出流
                   OutputStream out=connection.getOutputStream();
                   //你需要输出的数据
                   String data="ID="+username.getText()+"&password="+password.getText();
                   //输出数据，并转化为字节型式
                   out.write(data.getBytes());
                   //读取服务器的响应，通过UTF-8格式读取，因为服务器的中文格式为UTF-8
                   BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
                   String str="";
                   //对服务器的响应做出不同的处理方式，由于android中不能在子线程改变UI，所以通过handler进行转换
                   while ((str=reader.readLine())!=null){


                   }
                   if(str.length()>0)
                   {
                       txt.setText(str);
                       SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                       SharedPreferences.Editor editor = sharedPreferences.edit();
                       editor.putString("username", username.getText()+"");
                       editor.putString("password", password.getText()+"");
                       editor.apply();
                       Intent intent = new Intent(Login.this, MainSumActivity.class);

                       intent.putExtra("name", username.getText()+"");
                       intent.putExtra("message", str);
                       startActivity(intent);
                       finish();
                   }
                   else
                   {
                       txt.setText("賬號或者密碼錯誤！");
                   }





               } catch (IOException e) {
                   e.printStackTrace();
                   // 处理异常
               }
           }
       }).start();

   }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


// 检查权限


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login);
        txt=(TextView) findViewById(R.id.txt);
        username=(EditText)findViewById(R.id.username);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (charSequence.toString().trim().length() == 0) {
                            // EditText 中没有输入
                            if(password.getText().length()==0||username.getText().length()==0) {
                                login.setEnabled(false);
                                login.setBackgroundColor(Color.GRAY);
                            }
                        } else {
                            if(password.getText().length()>0&&username.getText().length()>0) {
                                login.setEnabled(true);
                                login.setBackgroundColor(Color.BLUE);
                            }
                            // EditText 中有输入
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        password=(EditText)findViewById(R.id.password);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (charSequence.toString().trim().length() == 0) {
                            // EditText 中没有输入
                            if(password.getText().length()==0||username.getText().length()==0)
                            {     login.setEnabled(false);
                            login.setBackgroundColor(Color.GRAY);}
                        } else {
                            if(password.getText().length()>0&&username.getText().length()>0)
                            {   login.setEnabled(true);
                            login.setBackgroundColor(Color.BLUE);}
                            // EditText 中有输入
                        }
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new MyAsyncTask().execute();
            }
        });

        requestLocationPermission();
    }
    private class MyAsyncTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Login.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage(getString(R.string.progress_login));
            dialog.setTitle(R.string.option);
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL("http://114.117.161.248:4502/SQL_Date/Equipment_link.asmx/getQY");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //请求方式为POST
                connection.setRequestMethod("POST");
                //设置请求超时时间
                connection.setConnectTimeout(5000);
                //定义输出流
                OutputStream out = connection.getOutputStream();
                //你需要输出的数据
                String data = "ID=" + username.getText() + "&password=" + password.getText();
                //输出数据，并转化为字节型式
                out.write(data.getBytes());

                //读取服务器的响应，通过UTF-8格式读取，因为服务器的中文格式为UTF-8
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String str = "";
                while ((str = reader.readLine()) != null) {
                    response.append(str);
                }

                return response.toString();

            } catch (IOException e) {
                e.printStackTrace();
                // 处理异常
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {


            if (result != null) {
                result=result.replace("\"", "");
                if(result.length()>0)
                {
                    //txt.setText(result);
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username.getText()+"");
                    editor.putString("password", password.getText()+"");
                    editor.apply();
                    Intent intent = new Intent(Login.this, MainSumActivity.class);

                    intent.putExtra("name", username.getText()+"");
                    intent.putExtra("message", result);
                    startActivity(intent);
                    dialog.dismiss();
                    finish();

                }
                else
                {
                    dialog.dismiss();
                    txt.setText("賬號或者密碼錯誤！");
                }
                // 在这里处理从 Web 服务返回的数据 result
                // 在这个回调方法中，您可以将数据显示在界面上或进行其他处理
            } else {
                // 处理异常情况
                dialog.dismiss();
                txt.setText("网络不正常！");
            }
        }
    }

}