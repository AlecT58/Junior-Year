//package dlb_test;

/**
 * @author Alec Trievel
 */
public class DLB_Node   //the Node that is used for the links of the DLB Trie
{
      char character;
      boolean isSubString = false;
      DLB_Node sibling = null;
      DLB_Node child = null;
      
      public DLB_Node(char character)
      {
            this.character = character;
      }
      
      public DLB_Node()
      {
            
      }
}
