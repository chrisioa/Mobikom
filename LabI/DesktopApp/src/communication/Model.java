package communication;


/**
 * Singleton Model class that is used to make controller available everywhere, especially to tie cues from the GUI to the underlying, multithreaded server implementations and for instance stop them.
 */
public class Model {
    private static Model ourInstance = new Model();
    private ControllerInterface controller;


    /**
     * Retrieves model instance
     *
     * @return model
     */
    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
    }

    /**
     * Set the controller instance that can be used to stop all running server threads.
     *
     * @param controller
     */
    public void setController(ControllerInterface controller) {
        this.controller = controller;
    }

    public ControllerInterface getController() {
        return controller;
    }

    /**
     * Triggers the shutdown of all server threads.
     */
    public void stopServer() {
        controller.shutdownServers();
    }
}
