package sh.toxic.shirt.graphic;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import sh.toxic.shirt.data.*;

import java.util.LinkedList;
import java.util.Optional;

import lombok.Data;
import sh.toxic.shirt.math.ShirtMath;

@Data
public class ShirtCanvas extends Canvas {

    private int baseX = 0;
    private int baseY = 0;

    private int centerX = 0;
    private int centerY = 0;

    private int[][][] transpose;
    private Point[][] current;
    private Point[][] points;

    private LinkedList<ShirtEdge> edges;
    private LinkedList<ShirtDraw> draws;

    private GraphicsContext gc;
    private ShirtData data = new ShirtData();

    public ShirtCanvas(int width, int height) {
        super(width, height);

        baseX = 150;
        baseY = 150;

        current = new Point[width][height];
        points = new Point[width][height];
        edges = new LinkedList<>();
        gc = getGraphicsContext2D();


        centerX = data.getHip() / 2;
        centerY = data.getWidth() / 2;


        // ? These are our bases
        addHip(); // Cadera (Hip)
        addBack(); // Espalda (Back)
        addWidth(); // Largo (Width)
        addLength(); // Ancho (Length)

        // ? Extras
        addRightSleeve();
        addLeftSleeve();

        addRightNeck();
        addLeftNeck();
        addBackNeck();

        // Update
        // update();

        // Clear
        current = points;


        // Rotate
        // current = scale(current, 2);
        current = rotateCW(current);
        current = rotateCW(current);
        current = rotateCW(current);
        current = rotateCW(current);
        // points = rotateCW(points);
        // points = rotateCW(points);
        // points = rotateCW(points);

        // Update
        update();

    }

    public void update() {

        edges.clear();

        for(int x = 0; x < current.length; x++)
            for(int y = 0; y < current[x].length; y++) {

                Point point = current[x][y];
                if(point == null) continue;

                for(PointType type : point.getPoints()) {

                    Optional<ShirtEdge> optionalEdge = edges.stream().filter(edge -> edge.getParent() == type.getParent()).findAny();
                    ShirtEdge shirtEdge;

                    if(optionalEdge.isPresent())
                        shirtEdge = optionalEdge.get();
                    else {
                        shirtEdge = new ShirtEdge();
                        edges.add(shirtEdge);
                    }

                    shirtEdge.setParent(type.getParent());
                    shirtEdge.setColor(type.getColor());

                    if(type.isStart()) {

                        shirtEdge.setPointA(type);
                        shirtEdge.setX1(x);
                        shirtEdge.setY1(y);

                    } else {

                        shirtEdge.setPointB(type);
                        shirtEdge.setX2(x);
                        shirtEdge.setY2(y);

                    }

                }

            }

        // drawSilhouette();

        // Union (Hip-Length)
        for(ShirtEdge edge : edges) {

            gc.setStroke(edge.getColor());
            // drawLine(edge.getX1(), edge.getY1(), edge.getX2(), edge.getY2());

            double precision = 0.1;

            double[] firstPoint = new double[0];
            double[] lastPoint = new double[0];

            for(double t = 0; t < 1; t += precision) {

                double[] point = ShirtMath.casteljau(t, edge.getX1(), edge.getY1(), edge.getX2(), edge.getY2(), edge.getPointB().getBezierOffX(), edge.getPointB().getBezierOffY());

                if(firstPoint == null || firstPoint.length == 0)
                    firstPoint = point;

                if(lastPoint != null && lastPoint.length > 0) {

                    gc.setFill(Color.BLACK);
                    // gc.strokeLine(point[0], point[1], lastPoint[0], lastPoint[1]);
                    drawLine(point[0], point[1], lastPoint[0], lastPoint[1]);

                }

                lastPoint = point;

            }

        }

    }

