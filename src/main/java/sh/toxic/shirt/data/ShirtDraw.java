package sh.toxic.shirt.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShirtDraw {

    private DrawType drawType;

    private double[] start;
    private double[] end;

    public double getStartX() {
        return start[0];
    }

    public double getStartY() {
        return start[1];
    }

    public double getEndX() {
        return end[0];
    }

    public double getEndY() {
        return end[1];
    }

}
