package com.nulabinc.zxcvbn.matchers;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Dictionary {

    private static final String RESOURCES_PACKAGE_PATH = "matchers/dictionarys/";

    private static final String EXT = ".txt";

    private static String buildResourcePath(String filename) {
        return RESOURCES_PACKAGE_PATH + filename + EXT;
    }

    private static final String[] DICTIONARY_PARAMS = {
            "us_tv_and_film",
            "english_wikipedia",
            "passwords",
            "surnames",
            "male_names",
            "female_names"
    };

    private static Map<String, String[]> FREQUENCY_LISTS = null;

    public static Map<String, String[]> getFrequencyLists(Context context) {
        if (FREQUENCY_LISTS == null || FREQUENCY_LISTS.isEmpty()) {
            FREQUENCY_LISTS = read(context);
        }
        return FREQUENCY_LISTS;
    }

    private static Map<String, String[]> read(Context context) {
        Map<String, String[]> freqLists = new HashMap<>();
        for (String filename : DICTIONARY_PARAMS) {
            List<String> words = new ArrayList<>();
            try {
                InputStream is = context.getAssets().open(buildResourcePath(filename));
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    words.add(line);
                }
                is.close();
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            freqLists.put(filename, words.toArray(new String[]{}));
        }
        return freqLists;
    }
}