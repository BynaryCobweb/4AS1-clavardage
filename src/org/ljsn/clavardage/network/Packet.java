package org.ljsn.clavardage.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

public abstract class Packet implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3207904235185142071L;
	
	public enum Type {
		HELLO(PacketHello.class),
		HELLO_BACK(PacketHelloBack.class),
		MESSAGE(PacketMessage.class),
		GOODBYE(PacketMessage.class);
		
		public final Class<? extends Packet> packetClass;
		
		Type(Class<? extends Packet> c) {
			packetClass = c;
		}
	}
	
	
	private final Type type;
	
	protected Packet(Type type) {
		this.type = type;
	}


	public static Packet readPacket(ByteBuffer from) {
		
		Packet p = null;
		
		byte[] array = new byte[from.remaining()];
		from.get(array);
		ByteArrayInputStream bis = new ByteArrayInputStream(array);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bis);
			Object obj = ois.readObject();
			p = (Packet) obj;
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		/*
		
		// TODO check that type value is good, raise exception if there is any problem.
		Type type = Type.values()[from.getInt()];
		Packet p = null;
		try {
			p = type.packetClass.getConstructor().newInstance();
		} catch (Exception e) {
			throw new Error("Bad instantiation of packet " + type.packetClass.getName() + " : Missing constructor with no parameters");
		}
		// TODO check exception from this method
		p.readContent(from);*/
		return p;
	}
	
	public void write(ByteBuffer to) {
		/*to.putInt(this.type.ordinal());
		writeContent(to);*/
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		to.put(bos.toByteArray());
	}
	
	protected void writeContent(ByteBuffer to) {
		throw new UnsupportedOperationException("not implemented");
	}
	protected void readContent(ByteBuffer from) {
		throw new UnsupportedOperationException("not implemented");
	}
}
