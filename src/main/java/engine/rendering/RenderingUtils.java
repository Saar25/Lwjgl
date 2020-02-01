package engine.rendering;

import engine.models.Model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class RenderingUtils {

    private RenderingUtils() {

    }

    /**
     * Optimizes a renderArrays list, so the renderArrays would contain a lot less openGl calls
     *
     * @param list the list to optimize
     * @param <T>  the type of the game objects that the list contains
     * @return a map the maps all the models of the game objects to a list of game objects with the same model
     */
    public static <T extends Renderable> Map<Model, List<T>> optimizeList(List<T> list) {
        return mapList(list, Renderable::getModel);
    }

    /**
     * Inserts a game object to a map the maps all the models of
     * the game objects to a list of game objects with the same model
     *
     * @param map    the map to update
     * @param render the game object to insert
     * @param <T>    the type of the game objects that the list contains
     */
    public static <T extends Renderable> void process(Map<Model, List<T>> map, T render) {
        List<T> models = map.computeIfAbsent(
                render.getModel(), k -> new LinkedList<>());
        models.add(render);
    }

    /**
     * Splits a list into a map of lists. All the values that returns
     * the same value from the given function will be at the same list.
     *
     * @param list the list
     * @param func the function
     * @param <T>  the type of the split values
     * @param <E>  the type of the list objects
     * @return a map that contains the list values separated
     */
    public static <T, E> Map<T, List<E>> mapList(List<E> list, Function<E, T> func) {
        Map<T, List<E>> map = new HashMap<>();
        for (E value : list) {
            List<E> eList = map.computeIfAbsent(
                    func.apply(value), es -> new LinkedList<>());
            eList.add(value);
        }
        return map;
    }

}
