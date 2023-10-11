package com.wl.cfwl;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.google.gson.Gson;
import com.wl.CF.BackgroundWork;
import com.wl.ChildEntity;
import com.wl.ParentEntity;
import com.wl.ParentSumEntity;
import com.wl.adapter.ParentAdapter;
import com.wl.adapter.ParentSumAdapter;
import com.wl.comom.DownloadProgressHelper;
import com.wl.comom.NetHelper;
import com.wl.entry.JZXX;
import com.wl.entry.ResponseData;
import com.wl.entry.ResponseData2;
import com.wl.entry.Seclect;
import com.wl.entry.SourceNode;
import com.wl.entry.ZD;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainSumActivity extends Activity implements ExpandableListView.OnGroupExpandListener,
        ParentSumAdapter.OnChildTreeViewClickListener {


    private String ip = "114.117.161.248";
    private int port = 4500;


    Context mContext;


    private ArrayList<ParentEntity> parents = new ArrayList<ParentEntity>();
    private ArrayList<ParentSumEntity> parentsum = new ArrayList<ParentSumEntity>();
    private ParentSumAdapter adapter;


    public void initLocation() {
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        LocationClient.setAgreePrivacy(true);
        try {
            // 定位初始化
            mLocClient = new LocationClient(getApplicationContext());
            mLocClient.registerLocationListener(new MyLocationListener());
            //获取地图控件引用
            mMapView = (MapView) findViewById(R.id.bmapView);
            mBaiduMap = mMapView.getMap();
            //开启地图的定位图层
            mBaiduMap.setMyLocationEnabled(true);
            //通过LocationClientOption设置LocationClient相关参数
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true); // 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setScanSpan(1000);
            //设置locationClientOption
            mLocClient.setLocOption(option);
            //注册LocationListener监听器
            MyLocationListener myLocationListener = new MyLocationListener();
            mLocClient.registerLocationListener(myLocationListener);
            //开启地图定位图层
            mLocClient.start();

        } catch (Exception e) {

        }
    }

    private TextView admintxt1, admintxt2;
    private ImageView location, type, menu, back, ic_back, destination, refesh;
    private boolean getType = false;
    private RelativeLayout adminre, qc, main, downloadrl;
    private Button exit, login, ic_online, ic_off, kzttxt;


    //获取屏幕的高度
    public int getScreenHeight() {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        return point.y;
    }

    private void initEList() {

        eList = (ExpandableListView) findViewById(R.id.eList);
        ViewGroup.LayoutParams lp = eList.getLayoutParams();
        lp.height = getScreenHeight() / 3 * 2;
        eList.setLayoutParams(lp);
        eList.setOnGroupExpandListener(this);

        adapter = new ParentSumAdapter(mContext, parentsum);

        eList.setAdapter(adapter);

        adapter.setOnChildTreeViewClickListener(this);

    }

    private ExpandableListView eList;
    private EditText mEditText;
    private ImageView mImageView;

    private ImageView mTextView, kongzhitai;

    public void setDilog(String abc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

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

    private void initView() {

        // 创建一个地图状态改变监听器

        kongzhitai = (ImageView) findViewById(R.id.kongzhitai);
        kzttxt = (Button) findViewById(R.id.kzttxt);
        kzttxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainSumActivity.this, Datasum.class);


                startActivity(intent);
            }
        });
        if (!message.equals("全部")) {
            kzttxt.setVisibility(View.GONE);
            kongzhitai.setVisibility(View.GONE);
        }
        mTextView = (ImageView) findViewById(R.id.textview);
        mEditText = (EditText) findViewById(R.id.edittext);
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        mImageView = (ImageView) findViewById(R.id.imageview);

        //设置删除图片的点击事件
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把EditText内容设置为空
                mEditText.setText("");
                //把ListView隐藏

            }
        });

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
                    mImageView.setVisibility(View.GONE);
                } else {//长度不为0
                    //显示“删除图片”
                    mImageView.setVisibility(View.VISIBLE);
                    //显示ListView

                }
            }

            public void afterTextChanged(Editable s) {
            }//文本改变之后执行
        });

        mTextView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.e("yy", "123");
                List<Seclect> sumabc = new ArrayList<>();

                for (int i = 0; i < parents.size(); i++) {
                    for (int x = 0; x < parents.get(i).getChilds().size(); x++) {
                        for (int y = 0; y < parents.get(i).getChilds().get(x).abc.size(); y++) {
                            if (parents.get(i).getChilds().get(x).abc.get(y).name.contains(mEditText.getText() + "")) {
                                Seclect abc = new Seclect();
                                abc.name = parents.get(i).getChilds().get(x).abc.get(y).name;
                                abc.i = i;
                                abc.x = x;
                                abc.y = y;
                                sumabc.add(abc);
                                //

                            }
                        }
                    }
                }

                if (sumabc.size() == 0) {
                    setDilog("没有搜索到相关内容");
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainSumActivity.this);
                SpannableString spannableTitle = new SpannableString("请选择搜索出来的站点");
                spannableTitle.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
