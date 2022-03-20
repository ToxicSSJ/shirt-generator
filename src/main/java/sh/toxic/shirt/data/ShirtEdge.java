package sh.toxic.shirt.data;

import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShirtEdge {

    private int parent;
    private Color color;

    private PointType pointA;
    private PointType pointB;

    private int x1, x2;
    private int y1, y2;

}
