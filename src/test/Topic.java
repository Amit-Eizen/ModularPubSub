//package project_biu.test;
package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Topic
{
    public final String name;
    private final List<Agent> subscribers = new ArrayList<>();
    private final List<Agent> publishers = new ArrayList<>();

    public Topic(String name)
    {
        this.name = Objects.requireNonNull(name, "Topic name cannot be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic name cannot be empty");
        }
    }
    public String getName() {
        return name;
    }

    public void subscribe(Agent agent)
    {
        Objects.requireNonNull(agent, "Agent cannot be null");
        if (!subscribers.contains(agent)) {
            subscribers.add(agent);
        }

    }
    public void unsubscribe(Agent agent) {
        Objects.requireNonNull(agent, "Agent cannot be null");
        subscribers.remove(agent);
    }

    public void publish(Message message) {
        Objects.requireNonNull(message, "Message cannot be null");
        for (Agent agent : subscribers) {
            agent.callback(this.name, message);
        }
    }

    public void addPublisher(Agent agent) {
        Objects.requireNonNull(agent, "Agent cannot be null");
        if (!publishers.contains(agent)) {
            publishers.add(agent);
        }
    }

    public void removePublisher(Agent agent) {
        Objects.requireNonNull(agent, "Agent cannot be null");
        publishers.remove(agent);
    }

    public List<Agent> getSubscribers() {
        return Collections.unmodifiableList(subscribers);
    }

    public List<Agent> getPublishers() {
        return Collections.unmodifiableList(publishers);
    }
}
