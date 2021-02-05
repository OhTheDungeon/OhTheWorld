package cn.shadow.OhTheWorld.utils;

import java.util.Random;

public class SeedUtil {
	public static long getSeed(String seedString) {
		long seed;
		try {
			seed = Long.parseLong(seedString);
		} catch (NumberFormatException numberformatexception) {
            seed = (long) seedString.hashCode();
        }
		return seed;
	}
	
	public static String generateSeed(int targetStringLength) {
	    int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    return generatedString;
	}
}