// 设置对话框标题
                builder.setTitle(spannableTitle);
                String[] mapApps = new String[sumabc.size()];
                for (int i = 0; i < sumabc.size(); i++)
                    mapApps[i] = sumabc.get(i).name;
                builder.setItems(mapApps, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 根据点击的选项处理相应的逻辑
                        searchlocation(sumabc.get(which).i, sumabc.get(which).x, sumabc.get(which).y);
                    }
                });
                // 创建并显示对话框
                AlertDialog dialog = builder.create();
                dialog.show();
                //  mExpandableListView1.setSelectedChild(0, 1, true);
            }
        });


        mContext = MainSumActivity.this;
        initEList();

        admintxt1 = (TextView) findViewById(R.id.admintxt1);
        admintxt2 = (TextView) findViewById(R.id.admintxt2);
        admintxt2.setText(name);
        adminre = (RelativeLayout) findViewById(R.id.adminre);
        adminre.setVisibility(View.GONE);
        qc = (RelativeLayout) findViewById(R.id.qc);
        downloadrl = (RelativeLayout) findViewById(R.id.downloadrl);
        downloadrl.setVisibility(View.GONE);
        main = (RelativeLayout) findViewById(R.id.main);

        qc.setVisibility(View.GONE);
        refesh = (ImageView) findViewById(R.id.refesh);
        refesh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (marker != null) {
                    marker.remove(); // 移除标记
                }
                mBaiduMap.hideInfoWindow();
                if (mTextlist.size() > 0) {
                    mBaiduMap.removeOverLays(mTextlist);
                    mTextlist.clear();
                    Log.e("放大", "haha" + markersum.size());
                    if (markersum.size() > 0) {
                        for (Marker marker : markersum) {
                            marker.remove();
                        }
                        markersum.clear(); // 移除标记
                    }
                }
                showNetProgressDialog();

            }
        });
        ic_back = (ImageView) findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                qc.setVisibility(View.GONE);

            }
        });
        ic_online = (Button) findViewById(R.id.ic_online);
        ic_online.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                qc.setVisibility(View.VISIBLE);
            }
        });
        ic_off = (Button) findViewById(R.id.ic_off);
        ic_off.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                qc.setVisibility(View.VISIBLE);
            }
        });
        getType = true;
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(MainSumActivity.this, Login.class);
                intent.putExtra("message", "切换");
                startActivity(intent);
                finish();
            }
        });
        exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intenn = new Intent();
                intenn.setAction("android.intent.action.MAIN");
                intenn.addCategory("android.intent.category.HOME");
                startActivity(intenn);
                android.os.Process.killProcess(android.os.Process.myPid());

            }
        });
        menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                adminre.setVisibility(View.VISIBLE);

            }
        });
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                adminre.setVisibility(View.GONE);

            }
        });
        destination = (ImageView) findViewById(R.id.destination);
        destination.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
            }
        });
        type = (ImageView) findViewById(R.id.type);
        type.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (getType) {
                    type.setImageResource(R.drawable.ic_sat_red);
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    getType = false;
                } else {
                    type.setImageResource(R.drawable.ic_sat_green);
                    getType = true;
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                }

            }
        });
        location = (ImageView) findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                LatLng chinaCenter = new LatLng(mlatitude, mlongitude); // 中国中心点的经纬度坐标
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(chinaCenter));
            }
        });
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        initLocation();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(5));
        LatLng chinaCenter = new LatLng(35.861660, 104.195396); // 中国中心点的经纬度坐标
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(chinaCenter));
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);

        BaiduMap.OnMapStatusChangeListener mapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                // 地图状态改变开始时的回调
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                // 地图状态改变过程中的回调
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                // 地图状态改变结束时的回调
                float zoomLevel = mapStatus.zoom; // 获取当前地图的缩放级别
                Log.e("放大", zoomLevel + "" + mTextlist.size());
                // 根据缩放级别判断是否显示文字

                if (zoomLevel >= 10) {
                    if (mTextlist.size() == 0)
                        // 显示文字
                        setIcon();
                    if (marker != null) {
                        marker.remove();
                        BitmapDescriptor originalIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker_icon);


// 调整图标大小
                        int width = 120; // 图标的目标宽度（单位：像素）1
                        int height = 120; // 图标的目标高度（单位：像素）
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalIcon.getBitmap(), width, height, false);

                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(point).anchor(0.5f, 0.5f)
                                .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));

