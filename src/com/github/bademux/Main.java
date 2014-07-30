package com.github.bademux;

import com.github.bademux.captcharecognision.Element;
import com.github.bademux.captcharecognision.Image;
import com.github.bademux.captcharecognision.Utils;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Main {

  private final static int ELEMENT_QUANTITY = 6;

  private final static int ELEMENT_WIDTH = 21;

  private final static int FIRST_ELEMENT_OFFSET = 7;

  public static void main(String[] args) throws IOException {

    final Image img = createImage("/resources/134009.png");

    final List<Element> elements = new ArrayList<Element>(ELEMENT_QUANTITY);

    for (int i = FIRST_ELEMENT_OFFSET; i < ELEMENT_QUANTITY * ELEMENT_WIDTH; i += ELEMENT_WIDTH) {
      elements.add(Utils.getElement(img.offset(i)));
    }

    for (Element element : elements) {
      System.out.println(element.serialize());
      System.out.println(element.toString());
    }
  }

  private static Image createImage(final String path) throws IOException {
    final BufferedImage img = ImageIO.read(Main.class.getResourceAsStream(path));

    return new Image() {
      @Override
      protected int getAbsolutARGB(final int x, final int y) { return img.getRGB(x, y); }

      @Override
      public int getHeight() { return img.getHeight(); }

      @Override
      public int getWidth() { return ELEMENT_WIDTH; }
    };
  }

  private static void serialize(Element element, final String filename)
      throws IOException {
    OutputStream file = null;
    ObjectOutput stream = null;
    try {
      file = new FileOutputStream(filename);
      stream = new ObjectOutputStream(file);
      stream.writeObject(element);
    } finally {
      if (file != null) {
        file.close();
      }
      if (stream != null) {
        stream.close();
      }
    }
  }

  private static Element deserialize(final String filename)
      throws IOException, ClassNotFoundException {
    InputStream file = null;
    ObjectInput stream = null;
    try {
      file = new FileInputStream(filename);
      stream = new ObjectInputStream(file);
      return (Element) stream.readObject();
    } finally {
      if (file != null) {
        file.close();
      }
      if (stream != null) {
        stream.close();
      }
    }
  }
}
