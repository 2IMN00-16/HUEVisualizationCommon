package nl.mauriceknoop.sanseminar.visualization;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maurice on 11-1-2017.
 */
public class Settings {

    /**
     * All static commands that must be executed before the scripted part of the visualization is started.
     */
    private final List<Command> preCommands = new LinkedList<>();


    /**
     * All scripted commands that must be executed during the scripted part of the visualization.
     */
    private final List<Command> eventCommands = new LinkedList<>();

    /**
     * All static commands that must be executed after the scripted part of the visualization has finished.
     */
    private final List<Command> postCommands = new LinkedList<>();

    /**
     * All contained Events.
     */
    private final HashSet<Event> events = new HashSet<>();

}
