package org.bot0ff.entity;

public enum UserState {
    WAIT_REGISTRATION, //ожидает ввода имени или сохранения текущего имени
    ONLINE, //в статусе онлайн
    WAIT_FOR_GAME, //ожидает поиска противника
    PREPARE_GAME, //подготовка игрового поля
    IN_GAME, //в стадии игры
    OFFLINE; //в статусе офлайн
}
