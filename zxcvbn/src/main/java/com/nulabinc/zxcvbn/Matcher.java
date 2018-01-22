package com.nulabinc.zxcvbn;

import android.content.Context;

import com.nulabinc.zxcvbn.matchers.Match;

import java.util.List;

public interface Matcher {
    List<Match> execute(@ApplicationContext Context context, String password);
}