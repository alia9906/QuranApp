package ir.farzadshami.quran.models;

import android.util.Log;

import java.util.ArrayList;

import ir.farzadshami.quran.helpers.ADataBase;

public class Sura {

    public static String[] suraNamesArray = {"الفاتحة", "البقرة", "آل عمران", "النساء", "المائدة", "الأنعام", "الأعراف", "الأنفال", "التوبة", "يونس", "هود", "يوسف", "الرعد", "إبراهيم", "الحجر", "النحل", "الإسراء", "الكهف", "مريم", "طه", "الأنبياء", "الحج", "المؤمنون", "النور", "الفرقان", "الشعراء", "النمل", "القصص", "العنكبوت", "الروم", "لقمان", "السجدة", "الأحزاب", "سبأ", "فاطر", "يس", "الصافّات", "ص", "الزمر", "غافر", "فصلت", "الشورى‎", "الزخرف", "الدخان", "الجاثية", "الأحقاف", "محمد", "الفتح", "الحجرات", "ق", "الذاريات", "الطور", "النجم", "القمر", "الرحمن", "الواقعة", "الحديد", "المجادلة", "الحشر", "الممتحنة", "الصف", "الجمعة", "المنافقون", "التغابن", "الطلاق", "التحريم", "الملك", "القلم", "الحاقة", "المعارج", "نوح", "الجن", "المزمل", "المدثر", "القيامة", "الإنسان", "المرسلات", "النبأ", "النازعات", "عبس", "التكوير", "الإنفطار", "المطففين", "الإنشقاق", "البروج", "الطارق", "الأعلى", "الغاشية", "الفجر", "البلد", "الشمس", "الليل", "الضحى", "الشرح‎", "التين", "العلق", "القدر", "البينة", "الزلزلة", "العاديات", "القارعة", "التكاثر", "العصر", "الهمزة", "الفيل", "قريش", "الماعون", "الكوثر", "الكافرون", "النصر", "المسد", "الاخلاص", "الفلق", "الناس"};
    private static String[] jozDownloadArray = {"1", "1|2|3", "3|4", "4|5|6", "6|7", "7|8", "8|9", "9|10", "10|11", "11", "11|12",
            "12|13", "13", "13", "14", "14", "15", "15|16", "16", "16", "17", "17",
            "18", "18", "18|19", "19", "19|20", "20", "20|21", "21", "21", "21", "21|22",
            "22", "22", "22|23", "23", "23", "23|24", "24", "24|25", "25", "25", "25", "25",
            "26", "26", "26", "26", "26", "26|27", "27", "27", "27", "27", "27", "27",
            "28", "28", "28", "28", "28", "28", "28", "28", "28",
            "29", "29", "29", "29", "29", "29", "29", "29", "29", "29", "29",
            "30", "30", "30", "30", "30", "30", "30", "30", "30", "30",
            "30", "30", "30", "30", "30", "30", "30", "30", "30", "30",
            "30", "30", "30", "30", "30", "30", "30", "30", "30", "30",
            "30", "30", "30", "30", "30", "30", "30"};
    private static int[] jozsIdArray = {1, 1, 3, 4, 6, 7, 8, 9, 10, 11, 11, 12, 13, 13, 14, 14, 15, 15, 16, 16, 17, 17, 18, 18, 18, 19, 19, 20, 20, 21, 21, 21, 21, 22, 22, 22, 23, 23, 23, 24, 24, 25, 25, 25, 25, 26, 26, 26, 26, 26, 26, 27, 27, 27, 27, 27, 27, 28, 28, 28, 28, 28, 28, 28, 28, 28, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 29, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30};
    private static int[] hezbsIdArray = {1, 1, 10, 16, 22, 26, 31, 36, 38, 42, 44, 47, 50, 51, 53, 54, 57, 59, 61, 63, 65, 67, 69, 70, 72, 74, 76, 77, 80, 81, 82, 83, 84, 86, 87, 88, 89, 91, 92, 94, 95, 97, 98, 99, 100, 101, 101, 102, 103, 104, 104, 105, 105, 106, 107, 107, 108, 109, 109, 110, 110, 111, 111, 111, 112, 112, 113, 113, 114, 114, 114, 115, 115, 115, 116, 116, 116, 117, 117, 117, 117, 118, 118, 118, 118, 118, 119, 119, 119, 119, 119, 119, 119, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120};
    private static int[] nozolsIdArray = {5, 87, 89, 92, 113, 55, 39, 88, 114, 51, 52, 53, 96, 72, 54, 70, 50, 69, 44, 45, 73, 104, 74, 103, 42, 47, 48, 49, 85, 84, 57, 75, 90, 58, 43, 41, 56, 38, 59, 60, 61, 62, 63, 64, 65, 66, 95, 112, 107, 34, 67, 76, 23, 37, 97, 46, 94, 106, 101, 91, 111, 109, 105, 110, 99, 108, 77, 2, 78, 79, 71, 40, 3, 4, 31, 98, 33, 80, 81, 24, 7, 82, 86, 83, 27, 36, 8, 68, 10, 35, 26, 9, 11, 12, 28, 1, 25, 100, 93, 14, 30, 16, 13, 32, 19, 29, 17, 15, 18, 102, 6, 22, 20, 21};
    private static int[] suraCountArray = {7, 286, 200, 176, 120, 165, 206, 75, 129, 109, 123, 111, 43, 52, 99, 128, 111, 110, 98, 135, 112, 78, 118, 64, 77, 227, 93, 88, 69, 60, 34, 30, 73, 54, 45, 83, 182, 88, 75, 85, 54, 53, 89, 59, 37, 35, 38, 29, 18, 45, 60, 49, 62, 55, 78, 96, 29, 22, 24, 13, 14, 11, 11, 18, 12, 12, 30, 52, 52, 44, 28, 28, 20, 56, 40, 31, 50, 40, 46, 42, 29, 19, 36, 25, 22, 17, 19, 26, 30, 20, 15, 21, 11, 8, 8, 19, 5, 8, 8, 11, 11, 8, 3, 9, 5, 4, 7, 3, 6, 3, 5, 4, 5, 6};
    private static int[] placeIdArray = {1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1};
    private int id;
    private int nozolId;
    private int jozId;
    private int hezbId;

