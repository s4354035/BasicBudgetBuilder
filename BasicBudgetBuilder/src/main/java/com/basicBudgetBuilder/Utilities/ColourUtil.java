package com.basicBudgetBuilder.Utilities;

/**
 * Created by Hanzi Jing on 9/04/2017.
 */

import java.awt.*;

public class ColourUtil {
    public static String getHTMLColorString(Color color) {
        if (color == null){
            return null;
        }
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

//        String red = Integer.toHexString(color.getRed());
//        String green = Integer.toHexString(color.getGreen());
//        String blue = Integer.toHexString(color.getBlue());
//
//        return "#" +
//                (red.length() == 1? "0" + red : red) +
//                (green.length() == 1? "0" + green : green) +
//                (blue.length() == 1? "0" + blue : blue);
    }
}
