package com.github.bademux.captcharecognision;

public final class Utils {


  public static int hi(final long value) { return (int) (value >> 32); }

  public static int lo(final long value) { return (int) (value & 0xFFFFFFFFL); }

  public static long pack(final int hi, final int lo) {
    return (((long) hi) << 32) | (lo & 0xFFFFFFFFL);
  }

  public static Element getElement(final Image img) {
    final int width = img.getWidth();
    final int height = img.getHeight();

    final Element.Builder builder = new Element.Builder(width * height);
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        builder.add(x, y, img.getARGB(x, y));
      }
    }
    return builder.build();
  }
}
