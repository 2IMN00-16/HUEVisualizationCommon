package nl.mauriceknoop.sanseminar.visualization;

/**
 * Created by Maurice on 11-1-2017.
 *
 * Indicates that this class can be used to check whether any Event should lead to execution.
 */
public interface IEventMatcher {
    /**
     * <p>
     * Indicates whether the given Event is matched by this IEventMatcher object. This allows for a specification in
     * which the holder of an IEventMatcher waits for the occurrence of a specific Event, namely the one matched by this
     * IEventMatcher, before doing something.
     * </p>
     * <p>
     * It could be that the given Event must have very specific properties, or that it is accepted more generally.
     * For instance, if this IEventMatcher object would match for any job while the given Event is for a specific job.
     * Then if the given event is fired, anything depending on. Thus {@code true} should be returned for this
     * specific example.
     * </p>
     * @param event The event that could fire
     * @return Whether or not the given event should result in the holder of this IEventMatcher to do something with the
     * Event.
     */
    boolean matches(Event event);
}
