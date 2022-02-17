package com.clover.sdk.v3.bluetooth;

/**
 * Represents a single byte array as it came across the air on the bluetooth radio, or will be sent
 * over the bluetooth radio. May be a complete RemoteMessage if the message is smaller than the MTU
 * of the bluetooth connection, or will be one of multiple chunks.
 */
public class RemoteMessageFrame extends BluetoothFrame {

  /*
   *  Format:
   *  [ ----------------------- Header 14 Bytes ----------------------- ]
   *  [    B0     ][    B1     ][ B2 --- B5 ][ B6 --- B9 ][ B10 -- B13 ][   B14+  ]
   *  [ frameType ][  version  ][ messageId ][totalChunks][   chunk    ][ payload ]
   *
   *  For 'frameType', see BluetoothFrame.FrameType.
   *  */

  public FrameType frameType; //sized as a byte
  public byte version;
  public int messageId;
  public int totalChunks;
  public int chunk;
  public byte[] payload;
  public static final int HEADER_SIZE = 14;

  /**
   * Constructor primarily used to re-build remote messages from the chunks that come across the bluetooth radio.
   * @param rawFrame A byte array. Usually direct output from the bluetooth radio.
   */
  public RemoteMessageFrame(byte[] rawFrame) {
    frameType = FrameType.REMOTE_CHUNK;
    version = rawFrame[1];
    messageId = ((rawFrame[2] & 0xFF)) | ((rawFrame[3] & 0xFF) << 8) |
                ((rawFrame[4] & 0xFF) <<  16) | ((rawFrame[5] & 0xFF) <<  24);
    totalChunks = ((rawFrame[6] & 0xFF)) | ((rawFrame[7] & 0xFF) << 8) |
                  ((rawFrame[8] & 0xFF) <<  16) | ((rawFrame[9] & 0xFF) <<  24);
    chunk = ((rawFrame[10] & 0xFF)) | ((rawFrame[11] & 0xFF) << 8) |
            ((rawFrame[12] & 0xFF) <<  16) | ((rawFrame[13] & 0xFF) <<  24);

    //Log.d(LOG_TAG, "messageID: " + messageId + " -- totalChunks: " + totalChunks + " -- chunk: " + chunk);

    //everything after the header is payload
    payload = new byte[rawFrame.length - HEADER_SIZE];

    // access rawData + offset so we skip the header
    System.arraycopy(rawFrame, 14, payload, 0, payload.length);
  }

  /**
   * Create a RemoteMessageFrame from the passed-in parameters. Primarily used while converting a remote message to chunks to send over the bluetooth radio.
   * @param payload The actual byte array of content to send.
   * @param messageId An ID used to tie multiple RemoteMessage chunks together.
   * @param totalChunks The total number of chunks required to represent a single RemoteMessage.
   * @param chunk The specific chunk represented by this frame.
   */
  public RemoteMessageFrame(byte[] payload, int messageId, int totalChunks, int chunk) {
    this.frameType = FrameType.REMOTE_CHUNK;
    this.version = (byte) 1;
    this.payload = payload;
    this.messageId = messageId;
    this.totalChunks = totalChunks;
    this.chunk = chunk;
  }

  @Override
  public byte[] asBytes() {
    byte[] totalPayload = new byte[HEADER_SIZE + this.payload.length];

    //message type
    totalPayload[0] = frameType.getValue();

    //message version
    totalPayload[1] = version;

    //messageId
    totalPayload[2] = (byte) (messageId >> 24);
    totalPayload[3] = (byte) (messageId >> 16);
    totalPayload[4] = (byte) (messageId >> 8);
    totalPayload[5] = (byte) (messageId);

    //totalChunks
    totalPayload[6] = (byte) (totalChunks >> 24);
    totalPayload[7] = (byte) (totalChunks >> 16);
    totalPayload[8] = (byte) (totalChunks >> 8);
    totalPayload[9] = (byte) (totalChunks);

    //chunk
    totalPayload[10] = (byte) (chunk >> 24);
    totalPayload[11] = (byte) (chunk >> 16);
    totalPayload[12] = (byte) (chunk >> 8);
    totalPayload[13] = (byte) (chunk);

    //payload
    System.arraycopy(payload, 0, totalPayload, HEADER_SIZE, payload.length);

    return totalPayload;
  }

  @Override
  public byte getVersion() {
    return version;
  }

  @Override
  public FrameType getFrameType() {
    return FrameType.REMOTE_CHUNK;
  }
}
