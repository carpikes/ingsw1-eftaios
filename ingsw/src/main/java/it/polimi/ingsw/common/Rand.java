package it.polimi.ingsw.common;

import java.security.SecureRandom;

/** Secure random generator
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 * @since Jun 14, 2015
 */
public class Rand {
    /** Random source with high entropy */
    private static SecureRandom randSource = new SecureRandom();
    
    /** Private constructor */
    private Rand() {
        /** Unused. */
    }
    
    /** Next int
     * 
     * @return A random integer
     */
    public static int nextInt() {
        return randSource.nextInt();
    }
    
    /** Next int between 0 and bound
     * 
     * @param bound Upper limit
     * @return A random integer
     */
    public static int nextInt(int bound) {
        return randSource.nextInt(bound);
    }
}
