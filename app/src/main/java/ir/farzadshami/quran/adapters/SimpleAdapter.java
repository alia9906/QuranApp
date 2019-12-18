package ir.farzadshami.quran.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.farzadshami.quran.R;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.CViewHolder> {

    private ArrayList<String> result;
    private LayoutInflater lf;
    private View.OnClickListener listener;

    public SimpleAdapter(ArrayList<String> result , Context context , View.OnClickListener suraSelectedListener){
        this.result = result;
        this.lf = LayoutInflater.from(context);
        this.listener = suraSelectedListener;
    }

    @Override
    public void onBindViewHolder(@NonNull CViewHolder holder, int position) {
        holder.tv.setText(result.get(position));
        holder.tv.setOnClickListener(listener);
    }

    @NonNull
    @Override
    public CViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = lf.inflate(R.layout.item_simple_adapter , parent  , false);
        return new CViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class CViewHolder extends RecyclerView.ViewHolder{
        public TextView tv;
        public CViewHolder(View view){
            super(view);
            tv = view.findViewById(R.id.tv);
        }
    }

    public void updateAdapter(ArrayList<String> result){
        this.result = result;
        notifyDataSetChanged();
    }
}
