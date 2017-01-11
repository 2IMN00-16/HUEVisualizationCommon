package nl.mauriceknoop.sanseminar.visualization;

/**
 * Created by Maurice on 11-1-2017.
 *
 * Indicates that this class can be fired. This means that if a given Event is fired, this class should do something.
 * THat something can be perform an Action in the case of {@link Command}, or fire as well  in the case of {@link Event}.
 */
public interface Fireable {
    /**
     * <p>
     * Indicates whether firing of the given event should lead to firing of this Fireable object.
     * This is to handle some scenarios in which a Fireable object is less strict than the fired event.
     * </p>
     * <p>
     * For instance, if this Fireable object would fire for any job while the given event is for a specific job. Then if
     * the given event is fired, this Fireable object should fire as well. Thus {@code true} should be returned for this
     * specific example.
     * </p>
     * @param event The event that could fire
     * @return Whether or not the given event would cause this Fireable object to be fired as well. If the given event
     * is equal to this object, {@code true} should be returned. Otherwise it should depend on whether this object is a
     * general variant of the given event.
     */
    boolean isFiredBy(Event event);
}
