package nl.mauriceknoop.sanseminar.visualization;

/**
 * Created by Maurice on 12-1-2017.
 *
 * A Translator
 */
public interface Translator<T> {

    /**
     * Translates the given item to an integer that uniquely identifies that item.
     * @param item The item to translate. It is expected that for the given item a call to {@link #canTranslate(Object)}
     *             yields {@code true}. If this is not the case, this method may produce unexpected results.
     * @return An integer that identifies the given item. The returned value should lie in the range {@code [min,max]}
     * where {@code min} is equal to the result of {@link #minIdentifier()} and {@code max} is equal to the result of
     * {@link #maxIdentifier()}.
     */
    int translate(T item);

    /**
     * Indicates whether the given item can be translated by this translator.
     * @param item The item to translate.
     * @return Whether or not the given item can be translated. If this method returns false, then calling
     * {@link #translate(Object)} with that item might result in an Exception or in otherwise unexpected behavior.
     */
    boolean canTranslate(T item);

    /**
     * Defines the lowest value (inclusive) that this Translator may produce when {@link #translate(Object)} is called
     * with an item for which {@link #canTranslate(Object)} yields {@code true}, i.e. in normal circumstances.
     * @return The lowest value that {@link #translate(Object)} may produce in normal circumstances.
     */
    int minIdentifier();

    /**
     * Defines the highest value (inclusive) that this Translator may produce when {@link #translate(Object)} is called
     * with an item for which {@link #canTranslate(Object)} yields {@code true}, i.e. in normal circumstances.
     * @return The highest value that {@link #translate(Object)} may produce in normal circumstances.
     */
    int maxIdentifier();

}
