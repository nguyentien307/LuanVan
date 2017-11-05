package com.example.tiennguyen.thesis.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by Quyen Hua on 10/26/2017.
 */

public class StringUtils {

    public static String convertedToUnsigned(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d").replaceAll(" ", "+");
    }
}
