package ir.farzadshami.quran.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.farzadshami.quran.R;
import ir.farzadshami.quran.SuraTextActivity;
import ir.farzadshami.quran.helpers.ADataBase;
import ir.farzadshami.quran.helpers.SqliteDbHelper;
import ir.farzadshami.quran.models.Sura;

public class SubFavoritesAdapter extends RecyclerView.Adapter<SubFavoritesAdapter.CViewHolder> {

    private ArrayList<String> ayes = new ArrayList<>();
    private ADataBase db;
    private String groupename;
    private LayoutInflater lf;

    public SubFavoritesAdapter(String groupename ,LayoutInflater lf , ADataBase db){
        this.groupename = groupename;
        this.db = db;
        this.lf = lf;
    }
    @NonNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = lf.inflate(R.layout.favorite_item_expand , parent , false);
        return new CViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CViewHolder holder, int position) {

        String props = ayes.get(position);
        int suraId = Integer.valueOf(props.substring(0 , props.indexOf("_")));
        int verseId = Integer.valueOf(props.substring(props.indexOf("_") + 1));
        holder.tv.setTag(position);
        holder.tv.setText("سوره "+ Sura.suraNamesArray[suraId] + " آیه "+ (verseId + 1));
        holder.tv.setOnClickListener(gotolistener);

        holder.deleteaye.setTag(position);
        holder.deleteaye.setOnClickListener(deleter);
    }

    @Override
    public int getItemCount() {
        return ayes.size();
    }

    public class CViewHolder extends RecyclerView.ViewHolder{

        public TextView tv;
        public ImageView deleteaye;

        public CViewHolder(View view){
            super(view);

            tv = view.findViewById(R.id.sura_properties);
            deleteaye = view.findViewById(R.id.delete_aye_from_favorite);
        }
    }

    private void updateAyes(){
        ayes = (ArrayList<String>) db.readArray(groupename);
    }

    public void applyChanges(){
        updateAyes();
        notifyDataSetChanged();
    }
    public void clear(){
        ayes.clear();
        notifyDataSetChanged();
    }

    private View.OnClickListener deleter = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();
            ArrayList<String> all = (ArrayList<String>) db.readArray(groupename);
            all.remove(ayes.get(position));
            db.writeArray(groupename , all , 1);

            String props = ayes.get(position);
            int suraId = Integer.valueOf(props.substring(0 , props.indexOf("_")));
            int verseId = Integer.valueOf(props.substring(props.indexOf("_") + 1));

            String sql = "UPDATE quran SET Favorite = 0 WHERE " +
                    ("SuraId = " + (suraId + 1) + " AND VerseId = " + (verseId + 1));
            SqliteDbHelper.getInstance(view.getContext()).setDetails(sql);

            applyChanges();
        }
    };

    private View.OnClickListener gotolistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();
            String props = ayes.get(position);
            Intent suraText = new Intent(view.getContext(), SuraTextActivity.class);
            int suraId = Integer.valueOf(props.substring(0 , props.indexOf("_")));
            int verseId = Integer.valueOf(props.substring(props.indexOf("_") + 1));
            String suraPosition = String.valueOf(suraId + 1);
            suraText.putExtra("suraPosition", suraPosition);
            suraText.putExtra("versePosition" , String.valueOf(verseId));
            view.getContext().startActivity(suraText);
        }
    };
}
