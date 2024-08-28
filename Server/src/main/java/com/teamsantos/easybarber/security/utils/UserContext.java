package com.teamsantos.easybarber.security.utils;

import com.teamsantos.easybarber.exceptions.UserNotFoundException;

public class UserContext {
    private static final ThreadLocal<UserPrincipal> CONTEXT = new ThreadLocal<>();

    public static void setCurrentUser(UserPrincipal user) {
        if (CONTEXT.get() != null) {
            throw new IllegalStateException("User is already set");
        }
        CONTEXT.set(user);
    }

    public static UserPrincipal getCurrentUser() {
        return CONTEXT.get();
    }

    public static long getEmployeeId() throws UserNotFoundException {
        UserPrincipal user = CONTEXT.get();
        if (user == null || user.getEmployeeId() == null) {
            throw new UserNotFoundException();
        }
        return user.getEmployeeId();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
