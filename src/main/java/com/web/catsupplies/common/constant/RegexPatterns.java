package com.web.catsupplies.common.constant;

public class RegexPatterns {

    public static final String PASSWORD_PATTERN =
            "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}";

    private RegexPatterns() {}
}
