package nl.mauriceknoop.sanseminar.visualization;


/**
 * Created by Maurice on 11-1-2017.
 *
 * A command contains an Event and an Action. If the specified {@link EventMatcher} occurs, the specified {@link Action} should
 * be undertaken. If no {@link EventMatcher} is specified, the action should be undertaken as soon as possible possible.
 */
public class Command implements IEventMatcher{

    private Action action;

    private EventMatcher eventMatcher;


    /**
     * Create a new Command that should lead to the execution of the given {@link Action} if the given {@link EventMatcher} is fired.
     * @param action The {@link Action} that should be executed when the given {@link EventMatcher} is fired.
     * @param eventMatcher The {@link EventMatcher} that should lead to the execution of the given {@link EventMatcher} when it is fired.
     */
    public Command(EventMatcher eventMatcher, Action action) {
        this.setEventMatcher(eventMatcher);
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
     * Get the {@link EventMatcher} that causes this Command to be fired
     * @return The {@link EventMatcher} that is registered to fire this Command. The returned {@link EventMatcher} is never null.
     */
    public EventMatcher getEventMatcher() {
        return eventMatcher;
    }

    /**
     * Get the {@link EventMatcher} that causes this Command to be fired
     * @return The {@link EventMatcher} that is registered to fire this Command.
     * @throws IllegalArgumentException if the given {@link EventMatcher} is null.
     */
    public void setEventMatcher(EventMatcher eventMatcher) {
        if(eventMatcher == null)
            throw new IllegalArgumentException("Event must not be null.");
        this.eventMatcher = eventMatcher;
    }

    /**
     * <p>
     * Indicates whether firing of the given event should lead to this Command firing.
     * This is to handle some scenarios in which the Event is less strict than the fired event.
     * </p>
     *
     * @param event The event that could fire
     * @return Whether or not the given event would cause this Fireable object to be fired as well.
     */
    @Override
    public boolean matches(Event event) {
        return this.getEventMatcher().matches(event);
    }
}
