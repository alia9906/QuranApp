package ir.farzadshami.quran.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import ir.farzadshami.quran.MainActivity;
import ir.farzadshami.quran.R;
import ir.farzadshami.quran.helpers.SettingsPageHandler;
import ir.farzadshami.quran.helpers.SettingsPageInitilizer;

public class SubFragmentSettings extends Fragment {

    private int layout_id;
    private Fragment before;
    private FragmentActivity main;

    public SubFragmentSettings(int layout_id , Fragment before , FragmentActivity main){
        this.layout_id = layout_id;
        this.before = before;
        this.main = main;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layout_id , container , false);

        view.setTag(SettingsPageHandler.ARABICORFARSI);

        new SettingsPageInitilizer(layout_id , view)._init_();
        new SettingsPageHandler(layout_id , view , this).register();
        MainActivity.backRunnable = onBackPressed;
        view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        return view;
    }

    Runnable onBackPressed = new Runnable() {
        @Override
        public void run() {
            main.getSupportFragmentManager().beginTransaction().
                    replace(R.id.my_fragment , before).commit();
        }
    };
}
