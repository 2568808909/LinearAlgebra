package palyLA;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
public class Vector {

    private Double[] values;

    public Vector(Double[] values) {
        if (values == null) {
            throw new NullPointerException();
        }
        this.values = Arrays.copyOf(values, values.length);
    }

    public Vector(double[] values) {
        if (values == null) {
            throw new NullPointerException();
        }
        this.values = new Double[values.length];
        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i];
        }
    }

    /**
     * 返回向量的模
     *
     * @return
     */
    public double norm() {
        double sum = Arrays.stream(values).mapToDouble(values -> values * values).sum();
        return Math.sqrt(sum);
    }

    /**
     * 规范化，求单位向量
     *
     * @return
     */
    public Vector normalize() {
        double norm = norm();
        if (norm < Global.EPSILON) {
            throw new UnsupportedOperationException("零向量无法规范化");
        }
        return new Vector(Arrays.stream(values).mapToDouble(value -> value / norm).toArray());
    }

    /**
     * 创建一个n维的零向量
     *
     * @param dim
     * @return
     */
    public static Vector zero(int dim) {
        if (dim < 0) {
            throw new IllegalArgumentException("向量的维度不能小于0");
        }
        return new Vector(new double[dim]);
    }

    /**
     * 获取向量某个维度的数
     *
     * @param index
     * @return
     */
    public Double get(int index) {
        if (index < 0 || index > values.length) {
            throw new IllegalArgumentException("索引不正确");
        }
        return values[index];
    }

    /**
     * 返回向量的维度
     *
     * @return
     */
    public int len() {
        return values.length;
    }

    /**
     * 两个向量之间的点乘结果(标量)
     *
     * @param another 另一向量
     * @return
     */
    public double dot(Vector another) {
        if (values.length != another.len()) {
            throw new IllegalArgumentException("两个向量长度必须一致");
        }
        return IntStream.range(0, values.length).mapToDouble(i -> values[i] * another.get(i)).sum();
    }

    /**
     * 向量相加
     *
     * @param another
     * @return
     */
    public Vector add(Vector another) {
        if (values.length != another.len()) {
            throw new IllegalArgumentException("两个向量长度必须一致");
        }
        return new Vector(IntStream.range(0, values.length).mapToDouble(i -> values[i] + another.get(i)).toArray());
    }

    /**
     * 向量相减
     *
     * @param another
     * @return
     */
    public Vector sub(Vector another) {
        return add(another.neg());
    }

    /**
     * 标量与向量相乘
     *
     * @param k
     * @return
     */
    public Vector mul(double k) {
        return new Vector(Arrays.stream(values).mapToDouble(value -> value * k).toArray());
    }

    public Vector div(double k) {
        if (k == 0) {
            throw new IllegalArgumentException("不可除0");
        }
        return mul(1 / k);
    }

    /**
     * 向量取正
     *
     * @return
     */
    public Vector pos() {
        return this;
    }

    /**
     * 向量取负
     *
     * @return
     */
    public Vector neg() {
        return new Vector(Arrays.stream(values).mapToDouble(value -> -value).toArray());
    }

    @Override
    public String toString() {
        return "(" + Arrays.stream(values).map(Object::toString).collect(Collectors.joining(", ")) + ")";
    }
}
