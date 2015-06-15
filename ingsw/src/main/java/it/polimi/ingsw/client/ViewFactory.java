package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLIView;
import it.polimi.ingsw.client.gui.GUIView;
import it.polimi.ingsw.exception.InvalidViewException;

/** View Factory
 * @author Alain Carlucci (alain.carlucci@mail.polimi.it)
 * @author Michele Albanese (michele.albanese@mail.polimi.it)
 */
public class ViewFactory {

    /** GUI id */
    public static final int VIEW_GUI = 0;

    /** CLI id */
    public static final int VIEW_CLI = 1;

    /** Names */
    private static String[] mAssoc = {"GUI", "Console"};

    /** Private constructor */
    private ViewFactory() {
        /** Nothing here */
    }

    /** Get the view list
     *
     * @return View list
     */
    public static final String[] getViewList() {
        return mAssoc;
    }

    /** Get the chosen view
     *
     * @param c Game Controller
     * @param type View id
     * @return Get the view
     */
    public static View getView(GameController c, int type) {
        switch(type) {
            case VIEW_GUI: return new GUIView( c );
            case VIEW_CLI: return new CLIView( c );
            default:       throw new InvalidViewException("Invalid view selected");
        }
    }
}
