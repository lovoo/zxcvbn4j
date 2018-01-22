package com.nulabinc.zxcvbn;

import android.content.Context;

import com.nulabinc.zxcvbn.matchers.Keyboard;
import com.nulabinc.zxcvbn.matchers.Match;

import java.util.ArrayList;
import java.util.List;

public class Zxcvbn {

    private Zxcvbn() {}

    @BlockingCall public Zxcvbn(@ApplicationContext Context context) {
        Keyboard.initKeyboard(context.getApplicationContext());
    }

    @BlockingCall public Strength measure(@ApplicationContext Context context, String password) {
        return measure(context, password, null);
    }

    @BlockingCall public Strength measure(@ApplicationContext Context context, String password, List<String> sanitizedInputs) {
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
