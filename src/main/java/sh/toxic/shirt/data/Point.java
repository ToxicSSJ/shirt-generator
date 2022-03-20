package sh.toxic.shirt.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point {

    private LinkedList<PointType> points = new LinkedList<>();

    public boolean hasParent(int parent) {
        return points.stream().filter(type -> type.getParent() == parent).findAny().isPresent();
    }

    public void add(PointType pointType) {
        points.add(pointType);
    }

}
