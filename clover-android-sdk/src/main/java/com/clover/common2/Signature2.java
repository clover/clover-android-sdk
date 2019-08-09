package com.clover.common2;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Signature2 implements Parcelable {
  private static final String TAG = "Signature2";

  int width = -1;
  int height = -1;

  public List<Stroke> strokes;

  public Signature2() {
    strokes = new ArrayList<Stroke>();
  }

  public Signature2(int width, int height) {
    this();

    this.width = width;
    this.height = height;
  }

  public Signature2(Parcel in) {
    this();

    this.width = in.readInt();
    this.height = in.readInt();

    in.readList(strokes, ((Object) this).getClass().getClassLoader());
  }

  public void startStroke(int x, int y) {
    strokes.add(new Stroke(x, y));
  }

  public void addToStroke(int x, int y) {
    strokes.get(strokes.size() - 1).addPoint(x, y);
  }

  public void clear() {
    strokes.clear();
  }

  public Stroke getLastStroke() {
    if (strokes.isEmpty()) {
      return null;
    }
    return strokes.get(strokes.size() - 1);
  }

  public boolean isEmpty() {
    return strokes.isEmpty();
  }

  public void transform(int newWidth, int newHeight, boolean expand) {
    if (Log.isLoggable(TAG, Log.VERBOSE)) {
      Log.v(TAG, String.format(Locale.US, "width=%d, new width=%d, height=%d, new height=%d", width, newWidth, height, newHeight));
    }

    if (width != -1 && height != -1) {
      Pair<Point, Point> bounds = getBounds();

      // expand the bounds by 4 pixel on each side if possible
      bounds.first.x = Math.max(0, bounds.first.x - 4);
      bounds.first.y = Math.max(0, bounds.first.y - 4);

      bounds.second.x = Math.min(width, bounds.second.x + 4);
      bounds.second.y = Math.min(height, bounds.second.y + 4);

      Point diff = bounds.first.diff(bounds.second);

      float factor;

      if (expand) {
        float xFactor = (float) newWidth / diff.x;
        float yFactor = (float) newHeight / diff.y;
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
          Log.v(TAG, String.format(Locale.US, "expanding, x factor=%f, y factor=%f", xFactor, yFactor));
        }
        factor = Math.min(xFactor, yFactor);
      } else {
        float xFactor = (float) newWidth / width;
        float yFactor = (float) newHeight / height;
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
          Log.v(TAG, String.format(Locale.US, "not expanding, x factor=%f, y factor=%f", xFactor, yFactor));
        }
        if (diff.x > diff.y) {
          factor = xFactor;
        } else {
          factor = yFactor;
        }
      }

      int strokeNum = 0;
      for (Stroke s : strokes) {
        int pointNum = 0;
        for (Point p : s.points) {

          float nx = p.x * factor;
          float ny = p.y * factor;

          int newX = Math.round(nx);
          int newY = Math.round(ny);

          if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, String.format(Locale.US, "stroke %d, point %d: x=%d->%d (%f), y=%d->%d (%f), factor=%f", strokeNum, pointNum, p.x, newX, nx, p.y, newY, ny, factor));
          }

          p.x = Math.round(newX);
          p.y = Math.round(newY);

          pointNum++;
        }
        strokeNum++;
      }
    }

    width = newWidth;
    height = newHeight;

    center();
  }

  public void center() {
    Point viewCenter = new Point(width / 2, height / 2);
    Pair<Point, Point> bounds = getBounds();
    Point sigCenter = new Point(bounds.first.x + ((bounds.second.x - bounds.first.x) / 2), bounds.first.y + ((bounds.second.y - bounds.first.y) / 2));

    int xDiff = viewCenter.x - sigCenter.x;
    int yDiff = viewCenter.y - sigCenter.y;

    for (Stroke s : strokes) {
      for (Point p : s.points) {
        p.x += xDiff;
        p.y += yDiff;
      }
    }
  }

  public void leftAlign() {
    Point viewCenter = new Point(width / 2, height / 2);
    Pair<Point, Point> bounds = getBounds();
    Point sigCenter = new Point(bounds.first.x + ((bounds.second.x - bounds.first.x) / 2), bounds.first.y + ((bounds.second.y - bounds.first.y) / 2));

    int xDiff = bounds.first.x;
    int yDiff = viewCenter.y - sigCenter.y;

    for (Stroke s : strokes) {
      for (Point p : s.points) {
        p.x -= xDiff - 2; // padding the left
        p.y += yDiff;
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append("Signature{");
    for (Stroke s : strokes) {
      str.append(s.toString());
    }
    str.append("}");
    return str.toString();
  }

  public static class Point implements Parcelable {
    public int x, y;

    public Point() {
    }

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public Point(Parcel in) {
      this.x = in.readInt();
      this.y = in.readInt();
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(x);
      dest.writeInt(y);
    }

    public static final Creator<Point> CREATOR = new Creator<Point>() {
      public Point createFromParcel(Parcel p) {
        return new Point(p);
      }

      public Point[] newArray(int size) {
        return new Point[size];
      }
    };

    @Override
    public String toString() {
      return String.format(Locale.US, "Point{x=%d, y=%d}", x, y);
    }

    public Point diff(Point other) {
      Point diff = new Point(Math.abs(x - other.x), Math.abs(y - other.y));
      return diff;
    }
  }

  public static class Stroke implements Parcelable {
    public List<Point> points;

    public Stroke() {
      points = new ArrayList<Point>();
    }

    public Stroke(Parcel in) {
      this();

      in.readList(points, ((Object) this).getClass().getClassLoader());
    }

    public Stroke(int x, int y) {
      this();

      if (x < 0) {
        x = 0;
      }
      if (y < 0) {
        y = 0;
      }

      points.add(new Point(x, y));
    }

    public void addPoint(int x, int y) {
      if (x < 0) {
        x = 0;
      }
      if (y < 0) {
        y = 0;
      }

      points.add(new Point(x, y));
    }

    public int size() {
      return points.size();
    }

    @Override
    public String toString() {
      StringBuilder str = new StringBuilder();
      str.append("Stroke{");
      for (Point point : points) {
        str.append(point);
      }
      str.append("}");
      return str.toString();
    }

    public static final Creator<Stroke> CREATOR = new Creator<Stroke>() {
      public Stroke createFromParcel(Parcel p) {
        return new Stroke(p);
      }

      public Stroke[] newArray(int size) {
        return new Stroke[size];
      }
    };

    /**
     * No special parcel contents.
     */
    public int describeContents() {
      return 0;
    }

    public void writeToParcel(Parcel p, int flags) {
      p.writeList(points);
    }

    public Pair<Point, Point> getBounds() {
      Pair<Point, Point> bounds = new Pair<Point, Point>(new Point(-1, -1), new Point(-1, -1));
      for (Point p : points) {
        if (bounds.first.x == -1 || p.x < bounds.first.x) {
          bounds.first.x = p.x;
        }
        if (bounds.first.y == -1 || p.y < bounds.first.y) {
          bounds.first.y = p.y;
        }
        if (bounds.second.x == -1 || p.x > bounds.second.x) {
          bounds.second.x = p.x;
        }
        if (bounds.second.y == -1 || p.y > bounds.second.y) {
          bounds.second.y = p.y;
        }
      }
      return bounds;
    }
  }

  public Pair<Point, Point> getBounds() {
    Pair<Point, Point> bounds = new Pair<Point, Point>(new Point(-1, -1), new Point(-1, -1));
    for (Stroke stroke : strokes) {
      Pair<Point, Point> b = stroke.getBounds();
      if (bounds.first.x == -1 || b.first.x < bounds.first.x) {
        bounds.first.x = b.first.x;
      }
      if (bounds.first.y == -1 || b.first.y < bounds.first.y) {
        bounds.first.y = b.first.y;
      }
      if (bounds.second.x == -1 || b.second.x > bounds.second.x) {
        bounds.second.x = b.second.x;
      }
      if (bounds.second.y == -1 || b.second.y > bounds.second.y) {
        bounds.second.y = b.second.y;
      }
    }

    return bounds;
  }

  public static final Creator<Signature2> CREATOR = new Creator<Signature2>() {

    public Signature2 createFromParcel(Parcel p) {
      return new Signature2(p);
    }

    public Signature2[] newArray(int size) {
      return new Signature2[size];
    }
  };

  /**
   * No special parcel contents.
   */
  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel p, int flags) {
    p.writeInt(width);
    p.writeInt(height);

    p.writeList(strokes);
  }
}
