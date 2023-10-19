package org.bot0ff.service;

public enum ServiceCommands {
    START("/start"),
    CANCEL("/cancel"),
    HELP("/help");

    private final String cmd;

    ServiceCommands(String cmd) {
        this.cmd = cmd;
    }


    @Override
    public String toString() {
        return cmd;
    }

    public boolean equals(String cmd) {
        return this.toString().equals(cmd);
    }
}
