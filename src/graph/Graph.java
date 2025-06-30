package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Graph extends ArrayList<Node>{
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
    private void addTopicNode(HashMap<String, Node> map, String topicName) {
        map.putIfAbsent(topicNodeName(topicName), new Node(topicNodeName(topicName)));
    }
    private void addAgentNode(HashMap<String, Node> map, String agentName) {
        map.putIfAbsent(agentNodeName(agentName), new Node(agentNodeName(agentName)));
    }
    private String topicNodeName(String topicName) {
        return "T" + topicName;
    }
    private String agentNodeName(String agentName) {
        return "A" + agentName;
    }
    public void createFromTopics(){
        this.clear();
        Collection<Topic> allTopics = TopicManagerSingleton.get().getTopics();
        HashMap<String, Node> nodeMap = new HashMap<>();

        for (Topic topic : allTopics) { //Node for every Topic and Agent
            addTopicNode(nodeMap, topic.name);
            for (Agent agent : topic.getSubscribers()) {
                addAgentNode(nodeMap, agent.getName());
            }
            for (Agent agent : topic.getPublishers()) {
                addAgentNode(nodeMap, agent.getName());
            }
        }
        for (Topic topic : allTopics) {
            Node topicNode = nodeMap.get(topicNodeName(topic.name));
            for (Agent agent : topic.getSubscribers()) {
                Node agentNode = nodeMap.get(agentNodeName(agent.getName()));
                topicNode.addEdge(agentNode); // Topic -> Agent
            }
        }
        for (Topic topic : allTopics) {
            Node topicNode = nodeMap.get(topicNodeName(topic.name));
            for (Agent agent : topic.getPublishers()) {
                Node agentNode = nodeMap.get(agentNodeName(agent.getName()));
                agentNode.addEdge(topicNode); // Agent -> Topic
            }
        }

        this.addAll(nodeMap.values());
    }
}
