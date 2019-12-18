package ir.farzadshami.quran;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.farzadshami.quran.adapters.SurasAdapter;
import ir.farzadshami.quran.helpers.Cache;
import ir.farzadshami.quran.helpers.RecyclerItemClickListener;
import ir.farzadshami.quran.helpers.SortOnClickListener;
import ir.farzadshami.quran.models.Sura;

public class SuraListActivity extends Fragment {
    private ArrayList<Sura> suras;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_sura_list, container, false);
        final FragmentActivity c = getActivity();
        final RecyclerView recyclerView = view.findViewById(R.id.rvSuras);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);
        suras = Cache.getSuraList("Suras");
        final SurasAdapter surasAdapter = new SurasAdapter(c , suras , (TextView) view.findViewById(R.id.tartil_or_tahdir) , (TextView) view.findViewById(R.id.joz_or_hezb));
        recyclerView.setAdapter(surasAdapter);

        view.findViewById(R.id.nozol_sort_button).setOnClickListener(new SortOnClickListener(SortOnClickListener.SortBy.NOZOL ,
                SortOnClickListener.Type.ACCENDING , view ,
                suras));
        view.findViewById(R.id.name_sort_button).setOnClickListener(new SortOnClickListener(SortOnClickListener.SortBy.NAME ,
                SortOnClickListener.Type.ACCENDING , view ,
                suras));
        view.findViewById(R.id.id_sort_button).setOnClickListener(new SortOnClickListener(SortOnClickListener.SortBy.ID ,
                SortOnClickListener.Type.DECENDING , view ,
                suras));

        view.findViewById(R.id.show_favorite_button).setOnClickListener(new View.OnClickListener() {
            private boolean isShowingFavorite = false;
            @Override
            public void onClick(View view) {
                if(isShowingFavorite){
                    surasAdapter.setSuras(suras);
                    isShowingFavorite = false;
                    ((TextView) view).setText(getString(R.string.favorite));
                }else{
                    ArrayList<Sura> onlyFavoriteSuras = new ArrayList<>();
                    for(int i =0; i < 114 ; i++){
                        if(suras.get(i).getIsFavorite()){
                            onlyFavoriteSuras.add(suras.get(i));
                        }
                    }
                    surasAdapter.setSuras(onlyFavoriteSuras);
                    ((TextView) view).setText(getString(R.string.list));
                    isShowingFavorite = true;
                }
            }
        });

        view.findViewById(R.id.joz_or_hezb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView v = (TextView) view;
                if(v.getText().toString().equals(getString(R.string.joz))){
                    v.setText(getString(R.string.hezb));
                }else{
                    v.setText(getString(R.string.joz));
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
        view.findViewById(R.id.tartil_or_tahdir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView v = (TextView) view;
                if(v.getText().toString().equals(getString(R.string.tartil))){
                    v.setText(getString(R.string.tahdir));
                }else{
                    v.setText(getString(R.string.tartil));
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        view.findViewById(R.id.id_sort_button).callOnClick();

        return view;
    }

}
