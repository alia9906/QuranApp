package ir.farzadshami.quran.widget;

import ir.farzadshami.quran.models.Favorite;

public interface OnBookMarkSelectedListener {
    void onBookMarkSelected(Favorite favorite , String groupname);
    boolean onGroupeCreated(String groupename);
}
