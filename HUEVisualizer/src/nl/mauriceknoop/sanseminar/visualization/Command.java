package nl.mauriceknoop.sanseminar.visualization;


/**
 * Created by Maurice on 11-1-2017.
 *
 * A command contains an Event and an Action. If the specified {@link Event} occurs, the specified {@link Action} should
 * be undertaken. If no {@link Event} is specified, the action should be undertaken as soon as possible possible.
 */
public class Command {

    private Action action;

    private Event event;


    /**
     * Create a new Command that should lead to the execution of the given {@link Action} if the given {@link Event} is fired.
     * @param action The {@link Action} that should be executed when the given {@link Event} is fired.
     * @param event The {@link Event} that should lead to the execution of the given {@link Event} when it is fired.
     */
    public Command(Event event,Action action) {
        this.setEvent(event);
        this.setAction(action);
    }



    /**
     * Obtain the Action to execute when an event of this Command is fired.
     * @return The Action to execute. The returned Action is never null.
     */
    public Action getAction(){
        return this.action;
    }

    /**
     * Set the Action to execute when an Event of this Command is fired.
     * @param action The Action to execute. Must not be null
     * @throws IllegalArgumentException If the given Action is null.
     */
    public void setAction(Action action){
        if(action == null)
            throw new IllegalArgumentException("An Action must be specified. Null is not valid.");
        this.action = action;
    }

    /**
     * Get the {@link Event} that causes this Command to be fired
     * @return The {@link Event} that is registered to fire this Command. The returned {@link Event} is never null.
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Get the {@link Event} that causes this Command to be fired
     * @return The {@link Event} that is registered to fire this Command.
     * @throws IllegalArgumentException if the given {@link Event} is null.
     */
    public void setEvent(Event event) {
        if(event == null)
            throw new IllegalArgumentException("Event must not be null.");
        this.event = event;
    }
}
