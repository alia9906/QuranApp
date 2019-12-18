package ir.farzadshami.quran.helpers;

import ir.farzadshami.quran.models.Sura;

public class Sorting {
    public final static String ASCENDING = "Ascending";

    public static class NozolId implements java.util.Comparator<Sura> {
        private String sort;

        public NozolId(String sort) {
            this.sort = sort;
        }

        @Override
        public int compare(Sura sura1, Sura sura2) {
            Integer nozolId1 = sura1.getNozolId();
            Integer nozolId2 = sura2.getNozolId();
            if (sort.equals(ASCENDING))
                return nozolId1.compareTo(nozolId2);
            return nozolId2.compareTo(nozolId1);
        }
    }
    public static class Id implements java.util.Comparator<Sura> {
        private String sort;

        public Id(String sort) {
            this.sort = sort;
        }

        @Override
        public int compare(Sura sura1, Sura sura2) {
            Integer nozolId1 = sura1.getId();
            Integer nozolId2 = sura2.getId();
            if (sort.equals(ASCENDING))
                return nozolId1.compareTo(nozolId2);
            return nozolId2.compareTo(nozolId1);
        }
    }

    public static class Name implements java.util.Comparator<Sura> {
        private String sort;

        public Name(String sort) {
            this.sort = sort;
        }

        @Override
        public int compare(Sura sura1, Sura sura2) {
            String sura1Name = sura1.getSuraName();
            String sura2Name = sura2.getSuraName();
            if (sort.equals(ASCENDING))
                return sura1Name.compareTo(sura2Name);
            return sura2Name.compareTo(sura1Name);
        }
    }
}



