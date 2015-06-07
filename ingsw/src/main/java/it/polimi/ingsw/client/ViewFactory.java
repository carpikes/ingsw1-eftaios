package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLIView;
import it.polimi.ingsw.client.gui.GUIView;



public class ViewFactory {
    public static final int VIEW_GUI = 0;
    public static final int VIEW_CLI = 1;
    private static String[] tAssoc = {"GUI", "Console"};

    public static final String[] getViewList() {
        return tAssoc;
    }

    
    public static View getView(GameController c, int type) {
        switch(type) {
            case VIEW_GUI: return new GUIView( c );
            case VIEW_CLI: return new CLIView( c );
        }
        return null;
    }
}
