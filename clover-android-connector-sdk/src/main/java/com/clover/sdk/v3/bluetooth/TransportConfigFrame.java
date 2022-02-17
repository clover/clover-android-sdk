package com.clover.sdk.v3.bluetooth;

import java.util.HashMap;
import java.util.Map;

public class TransportConfigFrame extends BluetoothFrame {

  /*
   *  Format:
   *  [ ----------- Header 3 Bytes ----------- ]
   *  [    B0     ][    B1     ][     B2       ][    B3+    ]
   *  [ frameType ][  version  ][ transportMsg ][  payload  ]
   *
   *  For 'frameType', see BluetoothFrame.FrameType.
   *  For 'transportMessage', see 'TransportMessage' enum in this class
   *
   *  */


  public FrameType frameType;
  public Byte version;
  public TransportMessage transportMessage;
  public byte[] payload;
  public static final int headerSize = 3;

  /**
   * Build a TransportConfigFrame to send to the remove device to configure or inform of status changes.
   * @param transportMessage The type of message to send.
   * @param payload A byte array containing the payload to attach to this message.
   */
  public TransportConfigFrame(TransportMessage transportMessage, byte[] payload) {
    this.frameType = FrameType.TRANSPORT_CONFIG;
    this.version = 1;
    this.transportMessage = transportMessage;
    this.payload = payload;
  }

  /**
   * Build a TransportConfigFrame to send to the remove device to configure or inform of status changes. Helper for messages that don't need to send a payload.
   * @param transportMessage The type of message to send
   */
  public TransportConfigFrame(TransportMessage transportMessage) {
    this(transportMessage, null);
  }

  /**
   * Constructor primarily used to re-build config/setup messages that come across the bluetooth radio.
   * @param rawFrame A byte array. Usually direct output from the bluetooth radio.
   */
  public TransportConfigFrame(byte[] rawFrame) throws CloverBluetoothException {
    if (rawFrame.length > 2) {
      throw new CloverBluetoothException("TransportConfigFrame exception: requires at least 3 bytes to construct");
    }

    TransportMessage tm = TransportMessage.valueOf(rawFrame[2]);
    if (tm == null) {
      throw new CloverBluetoothException("TransportConfigFrame exception: unknown TransportMessage value");
    }

    frameType = FrameType.TRANSPORT_CONFIG;
    version = rawFrame[1];
    transportMessage = tm;

    //everything after the header is payload
    payload = new byte[rawFrame.length - headerSize];
    for (int i = 0; i < payload.length; i++) {
      payload[i] = rawFrame[i + headerSize]; // access rawData + offset so we skip the header
    }
  }

  @Override
  public byte[] asBytes() {
    byte[] totalPayload;
    if (payload != null && payload.length > 0) {
      totalPayload = new byte[headerSize + payload.length];
      System.arraycopy(payload, 0, totalPayload, headerSize, payload.length);
    } else {
      totalPayload = new byte[headerSize];
    }

    totalPayload[0] = this.frameType.getValue();
    totalPayload[1] = this.version;
    totalPayload[2] = this.transportMessage.getValue();

    return totalPayload;
  }

  @Override
  public byte getVersion() {
    return version;
  }

  @Override
  public FrameType getFrameType() {
    return frameType;
  }


  //Enum for the message types for the transport layer config messages
  public enum TransportMessage {

    /**
     * A notification to indicate that the connection is secured.
     */
    BONDED_AND_ENCRYPTED((byte) 0);

    //...future message types go here...


    private byte value;
    private static Map map = new HashMap<>();

    TransportMessage(byte value) {
      this.value = value;
    }

    static {
      for (TransportMessage transportMessage : TransportMessage.values()) {
        map.put(transportMessage.value, transportMessage);
      }
    }

    public static TransportMessage valueOf(byte transportMessage) {
      return (TransportMessage) map.get(transportMessage);
    }

    public byte getValue() {
      return value;
    }
  }
}
