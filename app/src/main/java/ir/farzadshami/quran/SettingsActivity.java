package ir.farzadshami.quran;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import ir.farzadshami.quran.widget.SubFragmentSettings;

public class SettingsActivity extends Fragment implements View.OnClickListener {

    public static Context context;
    private static SharedPreferences sharedPreferences;

    public enum Settings{
        SETTINGS,TRUE,FALSE,TARTIL,TAHDIR,FIRSTRUN,

        FONT_ARABIC,COLOR_ARABIC , COLOR_ERAB_ARABIC,SIZE_ARABIC,
        FONT_FARSI,COLOR_FARSI,SIZE_FARSI,
        SHOW_TARJOME,

        LOUDNESS,
        DEFAULT_SOUND_TYPE,
        BACKGROUND_PLAY,

        BRIGHTNESS,
        NIGHTMODE,
        KEEP_SCREEN_ON
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings , container , false);
        context = getActivity();
        view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        view.findViewById(R.id.show_quran_settings).setOnClickListener(this);
        view.findViewById(R.id.play_sound_settings).setOnClickListener(this);
        view.findViewById(R.id.display_settings).setOnClickListener(this);
        view.findViewById(R.id.about_settings).setOnClickListener(this);
        view.findViewById(R.id.share_settings).setOnClickListener(this);

        if(sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(Settings.SETTINGS.toString(),Context.MODE_PRIVATE);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.about_settings:
                startActivity(new Intent(getActivity() , AboutUsActivity.class));
                break;
            case R.id.show_quran_settings:
                transition(R.layout.show_quran_settings);
                break;
            case R.id.share_settings:
                shareApplication();
                break;
            case R.id.display_settings:
                transition(R.layout.display_settings);
                break;
            case R.id.play_sound_settings:
                transition(R.layout.play_sound_settings);
                break;
        }
    }

    private void shareApplication() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        ApplicationInfo app = context.getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        File originalApk = new File(filePath);
        try {
            File tempFile = new File(context.getExternalCacheDir() + "/ExtractedApk");
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            tempFile = new File(tempFile.getPath() + "/" + getString(app.labelRes).replace(" ", "").toLowerCase() + ".apk");
            if (!tempFile.exists())
                if (!tempFile.createNewFile())
                    return;
            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(tempFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0)
                out.write(buf, 0, len);
            in.close();
            out.close();
            System.out.println("File copied.");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            startActivity(Intent.createChooser(intent, "Share app via"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void applySetting(Settings setting , String value){
        if(context != null && sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(Settings.SETTINGS.toString() , Context.MODE_PRIVATE);
        if(sharedPreferences == null)
            return;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(setting.toString() , value);
        edit.commit();
    }
    public static String getSetting(Settings settings){
        if(context != null && sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(Settings.SETTINGS.toString() , Context.MODE_PRIVATE);
        if(sharedPreferences == null)
            return "NULL";
        return sharedPreferences.getString(settings.toString() , settings == Settings.FIRSTRUN ? Settings.TRUE.toString() : "");
    }

    private void transition(int layout_id){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.my_fragment , new SubFragmentSettings(layout_id , SettingsActivity.this , getActivity()))
        .commit();
    }
}
