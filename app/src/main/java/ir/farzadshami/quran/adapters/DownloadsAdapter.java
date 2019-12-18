package ir.farzadshami.quran.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.farzadshami.quran.R;
import ir.farzadshami.quran.models.Download;

public class DownloadsAdapter extends RecyclerView.Adapter<DownloadsAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<Download> list;
    private Download.Type type;

    public DownloadsAdapter(Context context , Download.Type type) {
        this.layoutInflater = LayoutInflater.from(context);
        this.list = Download.createDownloadList(type);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View downloadView = layoutInflater.inflate(R.layout.item_download, parent, false);
        return new ViewHolder(downloadView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Download download = list.get(position);
        CheckBox checkBox = holder.checkBox;
        checkBox.setText(download.getText());
        checkBox.setTag(position);
        checkBox.setOnClickListener(switchCheck);
        checkBox.setChecked(download.getChecked());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    public void update(Download.Type type){
        this.type = type;
        list = Download.createDownloadList(type);
        notifyDataSetChanged();
    }
    public void update(List<Download> list, Download.Type type){
        this.type = type;
        this.list = list;
        notifyDataSetChanged();
    }

    private View.OnClickListener switchCheck = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            list.get((Integer) view.getTag()).setChecked(((CheckBox) view).isChecked());
        }
    };

    public List<Download> getDownloadList(){
        return list;
    }
}
