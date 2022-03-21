package sh.toxic.shirt.graphic;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.effect.Glow;
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

    private int offsetX = 0;
    private int offsetY = 0;

    private int[][][] transpose;
    private Point[][] current;
    private Point[][] points;

    private LinkedList<ShirtEdge> edges;
    private LinkedList<ShirtDraw> draws;

    private GraphicsContext gc;
    private ShirtData data = new ShirtData();

    private Color color = Color.WHITE;

    private int rotates = 0;
    private int scales = 1;

    private boolean update;
    private boolean reference;

    private int baseWidth;
    private int baseHeight;

    public ShirtCanvas(int width, int height) {
        super(width * 2, height * 2);

        baseWidth = width;
        baseHeight = height;

        update = true;
        edges = new LinkedList<>();
        gc = getGraphicsContext2D();

        Glow glow = new Glow();
        glow.setLevel(2);
        setEffect(glow);

        update();

    }

    public void update() {

        if(!update) return;
        update = false;

        baseX = (baseWidth / 3) + offsetX;
        baseY = (baseHeight / 3) + offsetY;

        // * Clear everything
        current = new Point[baseWidth][baseHeight];
        points = new Point[baseWidth][baseHeight];
        gc.clearRect(0, 0, getWidth(), getHeight());
        current = points;
        edges.clear();

        // ? These are our bases
        if(reference) {

            addHip(); // Cadera (Hip)
            addBack(); // Espalda (Back)
            addWidth(); // Largo (Width)
            addLength(); // Ancho (Length)

        }

        // ? Pre-sleeves
        addLeftPreSleeve();
        addRightPreSleeve();

        // ? Sleeves
        addRightSleeve();
        addLeftSleeve();

        // ? Neck curves
        addRightNeck();
        addLeftNeck();
        addBackNeck();

        // ? Hip curves
        addLeftHipLength();
        addRightHipLength();
        addHipLongitude();

        for(int i = 0; i < rotates; i++)
            current = rotateCW(current);

        current = scale(current, scales);

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

        // Draw edges
        for(ShirtEdge edge : edges) {

            if(edge.getPointA() == null || edge.getPointB() == null)
                continue;

            if(edge.getColor() == Color.WHITE)
                gc.setStroke(color);
            else
                gc.setStroke(edge.getColor());

            // drawLine(edge.getX1(), edge.getY1(), edge.getX2(), edge.getY2());

            double precision = 0.1;

            double[] firstPoint = new double[0];
            double[] lastPoint = new double[0];

            PointType pointB = edge.getPointB();

            double bezierOffsetX = 0;
            double bezierOffsetY = 0;

            if(edge.getX1() > edge.getX2())
                bezierOffsetX = -pointB.getBezierOffX();
            else
                bezierOffsetX = pointB.getBezierOffX();

            if(edge.getY1() > edge.getY2())
                bezierOffsetY = -pointB.getBezierOffY();
            else
                bezierOffsetY = pointB.getBezierOffY();

            for(double t = 0; t < 1; t += precision) {

                double[] point = ShirtMath.casteljau(t, edge.getX1(), edge.getY1(), edge.getX2(), edge.getY2(), bezierOffsetX, bezierOffsetY);

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

    private void addRightPreSleeve() {

        int[] offsetX = new int[]{ (data.getBack() - data.getHip()) / 2, (data.getBack() - data.getLength()) / 2 };
        int[] offsetY = new int[]{ (data.getWidth() / 3), (data.getBack() / 9)};

        int x1 = (offsetX[1] + baseX) + data.getLength(), y1 = (offsetY[0] + baseY); // done
        int x2 = baseX + data.getBack(), y2 = (offsetY[1] + baseY);

        add(x1, y1, PointType.PRE_SLEEVE_UNION_C);
        add(x2, y2, PointType.PRE_SLEEVE_UNION_D);

    }

    private void addLeftPreSleeve() {

        int[] offsetX = new int[]{ (data.getBack() - data.getHip()) / 2, (data.getBack() - data.getLength()) / 2 };
        int[] offsetY = new int[]{ (data.getWidth() / 3), (data.getBack() / 9)};

        int x1 = (offsetX[1] + baseX), y1 = (offsetY[0] + baseY); // done
        int x2 = baseX, y2 = (offsetY[1] + baseY);

        add(x1, y1, PointType.PRE_SLEEVE_UNION_A);
        add(x2, y2, PointType.PRE_SLEEVE_UNION_B);

    }

    private void addHipLongitude() {

        int offsetX = (data.getBack() - data.getHip()) / 2;

        int x1 = (offsetX + baseX), y1 = baseY + data.getWidth();
        int x2 = (offsetX + baseX) + data.getHip(), y2 = baseY + data.getWidth();

        add(x1, y1, PointType.PRE_HIP_LONGITUDE_A);
        add(x2, y2, PointType.PRE_HIP_LONGITUDE_B);

    }

    private void addLeftHipLength() {

        int[] offsetX = new int[]{ (data.getBack() - data.getHip()) / 2, (data.getBack() - data.getLength()) / 2 };
        int offsetY = data.getWidth() / 3;

        int x1 = (offsetX[0] + baseX) + data.getHip(), y1 = baseY + data.getWidth();
        int x2 = (offsetX[1] + baseX) + data.getLength(), y2 = (offsetY + baseY);

        add(x1, y1, PointType.PRE_HIP_LENGTH_LEFT_A);
        add(x2, y2, PointType.PRE_HIP_LENGTH_LEFT_B);

    }

    private void addRightHipLength() {

        int[] offsetX = new int[]{ (data.getBack() - data.getHip()) / 2, (data.getBack() - data.getLength()) / 2 };
        int offsetY = data.getWidth() / 3;

        int x1 = (offsetX[0] + baseX), y1 = baseY + data.getWidth();
        int x2 = (offsetX[1] + baseX), y2 = (offsetY + baseY);

        add(x1, y1, PointType.PRE_HIP_LENGTH_RIGHT_A);
        add(x2, y2, PointType.PRE_HIP_LENGTH_RIGHT_B);

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

        if(x < 0 || y < 0) return;
        if(x > points.length || y > points[x].length) return;

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
        Point[][] ret = new Point[N][M];
        for (int r = 0; r < M; r++) {
            for (int c = 0; c < N; c++) {
                ret[c][M-1-r] = mat[r][c];
            }
        }
        return ret;
    }

    private Point[][] scale(Point[][] mat, int multiplier) {
        Point[][] newArray = new Point[(int) (mat.length*multiplier)][(int) (mat[0].length*multiplier)];

        for(int i = 0; i < newArray.length; i++)
            for(int j = 0; j < newArray[0].length; j++) {
                newArray[i][j] = mat[(int) (i/multiplier)][(int) (j/multiplier)];
            }
        return newArray;
    }

}
