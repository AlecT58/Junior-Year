import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Alec Trievel
 * Note: this is a modified version of the project I completed for the Spring 2016 semester
 */
public class RsaSign 
{
      public static void main(String[] args) 
      {
            try
            {
                  switch (args[0]) 
                  {
                        case "s":
                                    BigInteger[] priv = readKeys("privkey.rsa");    //read e and n from the private key file
                                    sign(args[1], priv[0], priv[1]);    //sign the file using e and n
                              break;
                        case "v":
                                    BigInteger[] pub = readKeys("pubkey.rsa"); //read d and n from the public key file
                                    verify(args[1], pub[0], pub[1]);    //verify a signed file by using d and n
                              break;
                        default:
                              throw new IllegalArgumentException();
                  }
            }
            catch (Exception e)
            {
                  System.out.println("Error. Invalid command line arguments or bad file IO. Program terminating...");
                  System.exit(0);
            }
      }
      
      private static BigInteger[] readKeys(String filename)
      {
            byte[] e_dAsBytes = new byte[129];  //will either be e or d depending on the file name
            byte[] nAsBytes = new byte[129];
            
            try
            {
                  Path path = Paths.get(filename);
                  byte[] contents = Files.readAllBytes(path);     //read everything from the file 
            
                  for(int i = 0; i < 129; i++)
                  {
                        e_dAsBytes[i] = contents[i + 9];          //e or d will be the first 129 bytes plus 9 bytes for a file header offset
                        nAsBytes[i] = contents[i + 9 + 129];      //n is the next 129 bytes
                  }
            }
            catch (IOException e)
            {
                  System.out.println("Error. RSA files not found. Try generating them using RsaKeyGen.java first. Program terminating...");
                  System.exit(0);
            }

            return new BigInteger[] {new BigInteger(e_dAsBytes), new BigInteger(nAsBytes)};
      }

      private static void sign(String filename, BigInteger d, BigInteger n) 
      {    	
            String message = null;
            String fileContents = null;
            
            try 
            {
                  Path path = Paths.get(filename);
                  byte[] data = Files.readAllBytes(path);
                  fileContents = new String(data);
                  
                  //below is provided code
	MessageDigest md = MessageDigest.getInstance("SHA-256");
                  md.update(fileContents.getBytes());
                  byte[] digest = md.digest();
                  BigInteger temp = new BigInteger(1, digest);
                  message = new BigInteger(1, digest).toString();
            }
            catch(Exception e) 
            {
                  System.out.println("Error involving file IO. Program terminating...");
                  System.exit(0);
            }
            
            message = new BigInteger(message).modPow(d, n).toString();  //raise the hash to dth power mod n
            
            String[] contentsToWrite = {message, fileContents};
            
            try
            {
                  DataOutputStream dos = new DataOutputStream(new FileOutputStream(filename + ".sig"));
                  
                  dos.writeInt(message.length());  //write the length of the hash to the file to make reading easier later
                  dos.writeChar('\t');
                  
                  dos.writeInt(fileContents.length()); //write the length of the orginal file to the file to make reading easier later
                  dos.writeChar('\t');
                 
                  dos.writeChars(contentsToWrite[0]);
                  dos.writeChar('\t');
                  
                  dos.writeChars(contentsToWrite[1]); 
                  dos.writeChar('\t');
                        
                  dos.close();
                  
                  System.out.println("THE FILE \"" + filename + "\" WAS CORRECTLY SIGNED.");
            }
            catch(Exception e)
            {
                  System.out.println("Error involving file IO. Program terminating...");
                  System.exit(0);
            }
      }
       
      private static void verify(String fileName, BigInteger e, BigInteger n) throws FileNotFoundException, IOException
      {
            char lineSep = '\t';    //the line separator used when signing the file
            String message = null, fileContents = null, hashMessage = null, compareThis = null;
            int messageSize, fileSize;
             
            DataInputStream dis = new DataInputStream(new FileInputStream(fileName));
            
            messageSize = dis.readInt();        //the size of the has
            dis.readChar();   //read the line separator
            fileSize = dis.readInt();     //size of the orginal file
            dis.readChar();   //read the line separator
            
            char chr;
            StringBuilder item = new StringBuilder(messageSize);  //new stringbuilder the size of the hash
            
            while ((chr = dis.readChar()) != lineSep)       //keep appending the hash character by character
            {
                  item.append(chr);
            }
            message = item.toString();
           
            item = new StringBuilder(fileSize); //new stringbuilder the size of the orginal file
            
            while ((chr = dis.readChar()) != lineSep)       //keep appending the orginal file character by character
            {
                  item.append(chr);
            }
            fileContents = item.toString();
                
            dis.close();
            
            hashMessage = new BigInteger(message).modPow(e, n).toString();          //raise the hash to the e power mod n
            
            try //regenerate the hash on the original file to compare the old hash to this one
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
            
            if(hashMessage.compareTo(compareThis) == 0)     //compare the old hash to the new, print results
            {
                  System.out.println("THE FILE CONTAINS A VALID SIGNITURE.");
            }
            else
            {
                   System.out.println("THE FILE CONTAINS AN INVALID SIGNITURE.");
            }        
      }       
}
