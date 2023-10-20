package org.bot0ff.entity;

public enum UserState {
    WAIT_REGISTRATION, //ожидает ввода имени или сохранения текущего имени
    ONLINE, //в статусе онлайн
    SEARCH_GAME, //ожидает поиска противника
    PREPARE_GAME,//подготовка игры
    PREPARE_MANUALLY,//подготовка игрового поля вручную
    PREPARE_AUTOMATIC, //подготовка игрового поля автоматически
    READY_FOR_GAME,
    IN_GAME, //в стадии игры
    OFFLINE; //в статусе офлайн
}
