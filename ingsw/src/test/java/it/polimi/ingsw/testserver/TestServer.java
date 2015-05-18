package it.polimi.ingsw.testserver;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;

import it.polimi.ingsw.client.network.TCPConnection;
import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.network.GameCommands;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.server.ClientConnTCP;
import it.polimi.ingsw.server.Server;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 18, 2015
 */

public class TestServer {
    /**
     * @throws java.lang.Exception Throws exception if something is wrong
     */
    @BeforeClass
    public static void setUp() throws Exception {
        new Thread(new Runnable() { public void run() { Server.getInstance().runServer(); } }).start();
        while(!Server.getInstance().isUp()) {
            Thread.sleep(10);
        }
    }

    /**
     * Test method for {@link it.polimi.ingsw.server.Server#addClient(it.polimi.ingsw.server.ClientConn)}.
     */
    @Test
    public void testClient() {
        ClientConnTest conn = new ClientConnTest();
        ClientConnTest conn2 = new ClientConnTest(); 
        
        conn.run();
        conn2.run();
        
        int clientsBefore = Server.getInstance().getConnectedClients();
        assertTrue(Server.getInstance().addClient(conn));
        assertTrue(Server.getInstance().addClient(conn2));
        
        // Emulate and invalid packet
        conn.emulateReadPacket(new NetworkPacket(GameCommands.CMD_PING));
        
        // Try to change username with an empty one
        conn.emulateReadPacket(new NetworkPacket(GameCommands.CMD_CS_USERNAME));
        assertFalse(conn.exposeClient().hasUsername());
        
        // Try a good username
        conn.emulateReadPacket(new NetworkPacket(GameCommands.CMD_CS_USERNAME, "test"));
        assertTrue(conn.exposeClient().hasUsername());
        
        // Try an already used username
        conn2.emulateReadPacket(new NetworkPacket(GameCommands.CMD_CS_USERNAME, "test"));
        assertFalse(conn2.exposeClient().hasUsername());
        
        // Disconnect clients
        conn.emulateDisconnect();
        conn2.emulateDisconnect();
        
        assertEquals(Server.getInstance().getConnectedClients(), clientsBefore);
    }
    
    @Test
    public void testTCP() {
        Socket s;
        Map<String, Object> paramsConfig = new TreeMap<String, Object>();
        paramsConfig.put("Host", "localhost");
        paramsConfig.put("Port", Config.SERVER_TCP_LISTEN_PORT);

        TCPConnection conn = new TCPConnection();
        conn.setConfiguration(paramsConfig);
        assertEquals(conn.isOnline(), false);
        try {
            conn.connect();
        } catch (IOException e) {
            fail("Cannot connect to server: " + e.toString());
        }
        assertEquals(conn.isOnline(), true);
        conn.disconnect();
        assertEquals(conn.isOnline(), false);
    }

    /**
     * @throws java.lang.Exception Throws exception if something is wrong
     */
    @AfterClass
    public static void tearDown() throws Exception {
        
        System.out.println("------------------Tearing down------------------");
        Server.getInstance().tearDown();
        while(!Server.getInstance().isDown()) {
            Thread.sleep(10);
        }
    }
}
