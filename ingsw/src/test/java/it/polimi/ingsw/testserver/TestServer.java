package it.polimi.ingsw.testserver;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;

import it.polimi.ingsw.client.network.TCPConnection;
import it.polimi.ingsw.game.GameCommand;
import it.polimi.ingsw.game.config.Config;
import it.polimi.ingsw.game.network.NetworkPacket;
import it.polimi.ingsw.server.ClientConnTCP;
import it.polimi.ingsw.server.Server;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @since  May 18, 2015
 */

public class TestServer {
    /**
     * @throws java.lang.Exception Throws exception if something is wrong
     */
    @Ignore
    @BeforeClass
    public static void setUp() throws Exception {
        new Thread(new Runnable() { public void run() { Server.getInstance().runServer(); } }).start();
        while(!Server.getInstance().isUp()) {
            Thread.sleep(10);
        }
    }
    
    @Ignore
    @Test
    public void testGame() {
        
        ClientConnMock[] conns = new ClientConnMock[Config.GAME_MAX_PLAYERS];
        
        for(int i=0;i<Config.GAME_MAX_PLAYERS;i++) {
            conns[i] = new ClientConnMock();
            conns[i].run();
            assertTrue(Server.getInstance().addClient(conns[i]));
            conns[i].emulateReadPacket(new NetworkPacket(GameCommand.CMD_CS_USERNAME, "test_" + i));
            assertTrue(conns[i].exposeClient().hasUsername());
        }
        try {
            Thread.sleep(500);
        } catch( InterruptedException e) { }
        for(ClientConnMock i : conns) {
            assertTrue(i.exposeClient().hasUsername());
            i.emulateReadPacket(new NetworkPacket(GameCommand.CMD_PING));
        }

        for(ClientConnMock i : conns)
            i.emulateDisconnect();
        
    }
    
    @Ignore
    public void testLoad() {
        TCPConnection[] conns = new TCPConnection[Config.SERVER_MAX_CLIENTS];

        int clientsBefore = Server.getInstance().getConnectedClients();
        
        assertEquals(clientsBefore, 0);
        
        try {
            for(int i = 0; i<conns.length;i++) {
                conns[i] = new TCPConnection();
                conns[i].setHost("localhost");
                try {
                    conns[i].connect();
                    //System.out.println("Connecting [" + i + "]");
                    double maxSecs = 3;
                    
                    while(maxSecs > 0 && Server.getInstance().getConnectedClients() != i + 1 ) {
                        Thread.sleep(50);
                        maxSecs -= 0.050;
                    }
                    assertEquals(Server.getInstance().getConnectedClients(), i + 1);
                    Thread.sleep(500);
                } catch (IOException e) {
                    fail("Cannot connect to server: " + e.toString());
                }
            }
        
        
            for(int i = 0;i<conns.length;i++) {
                assertTrue(conns[i].isOnline());
                conns[i].disconnect();
                double maxSecs = 3;
                
                while(maxSecs > 0 && Server.getInstance().getConnectedClients() != Config.SERVER_MAX_CLIENTS - i) {
                    Thread.sleep(50);
                    maxSecs -= 0.050;
                }
                //System.out.println("Disconnecting [" + i + "]");
                assertEquals(Server.getInstance().getConnectedClients(),Config.SERVER_MAX_CLIENTS - i);
                Thread.sleep(500);
            }
        
        } catch( InterruptedException e) { }
        assertEquals(Server.getInstance().getConnectedClients(), 0);
    }

    /**
     * Test method for {@link it.polimi.ingsw.server.Server#addClient(it.polimi.ingsw.server.ClientConn)}.
     */
    @Ignore
    @Test
    public void testClient() {
        ClientConnMock conn = new ClientConnMock();
        ClientConnMock conn2 = new ClientConnMock(); 
        
        conn.run();
        conn2.run();
        
        int clientsBefore = Server.getInstance().getConnectedClients();
        assertTrue(Server.getInstance().addClient(conn));
        assertTrue(Server.getInstance().addClient(conn2));
        
        // Emulate and invalid packet
        conn.emulateReadPacket(new NetworkPacket(GameCommand.CMD_PING));
        
        // Try to change username with an empty one
        conn.emulateReadPacket(new NetworkPacket(GameCommand.CMD_CS_USERNAME));
        assertFalse(conn.exposeClient().hasUsername());
        
        // Try a good username
        conn.emulateReadPacket(new NetworkPacket(GameCommand.CMD_CS_USERNAME, "test"));
        assertTrue(conn.exposeClient().hasUsername());
        
        // Try an already used username
        conn2.emulateReadPacket(new NetworkPacket(GameCommand.CMD_CS_USERNAME, "test"));
        assertFalse(conn2.exposeClient().hasUsername());
        
        // Disconnect clients
        conn.emulateDisconnect();
        conn2.emulateDisconnect();
        
        assertEquals(Server.getInstance().getConnectedClients(), clientsBefore);
    }
    
    @Ignore
    @Test
    public void testTCP() {
        TCPConnection conn = new TCPConnection();
        conn.setHost("localhost");
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
    @Ignore
    @AfterClass
    public static void tearDown() throws Exception {
        //System.out.println("------------------Tearing down------------------");
        Server.getInstance().tearDown();
        while(!Server.getInstance().isDown()) {
            Thread.sleep(10);
        }
    }
}