    private void drawSilhouette() {

        ShirtEdge back = getEdge(PointType.BACK_A);
        ShirtEdge width = getEdge(PointType.WIDTH_A);
        ShirtEdge hip = getEdge(PointType.HIP_A);
        ShirtEdge length = getEdge(PointType.LENGTH_A);

        // * Hips
        ShirtDraw hipLengthLeft = drawCurve(DrawType.HIP_LENGTH_LEFT, hip.getX1(), hip.getY1(), length.getX1(), length.getY1(), 5, 5);
        ShirtDraw hipLengthRight = drawCurve(DrawType.HIP_LENGTH_RIGHT, hip.getX2(), hip.getY2(), length.getX2(), length.getY2(), -5, -5);
        ShirtDraw hipLongitude = drawCurve(DrawType.HIP_LONGITUDE, hip.getX1(), hip.getY1(), hip.getX2(), hip.getY2(), 5, -5);

        // * Pre-Sleeve
        ShirtDraw preSleeveLeft = drawCurve(DrawType.PRE_SLEEVE_LEFT, hipLengthLeft.getEndX(), hipLengthLeft.getEndY(), back.getX1(), back.getY1(), 0.5, 5);
        ShirtDraw preSleeveRight = drawCurve(DrawType.PRE_SLEEVE_RIGHT, hipLengthRight.getEndX(), hipLengthRight.getEndY(), back.getX2(), back.getY2(), -0.5, 5);

        int sleeveLongLength = data.getBack() / 3;

        double neckCurve = data.getBack() / 10;
        double neckHeight = data.getBack() / 9;
        double neckWidth = data.getBack() / 3;

        double shirtCenterX = ((data.getBack() - data.getHip()) / 2) + (data.getHip() / 2);

        // ShirtDraw sleeveLeft1 = drawCurve(DrawType.SLEEVE_LEFT, back.getX1(), back.getY1(), length.getX1(), length.getY1() + 5, 0, 0);

        // * Sleeve Left
        // ShirtDraw sleeveLeft11 = drawCurve(DrawType.SLEEVE_LEFT, back.getX1(), back.getY1(), preSleeveLeft.getEndX() - sleeveLongLength, preSleeveLeft.getEndY() + sleeveLongLength, 0, 0);
        // ShirtDraw sleeveLeft2 = drawCurve(DrawType.SLEEVE_LEFT, length.getX1(), length.getY1(), preSleeveLeft.getStartX() - (sleeveLongLength / 2), preSleeveLeft.getStartY() + (sleeveLongLength / 2), 0, 0);
        // ShirtDraw sleeveLeft3 = drawCurve(DrawType.SLEEVE_LEFT, sleeveLeft1.getEndX(), sleeveLeft1.getEndY(), sleeveLeft2.getEndX(), sleeveLeft2.getEndY(), 0, 0);

        // * Sleeve Right
        // ShirtDraw sleeveRight1 = drawCurve(DrawType.SLEEVE_RIGHT, back.getX2(), back.getY2(), preSleeveRight.getEndX() + sleeveLongLength, preSleeveRight.getEndY() + sleeveLongLength, 0, 0);
        // ShirtDraw sleeveRight2 = drawCurve(DrawType.SLEEVE_RIGHT, length.getX2(), length.getY2(), preSleeveRight.getStartX() + (sleeveLongLength / 2), preSleeveRight.getStartY() + (sleeveLongLength / 2), 0, 0);
        // ShirtDraw sleeveRight3 = drawCurve(DrawType.SLEEVE_RIGHT, sleeveRight1.getEndX(), sleeveRight1.getEndY(), sleeveRight2.getEndX(), sleeveRight2.getEndY(), 0, 0);

        // * Pre Neck
        // ShirtDraw neckLeft = drawCurve(DrawType.NECK_LEFT, preSleeveLeft.getEndX(), preSleeveLeft.getEndY(), preSleeveLeft.getEndX() + neckWidth, preSleeveLeft.getEndY() - neckHeight, 0, 0);
        // ShirtDraw neckRight = drawCurve(DrawType.NECK_RIGHT, preSleeveRight.getEndX(), preSleeveRight.getEndY(), preSleeveRight.getEndX() - neckWidth, preSleeveRight.getEndY() - neckHeight, 0, 0);

        System.out.println(neckHeight);

        // * Neck
        // ShirtDraw neckLeftCurve = drawCurve(DrawType.NECK, neckLeft.getEndX(), neckLeft.getEndY(), baseX + shirtCenterX, neckLeft.getEndY() + 15, 15, 15);
        // ShirtDraw neckRightCurve = drawCurve(DrawType.NECK, neckRight.getEndX(), neckRight.getEndY(), neckLeftCurve.getEndX(), neckLeftCurve.getEndY(), -15, 15);
        // ShirtDraw neckInsideCurve = drawCurve(DrawType.NECK_INSIDE, neckLeft.getEndX(), neckLeft.getEndY(), neckRight.getEndX(), neckRight.getEndY(), 15, 0);

        System.out.println("WTF");

    }

