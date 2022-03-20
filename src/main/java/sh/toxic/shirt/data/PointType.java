package sh.toxic.shirt.data;

import javafx.scene.paint.Color;
import lombok.Getter;

public enum PointType {

    BACK_A(0, true, 0, 0, Color.BLUE),
    BACK_B(0, false, 0, 0, Color.BLUE),

    WIDTH_A(1, true, 0, 0, Color.RED),
    WIDTH_B(1, false, 0, 0, Color.RED),

    LENGTH_A(2, true, 0, 0, Color.LIMEGREEN),
    LENGTH_B(2, false, 0, 0, Color.LIMEGREEN),

    HIP_A(3, true, 0, 0, Color.YELLOW),
    HIP_B(3, false, 0, 0, Color.YELLOW),

    PRE_SLEEVE_RIGHT_A(4, true, 0, 0, Color.BLACK),
    PRE_SLEEVE_RIGHT_B(4, false, 0, 0, Color.BLACK),

    PRE_SLEEVE_RIGHT_C(5, true, 0, 0, Color.BLACK),
    PRE_SLEEVE_RIGHT_D(5, false, 0, 0, Color.BLACK),

    PRE_SLEEVE_RIGHT_E(6, true, 0, 0, Color.BLACK),
    PRE_SLEEVE_RIGHT_F(6, false, 0, 0, Color.BLACK),

    PRE_SLEEVE_LEFT_A(7, true, 0, 0, Color.BLACK),
    PRE_SLEEVE_LEFT_B(7, false, 0, 0, Color.BLACK),

    PRE_SLEEVE_LEFT_C(8, true, 0, 0, Color.BLACK),
    PRE_SLEEVE_LEFT_D(8, false, 0, 0, Color.BLACK),

    PRE_SLEEVE_LEFT_E(9, true, 0, 0, Color.BLACK),
    PRE_SLEEVE_LEFT_F(9, false, 0, 0, Color.BLACK),

    PRE_NECK_RIGHT_A(10, true, 0, 0, Color.BLACK),
    PRE_NECK_RIGHT_B(10, false, 0, 0, Color.BLACK),

    PRE_NECK_RIGHT_C(11, true, 0, 0, Color.BLACK),
    PRE_NECK_RIGHT_D(11, false, 15, 15, Color.BLACK),

    PRE_NECK_LEFT_A(12, true, 0, 0, Color.BLACK),
    PRE_NECK_LEFT_B(12, false, 0, 0, Color.BLACK),

    PRE_NECK_LEFT_C(13, true, 0, 0, Color.BLACK),
    PRE_NECK_LEFT_D(13, false, 15, 15, Color.BLACK),

    PRE_NECK_BACK_A(14, true, 0, 0, Color.BLACK),
    PRE_NECK_BACK_B(14, false, 0, 0, Color.BLACK),

    ;

    @Getter private int parent;
    @Getter private boolean start;
    @Getter private Color color;

    @Getter private double bezierOffX;
    @Getter private double bezierOffY;

    PointType(int parent, boolean start, double bezierOffX, double bezierOffY, Color color) {

        this.parent = parent;
        this.start = start;
        this.color = color;

        this.bezierOffX = bezierOffX;
        this.bezierOffY = bezierOffY;

    }

}
