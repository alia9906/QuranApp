package ir.farzadshami.quran.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.farzadshami.quran.DownloadMangerActivity;
import ir.farzadshami.quran.MainActivity;
import ir.farzadshami.quran.R;
import ir.farzadshami.quran.SettingsActivity;
import ir.farzadshami.quran.SuraTextActivity;
import ir.farzadshami.quran.helpers.Cache;
import ir.farzadshami.quran.helpers.Downloader;
import ir.farzadshami.quran.helpers.UrlCreator;
import ir.farzadshami.quran.models.Download;
import ir.farzadshami.quran.models.Sura;

import static java.lang.String.format;

public class SurasAdapter extends RecyclerView.Adapter<SurasAdapter.ViewHolder> {

    private List<Sura> suras;
    private LayoutInflater mInflater;
    private Typeface arabicFont;
    private Typeface farsiFont;
    private Context context;
    private TextView tartilOrTahdir;
    private TextView jozOrHezb;

    public SurasAdapter(Context context, List<Sura> suras, TextView tartlOrTahdir , TextView jozOrHezb) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.suras = suras;
        this.tartilOrTahdir = tartlOrTahdir;
        SharedPreferences settings = context.getSharedPreferences(SettingsActivity.Settings.SETTINGS.toString(), Context.MODE_PRIVATE);
        arabicFont = Cache.getFont(settings.getString(SettingsActivity.Settings.FONT_ARABIC.toString(), "Al Qalam Quran.ttf"), context);
        farsiFont = Cache.getFont(settings.getString(SettingsActivity.Settings.FONT_FARSI.toString(), "BNazanin.ttf"), context);
        this.jozOrHezb = jozOrHezb;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View suraView = mInflater.inflate(R.layout.item_sura, parent, false);
        return new ViewHolder(suraView);
    }

    @Override
    public void onBindViewHolder(@NonNull SurasAdapter.ViewHolder holder, int position) {
        Sura sura = suras.get(position);
        TextView suraName = holder.suraNameTv;
        TextView nozolId = holder.nozolIdTv;
        TextView hezbIdTv = holder.hezbIdTv;
        TextView suraCountTv = holder.suraCountTv;
        TextView suraPlaceTv = holder.suraPlaceTv;
        TextView idTv = holder.idTv;
        suraName.setText(sura.getSuraName());
        suraName.setTypeface(arabicFont);

        suraName.setTypeface(suraName.getTypeface() , Typeface.BOLD);

        nozolId.setText(persianNumbers(sura.getNozolId()));
        hezbIdTv.setText(persianNumbers(jozOrHezb.getText().toString().equals(jozOrHezb.getContext().getResources().getString(R.string.hezb))?
                sura.getHezbId() : sura.getJozId()));
        suraCountTv.setText(persianNumbers(sura.getSuraCount()));
        suraPlaceTv.setText(sura.getPlace() + " - ");
        idTv.setText(persianNumbers(sura.getId() + 1));

        holder.downloadButton.setTag(position);
        holder.bookmarkButton.setTag(position);
        holder.downloadButton.setOnClickListener(downloadOnClickListener);
        holder.bookmarkButton.setOnClickListener(bookmarkOnClickListener);

        if(sura.getIsDownloaded(tartilOrTahdir.getText().toString())) ((ImageView) holder.downloadButton).setImageResource(R.mipmap.ic_download_play);
        if(sura.getIsFavorite()) ((ImageView) holder.bookmarkButton).setImageResource(R.mipmap.ic_favorite_active);

        suraName.setTag(position);
        suraCountTv.setTag(position);
        suraPlaceTv.setTag(position);
        idTv.setTag(position);

        idTv.setOnClickListener(suraOnClickListener);
        ViewGroup suraNameParent = (ViewGroup) suraName.getParent();
        suraNameParent.setTag(position);
        suraNameParent.setOnClickListener(suraOnClickListener);
    }

    @Override
    public int getItemCount() {
        return suras.size();
    }

    private String persianNumbers(int input) {
        return format(new Locale("ar"), "%d", input);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView suraNameTv;
        public TextView nozolIdTv;
        public TextView hezbIdTv;
        public TextView suraPlaceTv;
        public TextView suraCountTv;
        public TextView idTv;
        public View downloadButton;
        public View bookmarkButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            suraNameTv = itemView.findViewById(R.id.sura_name);
            nozolIdTv = itemView.findViewById(R.id.nozol_id);
            hezbIdTv = itemView.findViewById(R.id.hezb_id);
            suraPlaceTv = itemView.findViewById(R.id.sura_place);
            suraCountTv = itemView.findViewById(R.id.sura_count);
            idTv = itemView.findViewById(R.id.id);
            downloadButton = itemView.findViewById(R.id.download_sura);
            bookmarkButton = itemView.findViewById(R.id.bookmark_sura);
        }
    }

    private View.OnClickListener downloadOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                int position = (Integer) view.getTag();
                if(!suras.get(position).getIsDownloaded(tartilOrTahdir.getText().toString())) {
                    int suraid = suras.get(position).getId();
                    Download.Type type = tartilOrTahdir.getText().toString().equals(context.getResources().getString(R.string.tartil)) ? Download.Type.TARTL: Download.Type.TAHDIR;
                    String jozzs = Sura.getJozInSura(suraid);
                    ArrayList<Integer> jozids = new ArrayList<Integer>();

                    int start = 0;
                    while (start < jozzs.length()){
                        int index = jozzs.indexOf('|' , start);
                        if(index == -1)
                            index = jozzs.length();
                        String val = jozzs.substring(start , index);
                        if("".equals(val))
                            break;
                        jozids.add(Integer.valueOf(val));
                        start = index + 1;
                    }

                    Integer[] last = new Integer[jozids.size()];
                    for(int i =0 ; i < last.length ; i++)
                        last[i] = jozids.get(i);

                    ((DownloadMangerActivity)((MainActivity) context).switchToId(MainActivity.ID_DOWNLOAD)).setInitialDowload(type , last);
                }else{
                    Intent suraText = new Intent(context, SuraTextActivity.class);
                    String suraPosition = String.valueOf(suras.get(position).getId() + 1);
                    suraText.putExtra("suraPosition", suraPosition);
                    context.startActivity(suraText);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener bookmarkOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();
            suras.get(position).setIsFavorite(!suras.get(position).getIsFavorite());
            ((ImageView) view).setImageResource(suras.get(position).getIsFavorite() ?
                    R.mipmap.ic_favorite_active : R.mipmap.ic_favorite_deactive);
        }
    };

    private View.OnClickListener suraOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();
            Intent suraText = new Intent(context, SuraTextActivity.class);
            String suraPosition = String.valueOf(suras.get(position).getId() + 1);
            suraText.putExtra("suraPosition", suraPosition);
            context.startActivity(suraText);
        }
    };


    public void setSuras(List<Sura> suras) {
        this.suras = suras;
        notifyDataSetChanged();
    }
}