    private int suraCount;
    private String place;
    private String suraName;
    private boolean isFavorite;
    private boolean isDownloadedTartil = true;
    private boolean isDownloadeTahdir = true;
    private ADataBase db;

    public Sura(int id, int nozolId, int jozId, int hezbId, int suraCount, String suraName, String place) {
        this.id = id;
        this.nozolId = nozolId;
        this.jozId = jozId;
        this.hezbId = hezbId;
        this.suraCount = suraCount;
        this.suraName = suraName;
        this.place = place;
        if(db == null)
            db = new ADataBase("Favorite");
        ArrayList<String> isfavoritelist = (ArrayList<String>) db.readArray("favoritesuras");
        this.isFavorite = isfavoritelist.get(id).equals("1");

        db= new ADataBase("Downloads");
        ArrayList<String> tartil = (ArrayList<String>) db.readArray("soundstartil");
        ArrayList<String> tahdir = (ArrayList<String>) db.readArray("soundstahdir");
        String jozzs = jozDownloadArray[id];
        ArrayList<Integer> jozids = new ArrayList<Integer>();

        int start = 0;
        while (start < jozzs.length()){
            int index = jozzs.indexOf('|' , start);
            if(index == -1)
                index = jozzs.length();
            String val = jozzs.substring(start , index);
            if("".equals(val))
                break;
            jozids.add(Integer.valueOf(val));
            start = index + 1;
        }

        for(int i =0 ; i < jozids.size() ; i++){
            isDownloadedTartil = isDownloadedTartil && (!"NA".equals(tartil.get(jozids.get(i) - 1)));
            isDownloadeTahdir = isDownloadeTahdir && (!"NA".equals(tahdir.get(jozids.get(i) - 1)));
        }
    }

    public static ArrayList<Sura> createSurasList() {
        ArrayList<Sura> suras = new ArrayList<>();
        for (int index = 0; index < suraNamesArray.length; index++) {
            suras.add(new Sura(index, nozolsIdArray[index], jozsIdArray[index], hezbsIdArray[index], suraCountArray[index], suraNamesArray[index], placeIdArray[index] == 1 ? "مکی" : "مدنی"));
        }
        return suras;
    }

    public int getId() {
        return id;
    }

    public int getNozolId() {
        return nozolId;
    }

    public int getJozId() {
        return jozId;
    }

    public int getHezbId() {
        return hezbId;
    }

    public String getSuraName() {
        return suraName;
    }

    public int getSuraCount() {
        return suraCount;
    }

    public String getPlace() {
        return place;
    }

    public boolean getIsFavorite(){
        return isFavorite;
    }
    public void setIsFavorite(boolean isFavorite){
        this.isFavorite = isFavorite;
        db = new ADataBase("Favorite");
        ArrayList<String> favs = (ArrayList<String>) db.readArray("favoritesuras");
        favs.set(id , isFavorite ? "1" : "0");
        db.writeArray("favoritesuras" , favs , 1);
    }

    public boolean getIsDownloaded(String tartilOrTahdir){
        if("ترتیل".equals(tartilOrTahdir))
            return isDownloadedTartil;
        else
            return isDownloadeTahdir;
    }
    public void setIsDownloaded(boolean downloaded , String tartilOrTahdir){
        if("ترتیل".equals(tartilOrTahdir))
            this.isDownloadedTartil = downloaded;
        else
            this.isDownloadeTahdir = downloaded;
    }
    public static ArrayList<String> searchInSura(String s){
        Log.d("s" , s);
        ArrayList<String> retval = new ArrayList<>();
        retval.add("همه سوره ها");
        for(int i =0 ; i < suraNamesArray.length ; i++){
            if(suraNamesArray[i].contains(s))
                retval.add(suraNamesArray[i]);
        }
        return retval;
    }
    public static int search(String suraName){
        for(int i =0 ; i < suraNamesArray.length ; i++){
            if(suraNamesArray[i].equals(suraName))
                return i;
        }
        return -1;
    }

    public static String getJozInSura(int id){
        return jozDownloadArray[id];
    }
}
