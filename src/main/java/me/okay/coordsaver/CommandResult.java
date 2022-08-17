package me.okay.coordsaver;

public enum CommandResult {
    SUCCESS,
    USAGE_FAILURE,
    PERMISSION_FAILURE;

    public boolean isSuccess() {
        return this == SUCCESS;
    }
}
