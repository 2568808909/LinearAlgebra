import palyLA.Matrix;
import palyLA.Vector;

public class Main {

    public static void main(String[] args) {
        Double[][] d = new Double[][]{{1d, 2d}, {3d,4d}};
        Double[][] c = new Double[][]{{5d,6d},{7d,8d}};
        Matrix matrix = new Matrix(Matrix.ROW,d);
        Matrix matrix1 =new Matrix(Matrix.ROW,c);
        System.out.println(matrix.dot(matrix1));

    }
}
