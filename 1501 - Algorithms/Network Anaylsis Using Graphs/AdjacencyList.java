import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Alec Trievel
 */
public class AdjacencyList 
{
      private  Map<Integer, List<Integer>> AdjList;	

      public AdjacencyList(int numVertices)
      {
            AdjList = new HashMap<Integer, List<Integer>>();	
            for (int i = 0 ; i <= numVertices - 1; i++)
            { 
                AdjList.put(i, new LinkedList<Integer>());
            }
      }

      /*
      * Adds a new edge (connection between two vertices) to the list
      */
      public boolean setEdge(int source , int destination)
      {
            if (source > AdjList.size() || destination > AdjList.size())      //source and destination must less than the size of the HashMap
            {
                  return false;
            }
            
            List<Integer> sourceList = AdjList.get(source);
            List<Integer> destinationList = AdjList.get(destination);
            
            sourceList.add(destination);
            destinationList.add(source);
            
            return true;
      }

      /* 
      * Returns the list of edges that are connected to a particular source vertex
      */	
      public List<Integer> getEdge(int source)
      {
            if (source > AdjList.size())
            {
                return null;
            }
            
            return AdjList.get(source);
      }
      
      /*
      * Tests if two vertices are connected by preforming exhaustive search
      */
      public boolean isConnected(int from, int to)
      {
            List<Integer> edgeList = AdjList.get(from);
            
            for(Integer anEdge : edgeList)
            {
                  if(anEdge == to)
                  {
                        return true;
                  }
            }
            
            return false;
      }
}
