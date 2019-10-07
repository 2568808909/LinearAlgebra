package palyLA;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Matrix {

    public final static boolean ROW = false;

    public final static boolean COL = true;

    private Double[][] values;

    public Matrix() {
    }

    public Matrix(boolean arrange, Double[]... vectors) {
        int length = vectors[0].length;
        if (Arrays.stream(vectors).anyMatch(vector -> vector.length != length)) {
            throw new IllegalArgumentException("各行向量的长度要相等");
        }
        values = Arrays.copyOf(vectors, vectors.length);
        if (arrange == COL) {
            transposition();
        }
    }

    /**
     * 矩阵转置
     *
     * @return 返回转置后的结果
     */
    public Matrix transposition() {
        int row = row();
        int col = col();
        Double[][] values = new Double[col][row];
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                values[i][j] = this.values[j][i];
            }
        }
        this.values = values;
        return this;
    }

    public Matrix(boolean arrange, double[]... vectors) {
        int length = vectors[0].length;
        if (Arrays.stream(vectors).anyMatch(vector -> vector.length != length)) {
            throw new IllegalArgumentException("各行向量的长度要相等");
        }
        int row = vectors.length;
        int col = vectors[0].length;
        values = new Double[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                values[i][j] = vectors[i][j];
            }
        }
        if (arrange == COL) {
            transposition();
        }
    }

    /**
     * 获矩阵取r行，c列的值
     *
     * @param r 行坐标
     * @param c 列坐标
     * @return 返回矩阵中r行c列的值
     */
    public double get(int r, int c) {
        if (r < 0 || r > row() || c < 0 || c > col()) {
            throw new IllegalArgumentException(String.format("索引不正确(%d,%d)", r, c));
        }
        return values[r][c];
    }

    /**
     * 获取矩阵行数
     *
     * @return 返回矩阵的行数
     */
    public int row() {
        return values.length;
    }

    /**
     * 返回行向量
     *
     * @param index 索引
     * @return 返回第index行
     */
    public Vector getRow(int index) {
        return new Vector(getRowArray(index));
    }

    /**
     * 返回列向量
     *
     * @param index 索引
     * @return 返回滴index列
     */
    public Vector getCol(int index) {
        return new Vector(getColArray(index));
    }

    private Double[] getRowArray(int index) {
        if (index < 0 || index > row()) {
            throw new IllegalArgumentException("索引不正确 ：" + index);
        }
        return values[index];
    }

    private Double[] getColArray(int index) {
        if (index < 0 || index > col()) {
            throw new IllegalArgumentException("索引不正确 ：" + index);
        }
        Double[] col = new Double[values.length];
        for (int i = 0; i < values.length; i++) {
            col[i] = values[i][index];
        }
        return col;
    }

    /**
     * 获取矩阵
     *
     * @return 返回矩阵的列数
     */
    public int col() {
        return values[0].length;
    }

    /**
     * 返回矩阵的形状
     *
     * @return 返回矩阵的形状
     */
    public String shape() {
        return String.format("Matrix{(%d,%d)}", row(), col());
    }

    public int size() {
        return row() * col();
    }

    /**
     * 矩阵的数量乘法
     *
     * @param k 与矩阵相乘的标量
     * @return 返回矩阵数乘的结果
     */
    public Matrix mul(double k) {
        return new Matrix(ROW, Arrays.stream(values)
                .map(value -> Arrays.stream(value).mapToDouble(val -> val * k).toArray())
                .toArray(double[][]::new));
    }

    public Matrix div(double k) {
        return mul(1 / k);
    }

    /**
     * 矩阵相加
     *
     * @param another 另一矩阵
     * @return 返回相加后的结果
     */
    public Matrix add(Matrix another) {
        if (!shape().equals(another.shape())) {
            throw new IllegalArgumentException("相同形状的矩阵才可以相加");
        }
        int row = row();
        int col = col();
        return new Matrix(ROW, IntStream.range(0, row)
                .mapToObj(i -> IntStream.range(0, col).mapToDouble(j -> values[i][j] + another.get(i, j)).toArray())
                .toArray(double[][]::new));
    }

    /**
     * 矩阵相减
     *
     * @param another 另一矩阵
     * @return 返回相减后的结果
     */
    public Matrix sub(Matrix another) {
        return add(another.neg());
    }

    /**
     * 矩阵取正
     *
     * @return 返回矩阵本身
     */
    public Matrix pos() {
        return this;
    }

    /**
     * 矩阵取反
     *
     * @return 返回取反后的矩阵
     */
    public Matrix neg() {
        return new Matrix(ROW, Arrays
                .stream(values).map(value -> Arrays.stream(value).mapToDouble(val -> -val).toArray())
                .toArray(double[][]::new));
    }

    /**
     * 矩阵与向量相乘
     *
     * @param another 向量
     * @return 返回相乘结果(向量)
     */
    public Vector dot(Vector another) {
        int col = col();
        if (col != another.len()) {
            throw new UnsupportedOperationException("矩阵的列数与向量行数不相等");
        }
        int row = row();
        return new Vector(IntStream.range(0, row).mapToDouble(i -> getRow(i).dot(another)).toArray());
    }

    /**
     * 矩阵相乘
     *
     * @param another 另一矩阵
     * @return 返回相乘的结果(矩阵)
     */
    public Matrix dot(Matrix another) {
        int row = row();
        int col = col();
        int anotherCol = another.col();
        if (col != another.row()) {
            throw new UnsupportedOperationException("前者列数与后者行数不相等");
        }
        return new Matrix(ROW, IntStream.range(0, row).mapToObj(i -> {
            Vector rowVector = getRow(i);
            return IntStream.range(0, anotherCol).mapToDouble(j -> rowVector.dot(another.getCol(j))).toArray();
        }).toArray(double[][]::new));
    }

    @Override
    public String toString() {
        return "{" + Arrays.stream(values)
                .map(value -> "( " + Arrays.stream(value).map(Objects::toString).collect(Collectors.joining(", ")) + " )")
                .collect(Collectors.joining("\n")) + "}";
    }
}
