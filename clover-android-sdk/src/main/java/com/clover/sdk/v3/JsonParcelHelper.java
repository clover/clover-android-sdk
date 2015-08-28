/**
 * Copyright (C) 2015 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clover.sdk.v3;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public final class JsonParcelHelper {

  private JsonParcelHelper() { }

  public static ObjectWrapper wrap(JSONObject object) {
    return new ObjectWrapper(object);
  }

  public static ArrayWrapper wrap(JSONArray array) {
    return new ArrayWrapper(array);
  }

  private static final int VAL_NULL = -1;
  private static final int VAL_STRING = 0;
  private static final int VAL_INTEGER = 1;
  private static final int VAL_MAP = 2;
  private static final int VAL_LONG = 6;
  private static final int VAL_FLOAT = 7;
  private static final int VAL_DOUBLE = 8;
  private static final int VAL_BOOLEAN = 9;
  private static final int VAL_OBJECTARRAY = 17;

  private static void writeValue(Parcel out, int flags, Object v) {
    if (v == null || v == JSONObject.NULL) {
      out.writeInt(VAL_NULL);
    } else {
      Class<?> c = v.getClass();
      if (c == String.class) {
        out.writeInt(VAL_STRING);
        out.writeString((String) v);
      } else if (c == Long.class) {
        out.writeInt(VAL_LONG);
        out.writeLong((Long) v);
      } else if (c == Boolean.class) {
        out.writeInt(VAL_BOOLEAN);
        out.writeInt((Boolean) v ? 1 : 0);
      } else if (c == JSONObject.class) {
        out.writeInt(VAL_MAP);
        wrap((JSONObject) v).writeToParcel(out, flags);
      } else if (c == JSONArray.class) {
        out.writeInt(VAL_OBJECTARRAY);
        wrap((JSONArray) v).writeToParcel(out, flags);
      } else if (c == Double.class) {
        out.writeInt(VAL_DOUBLE);
        out.writeDouble((Double) v);
      } else if (c == Integer.class) {
        out.writeInt(VAL_INTEGER);
        out.writeInt((Integer) v);
      } else if (c == Float.class) {
        out.writeInt(VAL_FLOAT);
        out.writeFloat((Float) v);
      } else {
        throw new RuntimeException("Json: unable to marshal value " + v);
      }
    }
  }

  private static Object readValue(Parcel in) {
    int type = in.readInt();

    switch (type) {
      case VAL_NULL:
        return JSONObject.NULL;

      case VAL_STRING:
        return in.readString();

      case VAL_INTEGER:
        return in.readInt();

      case VAL_MAP:
        return ObjectWrapper.CREATOR.createFromParcel(in).unwrap();

      case VAL_LONG:
        return in.readLong();

      case VAL_FLOAT:
        return in.readFloat();

      case VAL_DOUBLE:
        return in.readDouble();

      case VAL_BOOLEAN:
        return in.readInt() != 0;

      case VAL_OBJECTARRAY:
        return ArrayWrapper.CREATOR.createFromParcel(in).unwrap();

      default:
        int off = in.dataPosition() - 4;
        throw new IllegalArgumentException("Json: unmarshalling unknown type code " + type + " at offset " + off);
    }
  }

  public static final class ObjectWrapper implements Parcelable {

    private final JSONObject mObject;

    private ObjectWrapper(JSONObject object) {
      mObject = object;
    }

    public JSONObject unwrap() {
      return mObject;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
      out.writeInt(mObject.length());
      final Iterator keys = mObject.keys();
      while (keys.hasNext()) {
        final String key = (String) keys.next();
        final Object val;
        try {
          val = mObject.get(key);
        } catch (JSONException e) {
          throw new RuntimeException("unexpected json error", e);
        }
        out.writeInt(VAL_STRING);
        out.writeString(key);
        writeValue(out, flags, val);
      }
    }

    public static final Parcelable.Creator<ObjectWrapper> CREATOR
        = new Parcelable.Creator<ObjectWrapper>() {
      @Override
      public ObjectWrapper createFromParcel(Parcel in) {
        JSONObject json = new JSONObject();
        int size = in.readInt();
        while (size > 0) {
          int keyType = in.readInt();
          if (keyType != VAL_STRING) {
            throw new IllegalArgumentException("Json: unmarshalling bad key of type " + keyType);
          }
          String key = in.readString();
          Object val = readValue(in);
          try {
            json.put(key, val);
          } catch (JSONException e) {
            throw new RuntimeException("unexpected json error", e);
          }
          size--;
        }
        return new ObjectWrapper(json);
      }

      @Override
      public ObjectWrapper[] newArray(int size) {
        return new ObjectWrapper[size];
      }
    };

  }

  public static final class ArrayWrapper implements Parcelable {

    private final JSONArray mArray;

    private ArrayWrapper(JSONArray array) {
      mArray = array;
    }

    public JSONArray unwrap() {
      return mArray;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
      out.writeInt(mArray.length());
      for (int i = 0; i < mArray.length(); i++) {
        writeValue(out, flags, mArray.opt(i));
      }
    }

    public static final Parcelable.Creator<ArrayWrapper> CREATOR
        = new Parcelable.Creator<ArrayWrapper>() {
      @Override
      public ArrayWrapper createFromParcel(Parcel in) {
        JSONArray array = new JSONArray();
        int size = in.readInt();
        while (size > 0) {
          array.put(readValue(in));
          size--;
        }
        return new ArrayWrapper(array);
      }

      @Override
      public ArrayWrapper[] newArray(int size) {
        return new ArrayWrapper[size];
      }
    };

  }

}
