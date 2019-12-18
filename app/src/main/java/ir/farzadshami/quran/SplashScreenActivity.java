package ir.farzadshami.quran;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.util.ArrayList;

import ir.farzadshami.quran.helpers.ADataBase;
import ir.farzadshami.quran.models.Download;

public class SplashScreenActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 500 ;
    private final Handler handler = new Handler();
    private static final int PERMISSION_RESULT_CODE = 34835;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SettingsActivity.context = SplashScreenActivity.this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                initSettings();
                final boolean[] isFinished = new boolean[1];
                isFinished[0] = false;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        new MainActivity();
                        isFinished[0] = true;
                    }
                });
                while (!isFinished[0]);
                try{Thread.sleep(SPLASH_DISPLAY_LENGTH);}catch (Exception e){e.printStackTrace();}
                startActivity(new Intent(SplashScreenActivity.this , MainActivity.class));
                SplashScreenActivity.this.finish();
            }
        }).start();
    }

    private void initSettings() {
        boolean isFirstRun = SettingsActivity.getSetting(SettingsActivity.Settings.FIRSTRUN).
                equals(SettingsActivity.Settings.TRUE.toString());
        if (isFirstRun) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    String[] permissions = new String[4];
                    permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                    permissions[1] = Manifest.permission.READ_EXTERNAL_STORAGE;
                    permissions[2] = Manifest.permission.INTERNET;
                    permissions[3] = Manifest.permission.ACCESS_NETWORK_STATE;
                    while (!getPermissions(permissions)){
                        try{Thread.sleep(3000);}catch (Exception e){e.printStackTrace();}
                    }
                    ADataBase db = new ADataBase("Groupes" , SplashScreenActivity.this);
                    ArrayList<String> current = new ArrayList<>();
                    current.add("پیش فرض");
                    db.writeArray("groupesid" , current , 1);
                    db = new ADataBase("Downloads" , SplashScreenActivity.this);
                    ArrayList<String> downloadFileNames = new ArrayList<>();
                    for(int i =0 ; i < 30 ; i++)
                        downloadFileNames.add("NA");
                    db.writeArray("soundstartil" , downloadFileNames , 1);
                    db.writeArray("soundstahdir" , downloadFileNames , 1);
                    db = new ADataBase("Favorite" , SplashScreenActivity.this);
                    downloadFileNames.clear();
                    for(int i =0 ; i < 114 ; i++){
                        downloadFileNames.add("0");
                    }
                    db.writeArray("favoritesuras" , downloadFileNames , 1);

                    SettingsActivity.applySetting(SettingsActivity.Settings.BRIGHTNESS , "75");
                    SettingsActivity.applySetting(SettingsActivity.Settings.KEEP_SCREEN_ON , SettingsActivity.Settings.TRUE.toString());
                    SettingsActivity.applySetting(SettingsActivity.Settings.NIGHTMODE , SettingsActivity.Settings.FALSE.toString());

                    SettingsActivity.applySetting(SettingsActivity.Settings.LOUDNESS, "8");
                    SettingsActivity.applySetting(SettingsActivity.Settings.DEFAULT_SOUND_TYPE , SettingsActivity.Settings.TARTIL.toString());
                    SettingsActivity.applySetting(SettingsActivity.Settings.BACKGROUND_PLAY , SettingsActivity.Settings.FALSE.toString());

                    SettingsActivity.applySetting(SettingsActivity.Settings.SHOW_TARJOME , SettingsActivity.Settings.TRUE.toString());
                    SettingsActivity.applySetting(SettingsActivity.Settings.FONT_FARSI , getResources().getStringArray(R.array.farsicfontscachename)[0]);
                    SettingsActivity.applySetting(SettingsActivity.Settings.FONT_ARABIC , getResources().getStringArray(R.array.arabicfontscachename)[0]);
                    SettingsActivity.applySetting(SettingsActivity.Settings.COLOR_FARSI , "FF000000");
                    SettingsActivity.applySetting(SettingsActivity.Settings.COLOR_ARABIC , "FF000000");
                    SettingsActivity.applySetting(SettingsActivity.Settings.COLOR_ERAB_ARABIC , "FFFF0000");
                    SettingsActivity.applySetting(SettingsActivity.Settings.SIZE_ARABIC , "30");
                    SettingsActivity.applySetting(SettingsActivity.Settings.SIZE_FARSI , "15");

                    SettingsActivity.applySetting(SettingsActivity.Settings.FIRSTRUN , SettingsActivity.Settings.FALSE.toString());
                }
            });
            t.start();
            while (t.isAlive());
        }
    }

    private boolean getPermissions(String[] permissions){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for(int i =0 ; i < permissions.length ; i++){
                if(checkSelfPermission(permissions[i]) == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(SplashScreenActivity.this , permissions , PERMISSION_RESULT_CODE);
                    return false;
                }
            }
            return true;
        }else
            return true;
    }
}
