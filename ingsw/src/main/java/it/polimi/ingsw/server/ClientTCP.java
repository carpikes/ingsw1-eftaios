package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientTCP implements Client{
	private final OutputStream mOut;
	private final InputStream mIn;
	
	public ClientTCP(Socket s) throws IOException{
		mOut = s.getOutputStream();
		mIn = s.getInputStream();
	}
}
