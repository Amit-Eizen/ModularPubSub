package test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Graph extends ArrayList<Node>
{
    public boolean hasCycles()
    {
        for (Node node : this)
        {
            if (node.hasCycles()) {
                return true;
            }
        }
            return false;
    }
    public void createFromTopics(){
        this.clear();
        Collection<Topic> allTopics = TopicManagerSingleton.get().getTopics();
        HashMap<String, Node> nodeMap = new HashMap<>();

        for (Topic topic : allTopics) {  //Node for every Topic and Agent
            String topicNodeName = "T" + topic.name;
            nodeMap.putIfAbsent(topicNodeName, new Node(topicNodeName));
        }
        for (Topic topic : allTopics) {
            for (Agent agent : topic.getSubscribers()) {
                String agentNodeName = "A" + agent.getName();
                nodeMap.putIfAbsent(agentNodeName, new Node(agentNodeName));
            }
            for (Agent agent : topic.getPublishers()) {
                String agentNodeName = "A" + agent.getName();
                nodeMap.putIfAbsent(agentNodeName, new Node(agentNodeName));
                }
        }

        for (Topic topic : allTopics) {  //  Edges from Topic to subscribers
            Node topicNode = nodeMap.get("T" + topic.name);
            for (Agent agent : topic.getSubscribers()) {
                Node agentNode = nodeMap.get("A" + agent.getName());
                topicNode.addEdge(agentNode); // Topic -> Agent
            }
        }

        for (Topic topic : allTopics) {  // Edges from Agent to Topics which he publishes to
            Node topicNode = nodeMap.get("T" + topic.name);
            for (Agent agent : topic.getPublishers()) {
                Node agentNode = nodeMap.get("A" + agent.getName());
                agentNode.addEdge(topicNode); // Agent -> Topic
            }
        }
        this.addAll(nodeMap.values());
    }
}
