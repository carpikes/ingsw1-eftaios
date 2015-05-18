package it.polimi.ingsw.testserver;

import static org.junit.Assert.*;
import it.polimi.ingsw.server.Server;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 18, 2015
 */

public class TestServer {
    private ClientConnTest conn;
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        conn = new ClientConnTest();
        new Thread(new Runnable() { public void run() { Server.getInstance().runServer(); } }).start();
    }

    /**
     * Test method for {@link it.polimi.ingsw.server.Server#addClient(it.polimi.ingsw.server.ClientConn)}.
     */
    @Test
    public void testClient() {
        assertTrue(Server.getInstance().addClient(conn));
        conn.doDisconnect();
        assertEquals(Server.getInstance().getConnectedClients(), 0);
    }

}