    private ShirtDraw drawCurve(DrawType drawType, double x1, double y1, double x2, double y2, double xoffset, double yoffset) {

        double precision = 0.1;

        double[] firstPoint = new double[0];
        double[] lastPoint = new double[0];

        for(double t = 0; t < 1; t += precision) {

            double[] point = ShirtMath.casteljau(t, x1, y1, x2, y2, xoffset, yoffset);

            if(firstPoint == null || firstPoint.length == 0)
                firstPoint = point;

            if(lastPoint != null && lastPoint.length > 0) {

                gc.setFill(Color.BLACK);
                gc.strokeLine(point[0], point[1], lastPoint[0], lastPoint[1]);
                // drawLine(point[0], point[1], lastPoint[0], lastPoint[1]);

            }

            lastPoint = point;

        }

        return ShirtDraw.builder()
                .drawType(drawType)
                .start(firstPoint)
                .end(lastPoint)
                .build();

    }

    private ShirtEdge getEdge(PointType startType) {
        return edges.stream().filter(edge -> edge.getParent() == startType.getParent()).findAny().get();
    }

    private void addBackNeck() {

        int neckWidth = (data.getBack() / 3);
        int neckHeight = (data.getBack() / 9);

        int[] offsetY = new int[]{ (data.getBack() / 9), (data.getWidth() / 3) };

        int x1 = baseX + data.getBack() - neckWidth, y1 = (offsetY[0] + baseY - neckHeight);
        int x2 = baseX + neckWidth, y2 = (offsetY[0] + baseY - neckHeight);

        add(x1, y1, PointType.PRE_NECK_BACK_A);
        add(x2, y2, PointType.PRE_NECK_BACK_B);

    }

    private void addLeftNeck() {

        int neckWidth = (data.getBack() / 3);
        int neckHeight = (data.getBack() / 9);
        int shirtCenterX = ((data.getBack() - data.getHip()) / 2) + (data.getHip() / 2);

        int[] offsetY = new int[]{ (data.getBack() / 9), (data.getWidth() / 3) };

        int x1 = baseX + data.getBack(), y1 = (offsetY[0] + baseY);
        int x2 = baseX + data.getBack() - neckWidth, y2 = (offsetY[0] + baseY - neckHeight);

        add(x1, y1, PointType.PRE_NECK_LEFT_A);
        add(x2, y2, PointType.PRE_NECK_LEFT_B);

        int x3 = baseX + data.getBack() - neckWidth, y3 = (offsetY[0] + baseY - neckHeight);
        int x4 = baseX + shirtCenterX, y4 = (offsetY[0] + baseY - neckHeight) + 15;

        add(x3, y3, PointType.PRE_NECK_LEFT_C);
        add(x4, y4, PointType.PRE_NECK_LEFT_D);

    }

    private void addRightNeck() {

        int neckWidth = (data.getBack() / 3);
        int neckHeight = (data.getBack() / 9);
        int shirtCenterX = ((data.getBack() - data.getHip()) / 2) + (data.getHip() / 2);

        int[] offsetY = new int[]{ (data.getBack() / 9), (data.getWidth() / 3) };

        int x1 = baseX, y1 = (offsetY[0] + baseY);
        int x2 = baseX + neckWidth, y2 = (offsetY[0] + baseY - neckHeight);

        add(x1, y1, PointType.PRE_NECK_RIGHT_A);
        add(x2, y2, PointType.PRE_NECK_RIGHT_B);

        int x3 = baseX + neckWidth, y3 = (offsetY[0] + baseY - neckHeight);
        int x4 = baseX + shirtCenterX, y4 = (offsetY[0] + baseY - neckHeight) + 15;

        add(x3, y3, PointType.PRE_NECK_RIGHT_C);
        add(x4, y4, PointType.PRE_NECK_RIGHT_D);

    }

