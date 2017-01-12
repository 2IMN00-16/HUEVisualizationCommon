package nl.mauriceknoop.sanseminar.visualization;

import java.util.BitSet;

/**
 * Created by Maurice on 11-1-2017.
 */
public abstract class Event implements Fireable {

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
    @Override
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

    /**
     * Creates a mask that can be used to indicate that an Event accepts specific instances, based on the translation
     * made by the given translator.
     * @param min The lowest value for identifiers, inclusive. May be negative.
     * @param max The highest value for identifiers, inclusive. Must not be smaller than the minimum.
     * @return The created mask.
     */
    protected static Mask createMask(int min, int max){
        return new Mask(min, max);
    }

    /**
     * Mask class that can be used to indicate that items with a certain identifier are accepted. 
     */
    protected static final class Mask {

        /**
         * Internal representation of the mask.
         */
        private final BitSet bitSet;

        /**
         * The lowest value for identifiers, inclusive.
         */
        private final int min;

        /**
         * The highest value for identifiers, inclusive.
         */
        private final int max;

        /**
         * Create a new mask that accepts identifiers whose value lies between the given minimum and maximum, both
         * inclusive.
         * @param min The lowest value for identifiers, inclusive. May be negative.
         * @param max The highest value for identifiers, inclusive. Must not be smaller than the minimum.
         */
        private Mask(int min, int max) {
            if(max < min)
                throw new IllegalArgumentException("The maximum must not be smaller than the minimum.");
            this.min = min;
            this.max = max;
            this.bitSet = new BitSet(1 + this.max - this.min);
        }

        /**
         * Indicates whether this mask accepts the given item. That is, if this mask has been set to explicitly accept
         * the given item or if this mask accepts any item. If two items have the same translation then beware that this
         * method may produce unexpected results because it can't distinguish between the two.
         *
         * @param identifier The identifier for which to find acceptance. This method does not perform validation on the
         *                   identifier. If the given identifier is not within bounds, false is returned.
         * @return Whether or not the given task is accepted by this TaskEvent. We define a distinction on three
         * situations:
         * <ul>
         * <li>The given item can't be translated, in which case we can't accept it.</li>
         * <li>Otherwise check if the translation is within bounds. If so, return the value for the translation,
         * otherwise return false.</li>
         * </ul>
         * @see #isInBounds(int)
         */
        public boolean accepts(int identifier) {
            return this.isInBounds(identifier) && this.isInBounds(identifier) && this.bitSet.get(indexOf(identifier));
        }

        /**
         * Computes the bit index for the given identifier. The input is not validated.
         * @param identifier The identifier for which to get the BitSet index
         * @return The computed BitSet index.
         */
        private int indexOf(int identifier){
            return identifier - min;
        }

        /**
         * Indicate that the given identifier must be accepted.
         * @param identifier The identifier to accept. The identifier should lie in the range that is specified when
         *                   this Mask was created.
         */
        public void accept(int identifier){
            this.set(identifier, true);
        }
        /**
         * Set the given acceptance value for the given identifier
         * @param identifier The identifier for which to change the acceptance.
         * @param accepts Whether or not the given identifier is accepted.
         */
        public void set(int identifier, boolean accepts){
            validateItem(identifier);
            this.bitSet.set(indexOf(identifier), accepts);
        }

        /**
         * Indicates whether the given identifier lies in the range that was specified at the creation of this Mask.
         * @param identifier An identifier.
         * @return Whether the given identifier is within bounds the specified bounds.
         */
        private boolean isInBounds(int identifier){
            return identifier >= min || identifier <= max;
        }

        /**
         * Validates that the given identifier is in the range as specified by {@link #isInBounds(int)}. If
         * this is the case the method terminates normally. Otherwise an IllegalArgumentException is thrown, indicating
         * that the given identifier is not within the specified range.
         * @param identifier The identifier of the item to validate.
         * @throws IllegalArgumentException If the identifier does not lie within the defined range.
         */
        private void validateItem(int identifier){
            if(!isInBounds(identifier))
                throw new IllegalArgumentException("identifier must lie in the range ["+this.min+","+this.max+"].");
        }

    }
}
