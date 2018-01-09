import java.util.ArrayList;

/**
 * @author Alec Trievel
 *  A modified version of BFS traversal to decide if an edge can be reached by copper cables only
 *  areCopper[] will contain true if edge (resulting in two vertices being connected) is copper connected, false if not
 */
public class BFS_CopperConnected 
{
      private Queue<Integer> queue = new Queue<Integer>();
      private boolean[] areCopper;
      
      public BFS_CopperConnected(ArrayList<Edge> edges, int numVertices)
      {
            areCopper = new boolean[numVertices];
            
            for(int i = 0; i < numVertices; i++)
            {
                  areCopper[i] = false;
            }
            
            queue.enqueue(edges.get(0).getFrom());
            
            while(!queue.isEmpty())
            {
                  int current = queue.dequeue();
                  ArrayList<Edge> neighbors = new ArrayList<Edge>();
                  
                  for(Edge e: edges)
                  {
                        if(e.getFrom() == current)
                        {
                              neighbors.add(e);
                        }
                  }
                  
                  for(Edge e : neighbors)
                  {
                        if(e.getCableType().equalsIgnoreCase("copper") && areCopper[e.getTo()] == false)
                        {
                              queue.enqueue(e.getTo());
                              areCopper[current] = true;
                              areCopper[e.getTo()] = true;
                        }
                  }
            }      
      }
      
      public boolean allCopper()
      {
            for(int i = 0; i < areCopper.length; i++)
            {
                  if(areCopper[i] == false)
                        return false;
            }
            
            return true;
      }
      public boolean[] returnConnectedCopper()
      {
            return areCopper;
      }
}
