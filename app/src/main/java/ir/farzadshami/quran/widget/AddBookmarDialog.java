package ir.farzadshami.quran.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.farzadshami.quran.R;
import ir.farzadshami.quran.models.Favorite;

public class AddBookmarDialog extends AlertDialog {

    private OnBookMarkSelectedListener onBookMarkSelectedListener;
    private RecyclerView recyclerView;
    private EditText editText;
    private Favorite favorite;
    private ArrayList<String> groupes;
    private ArrayList<String> groupesToAdd = new ArrayList<>();
    private AlertDialog retval;

    public AddBookmarDialog(Context context , final OnBookMarkSelectedListener onBookMarkSelectedListener , final Favorite favorite , final ArrayList<String> groupes){
        super(context);
        this.onBookMarkSelectedListener = onBookMarkSelectedListener;
        View root = LayoutInflater.from(context).inflate(R.layout.bookmark_dialog , null , false);
        this.groupes = groupes;

        recyclerView = root.findViewById(R.id.groupes_rv);
        editText = root.findViewById(R.id.add_groupe);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        final RVAdapter adapter = new RVAdapter(groupes , context);
        recyclerView.setAdapter(adapter);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String newgroupe = editText.getText().toString();
                newgroupe = newgroupe.replace("\n" , "");
                if(newgroupe.length() > 0) {
                    if(groupes.contains(newgroupe)){
                        if(!groupesToAdd.contains(newgroupe)){
                            groupesToAdd.add(newgroupe);
                            adapter.notifyDataSetChanged();
                            editText.setText("");
                            return false;
                        }
                    }
                    if (onBookMarkSelectedListener != null)
                        if(onBookMarkSelectedListener.onGroupeCreated(newgroupe)){
                            groupes.add(newgroupe);
                            groupesToAdd.add(newgroupe);
                            adapter.notifyDataSetChanged();
                        }
                    editText.setText("");
                }else{
                    Toast.makeText(AddBookmarDialog.this.getContext() , "اسم گروه خالی است.",Toast.LENGTH_SHORT).show();
                    editText.setText("");
                }
                return false;
            }
        });
        retval = new AlertDialog.Builder(context)
                .setTitle("ایجاد نشانه گذاری")
                .setPositiveButton(R.string.chhose , new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface di , int j) {
                        if(onBookMarkSelectedListener != null){
                            for(int i =0 ; i < groupesToAdd.size() ;i++){
                                onBookMarkSelectedListener.onBookMarkSelected(favorite , groupesToAdd.get(i));
                            }
                        }
                        di.cancel();
                    }
                }).setNegativeButton(R.string.cancle, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).setView(root)
                .create();

    }

    @Override
    public void show() {
        retval.show();
    }

    private class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVViewHolder>{

        ArrayList<String> groupes;
        LayoutInflater layoutInflater;

        RVAdapter(ArrayList<String> groupes , Context context){
            this.groupes = groupes;
            this.layoutInflater = LayoutInflater.from(context);
        }
        @NonNull
        @Override
        public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View favoriteView = layoutInflater.inflate(R.layout.bookmark_groupe_item, parent, false);
            return new RVViewHolder(favoriteView);
        }

        @Override
        public void onBindViewHolder(@NonNull RVViewHolder holder, int position) {
            holder.name.setText(groupes.get(position));
            if(groupesToAdd.contains(groupes.get(position)))
                holder.checkBox.setChecked(true);
            holder.checkBox.setTag(position);
            holder.checkBox.setOnCheckedChangeListener(listener);
        }

        public class RVViewHolder extends RecyclerView.ViewHolder{
            public TextView name;
            public CheckBox checkBox;
            public RVViewHolder(@NonNull  View view){
                super(view);
                name = view.findViewById(R.id.groupe_name);
                checkBox = view.findViewById(R.id.grupe_checked);
            }
        }
        @Override
        public int getItemCount() {
            return groupes.size();
        }

        private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String groupename = groupes.get((Integer) compoundButton.getTag());
                if(b){
                    groupesToAdd.add(groupename);
                }else{
                    groupesToAdd.remove(groupename);
                }
            }
        };
    }
}
