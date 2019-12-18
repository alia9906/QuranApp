package ir.farzadshami.quran;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ir.farzadshami.quran.adapters.FavoriteAdapter;

public class FavoriteActivity extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_favorite, container, false);

        RecyclerView rv = view.findViewById(R.id.rvFavorite);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new FavoriteAdapter(getActivity()));

        return view;
    }
}
