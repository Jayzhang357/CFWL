package com.wl.cfwl;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DownloadActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView progressText;
    private long downloadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);

        // Check if update is needed and show AlertDialog
        if (isUpdateNeeded()) {
            showUpdateDialog();
        } else {
            startDownload();
        }

        // 注册广播接收器
        BroadcastReceiver receiver = new DownloadReceiver();
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, filter);
    }

    private boolean isUpdateNeeded() {
        // Check if the update is needed based on your logic
        // For example, you can compare the current version with the server version.
        // If the server version is higher, return true to indicate an update is needed.
        return true; // Replace with your actual check logic
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更新提示")
                .setMessage("有新的版本可供更新，是否更新？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startDownload();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // Close the activity if the user cancels the update
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void startDownload() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 如果未授权，向用户请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            // 如果已授权，开始下载
            String fileUrl = "http://114.117.161.248:4502/net6.0-windows/Date/apk/app-debug.apk";
            String fileName = "app-debug.apk";
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
                progressBar.setProgress(progress);
                progressText.setText(progress + "%");
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
                       Log.e("结果",status+"");
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            // 下载成功，执行安装操作
                            installApk(context, downloadId);
                        } else {
                            // 下载失败，显示错误信息
                            @SuppressLint("Range") int reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
                            String errorMessage = "下载失败，原因：" + getDownloadErrorMessage(reason);
                            Log.e("结果",errorMessage+"");
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            // 隐藏进度条并解除屏幕锁定
                            progressBar.setVisibility(View.GONE);
                            progressText.setVisibility(View.GONE);

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
            progressBar.setVisibility(View.GONE);
            progressText.setVisibility(View.GONE);

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
        if (downloadId != -1) {
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            downloadManager.remove(downloadId);
        }
    }

}
