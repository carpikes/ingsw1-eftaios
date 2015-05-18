package it.polimi.ingsw.client;

/**
 * @author Alain Carlucci <alain.carlucci@mail.polimi.it>
 * @since  May 18, 2015
 */

public class Controller {
    private View mView;
    
    public void setView(View v) {
        mView = v;
    }
    
    public void run() {
        mView.run();
    }
}
