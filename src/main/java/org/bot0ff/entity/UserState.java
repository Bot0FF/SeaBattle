package org.bot0ff.entity;

public enum UserState {
    WAIT_REGISTRATION, //ожидает ввода имени или сохранения текущего имени
    ONLINE, //в статусе онлайн
    PREPARE_GAME, //в статусе подготовки игры
    SEARCH_GAME, //ожидает поиска противника
    IN_GAME, //в стадии игры
}
