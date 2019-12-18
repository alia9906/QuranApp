package ir.farzadshami.quran.models;

public class DbModel {
    int id;
    int verseId;
    int suraId;
    String arabicText;
    String farsiText;
    Boolean isFavorite;

    public DbModel(int id, int suraId, int verseId, String farsiText, String arabicText, Boolean isFavorite) {
        this.id = id;
        this.verseId = verseId;
        this.suraId = suraId;
        this.arabicText = arabicText;
        this.farsiText = farsiText;
        this.isFavorite = isFavorite;
    }

    public int getId() {
        return id;
    }

    public int getVerseId() {
        return verseId;
    }

    public int getSuraId() {
        return suraId;
    }

    public String getArabicText() {
        return arabicText;
    }

    public String getFarsiText() {
        return farsiText;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }
}
