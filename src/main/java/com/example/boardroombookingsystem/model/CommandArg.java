package com.example.boardroombookingsystem.model;

public enum CommandArg {
    OPT_F(CommandArg.OPT_F_VALUE);

    private String arg;
    private static final String OPT_F_VALUE = "f";

    CommandArg(String arg) {
        this.arg = arg;
    }

    public String getValue() {
        return arg;
    }
}