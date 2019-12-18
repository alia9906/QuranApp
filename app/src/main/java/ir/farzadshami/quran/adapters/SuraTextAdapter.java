package ir.farzadshami.quran.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.farzadshami.quran.R;
import ir.farzadshami.quran.SettingsActivity;
import ir.farzadshami.quran.helpers.Cache;
import ir.farzadshami.quran.models.DbModel;

public class SuraTextAdapter extends RecyclerView.Adapter<SuraTextAdapter.ViewHolder> {

    private final int[] backgroundColors = {R.color.suraTextBackground, R.color.suraList};
    private final int[] movingLettersColors = {R.color.item_color_two, R.color.item_color_one};
    private ArrayList<DbModel> suraText;
    private LayoutInflater inflater;
    private Context context;


    public SuraTextAdapter(Context context, ArrayList<DbModel> sura) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.suraText = sura;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View suraView = inflater.inflate(R.layout.item_sura_text, parent, false);
        return new ViewHolder(suraView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DbModel sura = suraText.get(position);
        LinearLayout lr = holder.suraItemLr;
        TextView suraArabic = holder.suraArabicText;
        TextView suraFarsi = holder.suraFarsiText;
        int bgColor = ContextCompat.getColor(context, backgroundColors[position % 2]);
        SettingsActivity.context = context;
        String show_tarjome = SettingsActivity.getSetting(SettingsActivity.Settings.SHOW_TARJOME);
        if (SettingsActivity.Settings.TRUE.toString().equals(show_tarjome))
            suraFarsi.setVisibility(View.VISIBLE);
        else
            suraFarsi.setVisibility(View.GONE);
        Typeface arabicFont = Cache.getFont(SettingsActivity.getSetting(SettingsActivity.Settings.FONT_ARABIC), context);
        Typeface farsiFont = Cache.getFont(SettingsActivity.getSetting(SettingsActivity.Settings.FONT_FARSI), context);
        lr.setBackgroundColor(bgColor);
        suraArabic.setTypeface(arabicFont);
        suraFarsi.setTypeface(farsiFont);
        suraArabic.setTextSize(TypedValue.COMPLEX_UNIT_DIP,Integer.valueOf(SettingsActivity.getSetting(SettingsActivity.Settings.SIZE_ARABIC)));
        suraFarsi.setTextSize(TypedValue.COMPLEX_UNIT_DIP,Integer.valueOf(SettingsActivity.getSetting(SettingsActivity.Settings.SIZE_FARSI)));
        suraArabic.setTextColor(Color.parseColor("#" + SettingsActivity.getSetting(SettingsActivity.Settings.COLOR_ARABIC)));
        suraArabic.setText(Html.fromHtml(makeColoredStringBuffer(sura.getArabicText(), "#" + SettingsActivity.getSetting(SettingsActivity.Settings.COLOR_ERAB_ARABIC).substring(2))));
        suraFarsi.setTextColor(Color.parseColor("#" + SettingsActivity.getSetting(SettingsActivity.Settings.COLOR_FARSI)));
        suraFarsi.setText(sura.getFarsiText());
    }

    private String makeColoredStringBuffer(String text, String color) {
        Pattern movingLetters = Pattern.compile("([^ء-ي])");
        StringBuffer sb = new StringBuffer(text.length());
        Matcher o = movingLetters.matcher(text);
        while (o.find())
            o.appendReplacement(sb, "<font color=\"" + color + "\">" + o.group(1) + "</font>");
        o.appendTail(sb);
        return sb.toString();
    }


    @Override
    public int getItemCount() {
        return suraText.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView suraArabicText;
        public TextView suraFarsiText;
        public LinearLayout suraItemLr;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            suraArabicText = itemView.findViewById(R.id.sura_arabic_tv);
            suraFarsiText = itemView.findViewById(R.id.sura_farsi_tv);
            suraItemLr = itemView.findViewById(R.id.sura_item_lr);
        }
    }
}
