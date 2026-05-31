package com.dormitory.management.util;

public class OperationResult {
    private final boolean success;
    private final String messageKey;
    private final Object[] messageArgs;

    private OperationResult(boolean success, String messageKey, Object... messageArgs) {
        this.success = success;
        this.messageKey = messageKey;
        this.messageArgs = messageArgs == null ? new Object[0] : messageArgs.clone();
    }

    public static OperationResult success(String messageKey, Object... messageArgs) {
        return new OperationResult(true, messageKey, messageArgs);
    }

    public static OperationResult failure(String messageKey, Object... messageArgs) {
        return new OperationResult(false, messageKey, messageArgs);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getMessageArgs() {
        return messageArgs.clone();
    }
}
