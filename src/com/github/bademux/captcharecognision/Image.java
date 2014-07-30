package com.github.bademux.captcharecognision;

public abstract class Image {

  private int offset;

  public int offset() { return offset; }

  public Image offset(final int offset) {
    this.offset = offset;
    return this;
  }

  protected abstract int getAbsolutARGB(int x, int y);

  public int getARGB(int x, int y) { return getAbsolutARGB(x + offset, y); }

  public abstract int getHeight();

  public abstract int getWidth();
}