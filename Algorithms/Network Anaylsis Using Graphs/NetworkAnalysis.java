import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Alec Trievel
 */
public class NetworkAnalysis 
{
      public static void main(String[] args) 
      {
            Scanner in = new Scanner(System.in);
            String filename = null;
            
            AdjacencyList adjList = null;       //the adjacency list that describes the graph
            ArrayList<Edge> edgeList = null;    //the list of information concerning each edge
            FlowNetwork flow = null;            //used to apply Ford Fulkerson algorithm for max flow
            int numVertices = 0;               //the number of verticies represented in the graph
            
            System.out.print("Enter the name of the file the specifies the information about the network graph: ");
            filename = in.next();
            
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) 
            {
                  numVertices = Integer.parseInt(br.readLine());       //first line of the file is the # of edges
                  adjList = new AdjacencyList(numVertices);
                  edgeList = new ArrayList<Edge>();
                  flow = new FlowNetwork(numVertices);
                 
                  String line;
                  while ((line = br.readLine()) != null) 
                  {
                        String[] split = line.split(" ");   //read and parse each line, then add the information to the appropriate lists, graph, or network
                        
                        int from = Integer.parseInt(split[0]);
                        int to  = Integer.parseInt(split[1]);
                        String cableType = split[2];
                        double bandwidth = Double.parseDouble(split[3]);
                        double cableLength =  Double.parseDouble(split[4]);
                                 
                        adjList.setEdge(from, to);
                        edgeList.add(new Edge(from, to, cableType, bandwidth, cableLength));
                        edgeList.add(new Edge(to, from, cableType, bandwidth, cableLength));
                        flow.addEdge(new FlowEdge(from, to, bandwidth));
                        flow.addEdge(new FlowEdge(to, from, bandwidth));
                  }
            }
            catch (IOException e)
            {
                  System.out.println("Error opening or processing the desired file. Program terminating...");
                  System.exit(0);
            }
            
