package test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GenericConfig implements Config {
    private String configName;
    private final List<Agent> agents = new ArrayList<>(); // Holds all wrapped agents

    public GenericConfig() {}

    public void setConfFile(String configName) {
        this.configName = configName;
    }
    @Override
    public void create() { // Reads the config file, creates agents, and wires them to topics.
        List<String> lines;
        try { // Read all lines from the config file
            lines = Files.readAllLines(Paths.get(configName)).stream().map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read config file", e);
        }

        if (lines.size() % 3 != 0) // Check if the input is valid: must be divisible by 3
            throw new RuntimeException("Invalid config file: must be divisible by 3");

        for (int i = 0; i < lines.size(); i += 3) { // Process each agent definition (3 lines per agent)
            String className = lines.get(i).trim(); // Full class name (with package)
            List<Topic> subs = parseTopics(lines.get(i + 1)); // Topics to subscribe to
            List<Topic> pubs = parseTopics(lines.get(i + 2)); // Topics to publish to
            try {
                // Reflection: load class and find the proper constructor (List<Topic>, List<Topic>)
                Class<?> clazz = Class.forName(className);
                Constructor<?> ctor = clazz.getConstructor(List.class, List.class);
                Agent agent = (Agent) ctor.newInstance(subs, pubs);  // Create the agent instance

                Agent wrappedAgent = new ParallelAgent(agent); // Wrap the agent with ParallelAgent decorator
                agents.add(wrappedAgent);

                for (Topic t : subs) {t.subscribe(wrappedAgent);} // Subscribe the wrapped agent to each topic (subs)
                for (Topic t : pubs) {t.addPublisher(wrappedAgent);} // Add the wrapped agent as a publisher to each topic (pubs)

            }
            catch (ClassNotFoundException e) {
                System.err.println("Class not found: " + className + " " + e.getMessage());
            }
            catch (NoSuchMethodException e) {
                System.err.println("Constructor (List, List) not found for: " + className + " " + e.getMessage());
            }
            catch (Exception e) {
                System.err.println("Failed to create agent: " + className+ ", Reason: " + e.getMessage());
            }
        }
    }
    private List<Topic> parseTopics(String line) { // Helper function to parse a topic line into a list of Topic objects
        List<Topic> res = new ArrayList<>();
        if (line == null || line.trim().isEmpty()) return res;
        for (String name : line.split(",")) {
            res.add(TopicManagerSingleton.get().getTopic(name.trim()));
        }
        return res;
    }
    @Override
    public String getName() {
        return this.configName;
    }
    @Override
    public int getVersion() {
        return 0;
    }
    @Override
    public void close() {
        for (Agent a : agents) {
            a.close();
        }
    }
}
