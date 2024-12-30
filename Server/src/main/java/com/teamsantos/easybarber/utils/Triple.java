package com.teamsantos.easybarber.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Triple<T, U, V> {
    private T first;
    private U second;
    private V third;

    public Triple(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
