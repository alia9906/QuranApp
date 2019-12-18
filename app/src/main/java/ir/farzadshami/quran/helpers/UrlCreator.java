package ir.farzadshami.quran.helpers;

import ir.farzadshami.quran.models.Download;

public class UrlCreator {

    public String createUrlFromJoz(Download model){
        switch (model.getType()){
            case TAHDIR:
                return "https://hoststorage.ir/Tartil/Tahdir_64k/joze_" + (model.getJoz() + 1) + ".mp3";
            case TARTL:
                return "https://hoststorage.ir/Tartil/Tartil_64kb/" + ((model.getJoz() + 1) / 10 == 0 ? "0" + (model.getJoz() + 1) :"" + (model.getJoz() + 1)) + ".mp3";
        }
        return null;
    }
}
