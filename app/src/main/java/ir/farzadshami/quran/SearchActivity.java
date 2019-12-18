package ir.farzadshami.quran;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.farzadshami.quran.adapters.SearchAdapter;
import ir.farzadshami.quran.adapters.SimpleAdapter;
import ir.farzadshami.quran.helpers.RecyclerItemClickListener;
import ir.farzadshami.quran.helpers.SqliteDbHelper;
import ir.farzadshami.quran.models.DbModel;
import ir.farzadshami.quran.models.Sura;
import ir.farzadshami.quran.widget.MultiSwitch;

public class SearchActivity extends Fragment {
    private SearchAdapter adapter;
    private SimpleAdapter simpleAdapter;
    private ArrayList<DbModel> searchResult;
    private MultiSwitch multiSwitch;
    private EditText searchEditTxt;
    private RecyclerView recyclerView;
    private int sura_id_search = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_search, container, false);
        final FragmentActivity c = getActivity();
        searchEditTxt = view.findViewById(R.id.search_et);
        multiSwitch = view.findViewById(R.id.multi_switch_search);
        String[] s = new String[3];
        s[0] = "متن عربی";
        s[1] = "ترجمه";
        s[2] = "جستجو در سوره : همه سوره ها";
        int[] weights = new int[3];
        weights[0] = 8;
        weights[1] = 8;
        weights[2] = 26;
        multiSwitch.initialize(s, weights , new MultiSwitch.SwitchToggleListener() {
            @Override
            public void onSwitchToggle(int which) {
                switch (which){
                    case 2:
                        searchEditTxt.setHint("جستجو در اسم سوره ها");
                        break;
                    default:
                        searchEditTxt.setHint("جستجو حرف یا کلمه");
                }
                if(searchResult != null)
                    searchResult.clear();
                else
                    searchResult = new ArrayList<>();
                adapter.updateList(searchResult);
                searchEditTxt.setText("");
            }
        });
        recyclerView = view.findViewById(R.id.rvSearch);
        adapter = new SearchAdapter(c);
        simpleAdapter = new SimpleAdapter(new ArrayList<String>() , getActivity() , suraSelectedListener);
        recyclerView.setAdapter(adapter);

        InputMethodManager img = (InputMethodManager) ((Context) c).getSystemService(Context.INPUT_METHOD_SERVICE);
        img.toggleSoftInput(InputMethodManager.SHOW_FORCED , InputMethodManager.HIDE_IMPLICIT_ONLY);
        searchEditTxt.requestFocus();

        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);

        searchEditTxt.addTextChangedListener(new TextWatcher() {

            boolean ignore = false;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!ignore) {
                    ignore = true;
                    switch (multiSwitch.getCurrentSwitch()) {
                        case 0:
                            SearchAdapter.type = SqliteDbHelper.SearchIn.ARABI;
                            SearchAdapter.searchState = charSequence.toString();
                            SqliteDbHelper dbHelper = SqliteDbHelper.getInstance(c);
                            dbHelper.openDatabase();
                            searchResult = dbHelper.getSearchDetails(charSequence.toString() , SqliteDbHelper.SearchIn.ARABI , sura_id_search);
                            recyclerView.setAdapter(adapter);
                            adapter.updateList(searchResult);
                            break;
                        case 1:
                            SearchAdapter.type = SqliteDbHelper.SearchIn.FARSI;
                            SearchAdapter.searchState = charSequence.toString();
                            dbHelper = SqliteDbHelper.getInstance(c);
                            dbHelper.openDatabase();
                            searchResult = dbHelper.getSearchDetails(charSequence.toString() , SqliteDbHelper.SearchIn.FARSI , sura_id_search);
                            recyclerView.setAdapter(adapter);
                            adapter.updateList(searchResult);
                        break;
                        case 2:
                            recyclerView.setAdapter(simpleAdapter);
                            simpleAdapter.updateAdapter(Sura.searchInSura(charSequence.toString()));
                            break;
                    }
                    ignore = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(c, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(multiSwitch.getCurrentSwitch() == 2)
                    return;
                Intent suraText = new Intent(c, SuraTextActivity.class);
                String versePosition = String.valueOf(searchResult.get(position).getVerseId() - 1);
                String suraPosition = String.valueOf(searchResult.get(position).getSuraId());
                Toast.makeText(c, String.valueOf(searchResult.get(position).getVerseId()), Toast.LENGTH_SHORT).show();
                suraText.putExtra("suraPosition", suraPosition);
                suraText.putExtra("versePosition", versePosition);
                startActivity(suraText);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if(multiSwitch.getCurrentSwitch() == 2)
                    return;
            }
        }));

        multiSwitch.setCurrentSwitch(2);
        return view;
    }

    private View.OnClickListener suraSelectedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String suraname = ((TextView) view).getText().toString();
            multiSwitch.updateButtonText(2 , "جستجو در سوره : "+suraname);
            sura_id_search = Sura.search(suraname);
            multiSwitch.setCurrentSwitch(0);
            searchEditTxt.setText("");
            searchResult.clear();
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    };
}
