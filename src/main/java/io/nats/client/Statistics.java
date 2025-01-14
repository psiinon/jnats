package io.nats.client;

import java.util.concurrent.atomic.AtomicLong;

// Tracks various stats received and sent on this connection,
// including counts for messages and bytes.
public class Statistics implements Cloneable {

	private AtomicLong inMsgs = new AtomicLong();
	private AtomicLong outMsgs = new AtomicLong();
	private AtomicLong inBytes = new AtomicLong();
	private AtomicLong outBytes = new AtomicLong();
	private AtomicLong reconnects = new AtomicLong();
	
	Statistics () {
		
	}
	
    // deep copy contructor
    Statistics(Statistics obj)
    {
        this.inMsgs = obj.inMsgs;
        this.inBytes = obj.inBytes;
        this.outBytes = obj.outBytes;
        this.outMsgs = obj.outMsgs;
        this.reconnects = obj.reconnects;
    }

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void clear() {
		this.inBytes.set(0L);
		this.inMsgs.set(0L);;
		this.outBytes.set(0L);;
		this.outMsgs.set(0L);

	}

	/**
	 * @return the number of messages that have been 
	 * received on this Connection.
	 */
	public synchronized long getInMsgs() {
		return inMsgs.get();
	}

	/**
	 * Increments the number of messages received on 
	 * this connection. 
	 */
	synchronized long incrementInMsgs() {
		return inMsgs.incrementAndGet();
	}

	/**
	 * @return the number of messages published on 
	 * this Connection.
	 */
	public synchronized long getOutMsgs() {
		return outMsgs.get();
	}

	synchronized long incrementOutMsgs() {
		return outMsgs.incrementAndGet();
	}

	/**
	 * @return the number of bytes received on this
	 * Connection.
	 */
	public synchronized long getInBytes() {
		return inBytes.get();
	}
	
	/*
	 * Increments the number of bytes received. 
	 */
	synchronized long incrementInBytes(long amount) {
		return inBytes.addAndGet(amount);
	}

	/**
	 * @return the number of bytes that have been output
	 * on this Connection.
	 */
	public synchronized long getOutBytes() {
		return outBytes.get();
	}
	/*
	 * Increments the number of bytes output 
	 */
	synchronized long incrementOutBytes(long delta) {
		return outBytes.addAndGet(delta);
	}

	/**
	 * @return the number of times this Connection has 
	 * reconnected.
	 */
	public synchronized long getReconnects() {
		return reconnects.get();
	}
	
	synchronized long incrementReconnects() {
		return reconnects.incrementAndGet();
	}

}

