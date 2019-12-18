package ir.farzadshami.quran.helpers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.Set;

import ir.farzadshami.quran.R;
import ir.farzadshami.quran.SettingsActivity;
import ir.farzadshami.quran.widget.SubFragmentSettings;

public class SettingsPageHandler {
    private int layout_id;
    private View root;
    private Fragment current;

    public static String ARABICORFARSI = "";

    public SettingsPageHandler(int layout_id , View root , Fragment current){
        this.layout_id = layout_id;
        this.root = root;
        this.current = current;
    }

    public void register(){
        ARABICORFARSI = "";
        switch (layout_id){
            case R.layout.show_quran_settings:
                root.findViewById(R.id.arabi_ghalam_settings).setOnClickListener(justOpenClickListener);
                root.findViewById(R.id.farsi_ghalam_settings).setOnClickListener(justOpenClickListener);
                CheckBox checkBox = root.findViewById(R.id.show_tarjome_checked);
                checkBox.setTag(SettingsActivity.Settings.SHOW_TARJOME);
                checkBox.setOnClickListener(checkedListener);
                break;
            case R.layout.play_sound_settings:
                AppCompatSeekBar seek = root.findViewById(R.id.sound_seek_bar);
                seek.setTag(SettingsActivity.Settings.LOUDNESS);
                seek.setOnSeekBarChangeListener(seekBarListener);
                root.findViewById(R.id.default_sound_settings).setOnClickListener(applyASettingListener);
                checkBox = root.findViewById(R.id.play_sound_in_back_checked);
                checkBox.setTag(SettingsActivity.Settings.BACKGROUND_PLAY);
                checkBox.setOnClickListener(checkedListener);
                break;
            case R.layout.display_settings:
                seek = root.findViewById(R.id.brightness_seek_bar);
                seek.setTag(SettingsActivity.Settings.BRIGHTNESS);
                seek.setOnSeekBarChangeListener(seekBarListener);
                checkBox = root.findViewById(R.id.night_mode_check_box);
                checkBox.setTag(SettingsActivity.Settings.NIGHTMODE);
                checkBox.setOnClickListener(checkedListener);
                checkBox = root.findViewById(R.id.keep_screen_check_box);
                checkBox.setTag(SettingsActivity.Settings.KEEP_SCREEN_ON);
                checkBox.setOnClickListener(checkedListener);
                break;
            case R.layout.ghalam_settings:
                boolean isItArabic = "a".equals(root.getTag());
                root.findViewById(R.id.font_settings).setOnClickListener(applyASettingListener);
                root.findViewById(R.id.color_settings).setOnClickListener(applyASettingListener);
                root.findViewById(R.id.color_settings_erab).setOnClickListener(applyASettingListener);
                seek = root.findViewById(R.id.font_seek_bar);
                seek.setTag(isItArabic ? SettingsActivity.Settings.SIZE_ARABIC : SettingsActivity.Settings.SIZE_FARSI);
                seek.setOnSeekBarChangeListener(seekBarListener);
                break;
        }
    }

