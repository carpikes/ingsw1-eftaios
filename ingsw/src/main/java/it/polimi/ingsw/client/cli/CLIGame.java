package it.polimi.ingsw.client.cli;

import java.util.Map;

import it.polimi.ingsw.client.network.Connection;
import it.polimi.ingsw.client.network.ConnectionFactory;

class CLIGame {
	private Connection mConn;

	private static void banner() {
		System.out.println("*******************************************************************************");
		System.out.println("*******************************************************************************");
		System.out.println("***  ______        _____       _____                    _____       ______  ***");  
		System.out.println("*** |  ____|      / ____|     / ____|        /\\        |  __ \\     |  ____| ***");  
		System.out.println("*** | |__        | (___      | |            /  \\       | |__) |    | |__    ***");  
		System.out.println("*** |  __|        \\___ \\     | |           / /\\ \\      |  ___/     |  __|   ***");  
		System.out.println("*** | |____       ____) |    | |____      / ____ \\     | |         | |____  ***");  
		System.out.println("*** |______|     |_____/      \\_____|    /_/    \\_\\    |_|         |______| ***");  
		System.out.println("***  ___ ___  ___  __  __    _____ _  _ ___      _   _    ___ ___ _  _ ___  ***");
		System.out.println("*** | __| _ \\/ _ \\|  \\/  |  |_   _| || | __|    /_\\ | |  |_ _| __| \\| / __| ***");
		System.out.println("*** | _||   / (_) | |\\/| |    | | | __ | _|    / _ \\| |__ | || _|| .` \\__ \\ ***");
		System.out.println("*** |_| |_|_\\\\___/|_|  |_|    |_| |_||_|___|  /_/ \\_\\____|___|___|_|\\_|___/ ***");
		System.out.println("***  ___  _  _    ___   _   _  _____  ___  ___    ___  ___   _    ___  ___  ***");
		System.out.println("*** |_ _|| \\| |  / _ \\ | | | ||_   _|| __|| _ \\  / __|| _ \\ /_\\  / __|| __| ***");
		System.out.println("***  | | | .` | | (_) || |_| |  | |  | _| |   /  \\__ \\|  _// _ \\| (__ | _|  ***");
		System.out.println("*** |___||_|\\_|  \\___/  \\___/   |_|  |___||_|_\\  |___/|_| /_/ \\_\\\\___||___| ***");
		System.out.println("***                                                                         ***");
		System.out.println("*******************************************************************************");
		System.out.println("*******************************************************************************");
	}

	public CLIGame() {
		CLIGame.banner();
		
		Map<Integer,String> mConnectionList = ConnectionFactory.getConnectionList();
		
		Integer min = null, max = null;
		for(Map.Entry<Integer, String> m : mConnectionList.entrySet()) {
			if(min == null) min = m.getKey();
			if(max == null) max = m.getKey();
			min = Math.min(m.getKey(), min);
			max = Math.max(m.getKey(), max);
			IO.write(m.getKey() + ") " + m.getValue());
		}
		
		int connType = IO.readRangeInt(min, max);
		
		mConn = ConnectionFactory.getConnection(connType);
	}
}
