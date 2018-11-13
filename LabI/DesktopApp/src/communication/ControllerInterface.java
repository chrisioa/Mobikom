package communication;

public interface ControllerInterface {


    /**
     * Stop all underlying servers and prepare for shutdown
     */
    public void shutdownServers();

    /**
     * This method
     *
     * @param text String to be added to received messages
     */
    public void updateTextArea(String text);

    /**
     * The singleton model needs a controller instance, which can be injected with this method.
     */
    public void registerWithModel();
}
