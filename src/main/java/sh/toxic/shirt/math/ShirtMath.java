package sh.toxic.shirt.math;

public class ShirtMath {

    public static double[] casteljau(double t, double x1, double y1, double x2, double y2, double xoffset, double yoffset) {
        Casteljau casteljau = new Casteljau(new double[]{x1, x1 + xoffset, x2}, new double[]{y1, y1 + yoffset, y2}, 3);
        return casteljau.getXYvalues(t);
    }

    public static double[][] casteljau2(double t, double x1, double y1, double x2, double y2) {
        Casteljau casteljau1 = new Casteljau(new double[]{x1, x2}, new double[]{y1 / 2, y2 / 2}, 2);
        Casteljau casteljau2 = new Casteljau(new double[]{y1 / 2, y2 / 2}, new double[]{y1, y2}, 2);
        return new double[][]{casteljau1.getXYvalues(t), casteljau2.getXYvalues(t)};
    }

}
