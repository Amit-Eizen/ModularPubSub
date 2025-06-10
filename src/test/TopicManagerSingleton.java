package test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class TopicManagerSingleton
{
    public static TopicManager get()
    {
        return TopicManager.instance;
    }
    public static class TopicManager
    {
        private static final TopicManager instance = new TopicManager();
        private final HashMap<String,Topic> topics = new HashMap<>();

        private TopicManager() {}

        public Topic getTopic (String name)
        {
            Objects.requireNonNull(name, "Topic name cannot be null");

            Topic topic = topics.get(name);
            if (topic == null) {
                topic = new Topic(name);
                topics.put(name, topic);
            }
            return topic;

        }
        public Collection<Topic> getTopics() 
        {
            return topics.values();
        }
        public void clear()
        {
            topics.clear();
        }
    }
}
