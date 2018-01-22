package com.nulabinc.zxcvbn.matchers;

import android.content.Context;

import com.nulabinc.zxcvbn.ApplicationContext;
import com.nulabinc.zxcvbn.Matching;
import com.nulabinc.zxcvbn.Scoring;
import com.nulabinc.zxcvbn.Strength;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RepeatMatcher extends BaseMatcher {
    @Override
    public List<Match> execute(@ApplicationContext Context context, String password) {
        List<Match> matches = new ArrayList<>();
        Pattern greedy = Pattern.compile("(.+)\\1+");
        Pattern lazy = Pattern.compile("(.+?)\\1+");
        Pattern lazyAnchored = Pattern.compile("^(.+?)\\1+$");
        int passwordLength = password.length();
        int lastIndex = 0;
        while (lastIndex < passwordLength) {
            Matcher greedyMatch = greedy.matcher(password);
            Matcher lazyMatch = lazy.matcher(password);
            greedyMatch.region(lastIndex, passwordLength);
            lazyMatch.region(lastIndex, passwordLength);
            if (!greedyMatch.find()) break;
            Matcher match;
            String baseToken;
            if (greedyMatch.group(0).length() > (lazyMatch.find() ? lazyMatch.group(0).length() : 0)) {
                match = greedyMatch;
                Matcher matcher = lazyAnchored.matcher(match.group(0));
                baseToken = matcher.find() ? matcher.group(1) : match.group(0);
            } else {
                match = lazyMatch;
                baseToken = match.group(1);
            }
            int i = match.start(0);
            int j = match.start(0) + match.group(0).length() - 1;
            Strength baseAnalysis = Scoring.mostGuessableMatchSequence(baseToken,
                    new Matching(context, new ArrayList<String>()).omnimatch(context, baseToken));
            List<Match> baseMatches = baseAnalysis.getSequence();
            double baseGuesses = baseAnalysis.getGuesses();
            matches.add(MatchFactory.createRepeatMatch(i, j, match.group(0), baseToken, baseGuesses, baseMatches,
                    match.group(0).length() / baseToken.length()));
            lastIndex = j + 1;
        }
        return matches;
    }
}
