import java.util.ArrayList;

/**
 *
 * @author Alec Trievel 
 *  Modified version of Dijkstra's shortest path algorithm used to find the lowest latency between two vertices
 *  Iterator generated will return the path 
 */
public class Dijkstra_LowestLatencyPath 
{
      private double[] distance;
      private Edge[] edgeTo;
      private IndexMinPQ<Double> queue;
      private ArrayList<Edge> edges;
      
      public Dijkstra_LowestLatencyPath(int numVerticies, ArrayList<Edge> edges, int start)
      {
            distance = new double[numVerticies];
            edgeTo = new Edge[numVerticies];
            this.edges = edges;
            
            for(int i =0; i < numVerticies; i++)
            {
                  distance[i] = Double.POSITIVE_INFINITY;
            }
            
            distance[start] = 0;
            
            queue =  new IndexMinPQ<Double>(numVerticies);
            queue.insert(start, distance[start]);
            
            while(!queue.isEmpty())
            {
                  int from = queue.delMin();
                  for(Edge e : edges)
                  {
                        if(e.getFrom() == from)
                        {
                              relax(e);
                        }
                  }
            }
      }
      
      private void relax(Edge e)
      {
            int from = e.getFrom();
            int to = e.getTo();
            
            if(distance[to] > distance[from] + e.getLatency())
            {
                  distance[to] = distance[from] + e.getLatency();
                  edgeTo[to] = e;
                  
                  if(queue.contains(to))
                  {
                        queue.decreaseKey(to, distance[to]);
                  }
                  else
                  {
                        queue.insert(to, distance[to]);
                  }
            }
      }
      
      
      public Iterable<Edge> pathTo(int to)
      {
            Stack<Edge> path = new Stack<Edge>();
            
            for(Edge e = edgeTo[to]; e != null;  e = edgeTo[e.getFrom()])
            {
                  path.push(e);
            }
            
            return path;
      }
      
      public double distanceTo(int to) 
      {
            return distance[to];
      }
      
      
      
      public double findBandwith(int to)
      {
            return 0;
      }
}
