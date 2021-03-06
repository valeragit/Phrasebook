package ru.vandrikeev.android.phrasebook.model.network;

/**
 * Yandex.Translate API error.
 *
 * @see ru.vandrikeev.android.phrasebook.model.responses.BaseApiResponse#code
 */
public class YandexTranslateException extends Exception {

    /**
     * Code of error.
     *
     * @see ru.vandrikeev.android.phrasebook.model.responses.BaseApiResponse#code
     */
    private int code;

    public YandexTranslateException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
