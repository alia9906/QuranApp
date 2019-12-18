package ir.farzadshami.quran.helpers;

import java.util.Locale;

import static java.lang.String.format;

public class Utilities {
    public static String persianNumbers(int input) {
        return format(new Locale("ar"), "%d", input);
    }
    public static String changeToPersionNumbers(String a){
        String retval = "";
        for(int i =0 ; i < a.length() ; i++){
            if((int) a.charAt(i) >= 48 && (int) a.charAt(i) <= 57)
                retval+=persianNumbers((int) a.charAt(i) - 48);
            else
                retval+=a.charAt(i);
        }
        return retval;
    }

}
