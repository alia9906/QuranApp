package ir.farzadshami.quran.helpers;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.farzadshami.quran.R;
import ir.farzadshami.quran.models.Sura;

public class SortOnClickListener implements View.OnClickListener {

    public enum SortBy{
        NAME,
        NOZOL,
        ID
    }
    public enum Type{
        ACCENDING,
        DECENDING
    }

    private final SortBy sortBy;
    private Type type;
    private final List<Sura> suras;
    private View root;

    public SortOnClickListener(SortBy sortBy , Type type , View root , List<Sura> suras){
        this.sortBy = sortBy;
        this.type = type;
        this.suras = suras;
        this.root = root;
    }

    @Override
    public void onClick(View view) {
        switch (type){
            case DECENDING:
                type = Type.ACCENDING;
                break;
            case ACCENDING:
                type = Type.DECENDING;
                break;
        }
        switch (sortBy){
            case ID:
                sortById();
                break;
            case NAME:
                sortByName();
                break;
            case NOZOL:
                sortByNozol();
                break;
        }
        updateViews();
    }

    private void sortById(){
        switch (type){
            case ACCENDING:
                new Sort<Sura>().Sort((ArrayList<Sura>) suras , new Sorting.Id(Sorting.ASCENDING));
                break;
            case DECENDING:
                new Sort<Sura>().Sort((ArrayList<Sura>) suras , new Sorting.Id(""));
                break;
        }
    }

    private void sortByName(){
        switch (type){
            case ACCENDING:
                new Sort<Sura>().Sort((ArrayList<Sura>) suras , new Sorting.Name(Sorting.ASCENDING));
                break;
            case DECENDING:
                new Sort<Sura>().Sort((ArrayList<Sura>) suras , new Sorting.Name(""));
                break;
        }
    }

    private void sortByNozol(){
        switch (type){
            case ACCENDING:
                new Sort<Sura>().Sort((ArrayList<Sura>) suras , new Sorting.NozolId(Sorting.ASCENDING));
                break;
            case DECENDING:
                new Sort<Sura>().Sort((ArrayList<Sura>) suras , new Sorting.NozolId(""));
                break;
        }
    }
    private void updateViews(){
        RecyclerView rv = root.findViewById(R.id.rvSuras);
        rv.getAdapter().notifyDataSetChanged();

        ImageView nameacc = root.findViewById(R.id.name_accender);
        ImageView namedecc = root.findViewById(R.id.name_deccender);
        ImageView idacc = root.findViewById(R.id.id_accender);
        ImageView idadecc = root.findViewById(R.id.id_deccender);
        ImageView nozolacc = root.findViewById(R.id.nozol_accender);
        ImageView nozoldecc = root.findViewById(R.id.nozol_deccender);

        nameacc.setImageResource(R.mipmap.ic_ab_up_deactive_arrow);
        idacc.setImageResource(R.mipmap.ic_ab_up_deactive_arrow);
        nozolacc.setImageResource(R.mipmap.ic_ab_up_deactive_arrow);
        namedecc.setImageResource(R.mipmap.ic_ab_down_deactive_arrow);
        idadecc.setImageResource(R.mipmap.ic_ab_down_deactive_arrow);
        nozoldecc.setImageResource(R.mipmap.ic_ab_down_deactive_arrow);

        switch (sortBy){
            case ID:
                switch (type){
                    case ACCENDING:
                        idadecc.setImageResource(R.mipmap.ic_ab_down_active_arrow);
                        break;
                    case DECENDING:
                        idacc.setImageResource(R.mipmap.ic_ab_up_active_arrow);
                        break;
                }
                break;
            case NOZOL:
                switch (type){
                    case ACCENDING:
                        nozoldecc.setImageResource(R.mipmap.ic_ab_down_active_arrow);
                        break;
                    case DECENDING:
                        nozolacc.setImageResource(R.mipmap.ic_ab_up_active_arrow);
                        break;
                }
                break;
            case NAME:
                switch (type){
                    case ACCENDING:
                        namedecc.setImageResource(R.mipmap.ic_ab_down_active_arrow);
                        break;
                    case DECENDING:
                        nameacc.setImageResource(R.mipmap.ic_ab_up_active_arrow);
                        break;
                }
                break;
        }
    }
}
