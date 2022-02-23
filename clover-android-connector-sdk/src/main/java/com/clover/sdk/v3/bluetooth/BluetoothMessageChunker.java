package com.clover.sdk.v3.bluetooth;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Helper class for working with the message chunks that traverse the bluetooth connection. Necessary
 * since bluetooth packets have an MTU that requires that our RemoteMessages are broken up into smaller chunks.
 */
public class BluetoothMessageChunker {
  private int mtu = 23;
  private int messageId = 0;

  /**
   * Holds arrays (keyed by messageId) of the chunks we've gathered so far for each messageID
   */
  private final HashMap<Integer, RemoteMessageFrame[]> messagesBuffer = new HashMap<>();

  /**
   * Stores the individual message chunks until a full message can be reconstructed
   * @param chunk The original message's byte array, directly from the bluetooth characteristic
   * @return A string object representing the re-constructed RemoteMessage, or null
   */
  public String addMessageChunk(byte[] chunk) {
    RemoteMessageFrame btMsgChunk = new RemoteMessageFrame(chunk);

    if (btMsgChunk.totalChunks == 1) { //this is the only chunk for this messageID, don't bother storing
      //only one message, return immediately
      return new String(btMsgChunk.payload);
    }

    //see if we've already saved a message chunk with this messageId...
    RemoteMessageFrame[] messageChunks = messagesBuffer.get(btMsgChunk.messageId);

    //...we haven't, so create a new store for this messageId, and save this chunk at its index
    if (messageChunks == null) {
      messageChunks = new RemoteMessageFrame[btMsgChunk.totalChunks];
      messageChunks[btMsgChunk.chunk] =  btMsgChunk;

      messagesBuffer.put(btMsgChunk.messageId, messageChunks);

      return null; //go ahead and return now since we know that we're still waiting on future messages
    } else { //...did have previous chunks for this messageId, so add this current one to the list
      messageChunks[btMsgChunk.chunk] = btMsgChunk;
      messagesBuffer.put(btMsgChunk.messageId, messageChunks);
    }

    //returns either a completely reconstructed string representation of a RemoteMessage, or null
    String messageString = reconstructChunks(messageChunks);
    if (messageString == null) {
      return null; //incomplete message, return to continue waiting
    } else {
      messagesBuffer.remove(btMsgChunk.messageId); //we're done with this message, so clear it out of the buffer
      return messageString; //return full message string
    }
  }

  /**
   * Helper function that checks to see if we have all the necessary chunks to re-build a RemoteMessage
   * @param chunks The array of message chunks that we've accumulated thus far
   * @return A string representing the reconstructed RemoteMessage, or null if we haven't accumulated all messages yet
   */
  private String reconstructChunks(RemoteMessageFrame[] chunks) {
    int payloadSize = 0; //track the total number of bytes needed for the full RemoteMessage string

    //loop through all accumulated chunks, verifying we have them all and calculating the total size
    for (RemoteMessageFrame btMsgChunk: chunks) {
      if (btMsgChunk == null) {
        return null; //we're still missing an element, so we'll continue waiting
      } else {
        payloadSize += btMsgChunk.payload.length; //track the total bytes from all chunks (so we can consolidate later)
      }
    }

    int offset = 0;
    byte[] fullPayload = new byte[payloadSize];

    //combine all of the chunk payloads
    for (RemoteMessageFrame btMsgChunk: chunks) {
      System.arraycopy(btMsgChunk.payload, 0, fullPayload, offset, btMsgChunk.payload.length);
      offset += btMsgChunk.payload.length;
    }

    return new String(fullPayload); //return the byte array as the full RemoteMessage string
  }

  /**
   * Chunk a RemoteMessage string into smaller messages that will fit inside the provided {@link BluetoothMessageChunker#mtu}
   *
   * Convenience wrapper around the {@link BluetoothMessageChunker#chunk(byte[])} method, that takes
   * a RemoteMessage string instead of a byte array
   * @param string The RemoteMessage string to be chunked
   * @return A collection of bluetooth frames
   */
  public ConcurrentLinkedQueue<BluetoothFrame> chunk(String string) {
    return chunk(string.getBytes());
  }

  /**
   * Chunk a RemoteMessage payload into smaller messages that will fit inside the provided {@link BluetoothMessageChunker#mtu}
   * @param payload The raw data to be sent
   * @return A collection of bluetooth frames
   */
  public ConcurrentLinkedQueue<BluetoothFrame> chunk(byte[] payload) {
    int maxPayloadSize = getMtu() - RemoteMessageFrame.HEADER_SIZE;
    int totalChunks = payload.length / maxPayloadSize;
    int remainder = payload.length % maxPayloadSize;
    if (remainder > 0) {
      totalChunks++;
    }

    ConcurrentLinkedQueue<BluetoothFrame> messageQueue = new ConcurrentLinkedQueue<>();
    int offset = 0;
    int messageId = getMessageId();
    int chunk = 0;

    while (offset < payload.length) {
      if ((payload.length - offset) > maxPayloadSize) {  //need to chunk if remaining data from remote message is larger than max payload for each BT packet
        byte[] payloadChunk = new byte[maxPayloadSize];
        System.arraycopy(payload, offset, payloadChunk, 0, maxPayloadSize);
        RemoteMessageFrame messageChunk = new RemoteMessageFrame(payloadChunk, messageId, totalChunks, chunk);
        messageQueue.add(messageChunk);
        offset += maxPayloadSize;
      } else {                                //can fit the rest of the payload in one packet
        byte[] payloadChunk = new byte[payload.length - offset];
        System.arraycopy(payload, offset, payloadChunk, 0, payload.length - offset);
        RemoteMessageFrame messageChunk = new RemoteMessageFrame(payloadChunk, messageId, totalChunks, chunk);
        messageQueue.add(messageChunk);
        offset = payload.length; //setting offset equal to payload's length exits us out of the while-loop
      }

      chunk++;
    }

    return messageQueue;
  }



  //Getters and setters
  public int getMtu() {
    return mtu;
  }

  public void setMtu(int mtu) {
    this.mtu = mtu - 5; //give a little buffer for opcodes and ATT handle: https://stackoverflow.com/a/45556889/2347298
  }

  private int getMessageId() {
    int id = this.messageId;
    if (id == Integer.MAX_VALUE) {
      this.messageId = 0;
    } else {
      this.messageId++;
    }

    return id;
  }
}
