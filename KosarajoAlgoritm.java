import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by Erfan Rahnemoon on 6/26/17.
 *
 * The code is taken with some changes from the link below
 * https://www.geeksforgeeks.org/strongly-connected-components/
 * To understand this algorithm, be sure to read the description of the link
 *
 */
public class KosarajoAlgoritm {
    private int V;
    private LinkedList<Integer> adj[];
    private LinkedList<String> scc = new LinkedList<>();
    private static String loop="";
    private boolean NoLoop = true;
    KosarajoAlgoritm(int v)
    {
        V = v;
        adj = new LinkedList[v];
        for (int i=0; i<v; ++i)
            adj[i] = new LinkedList();
    }

    void addEdge(int v, int w)  { adj[v].add(w); }

    private String  DFSUtil(int v,boolean visited[])
    {
        visited[v] = true;
        loop = loop+v+"-";
//        System.out.print(v);
        int n;
        Iterator<Integer> i =adj[v].iterator();
        while (i.hasNext())
        {
            n = i.next();
            if (!visited[n])
                DFSUtil(n,visited);
        }
        return loop;
    }

    KosarajoAlgoritm getTranspose()
    {
        KosarajoAlgoritm graph = new KosarajoAlgoritm(V);
        for (int v = 0; v < V; v++)
        {
            Iterator<Integer> i =adj[v].listIterator();
            while(i.hasNext())
                graph.adj[i.next()].add(v);
        }
        return graph;
    }

    void fillOrder(int v, boolean visited[], Stack stack)
    {
        visited[v] = true;

        Iterator<Integer> i = adj[v].iterator();
        while (i.hasNext())
        {
            int n = i.next();
            if(!visited[n])
                fillOrder(n, visited, stack);
        }

        stack.push(new Integer(v));
    }

    void findSCCs()
    {
        Stack stack = new Stack();

        boolean visited[] = new boolean[V];
        for(int i = 0; i < V; i++)
            visited[i] = false;

        for (int i = 0; i < V; i++)
            if (visited[i] == false)
                fillOrder(i, visited, stack);
        KosarajoAlgoritm graph = getTranspose();
        for (int i = 0; i < V; i++)
            visited[i] = false;
        while (stack.empty() == false)
        {
            int v = (int)stack.pop();
            if (visited[v] == false)
            {
                loop="";
                String tempScc = graph.DFSUtil(v, visited);
//                System.out.println();
                scc.add(tempScc.substring(0,tempScc.length()-1));
            }
        }
        for (String item:scc) {
            if (item.length()>1){
                NoLoop = false;
            }
        }
    }
    public boolean getNoLoop(){
        return NoLoop;
    }


}
