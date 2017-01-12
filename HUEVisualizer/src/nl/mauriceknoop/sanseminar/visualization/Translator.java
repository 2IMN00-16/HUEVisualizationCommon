package nl.mauriceknoop.sanseminar.visualization;

/**
 * Created by Maurice on 12-1-2017.
 *
 * A Translator
 */
public interface Translator<T> {

    /**
     * Translates the given item to an integer that uniquely identifies that item.
     * @param item
     * @return An integer that identifies the given item. The returned value should lie in the range {@code [0,d)}
     * where {@code d} is equal to the result of {@link #distinctTranslations()}.
     */
    int translate(T item);

    /**
     * Indicates whether the given item can be translated by this translator.
     * @param item
     * @return
     */
    boolean canTranslate(T item);

    /**
     * The number of distinct translations this Translator can perform. For optimization reasons, it is better to
     * return a value that is equal to {@code 2^n - 1} than it is to return {@code 2^n}.
     * @return The number of distinct translations this Translator performs. This value is expected to be
     * deterministic.
     */
    int distinctTranslations();

}
