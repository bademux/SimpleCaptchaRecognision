package com.github.bademux.captcharecognision;

import java.util.AbstractList;
import java.util.Arrays;

import static com.github.bademux.captcharecognision.Utils.hi;
import static com.github.bademux.captcharecognision.Utils.lo;
import static com.github.bademux.captcharecognision.Utils.pack;

public class Element extends AbstractList<Long> {

  private final long[] points;

  public Element(final String serializedPoints) {
    points = deserialize(serializedPoints);
  }

  Element(final int pos, final int length, final long[] points) {
    this.points = new long[length];
    System.arraycopy(points, pos, this.points, 0, this.points.length);
  }

  @Override
  public Long get(int index) { return getLong(index); }

  public long getLong(int index) { return points[index]; }

  @Override
  public int size() { return points.length; }

  public boolean contains(final int x, final int y) { return contains(pack(x, y)); }

  protected int[] calcMaxMin() {
    int maxX = 0, maxY = 0, minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;

    for (int i = 0; i < size(); i++) {
      int x = hi(points[i]);
      int y = lo(points[i]);

      if (x > maxX) {
        maxX = x;
      } else if (x < minX) {
        minX = x;
      }
      if (y > maxY) {
        maxY = y;
      } else if (y < minY) {
        minY = y;
      }
    }
    return new int[]{maxX, minX, maxY, minY};
  }

  public final String serialize() {
    final StringBuffer sb = new StringBuffer();
    for (int i = 0; i < points.length; i++) {
      sb.append(hi(points[i])).append(',').append(lo(points[i])).append(';');
    }
    return sb.append("\n").toString();
  }

  /** @param serializedPoints - format: x,y;x,y; */
  private long[] deserialize(final String serializedPoints) {
    if (serializedPoints == null || serializedPoints.length() < 2) {
      throw new IllegalArgumentException("serializedPoints can't be empty");
    }
    String[] pairs = serializedPoints.split(";", Integer.MAX_VALUE);

    long[] points = new long[pairs.length];

    for (int i = 0; i < pairs.length; i++) {
      String[] point = pairs[i].split(",");
      if (point.length != 2) {
        throw new IllegalArgumentException("Item on position " + i + " is invalid");
      }
      this.points[i] = pack(Integer.valueOf(point[0]), Integer.valueOf(point[1]));
    }
    return points;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Element)) {
      return false;
    }

    return Arrays.equals(points, ((Element) o).points);
  }

  @Override
  public int hashCode() {
    return 31 + Arrays.hashCode(points);
  }

  @Override
  public String toString() {
    int[] stat = calcMaxMin();
    final int lengthX = stat[0];
    final int lengthY = stat[2];

    StringBuffer sb = new StringBuffer(lengthX * lengthY);
    for (int y = 0; y < lengthY; y++) {
      for (int x = 0; x < lengthX; x++) {
        sb.append(contains(x, y) ? '1' : '0');
      }
      sb.append("\n");
    }

    return sb.toString();
  }

  public static class Builder {

    private final long[] points;
    private volatile int i = 0;

    public Builder(final int elementMaxSize) { points = new long[elementMaxSize]; }

    public Element build() { return new Element(0, i, points); }

    public Builder add(final long point, final int argbPixel) {
      if (isEnabled(argbPixel)) {
        add(point);
      }
      return this;
    }

    public Builder add(final int x, final int y, final int argbPixel) {
      return add(pack(x, y), argbPixel);
    }

    public Builder add(final long point) {
      points[i++] = point;
      return this;
    }

    public Builder add(final int x, final int y) {
      return add(pack(x, y));
    }

    public static boolean isEnabled(final int argb) {
      return !((argb >> 24) == 0) //isAlpha
             && !((0x00FFFFFF & argb) == 0x00FFFFFF); //isWhite
    }
  }
}