package it.polimi.ingsw.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/** Load resources from jar or file
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public class ResourceLoader {

    /** Singleton Holder */
    private static class Holder {
        /** The singleton instance */
        private static final ResourceLoader INSTANCE = new ResourceLoader();
        
        /** Private constructor */
        private Holder() {
            /** Unused */
        }
    }
    
    /** Private constructor */
    private ResourceLoader() {
        
    }
    
    /** Returns the current server instance
     * 
     * @return The instance
     */
    public static ResourceLoader getInstance() {
        return Holder.INSTANCE;
    }
    
    /** Load resource
     * 
     * @param path Resource path
     * @return The resource input stream
     * @throws IOException File not found
     */
    public InputStream loadResource(String path) throws IOException {
        InputStream is;
        is = getClass().getResourceAsStream("/" + path);
        if( is == null)
            is = new FileInputStream(path);
        return is;
    }
}
