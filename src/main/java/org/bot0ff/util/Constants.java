package org.bot0ff.util;

public class Constants {
    //обозначения при расстановке кораблей
    //0 - свободная клетка
    //1 - однопалубный
    //2 - двухпалубный
    //3 - трехпалубный
    //4 - четырехпалубный
    //5 - занятая клетка вокруг корабля, с возможностью продолжения корабля
    //6 - занятая клетка вокруг корабля, с невозможностью нажатия на нее

    //Обозначения координат при конвертации в лист или из листа массив
    //- - однопалубный корабль
    //= - двухпалубный корабль
    //> - трехпалубный корабль
    //# - четырехпалубный корабль
    //: - пустая координата

    //Обозначения координат на gameBoard
    //-1 - подбитая часть корабля корабль
    //-2 - промах

    public static int GAME_FILED_LENGTH = 8;
}
