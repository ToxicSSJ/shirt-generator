package sh.toxic.shirt.data;

import javafx.scene.paint.Color;
import lombok.Getter;

public enum PointType {

    BACK_A(0, true, Color.BLUE),
    BACK_B(0, false, Color.BLUE),

    WIDTH_A(1, true, Color.RED),
    WIDTH_B(1, false, Color.RED),

    LENGTH_A(2, true, Color.LIMEGREEN),
    LENGTH_B(2, false, Color.LIMEGREEN),

    HIP_A(3, true, Color.YELLOW),
    HIP_B(3, false, Color.YELLOW)

    ;

    @Getter private int parent;
    @Getter private boolean start;
    @Getter private Color color;

    PointType(int parent, boolean start, Color color) {
        this.parent = parent;
        this.start = start;
        this.color = color;
    }

}
