//package dlb_test;
/**
 * @author Alec Trievel
 */
public class DLB_Trie
{
      private final DLB_Node root = new DLB_Node();
      
      /**
       *  There are two cases when adding to the DLB
       *          If the DLB is empty, simply add all the characters as the set of children under the root
       *          If the DLB is not empty, then we must loop until all characters can be added 
       *                Check to see if the current child's character == input at the current index
       *                      If yes, drop to the next child, and move to the next index
       *                      If no, go to the next sibling. If there are no siblings, create a new one and add all the remaining characters as children of current 
       *                If there are no more children, add the rest of the characters as children of current
       * @param input the string to add to the DLB
       */
      public void add(String input)
      {               
            if (root.child == null)       //nothing in DLB yet, so add new reference and all characters as children of the new reference
            {
                  root.child = new DLB_Node(input.charAt(0));
                  DLB_Node current = root.child;
                  
                  for(int i = 1; i < input.length(); i++)
                  {
                        current.child = new DLB_Node(input.charAt(i));
                        if(i == input.length() - 1)
                        {
                              current.child.isSubString = true;
                        }
                        
                        current = current.child;
                  }
            }
            else if(root.child != null)   //root reference is not null, find where to place
            {
                   DLB_Node current = root.child;
                   int index = 0;
                   
                   while(index < input.length())
                   {
                         if(current.character == input.charAt(index))   //if the child reference holds the value of the next character, drop down to the next child
                         {
                               if(current.child != null)    //more children, continue the loop
                               {
                                     current = current.child;
                                     index++;
                               }   
                               else //if the child reference has no more children below it, add the rest of the string or jump to a sibling
                               {
                                    current.child = new DLB_Node(input.charAt(index));    
                                     
                                    for(int i = index + 1; i < input.length(); i++)
                                    {
                                          current.child = new DLB_Node(input.charAt(i));
                                          if(i == input.length() - 1)
                                          {
                                                current.child.isSubString = true;
                                          }
                        
                                          current = current.child;
                                    }
                              
                                    index = input.length();
                               }
                         }
                         else if(current.sibling != null)   //try to go to the sibling first before adding new references
                         {
                              current = current.sibling;
                         }
                         else if(current.sibling == null)   //no sibling either, add the rest of the string as children of the current child
                         {
                               current.sibling = new DLB_Node(input.charAt(index));
                               current = current.sibling;
                               
                              for(int i = index + 1; i < input.length(); i++)
                              {
                                    current.child = new DLB_Node(input.charAt(i));
                                    if(i == input.length() - 1)
                                    {
                                          current.child.isSubString = true;
                                    }
                        
                                    current = current.child;
                              }
                              
                              index = input.length();
                         }
                   }
            }
      }
      
      public boolean search(String search)
      {
            //start by looking at all siblings n the first level of the DLB until the first characters match
            DLB_Node current = root.child;
            boolean found = false;
            int index = 0;
            
            while(!found)
            {     
                  if(current.character == search.charAt(index))   //character in node and at the current index are the same, try to continue
                  {
                        if(index == search.length() - 1 && current.isSubString)     //if the Node says this is a substring and the length is correct, the string is found
                        {
                              found = true;
                        }
                        else if(current.child != null)      //otherwise, drop down a child if not null
                        {
                              index++;
                              current = current.child;
                        }
                        else
                        {
                              return found;
                        }
                  }
                  else if(current.sibling != null)    //try to go to the sibling if the child does not have the correct character
                  {
                        current = current.sibling;
                  }
                  else
                  {
                        return found;
                  }
                  
                  if(index == search.length())  //the length of the string has been reached, search is over
                        return found;
            }
            
            return found;
      }
}