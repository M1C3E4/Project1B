package com.example.Project1B.Server;

public enum Operation {

    NULL,

    SEND_MESSAGE,
    SEND_MESSAGE_OK,
    SEND_MESSAGE_FAILED,

    RECEIVE_MESSAGE,

    LOGIN,
    LOGIN_OK,
    LOGIN_FAILED,

    FILE_TRANSFER,
    FILE_TRANSFER_OK,
    FILE_TRANSFER_FAILED,

    GET_HISTORY,
    GET_HISTORY_OK,
    GET_HISTORY_FAILED,

    JOIN_GROUP,
    JOIN_GROUP_OK,
    JOIN_GROUP_FAILED,

    LOGOUT,
    LOGOUT_OK,
    LOGOUT_FAILED,
}