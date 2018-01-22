package com.nulabinc.zxcvbn.matchers;

import com.nulabinc.zxcvbn.Matcher;

import java.util.*;

public abstract class BaseMatcher implements Matcher {

    protected List<Match> sorted(List<Match> matches) {
        Collections.sort(matches, new Comparator<Match>() {
            @Override public int compare(Match o1, Match o2) {
                int c = o1.i - o2.i;
                if (c != 0) {
                    return c;
                } else {
                    return (o1.j - o2.j);
                }
            }
        });
        return matches;
    }

    String translate(String string, Map<Character, Character> chrMap) {
        List<Character> characters = new ArrayList<>();
        for (Character chr : string.toCharArray()) {
            characters.add(chrMap.containsKey(chr) ? chrMap.get(chr) : chr);
        }
        StringBuilder result = new StringBuilder();
        for (char c : characters) {
            result.append(String.valueOf(c));
        }
        return String.valueOf(result.toString());
    }

    void extend(List<Match> lst, List<Match> lst2) {
        lst.addAll(lst2);
    }
}