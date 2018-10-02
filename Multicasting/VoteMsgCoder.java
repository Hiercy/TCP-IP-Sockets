package Multicasting;

import java.io.IOException;

public interface VoteMsgCoder {
	/*
	 * The toWire() converts the vote message to sequence of bytes
	 * according to a particular protocol
	 */
	byte[] toWire(VoteMsg msg) throws IOException;
	
	/*
	 * The fromWire() method parses a given sequence of bytes 
	 * according to the same  protocol
	 */
	VoteMsg fromWire(byte[] message) throws IOException;
}
