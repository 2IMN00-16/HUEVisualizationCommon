package nl.mauriceknoop.sanseminar.visualization.events;

import nl.mauriceknoop.sanseminar.visualization.Event;
import nl.mauriceknoop.sanseminar.visualization.EventMatcher;

import java.util.HashSet;

/**
 * Created by Maurice on 11-1-2017.
 *
 * Event that is fired if any of its children are fired.
 */
public final class MultiEventMatcher extends EventMatcher {

    private HashSet<EventMatcher> causes = new HashSet<>();

    /**
     * Creates a new OrEvent that fires when any of the given events is fired.
     * @param eventMatchers
     */
    public MultiEventMatcher(EventMatcher... eventMatchers){
        if(eventMatchers != null)
            for(EventMatcher eventMatcher : eventMatchers)
                if(eventMatcher != null)
                    this.causes.add(eventMatcher);
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
    public boolean matches(Event event) {
        for(EventMatcher cause : this.causes)
            if(cause.matches(event))
                return true;
        return false;
    }

    /**
     * All of the children of the OrEvent are referenced by the OrEvent. This is equal to the array that was given at
     * the creation of this event, save for the removal of null values, and an arbitrary ordering.
     * @return All Events references by this Event. The returned array is never null. If this event has no references,
     */
    @Override
    public EventMatcher[] references() {
        return causes.toArray(new EventMatcher[this.causes.size()]);
    }
}
