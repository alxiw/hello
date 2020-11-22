package io.github.alxiw.icq.geekbot.disp;

public enum Button {
    HELLO_START("\uD83D\uDE80 Start", "callback_hello_start"),
    ERROR_RETRY("\uD83C\uDFB2 Retry", "callback_error_retry"),
    ERROR_OK("✋ OK", "callback_error_ok"),
    SUCCESS_REFRESH("\uD83D\uDCA9 Bad", "callback_success_refresh"),
    SUCCESS_NEXT("\uD83D\uDE02 Good", "callback_success_next");

    private final String description;
    private final String code;

    Button(String description, String code) {
        this.description = description;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Button fromCode(String code) {
        for (Button button : Button.values()) {
            if (button.getCode().equals(code)) {
                return button;
            }
        }
        return null;
    }
}
