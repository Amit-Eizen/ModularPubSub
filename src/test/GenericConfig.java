package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GenericConfig implements Config {
    private String configName;
    //private List<ParallelAgent> agents = new ArrayList<>(); // Holds all wrapped agents
    private List<Agent> agents;
    public GenericConfig() {
        agents = new ArrayList<>();
    }

    public void setConfFile(String configName) {
        this.configName = configName;
    }
    @Override
    public void create() { // Reads the config file, creates agents, and wires them to topics.
        if (configName == null) {
            System.err.println("Config file not set!");
            return;
        }

        try {
            List<String> lines = readConfigFile(configName);

            if (lines.size() % 3 != 0) { // Validate that the number of lines is divisible by 3
                System.err.println("Invalid config file: must be divisible by 3");
            }

            for (int i = 0; i < lines.size(); i += 3) { // Process each agent definition (3 lines per agent)
                String className = lines.get(i).trim();
                String subsLine = lines.get(i + 1).trim();
                String pubsLine = lines.get(i + 2).trim();

                String[] subs = subsLine.isEmpty() ? new String[0] : subsLine.split(",");
                String[] pubs = pubsLine.isEmpty() ? new String[0] : pubsLine.split(",");

                for (int j = 0; j < subs.length; j++) {
                    subs[j] = subs[j].trim();
                }
                for (int j = 0; j < pubs.length; j++) {
                    pubs[j] = pubs[j].trim();
                }
                Agent agent = createAgentInstance(className, subs, pubs);
                if (agent != null) {
                    agents.add(agent);
                }
            }
        }
        catch(IOException e){
            System.err.println("Error reading config file: " + e.getMessage());
        }
    }
    private List<String> readConfigFile(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    private Agent createAgentInstance(String className, String[] subs, String[] pubs) {
        try {
            Class<?> clazz = Class.forName(className);

            Constructor<?> constructor = clazz.getConstructor(String[].class, String[].class);

            Object instance = constructor.newInstance(subs, pubs);

            if (instance instanceof Agent) {
                return (Agent) instance;
            } else {
                System.err.println("Class " + className + " does not implement Agent interface");
                return null;
            }

        } catch (Exception e) {
            System.err.println("Error creating agent instance for " + className + ": " + e.getMessage());
            return null;
        }
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
        agents.clear();
    }
}
