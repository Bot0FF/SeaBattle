package org.bot0ff.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class UserFiled {
    int[][] userFiled;

    int countOneDeckShip;
    int countTwoDeckShip;
    int countThreeDeckShip;
    int countFourDeckShip;
}