//// 添加标记到地图上

                        marker = (Marker) mBaiduMap.addOverlay(markerOptions);
                    }
                } else {
                    if (mTextlist.size() > 0) {
                        mBaiduMap.removeOverLays(mTextlist);
                        mTextlist.clear();
                        Log.e("放大", "haha" + markersum.size());
                        if (markersum.size() > 0) {
                            for (Marker marker : markersum) {
                                marker.remove();
                            }
                            markersum.clear(); // 移除标记
                        }
                    }
                    // 隐藏文字
                    // 检查并移除文字覆盖物
                    // ...
                }
            }
        };

// 设置地图状态改变监听器
        mBaiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);

        // 实现 BaiduMap.OnMarkerClickListener 接口，处理标记点击事件
        BaiduMap.OnMarkerClickListener markerClickListener = new BaiduMap.OnMarkerClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public boolean onMarkerClick(Marker mmarker) {
                long currentTime = System.currentTimeMillis();
                Log.e("时间间隔", currentTime - lastClickTime + "");

                Bundle data = mmarker.getExtraInfo();
                String name = data.getString("name");

                Double b = data.getDouble("b");
                Double l = data.getDouble("l");
                Boolean get = data.getBoolean("get");
                point = new LatLng(l, b);
                int qqqq = 1;
                if (get) qqqq = 0;
                BitmapDescriptor originalIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker_icon);

// 调整图标大小
                int width = 120; // 图标的目标宽度（单位：像素）1
                int height = 120; // 图标的目标高度（单位：像素）
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalIcon.getBitmap(), width, height, false);

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(point).anchor(0.5f, 0.5f)
                        .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));

//// 添加标记到地图上
                if (marker != null)
                    marker.remove();
                marker = (Marker) mBaiduMap.addOverlay(markerOptions);

                setView(name, qqqq, l, b);


                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));


                lastClickTime = currentTime;

                return true; // 返回 true 表示已处理该事件
            }
        };

        BaiduMap.OnMapClickListener mapClickListener = new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (adminre.getVisibility() == View.VISIBLE)
                    adminre.setVisibility(View.GONE);
                // 处理地图单击事件
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {
                // 处理地图上的POI（兴趣点）单击事件
            }
        };


// 将监听器注册到 BaiduMap 对象上
        mBaiduMap.setOnMarkerClickListener(markerClickListener);
        mBaiduMap.setOnMapClickListener(mapClickListener);


    }
    private   SuggestionSearch   mSuggestionSearch;
    OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener()
    {
        @Override
        public void onGetSuggestionResult(SuggestionResult suggestionResult) {
            //处理sug检索结果
            int i=0;
            i++;

        }
    };
    public void setIcon() {
        Log.e("放大", sum.size() + "");
        for (int i = 0; i < sum.size() - 1; i++) {

            ZD qq = sum.get(i);
            LatLng center = new LatLng(qq.l, qq.b);

            OverlayOptions mTextOptions = new TextOptions()
                    .text("  " + "\\\\\\" + qq.name) //文字内容

                    .fontSize(35) //字号
                    .fontColor(Color.WHITE) //文字颜色

                    .position(center);
            // 在地图上显示文字
            Overlay mTextOverlay = mBaiduMap.addOverlay(mTextOptions);
            mTextlist.add(mTextOverlay); // 将文字覆盖物添加到列表中

            BitmapDescriptor originalIcon = BitmapDescriptorFactory.fromResource(R.drawable.ico_gray);

// 调整图标大小
            int width = 120; // 图标的目标宽度（单位：像素）1
            int height = 120; // 图标的目标高度（单位：像素）
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalIcon.getBitmap(), width, height, false);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(center).anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));

