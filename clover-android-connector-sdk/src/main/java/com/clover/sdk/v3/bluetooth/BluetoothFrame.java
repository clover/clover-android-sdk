package com.clover.sdk.v3.bluetooth;


//Message types for the RemoteMessageFrame
public abstract class BluetoothFrame {

  /**
   * Used when we need a byte representation of the message to send over the radio to the remote device
   * @return Byte array representation of the frame ready to send
   */
  public abstract byte[] asBytes();

  /**
   *
   * @return The version of the Bluetooth frame
   */
  public abstract byte getVersion();

  /**
   *
   * @return The concrete type of the Bluetooth Frame (currently only TransportConfigFrame or RemoteMessageFrame)
   */
  public abstract FrameType getFrameType();

  //Message types for the Bluetooth messages
  public enum FrameType {
    TRANSPORT_CONFIG((byte) 0),
    REMOTE_CHUNK((byte) 1);

    private byte value;

    FrameType(byte value) {
      this.value = value;
    }

    public byte getValue() {
      return value;
    }
  }
}
