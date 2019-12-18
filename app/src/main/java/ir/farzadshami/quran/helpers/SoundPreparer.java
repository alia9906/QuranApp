package ir.farzadshami.quran.helpers;

import android.content.Context;
import android.os.Environment;

import java.util.ArrayList;

import ir.farzadshami.quran.models.Download;
import ir.farzadshami.quran.models.SoundProperties;
import ir.farzadshami.quran.models.SoundToPlay;
import ir.farzadshami.quran.models.Sura;

public class SoundPreparer {
    private static ADataBase db = new ADataBase("Downloads");

    public static SoundProperties getSoundFileAndIndexes(Context context , int sura_id, int verse_id ,SoundToPlay.SoundType type){
        SoundProperties sp = new SoundProperties();

        sp.filePath = getJozPathFile(sura_id , verse_id , type);

        if(sp.filePath == null)
            return null;
        else
            sp.filePath = Environment.getExternalStorageDirectory().toString() + "/" + sp.filePath;

        long[] indexes = new long[10000];
        indexes[0] = 20000;
        indexes[1] = 30000;

        sp.indexes = indexes;
        return sp;
    }

    private static String getJozPathFile(int suraid , int verseid , SoundToPlay.SoundType type){

        ArrayList<String> downloads = type == SoundToPlay.SoundType.TARTIL ? (ArrayList<String>) db.readArray("soundstartil"):(ArrayList<String>) db.readArray("soundstahdir");

        String jozzs = Sura.getJozInSura(suraid);
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

        for(int i =0 ; i < jozids.size() ; i++)
            if(downloads.get(jozids.get(i) - 1).equals("NA"))
                return null;

        return downloads.get(jozids.get(0) - 1);
    }
}