//// 添加标记到地图上

            Marker marker = (Marker) mBaiduMap.addOverlay(markerOptions);
            markersum.add(marker);
            Bundle markerData = new Bundle();
            markerData.putString("name", qq.name);
            markerData.putDouble("b", qq.b);
            markerData.putDouble("l", qq.l);
            markerData.putBoolean("get", qq.get);
            marker.setExtraInfo(markerData);
        }
    }

    long lastClickTime = 0;
    List<Marker> markersum = new ArrayList<>();

    List<Overlay> mTextlist = new ArrayList<>();
    protected String message;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private List<String> messageSum = new ArrayList<>();
    private List<String> dqySum = new ArrayList<>();
    private String name;
    private static final int REQUEST_LOCATION_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_sum);

        Intent intent = getIntent();
        message = intent.getStringExtra("message");
        name = intent.getStringExtra("name");

        showNetProgressDialog();
        initView();
        UpdateThread mUpdateThread = new UpdateThread();

        mPool.execute(mUpdateThread);

    }

    List<ZD> sum = new ArrayList<>();
    List<Overlay> mCirclelist = new ArrayList<>();


    public class InputParams {
        private String orgid;

        public String getorgid() {
            return orgid;
        }

        public void setorgid(String orgid) {
            this.orgid = orgid;
        }
    }

    public ArrayList<JZXX> infoList;

    public void getData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                InputParams inputParams = new InputParams();
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
                                            return jzxx1.qy.compareTo(jzxx2.qy);
                                        }
                                    });

                                    messageSum.clear();
                                    dqySum.clear();
                                    if (!message.equals("全部")) {
                                        messageSum.add(message);
                                        if (infoList.size() > 0) dqySum.add(infoList.get(0).dqy);
                                    } else {

                                        for (int i = 0; i < infoList.size(); i++) {
                                            if (i == 0) {
                                                messageSum.add(infoList.get(i).qy);
                                                dqySum.add(infoList.get(i).dqy);
                                            } else {
                                                if (!infoList.get(i).qy.equals(infoList.get(i - 1).qy)) {
                                                    messageSum.add(infoList.get(i).qy);
                                                    dqySum.add(infoList.get(i).dqy);
                                                }
                                            }
                                        }

                                    }
                                    returnQX(messageSum);


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

    ;

    @SuppressLint("SuspiciousIndentation")
    public void returnQX(List<String> messageSum) {

        mBaiduMap.removeOverLays(mCirclelist);
        parents.clear();
        int ic_online_c = 0;
        int ic_off_c = 0;
        sum = new ArrayList<>();


        for (int x = 0; x < messageSum.size(); x++) {
            List<String> parent1 = new ArrayList<>();
            List<String> parent2 = new ArrayList<>();
            List<String> dqy1 = new ArrayList<>();
            List<String> dqy2 = new ArrayList<>();
            List<ZD> online = new ArrayList<>();
            List<ZD> offline = new ArrayList<>();
            for (int i = 0; i < infoList.size(); ++i) {
                if (!infoList.get(i).qy.equals(messageSum.get(x)))
                    continue;
                ZD qq = new ZD();
                String username = infoList.get(i).name;
                qq.name = username;
                String gzd = infoList.get(i).gz;
                qq.gz = gzd;
                qq.qy = infoList.get(i).qy;
                qq.dtpl = infoList.get(i).dtbtl;
                qq.dqy=infoList.get(i).dqy;
                qq.user=infoList.get(i).user;
                try {
                    double b = Double.parseDouble(infoList.get(i).b);
                    double l = Double.parseDouble(infoList.get(i).l);

                    qq.b = b;
                    qq.l = l;
                } catch (Exception e) {

                }

                boolean get = false;
                for (int j = 0; j < nodeNames.length; j++) {
                    if (nodeNames[j].equals(gzd)) {
                        get = true;
                        break;
                    }
                }
                qq.get = get;
                sum.add(qq);
                if (get) {
                    online.add(qq);
                    parent1.add(username);
                    dqy1.add(infoList.get(i).dqy);
                    LatLng center = new LatLng(qq.l, qq.b);

                    //构造CircleOptions对象
                    CircleOptions mCircleOptions = new CircleOptions().center(center)
                            .radius(30000)
                            .fillColor(Color.argb(80, 0, 255, 0)) //填充颜色
                            .stroke(new Stroke(5, Color.argb(80, 0, 255, 0))); //边框宽和边框颜色

                    //在地图上显示圆
                    Overlay mCircle = mBaiduMap.addOverlay(mCircleOptions);
                    mCirclelist.add(mCircle);
                } else {
                    parent2.add(username);
                    offline.add(qq);
                    dqy2.add(infoList.get(i).dqy);
                    LatLng center = new LatLng(qq.l, qq.b);

                    //构造CircleOptions对象
                    CircleOptions mCircleOptions = new CircleOptions().center(center)
                            .radius(30000)
                            .fillColor(Color.argb(80, 255, 0, 0)) //填充颜色
                            .stroke(new Stroke(5, Color.argb(80, 255, 0, 0))); //边框宽和边框颜色

                    //在地图上显示圆
                    Overlay mCircle = mBaiduMap.addOverlay(mCircleOptions);
                    mCirclelist.add(mCircle);
                }

            }

            ParentEntity parent = new ParentEntity();

            parent.setGroupName(messageSum.get(x));

            parent.setdqy(dqySum.get(x));
            ArrayList<ChildEntity> childs = new ArrayList<ChildEntity>();


            ChildEntity child = new ChildEntity();

            child.setGroupName("在线" + parent1.size() + "台");

            child.setGroupColor(Color.parseColor("#00ff00"));

            ArrayList<String> childNames = new ArrayList<String>();


            ArrayList<Integer> childColors = new ArrayList<Integer>();

            for (int k = 0; k < parent1.size(); k++) {

                childNames.add(parent1.get(k));


            }

            child.setChildNames(childNames);

            child.abc = online;

            childs.add(child);

            child = new ChildEntity();

            child.setGroupName("不在线" + parent2.size() + "台");

            child.setGroupColor(Color.parseColor("#ff0000"));

            childNames = new ArrayList<String>();

            childColors = new ArrayList<Integer>();

            for (int k = 0; k < parent2.size(); k++) {

                childNames.add(parent2.get(k));

            }

            child.setChildNames(childNames);

            childs.add(child);

            child.abc = offline;
            parent.setChilds(childs);

            parents.add(parent);
            ic_online_c += parent1.size();
            ic_off_c += parent2.size();




        /*    mExpandableListData1.put("在线" + parent1.size() + "台", parent1);

            mExpandableListData2.put("不在线" + parent2.size() + "台", parent2);*/


            ic_online.setText("在线\n" + ic_online_c);
            ic_off.setText("离线\n" + ic_off_c);
            Log.e("母体", parents.size() + "");
        }
        ParentEntity parent = new ParentEntity();
        List<String> dqy = new ArrayList<>();
        List<String> dqysum = new ArrayList<>();
        for (int i = 0; i < infoList.size(); ++i) {
            dqy.add(infoList.get(i).dqy);

        }

        Set<String> uniqueSet = new HashSet<>(dqy);

        // 将Set转换回List
        List<String> uniqueList = new ArrayList<>(uniqueSet);
        for (String value : uniqueList) {
            dqysum.add(value);
        }
        parentsum.clear();
        for (int i = dqysum.size() - 1; i >= 0; --i) {
            ParentSumEntity parentabc = new ParentSumEntity();

            parentabc.setGroupName(dqysum.get(i));
            if (dqysum.get(i) == "") parentabc.setGroupName("其他");
            ArrayList<ParentEntity> parentqqq = new ArrayList<ParentEntity>();
            Boolean getnum=false;
            for (int xx = 0; xx < parents.size(); xx++) {
                if (parents.get(xx).getdqy().equals(dqysum.get(i))) {
                    parentqqq.add(parents.get(xx));
                    getnum=true;
                }
            }

            parentabc.setParent(parentqqq);
            if(getnum)
            parentsum.add(parentabc);
        }

        adapter = new ParentSumAdapter(mContext, parentsum);
        adapter.setOnChildTreeViewClickListener(this);
        eList.setAdapter(adapter);
        if (parents.size() > 0)
            eList.expandGroup(0);
        float zoomLevel = mBaiduMap.getMapStatus().zoom;

        Log.e("放大", "选择" + "" + mTextlist.size());
        // 根据缩放级别判断是否显示文字

        if (zoomLevel >= 10) {

            // 显示文字
            setIcon();

        }
    }

    private String[] nodeNames = new String[]{""};

    private void showNetProgressDialog() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getString(R.string.progress_wait));
        dialog.setTitle(R.string.option);
        BackgroundWork backwork = new BackgroundWork();

        backwork.setDowork(new BackgroundWork.IDoWork() {

            @Override
            public void doWork(BackgroundWork.WorkHandler handler, Object arg) {
                // TODO Auto-generated method stub

                List<SourceNode> nodess;
                try {
                    // boolean status = InetAddress.getByName(ip)
                    // .isReachable(3000);
                    Socket mBaiduSocket = new Socket();
                    boolean isNetOk = false;

                    if (mBaiduSocket.isConnected() == false) {
                        try {


                            SocketAddress remoteAddr = new InetSocketAddress(
                                    ip, port);
                            mBaiduSocket.connect(remoteAddr, 3000);

                        } catch (Exception e) {
                            // TODO: handle exception
                            isNetOk = false;
                        }

                        isNetOk = true;
                    }

                    if (isNetOk) {
                        nodess = NetHelper.GetSourceNode(ip, port + "");
                    } else {
                        nodess = null;
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    nodess = null;

                }
                handler.complete(false, null, nodess);

            }

        });

        backwork.setWorkListener(new BackgroundWork.RunWorkListener() {

            @Override
            public void started(Object arg) {
                // TODO Auto-generated method stub

            }

            @Override
            public void complete(boolean isCancel, Exception ex, Object result) {
                // TODO Auto-generated method stub

                if (result == null) {

                    //showInputSourceDialog();
                    return;
                }

                List<SourceNode> nodes = (List<SourceNode>) result;
                nodeNames = new String[nodes.size()];
                if (nodes.get(0) == null) return;
                for (int i = 0; i < nodeNames.length; i++) {
                    nodeNames[i] = nodes.get(i).Mountpoint;
                }
                getData();

                dialog.dismiss();
            }
        });

        backwork.run();
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onGroupExpand(int i) {

    }

    public Marker marker;
    public LatLng point;

    @Override
    public void onClickPosition(int parentPositionsum, int parentPosition, int groupPosition, int childPosition) {
        Log.e("输出", parentsum.get(parentPositionsum).getParent().get(parentPosition).getChilds().get(groupPosition).getChildNames().get(childPosition) + ";");
        searchlocation(parentPositionsum, parentPosition, groupPosition, childPosition);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(9));
    }

    public void searchlocation(int parentPosition, int groupPosition, int childPosition) {
        point = new LatLng(parents.get(parentPosition).getChilds().get(groupPosition).abc.get(childPosition).l, parents.get(parentPosition).getChilds().get(groupPosition).abc.get(childPosition).b);
        BitmapDescriptor originalIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker_icon);

