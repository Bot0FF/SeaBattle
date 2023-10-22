package org.bot0ff.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameFiled {
    private String Ship_First_4_1;
    private String Ship_First_4_2;
    private String Ship_First_4_3;
    private String Ship_First_4_4;

    private String Ship_First_3_1;
    private String Ship_First_3_2;
    private String Ship_First_3_3;
    private String Ship_Second_3_1;
    private String Ship_Second_3_2;
    private String Ship_Second_3_3;

    private String Ship_First_2_1;
    private String Ship_First_2_2;
    private String Ship_Second_2_1;
    private String Ship_Second_2_2;
    private String Ship_Third_2_1;
    private String Ship_Third_2_2;

    private String Ship_First_1_1;
    private String Ship_Second_1_1;
    private String Ship_Third_1_1;
    private String Ship_Fourth_1_1;

    private boolean contains(String cell) {
        return this.Ship_First_4_1.equals(cell)
                | this.Ship_First_4_2.equals(cell)
                | this.Ship_First_4_3.equals(cell)
                | this.Ship_First_4_4.equals(cell)

                | this.Ship_First_3_1.equals(cell)
                | this.Ship_First_3_2.equals(cell)
                | this.Ship_First_3_3.equals(cell)
                | this.Ship_Second_3_1.equals(cell)
                | this.Ship_Second_3_2.equals(cell)
                | this.Ship_Second_3_3.equals(cell)

                | this.Ship_First_2_1.equals(cell)
                | this.Ship_First_2_2.equals(cell)
                | this.Ship_Second_2_1.equals(cell)
                | this.Ship_Second_2_2.equals(cell)
                | this.Ship_Third_2_1.equals(cell)
                | this.Ship_Third_2_2.equals(cell)

                | this.Ship_First_1_1.equals(cell)
                | this.Ship_Second_1_1.equals(cell)
                | this.Ship_Third_1_1.equals(cell)
                | this.Ship_Fourth_1_1.equals(cell);

    }
}
