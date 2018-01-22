package com.nulabinc.zxcvbn;

import android.content.Context;

import com.nulabinc.zxcvbn.matchers.Dictionary;
import com.nulabinc.zxcvbn.matchers.Match;
import com.nulabinc.zxcvbn.matchers.OmnibusMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Matching {

    private Map<String, Map<String, Integer>> BASE_RANKED_DICTIONARIES;

    private final Map<String, Map<String, Integer>> rankedDictionaries;

    public Matching(Context context) {
        this(context, new ArrayList<String>());
    }

    public Matching(@ApplicationContext Context context, List<String> orderedList) {
        if (BASE_RANKED_DICTIONARIES == null) {
            BASE_RANKED_DICTIONARIES = new HashMap<>();
            for (Map.Entry<String, String[]> frequencyListRef : Dictionary.getFrequencyLists(context).entrySet()) {
                String name = frequencyListRef.getKey();
                String[] ls = frequencyListRef.getValue();
                BASE_RANKED_DICTIONARIES.put(name, buildRankedDict(ls));
            }
        }
        if (orderedList == null) orderedList = new ArrayList<>();
        this.rankedDictionaries = new HashMap<>();
        if (BASE_RANKED_DICTIONARIES == null) return;
        for (Map.Entry<String, Map<String, Integer>> baseRankedDictionary : BASE_RANKED_DICTIONARIES.entrySet()) {
            this.rankedDictionaries.put(
                    baseRankedDictionary.getKey(),
                    baseRankedDictionary.getValue());
        }
        this.rankedDictionaries.put("user_inputs", buildRankedDict(orderedList.toArray(new String[]{})));
    }

    public List<Match> omnimatch(@ApplicationContext Context context, String password) {
        return new OmnibusMatcher(rankedDictionaries).execute(context, password);
    }

    private static Map<String, Integer> buildRankedDict(String[] orderedList) {
        HashMap<String, Integer> result = new HashMap<>();
        int i = 1; // rank starts at 1, not 0
        for (String word : orderedList) {
            result.put(word, i);
            i++;
        }
        return result;
    }
}