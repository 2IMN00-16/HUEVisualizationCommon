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
     * @param translator The Translator with which a mask must be created.
     * @return The created mask.
     */
    protected static <T> Mask<T> createMask(Translator<T> translator){
        return new Mask<>(translator);
    }

    /**
     * Mask class that can be used to indicate that specific items of type {@code T} are accepted. This uses a
     * {@link Translator} to define whether an item is accepted.
     * @param <T> The type of the items that this mask may accept.
     */
    protected static final class Mask<T> {

        /**
         * Internal representation of the mask.
         */
        private final BitSet bitSet;

        /**
         * Translator that allows us to go from any item to an integer value
         */
        private final Translator<T> translator;

        private Mask(Translator<T> translator) {
            this.translator = translator;
            this.bitSet = new BitSet(translator.distinctTranslations());
        }

        /**
         * Indicates whether this mask accepts the given item. That is, if this mask has been set to explicitly accept
         * the given item or if this mask accepts any item. If two items have the same translation then beware that this
         * method may produce unexpected results because it can't distinguish between the two.
         *
         * @param item The item for which to find acceptance. This method does not perform validation on the item as is
         *             the case for other methods.
         * @return Whether or not the given task is accepted by this TaskEvent. We define a distinction on three
         * situations:
         * <ul>
         * <li>The given item can't be translated, in which case we can't accept it.</li>
         * <li>Otherwise check if the translation is within bounds. If so, return the value for the translation,
         * otherwise return false.</li>
         * </ul>
         * @see #isInBounds(int)
         */
        public boolean accepts(T item) {
            if(!this.translator.canTranslate(item))
                return false;
            else {
                int translation = this.translator.translate(item);
                return this.isInBounds(translation) && this.bitSet.get(translation);
            }
        }


        /**
         * Indicate that the given item must be accepted.
         * @param item The item to accept. The translation of this item lie in the range {@code [0,d)}, where {@code d}
         *             is equal to the number of distinct translations the translator could provide at the creation of
         *             this Mask.
         */
        public void accept(T item){
            this.set(item, true);
        }
        /**
         * Set the given acceptance value for the given item
         * @param item The item for which to change the acceptance. The translation of this item lie in the range
         *             {@code [0,d)}, where {@code d} is equal to the number of distinct translations the translator
         *             could provide at the creation of this Mask. Note that if this item has the same translation as
         *             another item that can be translated by the internal Translator, then calling this method will
         *             influence the behavior on both items.
         * @param accepts Whether or not the given item is accepted.
         */
        public void set(T item, boolean accepts){
            if(!this.translator.canTranslate(item))
                throw new IllegalArgumentException("The Translator can't translate the given item");

            int translation = this.translator.translate(item);
            validateItem(translation);
            this.bitSet.set(translation, accepts);
        }

        /**
         * Indicates whether the given translation lies in the range {@code [0,d)}, where {@code d} is equal to the
         * number of distinct translations the translator could provide at the creation of this Mask.
         * @param translation Any integer number that is the result of calling {@link Translator#translate(Object)}.
         * @return Whether the given translation is within bounds.
         */
        private boolean isInBounds(int translation){
            return translation >= 0 || translation < this.bitSet.size();
        }

        /**
         * Validates that the given translation of an item is in the range as specified by {@link #isInBounds(int)}. If
         * this is the case the method terminates normally. Otherwise an IllegalArgumentException is thrown, indicating
         * that the given translation is not within the specified range.
         * @param translation The translation of the item to validate.
         * @throws IllegalArgumentException If the translation does not lie within the defined range.
         */
        private void validateItem(int translation){
            if(!isInBounds(translation))
                throw new IllegalArgumentException("translation must lie in the range [0,"+this.bitSet.size()+").");
        }

    }
}
