/**
 *
 * @author Alec Trievel
 *  A class used to hold the data associated with an edge in our network graph
 *  Also includes functions to return the data and calculate the latency of this edge
 */
public class Edge implements Comparable<Edge>
{
      private final double COPPER = 230000000;
      private final double OPTICAL = 200000000;
      
      private int from;
      private int to;
      private String cableType;
      private double bandwidth;
      private double cableLength;
      private double flow = 0;
      private double capacity;
      
      public Edge(int from, int to, String cableType, double bandwidth, double cableLength)
      {
            this.from = from;
            this.to = to;
            this.cableType = cableType;
            this.bandwidth = bandwidth;
            this.cableLength = cableLength;
            this.capacity = bandwidth;
      }
      
      public int getTo()
      {
            return this.to;
      }
      
      public int getFrom()
      {
            return this.from;
      }
      
      public double getBandwidth()
      {
            return this.bandwidth;
      }
      
      public double getLength()
      {
            return this.cableLength;
      }

      public String getCableType()
      {
            return this.cableType;
      }
      
      public double getLatency()
      {
            if(this.cableType.equalsIgnoreCase("copper"))
            {
                  return this.cableLength / this.COPPER;
            }
            else
            {
                  return this.cableLength /  this.OPTICAL;
            }
      }
      
      public double getResidualCapacity(int vertex)
      {
            if(vertex == this.from)
            {
                  return this.flow;
            }
            else
            {
                  return this.capacity - this.flow;
            }
      }
      
      public void addResidualCapacity(int vertex, double amount)
      {
            if(vertex == this.from)
            {
                 this.flow -= amount;
            }
            else
            {
                  this.flow += amount;
            }
      }
      
      @Override
      public int compareTo(Edge that) 
      {
            return Double.compare(this.getLatency(), that.getLatency());
      }
}
