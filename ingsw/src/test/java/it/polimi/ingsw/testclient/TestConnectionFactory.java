package it.polimi.ingsw.testclient;

import static org.junit.Assert.assertTrue;
import it.polimi.ingsw.client.network.ConnectionFactory;
import it.polimi.ingsw.client.network.RMIConnection;
import it.polimi.ingsw.client.network.TCPConnection;

import org.junit.Test;

/** Test Connection Factory
 * 
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese93@gmail.com)
 * @since 16 Jun 2015
 */
public class TestConnectionFactory {

    /**
     * Test get connection switch
     */
    @Test
    public void testGetConnection() {        
        assertTrue ( ConnectionFactory.getConnection( ConnectionFactory.CONNECTION_RMI ) instanceof RMIConnection );    
        assertTrue ( ConnectionFactory.getConnection( ConnectionFactory.CONNECTION_TCP ) instanceof TCPConnection );    
    }
    
    /**
     *  Test internal static array with all the names of the connections
     */
    @Test
    public void testStringArray() {
        String[] list = ConnectionFactory.getConnectionList();
        assertTrue ( list[ ConnectionFactory.CONNECTION_RMI ].equals("RMI") );
    }

}
