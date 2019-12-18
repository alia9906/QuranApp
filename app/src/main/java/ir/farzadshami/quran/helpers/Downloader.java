package ir.farzadshami.quran.helpers;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import ir.farzadshami.quran.models.Download;


public class Downloader {

    public static boolean start(Download model , Context context) {
        try {
            File path = new File(Download.FILEPATH);
            if (!path.exists())
                path.mkdirs();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(new UrlCreator().createUrlFromJoz(model)));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setTitle("دانلود جز " + (model.getJoz() + 1));
            request.setDescription("دریافت فایل");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(path.getPath(), model.getDefaultName());
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadCompleteSaver dcs = new DownloadCompleteSaver(model);
            context.registerReceiver(dcs , new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            dcs.setEnqueuid(manager.enqueue(request));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    static class DownloadCompleteSaver extends BroadcastReceiver{

        long enqueuid;
        Download model;

        DownloadCompleteSaver(Download model){
            this.model = model;
        }

        public void setEnqueuid(long enqueuid) {
            this.enqueuid = enqueuid;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("download" , "complete");
            model.notifyDownloadComplete(model.getDefaultName());
        }

    }
}
