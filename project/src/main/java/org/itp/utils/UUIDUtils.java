package org.itp.utils;

	import java.nio.ByteBuffer;
	import java.util.UUID;

	public class UUIDUtils {
	    public static UUID bytesAsUUID(byte[] bytes) {
	        ByteBuffer bb = ByteBuffer.wrap(bytes);
	        long firstLong = bb.getLong();
	        long secondLong = bb.getLong();
	        return new UUID(firstLong, secondLong);
	    }

	    public static byte[] UUIDAsBytes(UUID uuid) {
	        ByteBuffer bb = ByteBuffer.allocate(16);
	        bb.putLong(uuid.getMostSignificantBits());
	        bb.putLong(uuid.getLeastSignificantBits());
	        return bb.array();
	    }
	}
