package ir.farzadshami.quran.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.farzadshami.quran.R;
import ir.farzadshami.quran.helpers.SqliteDbHelper;
import ir.farzadshami.quran.models.DbModel;
import ir.farzadshami.quran.models.Sura;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<DbModel> list;
    public static SqliteDbHelper.SearchIn type = SqliteDbHelper.SearchIn.ARABI;
    public static String searchState;

    public SearchAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View searchView = layoutInflater.inflate(R.layout.item_search, parent, false);
        return new ViewHolder(searchView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(searchState == null)
            searchState = "";
        DbModel model = list.get(position);

        String whole = type == SqliteDbHelper.SearchIn.FARSI ? model.getFarsiText() : model.getArabicText();
        int index = whole.indexOf(searchState);
        String before = whole.substring(0 , index);
        String main = searchState;
        String after = whole.substring(index + searchState.length());

        holder.txtbefore.setText(before);
        holder.txrafter.setText(after);
        holder.txt.setText(main);
        holder.ayesore.setText("سوره "+ Sura.suraNamesArray[model.getSuraId() - 1] + " آیه " + (model.getVerseId()));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void updateList(List<DbModel> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt;
        public TextView txtbefore;
        public TextView txrafter;
        public TextView ayesore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);
            txtbefore = itemView.findViewById(R.id.txtbefore);
            txrafter = itemView.findViewById(R.id.txtafter);
            ayesore = itemView.findViewById(R.id.ayesore);
        }
    }
}
