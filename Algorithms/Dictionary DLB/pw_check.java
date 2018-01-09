//package dlb_test;


import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Alec Trievel
 */
public class pw_check
{
      public static void main(String[] args) 
      {
            if(args.length <= 0 || args.length > 1)
            {
                  System.out.println("Invalid command line arguments. Program terminating...");
                  System.exit(0);
            }
            else if(args[0].equalsIgnoreCase("-find"))     //generate all the good passwords
            {
                  DLB_Trie trie = new DLB_Trie();     //trie to store the data from the dictionary.txt
                  Symbol_Table bad_dictionary = new Symbol_Table(); //symbol table to help the DLB
                  Symbol_Table good_dictionary = new Symbol_Table(); //symbol tbale to store good passwords
                  
                  System.out.println("Generating good passwords...");
                  bad_dictionary = populateAll("dictionary.txt", trie); //populate the DLB
                  
                  ArrayList<Symbol> list = new ArrayList(); //store good passwords generated here
                  final char[] set = {'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '2', '3', '4', '5', '6', '7', '8', '9', '!', '$', '^', '_', '*'};
                  generateGoodPasswords(set, 5, list);
                  
                  try {writeToFile(list);}      //write all good passwords to text file
                  catch (IOException ex) 
                  {
                        System.out.println("Error writing to \"all_passwords.txt\". Program terminating...");
                        System.exit(0);
                  }

                  System.out.println("DONE");
                  System.exit(0);
            }
            else if(args[0].equalsIgnoreCase("-check"))
            {
                  System.out.println("Populating DLB...");
                  String[] suggestions = new String[10];
                  Symbol_Table good_dictionary = populateTable("all_passwords.txt", suggestions);      //symbol tbale to store good passwords
                  Scanner in = new Scanner(System.in);
                  System.out.println("DONE");
                  
                  while(1 == 1)     //lazy loop until done, ask the user for a string first
                  {
                        System.out.print("Enter a password to check or Q to quit: ");
                        String input = in.next().toLowerCase();
                        
                        if(input.equalsIgnoreCase("q"))         //quit the checks
                              break;
                        
                        if(!validString(input))       //does the input follow the length and character constraints?
                        {
                              System.out.println("\nThe string you entered does not follow the password guidelines. Here are some suggestions: ");
                              for(String a : suggestions)
                                    System.out.println(a);
                              System.out.println();
                        }
                        else if(!good_dictionary.contains(input))       //is the password in the dictionary?
                        {
                              System.out.println("\nThe string you entered is not in the list of good passwords. Here are some suggestions: ");
                              for(String a : suggestions)
                                    System.out.println(a);
                              System.out.println();
                        }
                        else  //all tests pasted, display time to generate
                        {
                              System.out.println("\nThe password you entered follows the guidelines and does not contain dictionary words.");
                              System.out.println("The program guessed the password " + input + " in " + good_dictionary.get(input) + " ms.\n");
                        }
                  }
            }
            else
            {
                  System.out.println("Invalid command line arguments. Program terminating...");
                  System.exit(0);
            }
      }

      //populates the trie and symbol table by reading in a file and adding all the values
      private static Symbol_Table populateAll(String fileName, DLB_Trie trie)
      {     
            Symbol_Table table = new Symbol_Table();
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) 
            {
                 String line;
                 while ((line = br.readLine()) != null) 
                 {
                       final long startTime = System.nanoTime();
                       if(line.length() > 1)
                       {
                              trie.add(line);
                              final long duration = System.nanoTime() - startTime;
                              final double time = ((double) duration / 1000000);
                              table.put(line, time);
                       }
                 }
            } 
            catch (IOException e) 
            {
                  System.out.println("Error reading from \"dictionary.txt\". Program terminating...");
                 System.exit(0);
            }

            return table;
      }
      
       //populates the symbol table by reading in a file and adding all the values with their time
      private static Symbol_Table populateTable(String fileName, String[] suggestions)
      {     
            int count = 0;
            Symbol_Table table = new Symbol_Table();
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) 
            {
                 String line;
                 while ((line = br.readLine()) != null) 
                 {
                       if(line.length() > 1)
                       {
                              String[] values = line.split(",");
                              table.put(values[0], Double.parseDouble(values[1]));
                              if(count < 10)
                                    suggestions[count] = values[0];
                              count++;
                       }
                 }
            } 
            catch (IOException e) 
            {
                 System.out.println("Error reading from \"all_passwords.txt\". Program terminating...");
                 System.exit(0);
            }

            return table;
      }
      
      //writes the list of good passwords to file
      private static void writeToFile(ArrayList<Symbol> list) throws IOException
      {
            BufferedWriter bw = new BufferedWriter(new FileWriter("all_passwords.txt"));
            
            for(Symbol a : list)
            {
                  bw.write(a.data + "," + a.time);
                  bw.newLine();
            }
            
            bw.close();
      }

       
      //checks if string follows all of the guidelines
      //5 characters long, 1-3 letters, 1-2 numbers, and 1-2 special
      private static boolean validString(String input)
      {
            String inputCopy = input;
            int numChars = charCount(input);
            int numNums = numberCount(input);
            int specCount = specialCount(input);
            
            if(input.length() != 5)
            {
                  return false;
            }
            else if(input.contains("a") || input.contains("i") || input.contains("4") || input.contains("1"))
            {
                  return false;
            }
            else if (numChars > 3 || numChars < 1)
            {
                  return false;
            }
            else if (numNums > 2 || numNums < 1)
            {
                  return false;
            }
            else if (specCount > 2 || specCount < 1)
            {
                  return false;
            }
            
            return true;
      }
      
      //checks if the string has 1-3 valid letters 
      private static int charCount(String input)
      {
            int count = 0;
            
            for(int i = 0; i < input.length(); i++)
            {
                  if(input.charAt(i) <= 97 || input.charAt(i) >= 122)
                        count++;
            }
            return count;
      }

      //checks if the string has 1-2 valid numbers
      private static int numberCount(String input)
      {
           return input.replaceAll("\\D", "").length();
      }
      
      //checks if the string has 1-2 valid "special characters"
      private static int specialCount(String input)
      {
            int count = 0;
            
            for(int i = 0; i < input.length(); i++)
            {
                  if(input.charAt(i) == 33 || input.charAt(i) == 64 || input.charAt(i) == 36 || input.charAt(i) == 94 || input.charAt(i) == 95 || input.charAt(i) == 42)
                        count++;
            }
            return count;
      } 
      
      //helper function to generate all good passwords
      private static void generateGoodPasswords(char[] set, int length, ArrayList<Symbol> list)
      {
            generate(set, "", set.length, length, list, System.nanoTime());
      }
      
      //recursive method to generate all good passwords
      private static void generate(char[] set, String start, int setLength, int length, ArrayList<Symbol> list, long startTime)
      {
            if(length == 0)
            {
                  if(validString(start))
                  {
                        final long duration = System.nanoTime() - startTime;
                        final double time = ((double) duration / 1000000);
                        //System.out.println(start + " " + duration);
                        list.add(new Symbol(start, duration));
                  }
                  return;
            }
            
            for (int i = 0; i < setLength; i++) 
            {
                  String newPrefix = start + set[i]; 
                  generate(set, newPrefix, setLength, length - 1, list, System.nanoTime()); 
            }
      }
}