    private View.OnClickListener justOpenClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.arabi_ghalam_settings){
                ARABICORFARSI = "a";
                current.getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.my_fragment , new SubFragmentSettings(R.layout.ghalam_settings , current , current.getActivity()))
                        .commit();
                return;
            }
            if(view.getId() == R.id.farsi_ghalam_settings){
                ARABICORFARSI = "f";
                current.getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.my_fragment , new SubFragmentSettings(R.layout.ghalam_settings , current , current.getActivity()))
                        .commit();
            }
        }
    };

    private View.OnClickListener applyASettingListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.default_sound_settings){
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(root.getContext() , android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add(root.getContext().getResources().getString(R.string.tartil));
                arrayAdapter.add(root.getContext().getResources().getString(R.string.tahdir));
                final TextView tv = ((TextView) root.findViewById(R.id.sub_default_sound_settings));
                String current = tv.getText().toString();

                new AlertDialog.Builder(root.getContext())
                        .setSingleChoiceItems(arrayAdapter, current.equals(arrayAdapter.getItem(0)) ? 0 : 1,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SettingsActivity.applySetting(SettingsActivity.Settings.DEFAULT_SOUND_TYPE , i == 0 ?
                                        SettingsActivity.Settings.TARTIL.toString() : SettingsActivity.Settings.TAHDIR.toString());
                                tv.setText(arrayAdapter.getItem(i));
                                dialogInterface.cancel();
                            }
                        }).show();
                return;
            }
            if(view.getId() == R.id.font_settings){
                final boolean isItArabic = "a".equals(root.getTag());
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(root.getContext() , android.R.layout.select_dialog_singlechoice);
                if(isItArabic){
                    String[] fonts = root.getContext().getResources().getStringArray(R.array.arabicfontsname);
                    for(int i =0 ; i< fonts.length ; i++)
                        arrayAdapter.add(fonts[i]);
                }else{
                    String[] fonts = root.getContext().getResources().getStringArray(R.array.farsicfontsname);
                    for(int i =0 ; i< fonts.length ; i++)
                        arrayAdapter.add(fonts[i]);
                }
                int current = 0;
                final TextView tv = root.findViewById(R.id.sub_current_font);
                for(int i =0 ; i < arrayAdapter.getCount() ; i++)
                    if(arrayAdapter.getItem(i).equals(tv.getText().toString())) {
                        current = i;
                        break;
                    }
                new AlertDialog.Builder(root.getContext())
                        .setSingleChoiceItems(arrayAdapter, current,new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SettingsActivity.applySetting(isItArabic ? SettingsActivity.Settings.FONT_ARABIC : SettingsActivity.Settings.FONT_FARSI,
                                        isItArabic ? root.getContext().getResources().getStringArray(R.array.arabicfontscachename)[i]:
                                                root.getContext().getResources().getStringArray(R.array.farsicfontscachename)[i]);
                                tv.setText(arrayAdapter.getItem(i));
                                updateTvInGhalamSettings();
                                dialogInterface.cancel();
                            }
                        }).show();
                return;
            }
            if(view.getId() == R.id.color_settings){
                final boolean isItArabic = "a".equals(root.getTag());
                new ColorPickerDialog.Builder(root.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                        .setTitle("انتخاب رنگ قلم")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(root.getContext().getString(R.string.chhose),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        root.findViewById(R.id.color_present).setBackgroundColor(envelope.getColor());
                                        SettingsActivity.applySetting(isItArabic ? SettingsActivity.Settings.COLOR_ARABIC:SettingsActivity.Settings.COLOR_FARSI,
                                                String.valueOf(envelope.getHexCode()));
                                        updateTvInGhalamSettings();
                                    }
                                })
                        .setNegativeButton(root.getContext().getString(R.string.cancle),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                        .attachAlphaSlideBar(false)
                        .attachBrightnessSlideBar(true)  // default is true. If false, do not show the BrightnessSlideBar.
                        .show();
                return;
            }
            if(view.getId() == R.id.color_settings_erab){
                final boolean isItArabic = "a".equals(root.getTag());
                new ColorPickerDialog.Builder(root.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                        .setTitle(R.string.color_erab)
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(root.getContext().getString(R.string.chhose),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        root.findViewById(R.id.color_present_erab).setBackgroundColor(envelope.getColor());
                                        if(isItArabic)
                                        SettingsActivity.applySetting(SettingsActivity.Settings.COLOR_ERAB_ARABIC,
                                                String.valueOf(envelope.getHexCode()));
                                        updateTvInGhalamSettings();
                                    }
                                })
                        .setNegativeButton(root.getContext().getString(R.string.cancle),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                        .attachAlphaSlideBar(false)
                        .attachBrightnessSlideBar(true)  // default is true. If false, do not show the BrightnessSlideBar.
                        .show();
                return;
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
        SettingsActivity.Settings settings;
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Toast.makeText(seekBar.getContext() , String.valueOf(i) , Toast.LENGTH_SHORT).show();
            updateTvInGhalamSettings();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if(settings == null)
                 settings = (SettingsActivity.Settings) seekBar.getTag();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            SettingsActivity.applySetting(settings , String.valueOf(seekBar.getProgress()));
        }
    };

    private View.OnClickListener checkedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CheckBox c = (CheckBox) view;
            SettingsActivity.applySetting((SettingsActivity.Settings) view.getTag() ,
                    c.isChecked() ? SettingsActivity.Settings.TRUE.toString() : SettingsActivity.Settings.FALSE.toString());

            return;
        }
    };

    private void updateTvInGhalamSettings(){
        if(root.findViewById(R.id.font_seek_bar) == null)
            return;
        int size = ((AppCompatSeekBar)root.findViewById(R.id.font_seek_bar)).getProgress();
        String font = ((TextView) root.findViewById(R.id.sub_current_font)).getText().toString();
        String fontCache = "Al Qalam New.ttf";
        String[] fonts = "a".equals(root.getTag()) ? root.getContext().getResources().getStringArray(R.array.arabicfontsname):
                root.getContext().getResources().getStringArray(R.array.farsicfontsname);
        for(int i =0 ; i < fonts.length ; i++)
            if(font.equals(fonts[i])) {
                fontCache = "a".equals(root.getTag()) ? root.getContext().getResources().getStringArray(R.array.arabicfontscachename)[i] :
                        root.getContext().getResources().getStringArray(R.array.farsicfontscachename)[i];
                break;
            }
        Typeface typeface = Cache.getFont(fontCache,root.getContext());
        TextView tv = root.findViewById(R.id.font_view);
        tv.setTypeface(typeface);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP , size);
        tv.setTextColor(((ColorDrawable) root.findViewById(R.id.color_present).getBackground()).getColor());
    }
}
