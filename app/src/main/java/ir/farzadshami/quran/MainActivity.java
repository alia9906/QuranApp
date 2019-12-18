package ir.farzadshami.quran;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends FragmentActivity {

    public static final int ID_DOWNLOAD = 0;
    public static final int ID_SEARCH = 1;
    public static final int ID_HOME = 2;
    public static final int ID_BOOKMARK = 3;
    public static final int ID_SETTINGS = 4;

    private int current_index = ID_HOME;
    private static MeowBottomNavigation navigation;

    private static Fragment[] fragments = new Fragment[5];
    private static Fragment home_fragment = new SuraListActivity();
    private static Fragment settings_fragment = new SettingsActivity();
    private static Fragment bookmark_fragment = new FavoriteActivity();
    private static Fragment search_fragment = new SearchActivity();
    private static Fragment download_fragment = new DownloadMangerActivity();
    private static Fragment current_fragment = home_fragment;
    private static String[] page_titles;

    public static Runnable backRunnable;
    public static MainActivity mainActivity;
    public static boolean isOnUi = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;

        navigation = findViewById(R.id.bottomNavigation);
        page_titles = getResources().getStringArray(R.array.pages);

        fragments[0] = download_fragment;
        fragments[1] = search_fragment;
        fragments[2] = home_fragment;
        fragments[3] = bookmark_fragment;
        fragments[4] = settings_fragment;

        navigation.add(new MeowBottomNavigation.Model(ID_DOWNLOAD, R.drawable.ic_download));
        navigation.add(new MeowBottomNavigation.Model(ID_SEARCH, R.drawable.ic_search));
        navigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_home));
        navigation.add(new MeowBottomNavigation.Model(ID_BOOKMARK, R.drawable.ic_bookmark));
        navigation.add(new MeowBottomNavigation.Model(ID_SETTINGS, R.drawable.ic_settings));

        navigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                if(model.getId() == current_index){
                    return null;
                }
               FragmentTransaction ft =  getSupportFragmentManager().beginTransaction().
                        replace(R.id.my_fragment , fragments[model.getId()]);
                if(isOnUi)
                    ft.commit();
                else
                    ft.commitAllowingStateLoss();
                current_index = model.getId();
                current_fragment = fragments[current_index];
                ((TextView) findViewById(R.id.page_title)).setText(page_titles[current_index]);
                return null;
            }
        });

        current_fragment = null;
        navigation.show(ID_DOWNLOAD , true);
        while (current_fragment == null);
        navigation.show(ID_HOME , true);
    }

    @Override
    public void onBackPressed() {
        if(backRunnable != null){
            new Handler().post(backRunnable);
            return;
        }
        super.onBackPressed();
    }

    public Fragment switchToId(int id){
        current_fragment = null;
        navigation.show(id , true);
        while (current_fragment == null);
        return current_fragment;
    }

    @Override
    protected void onDestroy() {
        if(SuraTextActivity.sound != null)
            SuraTextActivity.sound.stopAllSoundServices();
        super.onDestroy();
    }
}
