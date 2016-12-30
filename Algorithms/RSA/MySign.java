package mysign;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.io.*;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Alec Trievel
 */
public class MySign
{
      public static void main(String[] args) throws IOException 
      {
            switch (args[0]) 
            {
                  case "s":
                              BigInteger[] priv = readPrivKey();
                              sign(args[1], priv[0], priv[1]);
                        break;
                  case "v":
                              BigInteger[] pub = readPubKey();
                              verify(args[1], pub[0], pub[1]);
                        break;
                  default:
                        throw new IllegalArgumentException("Illegal command line argument");
            }
      }
           
      private static void sign(String fileName, BigInteger d, BigInteger n) 
      {    	
            String message = null;
            String fileContents = null;
            
            try 
            {
                  Path path = Paths.get(fileName);
                  byte[] data = Files.readAllBytes(path);

                  fileContents = new String(data);
                  
	MessageDigest md = MessageDigest.getInstance("SHA-256");
                  md.update(fileContents.getBytes());
                  byte[] digest = md.digest();
                  BigInteger temp = new BigInteger(1, digest);
                  message = new BigInteger(1, digest).toString();
            }
            catch(IOException | NoSuchAlgorithmException e) 
            {
                  System.out.println(e.toString());
            }
            
            message = new BigInteger(message).modPow(d, n).toString();
            
            String[] contentsToWrite = {message, fileContents};
            
            try
            {
                  DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName + ".signed"));
                  
                  dos.writeInt(message.length());  
                  dos.writeChar('\t');
                  
                  dos.writeInt(fileContents.length());
                  dos.writeChar('\t');
                 
                  dos.writeChars(contentsToWrite[0]);
                  dos.writeChar('\t');
                  
                  dos.writeChars(contentsToWrite[1]); 
                  dos.writeChar('\t');
                        
                  dos.close();
                  
//                  //FOR TESTING
//                  System.out.println("SIZE OF HASH: " + message.length());
//                  System.out.println("SIZE OF FILE: " + fileContents.length());
//                  System.out.println("HASH: " + message);
//                  System.out.println("FILE CONTENTS: " + fileContents);
                  
                  System.out.println("THE FILE " + fileName + " WAS CORRECTLY SIGNED.");
            }
            catch(Exception e)
            {
                  System.out.println("Error writing to file.");
            }
      }
      
      private static void verify(String fileName, BigInteger e, BigInteger n) throws FileNotFoundException, IOException
      {
            char lineSep = '\t';
            String message = null;
            String fileContents = null;
            int messageSize;
            int fileSize;
            String hashMessage = null;
            String compareThis = null;
             
            DataInputStream dis = new DataInputStream(new FileInputStream(fileName));
            
            messageSize = dis.readInt();
            dis.readChar();
            fileSize = dis.readInt();
            dis.readChar();
            
            char chr;
            StringBuilder item = new StringBuilder(messageSize);
            while ((chr = dis.readChar()) != lineSep)
            {
                  item.append(chr);
            }
            message = item.toString();
           
            item = new StringBuilder(fileSize);
            while ((chr = dis.readChar()) != lineSep)
            {
                  item.append(chr);
            }
            fileContents = item.toString();
                
            dis.close();
            
            hashMessage = new BigInteger(message).modPow(e, n).toString();
            
            try 
            {
	MessageDigest md = MessageDigest.getInstance("SHA-256");
                  md.update(fileContents.getBytes());
                  byte[] digest = md.digest();
                  BigInteger temp = new BigInteger(1, digest);
                  compareThis = new BigInteger(1, digest).toString();
            }
            catch(NoSuchAlgorithmException ex) 
            {
                  System.out.println(ex.toString());
            }
            
//            //FOR TESTING
//            System.out.println("SIZE OF HASH: " + messageSize);
//            System.out.println("SIZE OF FILE: " + fileSize);
//            System.out.println("HASH: " + message);
//            System.out.println("FILE CONTENTS: " + fileContents);
//            System.out.println("NEW HASH: " + hashMessage);
            
            if(hashMessage.compareTo(compareThis) == 0)
            {
                  System.out.println("THE FILE CONTAINS A VALID SIGNITURE.");
            }
            else
            {
                   System.out.println("THE FILE CONTAINS AN INVALID SIGNITURE.");
            }        
      }      
      
      private static BigInteger[] readPubKey() 
      {
            String fileName = "pubkey.rsa";
            byte[] eAsBytes = new byte[129];
            byte[] nAsBytes = new byte[129];
            
            try
            {
                  Path path = Paths.get(fileName);
                  byte[] contents = Files.readAllBytes(path);
            
                  for(int i = 0; i < 129; i++)
                  {
                        eAsBytes[i] = contents[i + 9];
                        nAsBytes[i] = contents[i + 9 + 129];
                  }
            }
            catch (IOException e)
            {
                  System.out.println("Error. pubkey.rsa not found");
            }
            
//            //FOR TESTING
//            System.out.println("E: " + new BigInteger(eAsBytes).toString());  
//            System.out.println("N: " + new BigInteger(nAsBytes).toString());
            
            return new BigInteger[] {new BigInteger(eAsBytes), new BigInteger(nAsBytes)};
      }
      
      private static BigInteger[] readPrivKey() 
      {
            String fileName = "privkey.rsa";
            byte[] dAsBytes = new byte[129];
            byte[] nAsBytes = new byte[129];
           
            try
            {
                  Path path = Paths.get(fileName);
                  byte[] contents = Files.readAllBytes(path);
            
                  for(int i = 0; i < 129; i++)
                  {
                        dAsBytes[i] = contents[i + 9];
                        nAsBytes[i] = contents[i + 9 + 129];
                  }
            }
            catch (IOException e)
            {
                  System.out.println("Error. privkey.rsa not found");
            }
          
//            //FOR TESTING
//            System.out.println("D: " + new BigInteger(dAsBytes).toString()); 
//            System.out.println("N: " + new BigInteger(nAsBytes).toString()); 
            
             return new BigInteger[] {new BigInteger(dAsBytes), new BigInteger(nAsBytes)};
      
      }
}