    private void addLeftSleeve() {

        int sleeve = (data.getBack() / 3);

        int[] offsetX = new int[]{0, ((data.getBack() - data.getLength()) / 2)};
        int[] offsetY = new int[]{ (data.getBack() / 9), (data.getWidth() / 3) };

        int x1 = baseX + data.getBack(), y1 = (offsetY[0] + baseY);
        int x2 = baseX + data.getBack() + sleeve, y2 = (offsetY[0] + baseY + sleeve);

        add(x1, y1, PointType.PRE_SLEEVE_LEFT_A);
        add(x2, y2, PointType.PRE_SLEEVE_LEFT_B);

        int x3 = (offsetX[1] + baseX + data.getLength()), y3 = (offsetY[1] + baseY);
        int x4 = (offsetX[1] + baseX + data.getLength()) + (sleeve / 2), y4 = (offsetY[1] + baseY + (sleeve / 2));

        add(x3, y3, PointType.PRE_SLEEVE_LEFT_C);
        add(x4, y4, PointType.PRE_SLEEVE_LEFT_D);

        add(x2, y2, PointType.PRE_SLEEVE_LEFT_E);
        add(x4, y4, PointType.PRE_SLEEVE_LEFT_F);

    }

    private void addRightSleeve() {

        int sleeve = (data.getBack() / 3);

        int[] offsetX = new int[]{0, ((data.getBack() - data.getLength()) / 2)};
        int[] offsetY = new int[]{ (data.getBack() / 9), (data.getWidth() / 3) };

        int x1 = baseX, y1 = (offsetY[0] + baseY);
        int x2 = baseX - sleeve, y2 = (offsetY[0] + baseY + sleeve);

        add(x1, y1, PointType.PRE_SLEEVE_RIGHT_A);
        add(x2, y2, PointType.PRE_SLEEVE_RIGHT_B);

        int x3 = (offsetX[1] + baseX), y3 = (offsetY[1] + baseY);
        int x4 = (offsetX[1] + baseX) - (sleeve / 2), y4 = (offsetY[1] + baseY + (sleeve / 2));

        add(x3, y3, PointType.PRE_SLEEVE_RIGHT_C);
        add(x4, y4, PointType.PRE_SLEEVE_RIGHT_D);

        add(x2, y2, PointType.PRE_SLEEVE_RIGHT_E);
        add(x4, y4, PointType.PRE_SLEEVE_RIGHT_F);

    }

    private void addHip() {

        int offsetX = (data.getBack() - data.getHip()) / 2;

        add((offsetX + baseX), baseY + data.getWidth(), PointType.HIP_A);
        add((offsetX + baseX) + data.getHip(), baseY + data.getWidth(), PointType.HIP_B);

    }

    private void addWidth() {

        int offsetX = (int) ((data.getHip() * 3.4) / 4.0);

        add((offsetX + baseX), baseY + data.getWidth(), PointType.WIDTH_A);
        add((offsetX + baseX), baseY, PointType.WIDTH_B);

    }

    private void addLength() {

        int offsetX = (data.getBack() - data.getLength()) / 2;
        int offsetY = data.getWidth() / 3;

        add((offsetX + baseX), (offsetY + baseY), PointType.LENGTH_A);
        add((offsetX + baseX) + data.getLength(), (offsetY + baseY), PointType.LENGTH_B);

    }

    private void addBack() {

        int offsetY =  (data.getBack() / 9);

        add(baseX, (offsetY + baseY), PointType.BACK_A);
        add(baseX + data.getBack(), (offsetY + baseY), PointType.BACK_B);

    }

    private void add(int x, int y, PointType type) {
        if(points[x][y] == null) points[x][y] = new Point();
        points[x][y].add(type);
    }

    private void drawLine(double x1, double y1, double x2, double y2) {

        Affine prije = gc.getTransform();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);

        int len = (int) Math.sqrt(dx * dx + dy * dy);

        Transform transform = Transform.translate(x1, y1);
        transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));

        gc.setTransform(new Affine(transform));
        gc.strokeLine(0, 0, len, 0);
        gc.setTransform(prije);

    }

    private Point[][] rotateCW(Point[][] mat) {
        final int M = mat.length;
        final int N = mat[0].length;
        transpose = new int[N][M][2];
        Point[][] ret = new Point[N][M];
        for (int r = 0; r < M; r++) {
            for (int c = 0; c < N; c++) {
                ret[c][M-1-r] = mat[r][c];
                transpose[c][M-1-r] = new int[]{r, c};
            }
        }
        return ret;
    }

    private Point[][] scale(Point[][] mat, int multiplier) {
        Point[][] newArray = new Point[mat.length*2][mat[0].length*2];

        for(int i = 0; i < newArray.length; i++)
            for(int j = 0; j < newArray[0].length; j++) {
                newArray[i][j] = mat[i/multiplier][j/multiplier];
            }
        return newArray;
    }

}
