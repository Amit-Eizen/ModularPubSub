package test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Node
{
    private String name;
    private List<Node> edges;
    private Message msg;

    public Node(String name) {
        setName(name);
        edges = new ArrayList<Node>();
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {return this.name;}
    public void setMsg(Message msg) {
        this.msg = msg;
    }
    public Message getMsg() {return this.msg;}
    public void setEdges(List<Node> edges) {
        this.edges = edges;
    }
    public List<Node> getEdges() {return this.edges;}

    public void addEdge(Node node) {
        this.edges.add(node);
    }
    public boolean hasCycles() {
        return hasCycles(new HashSet<>());
    }
    private boolean hasCycles(Set<Node> visited)    {
        if(visited.contains(this)) {
            return true; //we have cycle
        }
        visited.add(this);
        {
           for(Node n : this.getEdges()) {
               if(n.hasCycles(visited)) {
                   return true;
               }
           }
        }
        visited.remove(this);
        return false;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return name.equals(node.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}