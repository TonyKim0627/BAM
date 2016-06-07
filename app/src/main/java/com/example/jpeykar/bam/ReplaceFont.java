package com.example.jpeykar.bam;

import android.graphics.Typeface;
import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by ggoma on 4/25/16.
 */
public class ReplaceFont {

    public static void replaceDefaultFont(Context context, String old_name, String new_name) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), new_name);
    }

    private static void replaceFont(String old_name, Typeface new_font){
        try{
            Field myfield = Typeface.class.getDeclaredField(old_name);
            myfield.setAccessible(true);
            myfield.set(null, new_font);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
