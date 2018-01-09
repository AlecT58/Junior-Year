import java.util.ArrayList;

/**
 * @author Alec Trievel 
 *  Modified version of Kruskal's algorithm to find the MST of a weighted graph
 *  Iterator generated will return the MST 
 */
public class Kruskal_LatencyTree 
{
      private double weight;                       
      private Queue<Edge> mst = new Queue<Edge>();  
      
      public Kruskal_LatencyTree(ArrayList<Edge> edges, int numVerticies)
      {
            MinPQ<Edge> queue = new MinPQ<Edge>();
            
            for(Edge e : edges)
            {
                  queue.insert(e);
            }
            
            UnionFind uf = new UnionFind(numVerticies);
            
            while(!queue.isEmpty() && mst.size() < numVerticies - 1)
            {
                  Edge e = queue.delMin();
                  int from = e.getFrom();
                  int to = e.getTo();
                  
                  if(!uf.connected(from, to))
                  {
                        uf.union(from, to);
                        mst.enqueue(e);
                        weight += e.getLatency();
                  }
            }
      }
      
      public Iterable<Edge> getMST() 
      {
            return mst;
      }
      
      public double getLatency()
      {
            return weight;
      }
}
