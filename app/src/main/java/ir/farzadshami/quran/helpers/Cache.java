package ir.farzadshami.quran.helpers;

import android.content.Context;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.Hashtable;

import ir.farzadshami.quran.models.Sura;

public class Cache {

    private static Hashtable<String, Typeface> fontCache = new Hashtable<>();
    private static Hashtable<String, ArrayList<Sura>> suraListCache = new Hashtable<>();

    public static Typeface getFont(String name, Context context) {
        Typeface tf = fontCache.get(name);
        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + name);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }

    public static ArrayList<Sura> getSuraList(String name) {
        ArrayList<Sura> suraList = suraListCache.get(name);
        if (suraList == null) {
            try {
                suraList = Sura.createSurasList();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            suraListCache.put(name, suraList);
        }
        return suraList;
    }
}
//android:configChanges="keyboardHidden|orientation|screenSize"