// 调整图标大小
        int width = 120; // 图标的目标宽度（单位：像素）1
        int height = 120; // 图标的目标高度（单位：像素）
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalIcon.getBitmap(), width, height, false);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(point).anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));

//// 添加标记到地图上
        if (marker != null) {
            marker.remove(); // 移除标记
        }
        marker = (Marker) mBaiduMap.addOverlay(markerOptions);


        setView(parents.get(parentPosition).getChilds().get(groupPosition).abc.get(childPosition).name, groupPosition, parents.get(parentPosition).getChilds().get(groupPosition).abc.get(childPosition).l, parents.get(parentPosition).getChilds().get(groupPosition).abc.get(childPosition).b);


        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
        qc.setVisibility(View.INVISIBLE);
    }

    public void searchlocation(int parentPositionsum, int parentPosition, int groupPosition, int childPosition) {
        point = new LatLng(parentsum.get(parentPositionsum).getParent().get(parentPosition).getChilds().get(groupPosition).abc.get(childPosition).l, parentsum.get(parentPositionsum).getParent().get(parentPosition).getChilds().get(groupPosition).abc.get(childPosition).b);
        BitmapDescriptor originalIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker_icon);

// 调整图标大小
        int width = 120; // 图标的目标宽度（单位：像素）1
        int height = 120; // 图标的目标高度（单位：像素）
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalIcon.getBitmap(), width, height, false);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(point).anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));

