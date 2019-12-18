package ir.farzadshami.quran.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import ir.farzadshami.quran.R;
import ir.farzadshami.quran.helpers.ADataBase;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.CViewHolder> {

    private ArrayList<String> groupenames;
    private LayoutInflater lf;
    private Context context;
    private final ADataBase db ;

    public FavoriteAdapter(Context context){
        this.groupenames = groupenames;
        this.lf = LayoutInflater.from(context);
        this.context = context;
        this.db = new ADataBase("Groupes" ,context);
        groupenames = (ArrayList<String>) db.readArray("groupesid");
    }

    @Override
    public void onBindViewHolder(@NonNull CViewHolder holder, int position) {
        holder.deletegroupe.setTag(position);
        holder.deletegroupe.setOnClickListener(deleter);
        holder.groupename.setText(groupenames.get(position));
        Object[] args = new Object[3];
        args[0] = position;
        args[1] = false;
        args[2] = holder.content;
        holder.content.setLayoutManager(new LinearLayoutManager(context));
        holder.content.setAdapter(new SubFavoritesAdapter(groupenames.get(position) , lf , db));

        holder.expand.setTag(args);
        holder.expand.setOnClickListener(expander);
    }

    @NonNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = lf.inflate(R.layout.item_favorite , parent , false);
        return new CViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return groupenames.size();
    }

    public class CViewHolder extends RecyclerView.ViewHolder{

        public TextView groupename;
        public ImageView deletegroupe;
        public ImageView expand;
        public RecyclerView content;

        public CViewHolder(View view){
            super(view);

            groupename = view.findViewById(R.id.groupe_name_favorite);
            deletegroupe = view.findViewById(R.id.delete_groupe);
            expand = view.findViewById(R.id.expand_favorite);
            content = view.findViewById(R.id.expand_content);

        }
    }

    private View.OnClickListener deleter = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();
            ArrayList<String> ids = (ArrayList<String>) db.readArray("groupesid");
            ids.remove(groupenames.get(position));
            db.deleteArray(groupenames.get(position));
            db.writeArray("groupesid" , ids , 1);
            groupenames.remove(position);
            notifyDataSetChanged();
        }
    };

    private View.OnClickListener expander = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Object[] args = (Object[]) view.getTag();
            int position = (Integer) args[0];
            Boolean isExpanded = (Boolean) args[1];
            RecyclerView content = (RecyclerView) args[2];

            if(isExpanded){
                ((SubFavoritesAdapter) content.getAdapter()).clear();
                args[1] = false;
                ((ImageView) view).setImageResource(R.mipmap.ic_expand_more);
            }else{
                ((SubFavoritesAdapter) content.getAdapter()).applyChanges();
                args[1] = true;
                ((ImageView) view).setImageResource(R.mipmap.ic_expand_less);
            }
        }
    };
}
