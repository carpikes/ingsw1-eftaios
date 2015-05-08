package it.polimi.ingsw.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTCPConn extends ClientConn{
	private final PrintWriter mOut;
	private final BufferedReader mIn;
	private final Socket mSocket;
	
	public ClientTCPConn(Socket socket) throws IOException{
		mOut = new PrintWriter(socket.getOutputStream());
		mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		mSocket = socket;
	}

	public void run() {
		try {
			while(mSocket.isConnected()) {
				String msg = mIn.readLine().trim();
				mClient.handleMessage(msg);
			}
		} catch(IOException e) {
			// Connection closed. Nothing important here
		}
		
		mClient.handleDisconnect();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
}
