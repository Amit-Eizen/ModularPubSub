//package project_biu.graph;
package graph;
import java.util.ArrayList;
import java.util.List;

public class Topic
{
    public final String name;
    private final List<Agent> subscribers = new ArrayList<>();
    private final List<Agent> publishers = new ArrayList<>();
    Topic(String name)
    {
        this.name=name;
    }
    public void subscribe(Agent a)
    {
        subscribers.add(a);
    }
    public void unsubscribe(Agent a)
    {
        subscribers.remove(a);
    }
    public void publish(Message m)
    {
        for (Agent a : subscribers)
        {
            a.callback(this.name,m);
        }
    }
    public void addPublisher(Agent a)
    {
        publishers.add(a);
    }
    public void removePublisher(Agent a)
    {
        publishers.remove(a);
    }
    public List<Agent> getSubscribers() {
        return subscribers;
    }

    public List<Agent> getPublishers() {
        return publishers;
    }
}
