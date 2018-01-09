import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Alec Trievel
 *  A modified version of BFS traversal to decide if the network can survive a pair of failures
 *  isVisited[] will contain true if the vertex can be reached during the two other node failures, false if otherwise
 */
public class BFS_NetworkFailure
{
      private final int VERTEX = -1;
      private final int VISITED = 0;
      private final int NOT_VISITED = 1;
      
      private int[] visited;
      private int start = -1;
      private Queue<Integer> queue = new LinkedList<>();
      private AdjacencyList adjList = null;
      
      public BFS_NetworkFailure(AdjacencyList adjList, int numVertices, int vertex1, int vertex2)
      {
            this.adjList = adjList;
            visited = new int[numVertices];
            
            for (int i = 0; i < numVertices; i++)
            {
                  if(i == vertex1 || i == vertex2)
                  {
                        visited[i] = VERTEX;
                  }
                  else
                  {
                        visited[i] = NOT_VISITED; 
                  }
                  
                  if(start == VERTEX && i != vertex1 && i != vertex2)
                  {
                        start = i;
                        queue.add(start);
                  }
            }
      }
      
      public void findConnections()
      {
            while(!queue.isEmpty())
            {
                  int current = queue.poll();
                  
                  if(visited[current] == NOT_VISITED)
                  {
                        visited[current] = VISITED;
                        
                        List<Integer> connections = adjList.getEdge(current);
                        
                        for(Integer edge : connections)
                        {
                              if(visited[edge] > 0)
                              {
                                    queue.add(edge);
                              }
                        }
                  }
            }
      }
      
      public boolean isConnected()
      {
            findConnections();
            
            for(int i = 0; i < visited.length; i++)
            {
                  if(visited[i] == NOT_VISITED)
                  {
                        return false;
                  }
            }
            
            return true;
      }
}
