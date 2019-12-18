package ir.farzadshami.quran.models;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

import ir.farzadshami.quran.helpers.SqliteDbHelper;

public class Favorite {
    private static String[] suraNamesArray = {"الفاتحة", "البقرة", "آل عمران", "النساء", "المائدة", "الأنعام", "الأعراف", "الأنفال", "التوبة", "يونس", "هود", "يوسف", "الرعد", "إبراهيم", "الحجر", "النحل", "الإسراء", "الكهف", "مريم", "طه", "الأنبياء", "الحج", "المؤمنون", "النور", "الفرقان", "الشعراء", "النمل", "القصص", "العنكبوت", "الروم", "لقمان", "السجدة", "الأحزاب", "سبأ", "فاطر", "يس", "الصافّات", "ص", "الزمر", "غافر", "فصلت", "الشورى‎", "الزخرف", "الدخان", "الجاثية", "الأحقاف", "محمد", "الفتح", "الحجرات", "ق", "الذاريات", "الطور", "النجم", "القمر", "الرحمن", "الواقعة", "الحديد", "المجادلة", "الحشر", "الممتحنة", "الصف", "الجمعة", "المنافقون", "التغابن", "الطلاق", "التحريم", "الملك", "القلم", "الحاقة", "المعارج", "نوح", "الجن", "المزمل", "المدثر", "القيامة", "الإنسان", "المرسلات", "النبأ", "النازعات", "عبس", "التكوير", "الإنفطار", "المطففين", "الإنشقاق", "البروج", "الطارق", "الأعلى", "الغاشية", "الفجر", "البلد", "الشمس", "الليل", "الضحى", "الشرح‎", "التين", "العلق", "القدر", "البينة", "الزلزلة", "العاديات", "القارعة", "التكاثر", "العصر", "الهمزة", "الفيل", "قريش", "الماعون", "الكوثر", "الكافرون", "النصر", "المسد", "الاخلاص", "الفلق", "الناس"};
    private String ArabicText;
    private String FarsiText;
    private String SuraName;
    private int VerseId;


    public Favorite(String arabicText, String farsiText, String suraName, int verseId) {
        ArabicText = arabicText;
        FarsiText = farsiText;
        SuraName = suraName;
        VerseId = verseId;
    }

    public static ArrayList<Favorite> createFavoriteList(Context context) {
        ArrayList<Favorite> favoritesList = new ArrayList<>();
        SqliteDbHelper dbHelper = SqliteDbHelper.getInstance(context);
        dbHelper.openDatabase();
        ArrayList<DbModel> favorites = dbHelper.getDetails("Favorite", "1");
        for (int index = 0; index < favorites.size(); index++) {
            DbModel favorite = favorites.get(index);
            favoritesList.add(new Favorite(favorite.getArabicText(), favorite.getFarsiText(), suraNamesArray[favorite.getSuraId() + 1], favorite.getVerseId()));
        }
        return favoritesList;
    }

    public String getArabicText() {
        return ArabicText;
    }

    public String getFarsiText() {
        return FarsiText;
    }

    public String getSuraName() {
        return SuraName;
    }

    public int getVerseId() {
        return VerseId;
    }
}
