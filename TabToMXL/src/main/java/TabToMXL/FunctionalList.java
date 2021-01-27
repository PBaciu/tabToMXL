package TabToMXL;

import java.util.*;
import java.util.function.*;

/**
 * This class extends ArrayList and provides functional programming utility methods.
 *
 * @param <E>
 */
public final class FunctionalList<E> extends ArrayList<E> {

    /**
     * Creates a FunctionalList from a given list
     *
     * @param list the list whose data to use
     */
    public FunctionalList(List<E> list) {
        super(list);
    }

    /**
     * Creates an empty FunctionalList
     */
    public FunctionalList() {
        super();
    }

    /**
     * Returns the first element
     *
     * @return the first element
     */
    public final E first() {
        return this.get(0);
    }

    /**
     * Returns a new list comprised of the last (len() - n) elements of the list
     *
     * @param n first n elements to omit
     * @return the new list
     */
    public final FunctionalList<E> drop(int n) {
        return new FunctionalList<>(this.subList(n, this.size()));
    }

    /**
     * Maps this list to another list based on the index and value of each element
     *
     * @param transform transformation function to apply
     * @param <R>
     * @return the resulting list
     */
    public final <R> FunctionalList<R> mapIndexed(BiFunction<Integer, E, R> transform) {
        return flatMapIndexed((integer, e) -> List.of(transform.apply(integer, e)));
    }

    /**
     * Flattens nested list into a new list based on the index and value of each element
     *
     * @param transform transformation function to apply
     * @param <R>
     * @return the new list
     */
    public final <R> FunctionalList<R> flatMapIndexed(BiFunction<Integer, E, List<R>> transform) {
        return flatMapIndexedTo(new FunctionalList<>(), transform);
    }

    /**
     * Flattens nested list into a new list based on the value of each element
     *
     * @param transform transformation function to apply
     * @param <R>
     * @return the new list
     */
    public final <R> FunctionalList<R> flatMap(Function<E, List<R>> transform) {
        return flatMapTo(new FunctionalList<>(), transform);
    }

    /**
     * Flattens nested list into {@code destination} based on the index and value of each element.
     *
     * @param destination the Collection in which to store the return value
     * @param transform   transformation function to apply
     * @param <R>
     * @param <C>
     * @return {@code destination}
     */
    public final <R, C extends Collection<R>> C flatMapIndexedTo(C destination, BiFunction<Integer, E, List<R>> transform) {
        var index = 0;
        for (var element : this) {
            var list = transform.apply(index++, element);
            destination.addAll(list);
        }
        return destination;
    }

    /**
     * Flattens nested list into {@code destination} based on the value of each element.
     *
     * @param destination the Collection in which to store the return value
     * @param transform   transformation function to apply
     * @param <R>
     * @param <C>
     * @return {@code destination}
     */
    public final <R, C extends Collection<R>> C flatMapTo(C destination, Function<E, List<R>> transform) {
        for (var element : this) {
            var list = transform.apply(element);
            destination.addAll(list);
        }
        return destination;
    }


    /**
     * Groups elements of list based on {@code keySelector}
     *
     * @param keySelector a function that selects a key
     * @param <K>
     * @return resulting map
     */
    public <K> Map<K, FunctionalList<E>> groupBy(Function<E, K> keySelector) {
        return groupByTo(new HashMap<>(), keySelector);
    }

    /**
     * Groups elements of list based on {@code keySelector} and stores the resulting map in {@code destination}
     *
     * @param destination the Collection in which to store the return value
     * @param keySelector a function that selects a key
     * @param <K>
     * @param <M>
     * @return {@code destination}
     */
    public final <K, M extends Map<? super K, FunctionalList<E>>> M groupByTo(M destination, Function<E, K> keySelector) {
        for (var element : this) {
            var key = keySelector.apply(element);
            var list = destination.computeIfAbsent(key, (k) -> new FunctionalList<>());
            list.add(element);
        }
        return destination;
    }

}
