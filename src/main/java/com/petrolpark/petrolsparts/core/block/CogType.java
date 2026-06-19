package com.petrolpark.petrolsparts.core.block;

public enum CogType {
    
    NONE,
    SMALL,
    LARGE;

    public static final CogType small(boolean small) {
        return small ? SMALL : NONE;
    };

    public static final CogType large(boolean large) {
        return large ? LARGE : NONE;
    };

    public boolean isNone(){
        return this == NONE;
    };

    public boolean isSmall() {
        return this == SMALL;
    };

    public boolean isLarge() {
        return this == LARGE;
    };
};
