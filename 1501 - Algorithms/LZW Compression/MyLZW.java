/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class MyLZW 
{
      private static final int MIN_WIDTH = 9;   //always start with codebook at width 9
      private static final int MAX_WIDTH = 16;  //max codebook width is 16
      private static final int MIN_CODEWORDS = (int) Math.pow(2, MIN_WIDTH);  //min codewords in the book is 2^9
      private static final int MAX_CODEWORDS = (int) Math.pow(2, MAX_WIDTH);  //max codewords in the book is 2^16
      private static final double MAX_RATIO = 1.1;    //ratio of compressions cannot excede 1.1

      private static int R = 256;        // number of input chars
      private static int L = MIN_CODEWORDS;       // number of codewords = 2^W
      private static int W = MIN_WIDTH;         // codeword width

      public static void compress(String mode) 
      { 
            switch (mode)     //need to write the mode out to file so we can decompress properly
            {
                  case "n":
                        BinaryStdOut.write('n', 8);
                        break;
                  case "r":
                        BinaryStdOut.write('r', 8);
                        break;
                  default:
                        BinaryStdOut.write('m', 8);
                        break;
            }
          
            //next 5 lines are author code
            String input = BinaryStdIn.readString();
            TST<Integer> st = new TST<Integer>();
            for (int i = 0; i < R; i++)
                st.put("" + (char) i, i);
            int code = R+1;  // R is codeword for EOF

            int uncompressed = 0;   //amount of uncompressed data
            int compressed = 0;     //amount of compressed data
            double currentRatio = 0;   //current ratio starts off at 0
            double oldRatio = Integer.MIN_VALUE;   //start the saved value at an impossible ratio so we can detect if any calculations were preformed yet

            while (input.length() > 0) 
            {
                  L = (int) Math.pow(2, W);
                  String s = st.longestPrefixOf(input);  // Find max prefix match s.
                  uncompressed += s.length() * 8; //size of longest prefix * 8 becuase String size
                  BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
                  compressed += W;        //compressed data written is the current width 
                  
                  currentRatio = uncompressed / compressed; //calculate the ratio by dividing amount of uncompressed by compressed data

                  //next 3 lines are author code
                  int t = s.length();
                  if (t < input.length() && code < L)    // Add s to symbol table.
                        st.put(input.substring(0, t + 1), code++);

                  if((code == getNumCodeWords(W)) && (W < MAX_WIDTH))     //if the code matches to 2^W and we are still less than 2^16 width...
                  {
                        L = getNumCodeWords(W+1);     //L = 2^(W+1)
                        W++;        //increment width
                        st.put(input.substring(0, t+1), code++);   //put the data in the buffer based on code+1 position
                  }
                  if(code == MAX_CODEWORDS)     //if we are out of codewords...
                  {
                        if(mode.equalsIgnoreCase("r")) //enter reset mode logic
                        {
                              //next 5 lines are author code, copied from above
                              st = new TST<Integer>();
                              for (int i = 0; i < R; i++)
                                    st.put("" + (char) i, i);
                              code = R+1;  // R is codeword for EOF

                              //Reset width and codewords to min values
                              W = MIN_WIDTH;
                              L = MIN_CODEWORDS;
                        }
                        else if(mode.equalsIgnoreCase("m")) //enter monitor mode logic
                        {
                              if(oldRatio == Double.MIN_VALUE)   //if no current ratio set, set to the current
                              {
                                    oldRatio = currentRatio;
                              }

                              if(getRatio(oldRatio, currentRatio) > MAX_RATIO)      //if the ratio is above 1.1, we need to reset
                              {
                                    //next 5 lines are author code, copied from above
                                    st = new TST<Integer>();
                                    for (int i = 0; i < R; i++)
                                          st.put("" + (char) i, i);
                                    code = R+1;  // R is codeword for EOF

                                    //next 4 lines just reset everything to the default value
                                    oldRatio = Double.MIN_VALUE; 
                                    currentRatio = 0;
                                    W = MIN_WIDTH;
                                    L = MIN_CODEWORDS;
                              }
                        }
                  }

                  input = input.substring(t);            // Scan past s in input.
            }
            BinaryStdOut.write(R, W);
            BinaryStdOut.close();
    } 


      public static void expand() 
      {
            char mode = BinaryStdIn.readChar(8); //reads the first 8 bits to locate the mode
            
            String[] st = new String[MAX_CODEWORDS];  //assume that we need to hold the max number of codewords possible
            int i; // next available codeword value

            //next 7 lines are author code
            // initialize symbol table with all 1-character strings
            for (i = 0; i < R; i++)
                  st[i] = "" + (char) i;
            st[i++] = "";                        // (unused) lookahead for EOF

            int codeword = BinaryStdIn.readInt(W);
            if (codeword == R) return;           // expanded message is empty string
            String val = st[codeword];

            int uncompressed = 0;   //amount of uncompressed data
            int compressed = 0;     //amount of compressed data
            double currentRatio = 0;   //current ratio starts off at 0
            double oldRatio = Double.MIN_VALUE;   //start the saved value at an impossible ratio so we can detect if any calculations were preformed yet

            while (true) 
            {
                  BinaryStdOut.write(val);
                  uncompressed += val.length() * 8; //size of longest prefix * 8 because String size

                  codeword = BinaryStdIn.readInt(W);
                  
                  compressed += W;
                  currentRatio = uncompressed / compressed;

                  if (codeword == R) break;
                  String s = st[codeword];
                  if (i == codeword) s = val + val.charAt(0);   // special case hack
                  if (i < L-1) st[i++] = val + s.charAt(0);
                  

                  if(i == L-1 && W < MAX_WIDTH) //makes sure to increment W and increase the number of codewords as we move along
                  {
                        st[i++] = val + s.charAt(0);
                        L = getNumCodeWords(W+1);
	                  W++;
                  }
                  val = s;

                  if(i == MAX_CODEWORDS-1)      //can't exced the number of codewords, so it should be the max - 1
                  {
                        if(mode == 'r') //enter reset mode logic
                        {
                              //next 4 lines just reset everything to the default value
                              W = MIN_WIDTH;
                              L = MIN_CODEWORDS;

                              //next 7 lines are author code
                              st = new String[(int)Math.pow(2, 16)];
                              for (i = 0; i < R; i++)
                                    st[i] = "" + (char) i;
                              st[i++] = ""; 
                              codeword = BinaryStdIn.readInt(W);
                              if (codeword == R) return; // expanded message is empty string
                              val = st[codeword];
                        }
                        if(mode == 'm')   //enter monitor mode logic
                        {
                              if(oldRatio == Double.MIN_VALUE)      //if no current ratio set, set to the current
                              {
                                    oldRatio = currentRatio;
                              }
                              if(getRatio(oldRatio, currentRatio) > MAX_RATIO)      //if ratio > 1.1, reset the values
                              {
                                    //next 4 lines just reset everything to the default value
                                    W = MIN_WIDTH;
                                    L = MIN_CODEWORDS;
                                    oldRatio = Double.MIN_VALUE;
                                    currentRatio = 0;

                                    //next 7 lines are author code
                                    st = new String[(int)Math.pow(2, 16)];
			                  for (i = 0; i < R; i++)
				                  st[i] = "" + (char) i;
			                  st[i++] = ""; 
			                  codeword = BinaryStdIn.readInt(W);
			                  if (codeword == R) return; // expanded message is empty string
			                  val = st[codeword];
                              }
                        }
                  }
            }
            BinaryStdOut.close();
      }

      //used to return 2^W codewords becuase I was tired of typing Math.pow
      private static int getNumCodeWords(int W)   
      {
            return (int) Math.pow(2, W);
      }

      //used to return oldratio/newRatio when checking if ratio > 1.1
      private static double getRatio(double oldRatio, double newRatio)
      {
            return oldRatio/newRatio;
      }

      public static void main(String[] args)
      {
            switch (args[0]) 
            {
                  case "+":     //expand a file, no need to check for other command line arguments
                        expand();
                        break;
                  case "-":     //compress a file, check for mode type (do nothing, reset, or monitor)
                        if(args[1].equalsIgnoreCase("n"))
                        {
                              compress("n");
                              break;
                        }
                        else if(args[1].equalsIgnoreCase("r"))
                        {
                              compress("r");
                              break;
                        }
                        else if(args[1].equalsIgnoreCase("m"))
                        {
                            compress("n");
                            break;
                        }     
                  default:
                        System.exit(0);
            }
      }
}