package nl.mauriceknoop.sanseminar.visualization;

/**
 * Created by Maurice on 11-1-2017.
 */
public abstract class Event {

    /**
     * Indicates whether the {@link #isScripted()} method is overridden by a subclass.
     */
    // This is set to true. Within isScripted it is set to false. As long as calls to isScripted arrive and stop at
    // subclasses this value will remain true. If classes don't override isScripted, the definition of isScripted in
    // this class is used, which sets it to false. This is not 100% correct seeing that subclasses could call
    // super.isScripted. It is documented however that one should not, or otherwise accept unexpected results.
    private boolean isScriptedOverride = true;

    /**
     * Indicates whether the {@link #isStatic()} method is overridden by a subclass.
     */
    // This is set to true. Within isStatic it is set to false. As long as calls to isStatic arrive and stop at
    // subclasses this value will remain true. If classes don't override isStatic, the definition of isStatic in this
    // class is used, which sets it to false. This is not 100% correct seeing that subclasses could call super.isStatic.
    // It is documented however that one should not, or otherwise accept unexpected results.
    private boolean isStaticOverride = true;

    /**
     * <p>
     * Indicates whether firing of the given event should lead to firing of this event. This is to handle some scenarios
     * in which an event is less strict than the fired event.
     * </p>
     * <p>
     * For instance, if this event would fire for any job while
     * the given event is for a specific job. Then if the given event is fired, this event should fire as well. Thus
     * {@code true} should be returned for this specific example.
     * </p>
     * <p>
     * This method should return {@code true} for any Event that is returned by {@link #references()}. This means that
     * typically if {@link #references()} is overridden, this method should be too. This also holds the other way
     * around.
     * </p>
     * @param event The event that could fire
     * @return Whether or not the given event would cause this event.
     */
    public abstract boolean isFiredBy(Event event);

    /**
     * Indicates whether this Event occurs within the visualization run. This means that it is executed based on the
     * state of the visualized script. Do not call this method when overriding it, failure to follow this contract can
     * lead to unexpected results.
     * @return the negation of the result of {@link #isStatic} if any subclass overrides that method. Otherwise
     * {@code false}.
     */
    public boolean isScripted() {
        isScriptedOverride = false;
        return isStaticOverride && !isStatic();
    }

    /**
     * Indicates whether this Event occurs outside the visualization run. This means that it is executed based on the
     * state of the visualizer, not based on the visualized script. Do not call this method when overriding it, failure
     * to follow this contract can lead to unexpected results.
     * @return the negation of the result of {@link #isScripted} if any subclass overrides that method. Otherwise
     * {@code false}.
     */
    public boolean isStatic(){
        isStaticOverride = false;
        return isScriptedOverride && !isScripted();
    }

    /**
     * <p>
     * Indicates which Events are referenced by this Event. In practice this should mean that for every Event
     * {@code event}that is returned by this method, a call on this.{@link #isFiredBy(Event)} with {@code event} as an
     * argument should always return true. In other words, for each of the returned events: if any of them fires, this
     * event fires as well.
     * </p>
     * <p>
     * Typically if {@link #isFiredBy(Event)} is overridden, this method should be too. This also holds the other way
     * around.
     * </p>
     * @return All Events that are referenced by this Event. Return {@code null} to indicate that no Events are
     * referenced.
     */
    public Event[] references() {
        return null;
    }
}
