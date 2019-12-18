package ir.farzadshami.quran.models;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import ir.farzadshami.quran.helpers.ADataBase;

import static java.lang.String.format;

public class Download {

    private String text;
    public enum Type{
        TARTL , TAHDIR
    }
    private Type type;
    private boolean isDownloaded;
    private boolean isChecked = false;
    private int joz;
    public final static String FILEPATH = "/Android/data/ir.farzadshami.quran/QuranSounds";

    public Download(String text , boolean isDownloaded , int joz , Type type) {
        this.text = text;
        this.isDownloaded = isDownloaded;
        this.joz = joz;
        this.type = type;
    }

    public static ArrayList<Download> createDownloadList(Type type) {
        ADataBase db = new ADataBase("Downloads");
        ArrayList<String> filepaths = type == Type.TARTL ? (ArrayList<String>) db.readArray("soundstartil"):(ArrayList<String>) db.readArray("soundstahdir");
        ArrayList<Download> downloadList = new ArrayList<>();
        for (int index = 0; index < 30; index++) {

            boolean exists = new File(new File(Environment.getExternalStorageDirectory().toString() + FILEPATH) , filepaths.get(index).substring(filepaths.get(index).lastIndexOf('/') + 1)).exists();
            if(!exists){
                filepaths.set(index , "NA");
            }
            downloadList.add(new Download("جز " + persianNumbers(index + 1) + (exists ? " (دانلود شده)":"") , exists , index ,type));
        }
        db.writeArray(type == Type.TARTL ?"soundstartil":"soundstahdir" , filepaths , 1);
        return downloadList;
    }

    private static String persianNumbers(int input) {
        return format(new Locale("ar"), "%d", input);
    }

    public String getText() {
        return text;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public boolean getIsDownloaded(){
        return isDownloaded;
    }

    public void notifyDownloadComplete(String filename){
        isDownloaded = true;
        ADataBase db = new ADataBase("Downloads");
        ArrayList<String> downloads = type == Type.TARTL ? (ArrayList<String>) db.readArray("soundstartil"):(ArrayList<String>) db.readArray("soundstahdir");
        downloads.set(joz , FILEPATH + "/" + filename);
        db.writeArray(type == Type.TARTL ?"soundstartil":"soundstahdir" ,downloads ,1 );
    }

    public String getDefaultName(){
        return "sound_"+type.toString()+"_"+ "jozid_" + joz + ".mp3";
    }

    public int getJoz(){
        return joz;
    }

    public Type getType(){
        return type;
    }
    public void setChecked(boolean checked){
        this.isChecked = checked;
    }
}
