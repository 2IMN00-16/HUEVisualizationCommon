package nl.mauriceknoop.sanseminar.visualization.events;

import nl.mauriceknoop.sanseminar.visualization.Event;

import java.util.HashSet;

/**
 * Created by Maurice on 11-1-2017.
 *
 * Event that is fired if any of its children are fired.
 */
public final class OrEvent extends Event {

    private HashSet<Event> causes = new HashSet<>();

    /**
     * Creates a new OrEvent that fires when any of the given events is fired.
     * @param events
     */
    public OrEvent(Event ... events){
        if(events != null)
            for(Event event : events)
                if(event != null)
                    this.causes.add(event);
    }


    /**
     * <p>
     * Indicates whether firing of the given event should lead to firing of this event. If any of the child events are
     * fired by the given event, then {@code true} is returned. Otherwise {@code false} is returned.
     * </p>
     *
     * @param event The event that could fire
     * @return Whether or not the given event would cause this event to fire.
     */
    @Override
    public boolean isFiredBy(Event event) {
        for(Event cause : this.causes)
            if(cause.isFiredBy(event))
                return true;
        return false;
    }

    /**
     * All of the children of the OrEvent are referenced by the OrEvent. This is equal to the array that was given at
     * the creation of this event, save for the removal of null values, and an arbitrary ordering.
     * @return All Events references by this Event. The returned array is never null. If this event has no references,
     */
    @Override
    public Event[] references() {
        return causes.toArray(new Event[this.causes.size()]);
    }
}
