package ir.farzadshami.quran.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;

import java.util.Set;

import ir.farzadshami.quran.R;
import ir.farzadshami.quran.SettingsActivity;

public class SettingsPageInitilizer {
    private int layout_id;
    private View root;
    public SettingsPageInitilizer(int layout_id , View root){
        this.layout_id = layout_id;
        this.root = root;
    }

    public void _init_(){
        switch (layout_id){
            case R.layout.show_quran_settings:
                String show_tarjome = SettingsActivity.getSetting(SettingsActivity.Settings.SHOW_TARJOME);
                ((CheckBox)root.findViewById(R.id.show_tarjome_checked)).setChecked(
                        SettingsActivity.Settings.TRUE.toString().equals(show_tarjome) ? true : false
                );
                break;
            case R.layout.ghalam_settings:
                boolean isItArabic = "a".equals(root.getTag());
                String[] fontscache = isItArabic? root.getContext().getResources().getStringArray(R.array.arabicfontscachename):
                        root.getContext().getResources().getStringArray(R.array.farsicfontscachename);
                String curfontcache = SettingsActivity.getSetting(isItArabic ? SettingsActivity.Settings.FONT_ARABIC : SettingsActivity.Settings.FONT_FARSI);

                for(int i =0 ; i < fontscache.length ; i++)
                    if(fontscache[i].equals(curfontcache)) {
                        ((TextView) root.findViewById(R.id.sub_current_font)).setText(
                                root.getContext().getResources().getStringArray(isItArabic ? R.array.arabicfontsname : R.array.farsicfontsname)[i]
                        );
                        break;
                    }

                String hexColor = SettingsActivity.getSetting(isItArabic ? SettingsActivity.Settings.COLOR_ARABIC : SettingsActivity.Settings.COLOR_FARSI);
                hexColor = "#" + hexColor;
                root.findViewById(R.id.color_present).setBackgroundColor(Color.parseColor(hexColor));
                if(isItArabic) {
                    String hexColor2 = SettingsActivity.getSetting(SettingsActivity.Settings.COLOR_ERAB_ARABIC);
                    hexColor2 = "#" + hexColor2;
                    root.findViewById(R.id.color_present_erab).setBackgroundColor(Color.parseColor(hexColor2));
                }

                String size = SettingsActivity.getSetting(isItArabic ? SettingsActivity.Settings.SIZE_ARABIC : SettingsActivity.Settings.SIZE_FARSI);
                ((AppCompatSeekBar) root.findViewById(R.id.font_seek_bar)).setProgress(Integer.valueOf(size));

                ((TextView) root.findViewById(R.id.font_view)).setText(isItArabic ? root.getContext().getResources().getString(R.string.arabic_sample):
                        root.getContext().getResources().getString(R.string.farsi_sample));

                updateTvInGhalamSettings();
                break;
            case R.layout.play_sound_settings:
                String volume = SettingsActivity.getSetting(SettingsActivity.Settings.LOUDNESS);
                ((AppCompatSeekBar) root.findViewById(R.id.sound_seek_bar)).setProgress(Integer.valueOf(volume));
                String defaultsound = SettingsActivity.getSetting(SettingsActivity.Settings.DEFAULT_SOUND_TYPE);
                ((TextView) root.findViewById(R.id.sub_default_sound_settings)).setText(SettingsActivity.Settings.TARTIL.toString().equals(defaultsound)?
                root.getContext().getResources().getString(R.string.tartil) : root.getContext().getString(R.string.tahdir));
                String backplay = SettingsActivity.getSetting(SettingsActivity.Settings.BACKGROUND_PLAY);
                ((CheckBox) root.findViewById(R.id.play_sound_in_back_checked)).setChecked(
                        SettingsActivity.Settings.TRUE.toString().equals(backplay) ? true : false
                );
                break;
            case R.layout.display_settings:
                String brightness = SettingsActivity.getSetting(SettingsActivity.Settings.BRIGHTNESS);
                ((AppCompatSeekBar) root.findViewById(R.id.brightness_seek_bar)).setProgress(Integer.valueOf(brightness));
                String nightmode = SettingsActivity.getSetting(SettingsActivity.Settings.NIGHTMODE);
                ((CheckBox) root.findViewById(R.id.night_mode_check_box)).setChecked(
                        SettingsActivity.Settings.TRUE.toString().equals(nightmode) ? true : false);
                String keepscreen = SettingsActivity.getSetting(SettingsActivity.Settings.KEEP_SCREEN_ON);
                ((CheckBox) root.findViewById(R.id.keep_screen_check_box)).setChecked(
                        SettingsActivity.Settings.TRUE.toString().equals(keepscreen) ? true : false);
                break;
        }
    }

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
