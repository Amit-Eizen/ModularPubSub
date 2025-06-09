package test;

import java.util.Collection;
import java.util.HashMap;

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
            if(topics.containsKey(name))
            {
                return topics.get(name);
            }
            else
            {
                topics.put(name, new Topic(name));
                return topics.get(name);
            }
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