//// 添加标记到地图上
        if (marker != null) {
            marker.remove(); // 移除标记
        }
        marker = (Marker) mBaiduMap.addOverlay(markerOptions);


        setView(parentsum.get(parentPositionsum).getParent().get(parentPosition).getChilds().get(groupPosition).abc.get(childPosition).name, groupPosition, parentsum.get(parentPositionsum).getParent().get(parentPosition).getChilds().get(groupPosition).abc.get(childPosition).l, parentsum.get(parentPositionsum).getParent().get(parentPosition).getChilds().get(groupPosition).abc.get(childPosition).b);


        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
        qc.setVisibility(View.INVISIBLE);
    }

    public void setView(String name, int groupPosition, double l, double b) {
// 缩放图片
        for (int i = 0; i < sum.size(); i++) {
            if (name.equals(sum.get(i).name)) {
                name = sum.get(i).name + "(" + sum.get(i).gz + ")" + "\r\n" + "区域:" +sum.get(i).dqy+""+ sum.get(i).qy + ";电台频率:" + sum.get(i).dtpl ;
                if(sum.get(i).user.length()>0)
                    name+=  ";管理者:" + sum.get(i).user;
            }
        }
        View customView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_info_window, null);
        TextView infoText = customView.findViewById(R.id.info_text);
        infoText.setText(name);


        InfoWindow mInfoWindow = new InfoWindow(customView, point, -80);
        ImageView daohan = customView.findViewById(R.id.daohan);
        if (groupPosition == 0) {
            infoText.setTextColor(Color.GREEN);

        } else {
            infoText.setTextColor(Color.RED);
        }
        daohan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Geocoder mCoder = new Geocoder(getBaseContext(), Locale.CHINESE);


                List<Address> addresses = null;
                try {
                    addresses = mCoder.getFromLocation(l, b
                            ,
                            1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address1 = addresses.get(0);
                final String[] destinationAddress = {""};
                int addressLineCount = address1.getMaxAddressLineIndex();
                for (int i = 0; i <= addressLineCount; i++) {
                    String addressLine = address1.getAddressLine(i);
                    if (addressLine != null) {
                        destinationAddress[0] += addressLine + " ";
                    }
                }
                destinationAddress[0] = destinationAddress[0].trim(); // 移除首尾空格
                Log.e("地址", destinationAddress[0] + "111");
// 获取目标地址的经纬度坐标和地址文本
                double latitude = address1.getLatitude();
                double longitude = address1.getLongitude();

                String addressText = address1.getAddressLine(0); // 假设这里获取到了地址的文本信息
                Log.e("地址", addressText + ";" + address1.getMaxAddressLineIndex());
// 创建导航的隐式意图

                AlertDialog.Builder builder = new AlertDialog.Builder(MainSumActivity.this);

// 设置对话框标题
                builder.setTitle("选择导航的地图应用");

// 设置对话框选项
                final String[] mapApps = {"百度地图", "腾讯地图", "高德地图"};
                String address = Uri.encode(destinationAddress[0]);
                builder.setItems(mapApps, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 根据点击的选项处理相应的逻辑
                        switch (which) {
                            case 0:

                              /*  Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("geo:" + latitude + "," + longitude + "?q=" + Uri.encode(destinationAddress[0])));

// 设置特定的地图应用程序包名，例如百度地图
                                intent.setPackage("com.baidu.BaiduMap");*/


                                Uri uri = Uri.parse("baidumap://map/marker?location=" + latitude + "," + longitude + "&title=" + address);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    // 启动腾讯地图导航
                                    startActivity(intent);
                                } else {
                                    // 提示用户安装腾讯地图应用
                                    Toast.makeText(MainSumActivity.this, "请安装百度地图应用", Toast.LENGTH_SHORT).show();
                                    // 打开百度地图应用在应用商店的页面
                                    try {
                                        Uri storeUri = Uri.parse("market://details?id=com.baidu.BaiduMap");
                                        Intent storeIntent = new Intent(Intent.ACTION_VIEW, storeUri);
                                        startActivity(storeIntent);
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
                                        // 如果无法打开应用商店，则打开百度地图的下载页面
                                        Uri downloadUri = Uri.parse("https://map.baidu.com/m?tn=site888_pg&appuid=123456");
                                        Intent downloadIntent = new Intent(Intent.ACTION_VIEW, downloadUri);
                                        startActivity(downloadIntent);
                                    }
                                }
                                //   startActivity(intent);
                                // 打开百度地图
                                //  openBaiduMap();
                                break;
                            case 1:
                                // 获取地址的第一行，作为导航目的地

// 创建导航的隐式意图
                                // uri = Uri.parse("qqmap://map/routeplan?type=drive&to=" + Uri.encode(destinationAddress[0]) + "&tocoord=" + latitude + "," + longitude);
                                uri = Uri.parse("qqmap://map/routeplan?type=drive&to=" + address + "&tocoord=" + latitude + "," + longitude + "&referer=yourAppName");
                                intent = new Intent(Intent.ACTION_VIEW, uri);

// 启动腾讯地图导航
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    // 启动腾讯地图导航
                                    startActivity(intent);
                                } else {
                                    // 提示用户安装腾讯地图应用
                                    Toast.makeText(MainSumActivity.this, "请安装腾讯地图应用", Toast.LENGTH_SHORT).show();

                                    // 打开腾讯地图应用在应用商店的页面
                                    try {
                                        uri = Uri.parse("market://details?id=com.tencent.map");
                                        intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
                                        // 如果无法打开应用商店，则打开腾讯地图的下载页面
                                        uri = Uri.parse("https://mapdownload.map.qq.com/");
                                        intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }
                                }
                                break;
                            case 2:
                                // 打开高德地图

// 创建导航的隐式意图
                                uri = Uri.parse("androidamap://viewMap?sourceApplication=yourAppName&poiname=" + address + "&lat=" + latitude + "&lon=" + longitude + "&dev=0");
                                //  uri = Uri.parse("amapuri://route/plan/?dlat=" + latitude + "&dlon=" + longitude + "&dname=" + Uri.encode(destinationAddress[0]) + "&dev=0&t=0");
                                intent = new Intent(Intent.ACTION_VIEW, uri);
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    // 启动腾讯地图导航
                                    startActivity(intent);
                                } else {
                                    // 提示用户安装腾讯地图应用
                                    Toast.makeText(MainSumActivity.this, "请安装高德地图应用", Toast.LENGTH_SHORT).show();
                                    // 打开高德地图应用在应用商店的页面
                                    try {
                                        Uri storeUri = Uri.parse("market://details?id=com.autonavi.minimap");
                                        Intent storeIntent = new Intent(Intent.ACTION_VIEW, storeUri);
                                        startActivity(storeIntent);
                                    } catch (ActivityNotFoundException e) {
                                        e.printStackTrace();
                                        // 如果无法打开应用商店，则打开高德地图的下载页面
                                        Uri downloadUri = Uri.parse("https://www.amap.com/navi/");
                                        Intent downloadIntent = new Intent(Intent.ACTION_VIEW, downloadUri);
                                        startActivity(downloadIntent);
                                    }
                                }
                                break;
                        }
                    }
                });

