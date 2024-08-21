package com.teamsantos.easybarber.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TestsState {
    private static Set<String> completedSetups = Collections.synchronizedSet(new HashSet<>());

    public static synchronized boolean ran(String setupName) {
        return completedSetups.contains(setupName);
    }

    public static synchronized void mark(String setupName) {
        completedSetups.add(setupName);
    }
}
