package com.wl.comom;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class DownloadProgressHelper {
    public static int getDownloadProgress(Context context, long downloadId) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);

        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);

            if (status == DownloadManager.STATUS_RUNNING) {
                columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int bytesDownloadedSoFar = cursor.getInt(columnIndex);

                columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                int totalSizeBytes = cursor.getInt(columnIndex);

                cursor.close();

                if (totalSizeBytes > 0) {
                    return (int) ((bytesDownloadedSoFar * 100L) / totalSizeBytes);
                }
            }
        }

        cursor.close();
        return -1; // 表示获取进度失败
    }
}