// 创建并显示对话框
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
//使InfoWindow生效
        mBaiduMap.showInfoWindow(mInfoWindow);
// 显示信息窗口
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private ExecutorService mPool = Executors.newFixedThreadPool(8);

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();

    }

    private LocationClient mLocClient;
    private MyLocationListener myListener;
    private double mlatitude, mlongitude;

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // MapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mlatitude = location.getLatitude();
            mlongitude = location.getLongitude();
            MyLocationData myLocationData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())// 设置定位数据的精度信息，单位：米
                    .direction(300)// 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(myLocationData);
        }
    }

    @Override
    protected void onDestroy() {
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    private Button btnCancelDownload;

    private void setDownloadview(String ABC) {
        downloadrl.setVisibility(View.VISIBLE);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);
        btnCancelDownload = findViewById(R.id.progresscancle);
        btnCancelDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDownload();
            }
        });
        // Check if update is needed and show AlertDialog

        showUpdateDialog(ABC);


        // 注册广播接收器
        BroadcastReceiver receiver = new DownloadReceiver();
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, filter);
    }

    private ProgressBar progressBar;
    private TextView progressText;
    private long downloadId;
    private static final String NAMESPACE = "http://tempuri.org/";

    class UpdateThread extends Thread {
        @Override
        public void run() {

            Looper.prepare();

            String method = "checkUpdate";
            SoapObject rpc = new SoapObject(NAMESPACE, method);
            Log.e("收到", MainSumActivity.this.getResources().getString(R.string.Version) + ".apk");
            /*
             * PropertyInfo pi = new PropertyInfo(); pi.setName("requestFileName");
             * pi.setValue(apkName); pi.setType(apkName.getClass());
             * rpc.addProperty(pi);
             */
            rpc.addProperty("apkname", MainSumActivity.this.getResources().getString(R.string.Version) + ".apk");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);

            HttpTransportSE ht = new HttpTransportSE("http://114.117.161.248:4502/SQL_Date/Equipment_link.asmx", 5000);
            ht.debug = true;
            try {
                ht.call(NAMESPACE + method, envelope);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            try {
                Object object = envelope.getResponse();
                Log.e("收到", "软件名称" + object.toString());
                if (object.toString().contains("apk") && !object.toString().equals(MainSumActivity.this.getResources().getString(R.string.Version) + ".apk")) {

                    setDownloadview(object.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            Looper.loop();

        }
    }

    private void showUpdateDialog(String abc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更新提示")
                .setMessage("有新的版本可供更新，是否更新？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startDownload(abc);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Close the activity if the user cancels the update

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                downloadrl.setVisibility(View.GONE);
                            }
                        });
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void startDownload(String abc) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 如果未授权，向用户请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            // 如果已授权，开始下载
            String fileUrl = "http://114.117.161.248:4502/net6.0-windows/Date/apk/" + abc;
            String fileName = abc;
            Log.e("收到", "软件名称1111" + fileUrl);
            downloadFile(fileUrl, fileName);
        }
    }

    private void downloadFile(String fileUrl, String fileName) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadId = downloadManager.enqueue(request);

        // 可选：使用 Handler 定期查询进度
        final Handler handler = new Handler();
        final Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                updateProgressBar();
                handler.postDelayed(this, 1000); // 每秒更新一次进度
            }
        };
        handler.post(progressRunnable);
    }

    private void updateProgressBar() {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            if (status == DownloadManager.STATUS_RUNNING) {
                @SuppressLint("Range") int bytesDownloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                @SuppressLint("Range") int bytesTotal = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                int progress = (int) ((bytesDownloaded * 100L) / bytesTotal);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progressBar.setProgress(progress);
                        progressText.setText(progress + "%");
                    }
                });

            }
        }
        cursor.close();
    }

    private class DownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long receivedId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (receivedId == downloadId) {
                    // 检查下载状态
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        @SuppressLint("Range") int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        Log.e("结果", status + "");
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            // 下载成功，执行安装操作
                            installApk(context, downloadId);
                        } else {
                            // 下载失败，显示错误信息
                            @SuppressLint("Range") int reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
                            String errorMessage = "下载失败，原因：" + getDownloadErrorMessage(reason);
                            Log.e("结果", errorMessage + "");
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            // 隐藏进度条并解除屏幕锁定
                            downloadrl.setVisibility(View.GONE);

                        }
                    }
                    cursor.close();
                }
            }
        }

        private void installApk(Context context, long downloadId) {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
            if (uri != null) {
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(installIntent);
            } else {
                Toast.makeText(context, "下载失败，请重试", Toast.LENGTH_SHORT).show();
            }
            // 隐藏进度条并解除屏幕锁定
            downloadrl.setVisibility(View.GONE);


        }

        private String getDownloadErrorMessage(int reason) {
            String errorMessage;
            switch (reason) {
                case DownloadManager.ERROR_CANNOT_RESUME:
                    errorMessage = "无法恢复下载";
                    break;
                case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                    errorMessage = "未找到存储设备";
                    break;
                case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                    errorMessage = "文件已存在";
                    break;
                // 添加其他错误情况的处理...
                default:
                    errorMessage = "未知错误";
                    break;
            }
            return errorMessage;
        }
    }

    @Override
    public void onBackPressed() {
        // 取消下载并关闭Activity
        cancelDownload();
        super.onBackPressed();
    }

    private void cancelDownload() {
        downloadrl.setVisibility(View.GONE);
        if (downloadId != -1) {
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            downloadManager.remove(downloadId);
        }
    }

}