            while(true)
            {
                  System.out.println("\nSelect an option: ");
                  System.out.println("1  -  Find lowest latency path between two points");
                  System.out.println("2  -  Determine if graph is copper-only connected");
                  System.out.println("3  -  Find the maximum amount of data that can be transfered along two verticies");
                  System.out.println("4  -  Find the lowest average latency spanning tree");
                  System.out.println("5  -  Determine if graph will remain connected if any two verticies fail");
                  System.out.println("0  -  Exit the program");
                  System.out.print("Your choice: ");
                  
                  int menu = in.nextInt();
                  int start = 0;
                  int end = 0;
                  
                  switch (menu)
                  {
                        case 0:   //quit the program
                              System.out.println("Program terminating...");
                              System.exit(0);
                              break;
                        case 1:
                              System.out.println();
                              
                              start = 0;
                              end = 0; 
                              
                              while(1 == 1)           //get the verticies from the user, loop until correct input tyoe is recieved 
                              {     
                                    try
                                    {
                                          System.out.print("Enter the starting vertex: ");
                                          start = in.nextInt();
                                          System.out.print("Enter the ending vertex: ");
                                          end = in.nextInt();
                                          break;
                                    }
                                    catch (Exception e)
                                    {
                                          System.out.println("Error. One of the values was invalid. Please try again.");
                                    }
                              }
                              
                              Dijkstra_LowestLatencyPath findPath = new Dijkstra_LowestLatencyPath(numVertices, edgeList, start);  //run modified Dijkstra's to find the lowest latency paths for all paths that start at the vertex "start"
                              Iterable<Edge> path = findPath.pathTo(end);     //returns a list of edges that are in the lowest latency path

                              double totalLatency = 0;
                              double totalBandwidth = 0;
                              
                              //below will print the path, calculate the total latency and bandwidth, and then display the results
                              System.out.println("\nHere is the lowest latency path between verticies " + start + " and " + end + ":");
                              for(Edge e : path)
                              {
                                    System.out.println(e.getFrom() + " -> " + e.getTo());
                                    totalLatency += e.getLatency();
                                    totalBandwidth += e.getBandwidth();
                              }
                              System.out.println("The total latency of the path is " + totalLatency + " meters squared per second.");
                              System.out.println("The total bandwidth of the path is " + totalBandwidth/1000 + " gigabits per second (" + totalBandwidth + " megabits per second).");
                              
                              break;
                        case 2: 
                              System.out.println();
                              
                              BFS_CopperConnected copperConnected = new BFS_CopperConnected(edgeList, numVertices);    //runs a modified version of BFS to locate a path that is only represented by copper cables
                              boolean[] allCopper = copperConnected.returnConnectedCopper();    //array of all verticies, either marked true if reached by copper or false if not
                              
                              //display the results, either copper connected or not. Also display the verticies that cannot be reached by copper cables, if applicable
                              if(copperConnected.allCopper())
                              {
                                    System.out.println("The network can be connected by copper cables only.");
                              }
                              else
                              {
                                    System.out.println("The network can NOT be connected by copper cables only.");
                                    System.out.print("This is because the vertex (verticies) ");
                                    
                                    for(int i =0; i < allCopper.length; i++)
                                    {
                                          if(allCopper[i] == false)
                                          {
                                                System.out.print(i + " ");
                                          }
                                    }
                                    
                                    System.out.println("cannot be reached by copper cables only.");
                              }
                              
                              
                              break;
                        case 3:
                              System.out.println();
                              
                              start = 0;
                              end = 0; 
                              
                              while(1 == 1)                 //get the verticies from the user, loop until correct input tyoe is recieved 
                              {     
                                    try
                                    {
                                          System.out.print("Enter the starting vertex: ");
                                          start = in.nextInt();
                                          System.out.print("Enter the ending vertex: ");
                                          end = in.nextInt();
                                          break;
                                    }
                                    catch (Exception e)
                                    {
                                          System.out.println("Error. One of the values was invalid. Please try again.");
                                    }
                              }
                              
                              FordFulkerson_MaximumBandwidth maxBandwidth = new FordFulkerson_MaximumBandwidth(flow, start, end);         //run Ford Fulkerson the textbook author provided us to find the max flow between two nodes in the network
                              
                              System.out.println("The maximum bandwidth between verticies " + start + " and " + end + " is " + maxBandwidth.value()/1000 + " gigabits per second (" + maxBandwidth.value() + " megabits per second).");
                              
                              //re-populate the network; was causing issues if not re-populated
                              flow = new FlowNetwork(numVertices);
                              for(Edge e: edgeList)
                              {
                                    flow.addEdge(new FlowEdge(e.getFrom(), e.getTo(), e.getBandwidth()));
                                    //flow.addEdge(new FlowEdge(e.getTo(), e.getFrom(), e.getBandwidth()));
                              }
                              
                              break;
                        case 4: 
                              System.out.println();
                              
                              Kruskal_LatencyTree mstData = new Kruskal_LatencyTree(edgeList, numVertices);            //run a modified version of Kruskal's algorithm to find the MST
                              Iterable<Edge> mst = mstData.getMST();          //list that represents the list of verticies in the MST
                              
                              //Display the results of the MST, including the total (average) latency
                              System.out.println("Here is the lowest average latency spanning tree for the entire graph:");
                              for(Edge e : mst)
                              {
                                    System.out.println(e.getFrom() + " -> " + e.getTo());
                              }
                              System.out.println("Additionally, the total latency for this MST is: " + mstData.getLatency() + " meters squared per second.");
                              
                              break;
                        case 5:     
                              System.out.println();
                              
                              boolean hasFailed = false;
                              
                              //run a modified version of BFS traversal to locate any articulation points (network failures)
                              //have to check every combination of points, so runtime increases to O(V^2)
                              //then either display the pairs that cause failure, or a message that says there are no failures
                              for(int i = 0; i < numVertices; i++)
                              {
                                    for(int j = i + 1; j < numVertices; j++)
                                    {
                                          BFS_NetworkFailure failure = new BFS_NetworkFailure(adjList, numVertices,  i,  j);
                                          
                                          if(!failure.isConnected())
                                         {
                                                System.out.println("The network will fail if the verticies " + i + " and " + j + " were to fail.");
                                                hasFailed = true;
                                         }      
                                    }
                              }
                              
                              if(!hasFailed)
                              {
                                    System.out.println("The network will not fail regardless if any random pair of verticies were to fail.");
                              }
                              
                              break;
                        default:
                              System.out.println("\nError. Invalid menu option. Please try again.");
                  }
            }
      }
}
