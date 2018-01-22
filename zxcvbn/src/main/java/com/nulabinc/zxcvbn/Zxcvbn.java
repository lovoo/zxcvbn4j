package com.nulabinc.zxcvbn;

import android.content.Context;

import com.nulabinc.zxcvbn.matchers.Keyboard;
import com.nulabinc.zxcvbn.matchers.Match;

import java.util.ArrayList;
import java.util.List;

public class Zxcvbn {

    public Zxcvbn() {}

    @BlockingCall public Strength measure(@ApplicationContext Context context, String password) {
        Keyboard.initKeyboard(context);
        return measure(context, password, null);
    }

    @BlockingCall public Strength measure(@ApplicationContext Context context, String password, List<String> sanitizedInputs) {
        Keyboard.initKeyboard(context);
        context = context.getApplicationContext();
        if (password == null) {
            throw new IllegalArgumentException("Password is null.");
        }
        List<String> lowerSanitizedInputs = new ArrayList<>();
        if (sanitizedInputs != null) {
            for (String sanitizedInput : sanitizedInputs) {
                lowerSanitizedInputs.add(sanitizedInput.toLowerCase());
            }
        }
        long start = time();
        Matching matching = new Matching(context, lowerSanitizedInputs);
        List<Match> matches = matching.omnimatch(context, password);
        Strength strength = Scoring.mostGuessableMatchSequence(password, matches);
        strength.setCalcTime(time() - start);
        AttackTimes attackTimes = TimeEstimates.estimateAttackTimes(strength.getGuesses());
        strength.setCrackTimeSeconds(attackTimes.getCrackTimeSeconds());
        strength.setCrackTimesDisplay(attackTimes.getCrackTimesDisplay());
        strength.setScore(attackTimes.getScore());
        strength.setFeedback(Feedback.getFeedback(strength.getScore(), strength.getSequence()));
        return strength;
    }

    private long time() {
        return System.nanoTime();
    }

}
