package com.teamsantos.easybarber.security.utils;

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

    public static void clear() {
        CONTEXT.remove();
    }
}
