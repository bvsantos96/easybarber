package com.teamsantos.easybarber.utils;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.http.HttpStatus;

public class AnyOfStatusMatcher extends TypeSafeMatcher<Integer> {

    private final int[] expectedStatuses;

    public AnyOfStatusMatcher(int... expectedStatuses) {
        this.expectedStatuses = expectedStatuses;
    }

    @Override
    protected boolean matchesSafely(Integer actualStatus) {
        for (int expectedStatus : expectedStatuses) {
            if (actualStatus.equals(expectedStatus)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("one of ");
        description.appendValueList("[", ", ", "]", expectedStatuses);
    }

    // Factory method for creating the matcher
    public static Matcher<Integer> anyOfStatus(int... expectedStatuses) {
        return new AnyOfStatusMatcher(expectedStatuses);
    }

    public static Matcher<? super Integer> createdOrFound() {
        return anyOfStatus(HttpStatus.CREATED.value(), HttpStatus.FOUND.value());
    }
}
