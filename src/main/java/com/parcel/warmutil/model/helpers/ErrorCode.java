/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parcel.warmutil.model.helpers;

/**
 *  0 – Нет ошибок
    1 – Ошибка инициализации порта
    2 - Ошибка проверки порта, ответ порта не соответствует протоколу
    3 – Ошибка передачи сообщения
    4 – Ошибка потока
    5 – Ошибка приема ответа
    6 – Не удалось дождаться ответа
 */
public enum ErrorCode {

    NO_ERROR(0, ""),
    PORT_INIT_ERROR(1, "Ошибка проверки порта, ответ порта не соответствует протоколу"),
    ANSWER_FORMAT_ERROR(2, "Ошибка приема ответа"),
    MESSAGE_TRANSFER_ERROR(3, "Ошибка передачи сообщения"),
    THREAD_ERROR(4, "Ошибка инициализации порта"),
    ANSWER_RECEIVE_ERROR(5, "Ошибка потока"),
    TIMEOUT_ERROR(6, "Не удалось дождаться ответа");


    private final String message;
    private final int code;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ErrorCode byCode(int code) {
        for(ErrorCode errorCode : values()) {
            if(errorCode.getCode() == code) {
                return errorCode;
            }
        }
        throw new RuntimeException("ErrorCode "+code+" not supported");
    }
}
