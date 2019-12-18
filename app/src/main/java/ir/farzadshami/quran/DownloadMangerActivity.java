package ir.farzadshami.quran;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.farzadshami.quran.adapters.DownloadsAdapter;
import ir.farzadshami.quran.helpers.Downloader;
import ir.farzadshami.quran.models.Download;
import ir.farzadshami.quran.widget.MultiSwitch;

public class DownloadMangerActivity extends Fragment {
    private static final int PERMISSION_STORAGE_CODE = 1000;
    private FragmentActivity c;
    private Download.Type type = Download.Type.TARTL;
    private DownloadsAdapter adapter;
    private Button downloaderButton;
    private MultiSwitch voiceSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_download_manger, container, false);
        c = getActivity();
        voiceSwitch = view.findViewById(R.id.voice_switch);
        final RecyclerView recyclerView = view.findViewById(R.id.download_rv);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(c, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new DownloadsAdapter(c , type);
        recyclerView.setAdapter(adapter);
        String[] arg1 = {getString(R.string.tahdir) , getString(R.string.tartil)};
        int[] arg2 = {1 , 1};
        voiceSwitch.initialize(arg1, arg2, new MultiSwitch.SwitchToggleListener() {
            @Override
            public void onSwitchToggle(int which) {
                type = which == 0 ? Download.Type.TAHDIR : Download.Type.TARTL;
                adapter.update(type);
            }
        });

        downloaderButton = view.findViewById(R.id.download_btn);

        downloaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (c.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_STORAGE_CODE);
                    } else {
                        downloadList(adapter.getDownloadList());
                    }
                } else {
                    downloadList(adapter.getDownloadList());
                }
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadList(adapter.getDownloadList());
                } else {
                    Toast.makeText(c, "Permission denied ...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void downloadList(List<Download> list){
        for(int i =0 ; i < list.size() ; i++){
            if(list.get(i).getChecked()) {
                Log.d("download", list.get(i).getDefaultName());
                Log.d(list.get(i).getDefaultName(), Downloader.start(list.get(i), DownloadMangerActivity.this.getActivity()) + list.get(i).getType().toString());
            }
        }
    }
    public void setInitialDowload(final Download.Type type ,final Integer... arr){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                while (!DownloadMangerActivity.this.isVisible());

                if(type == Download.Type.TARTL)
                    voiceSwitch.setCurrentSwitch(1);
                else
                    voiceSwitch.setCurrentSwitch(0);

                List<Download> list  = Download.createDownloadList(type);
                for(int i =0 ; i < arr.length ; i++) {
                    list.get(arr[i] - 1).setChecked(true);
                }
                adapter.update(list , type);
            }
        } , 500);
    }
}
