import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author Alec Trievel
 * Note: this code is modified from my project completed in the Spring 2016 semester
 */
public class RsaKeyGen 
{
      public static void main(String[] args) 
      {
            BigInteger e = null;
            BigInteger d = null;
            BigInteger n = null;
            
            System.out.println("GENERATING KEYS");
            
            do
            {
                  BigInteger p = new BigInteger(512, 20, new Random());             //creates p of size 512 bits with only a 1/(2^20) chance of not being prime
                  BigInteger q = new BigInteger(512, 20,  new Random());            //creates q of size 512 bits with only a 1/(2^20) chance of not being prime
                  n = p.multiply(q);                                                                     //n is the result of p*q
                  BigInteger phi = p.subtract(BigInteger.valueOf(1));
                  phi = phi.multiply(q.subtract(BigInteger.valueOf(1)));                   //phi(n) = (p-1) * (q-1)

                  do
                  {
                        e = new BigInteger(1024, new Random());
                  }
                  while((e.compareTo(phi) != 1) || (e.gcd(phi).compareTo(BigInteger.valueOf(1)) != 0));

                   d = e.modInverse(phi);                                                           //creates d such that: (d * e) = 1 mod phi(n)
                   
            }while(d.toByteArray().length < 129);                                            //loops until e, n, and d have no leading zeros
            
            //three arrays below are the representations of the BigIntegers as byte arrays.
            //they will be combined before writing to their respective files 
            byte[] eAsByte = e.toByteArray();  
            byte[] nAsByte = n.toByteArray(); 
            byte[] dAsByte = d.toByteArray();
            byte[] publicData = new byte[258];
            byte[] privateData = new byte[258];

            for(int i = 0; i < 129; i++)  //combines e and n for the public key
            {
                  publicData[i] = eAsByte[i];
                  publicData[i + 129] = nAsByte[i];
            }
            
            for(int i = 0; i < 129; i++)  //combines d and n for the private key
            {
                  privateData[i] = dAsByte[i];
                  privateData[i + 129] = nAsByte[i];
            }
            
            FileOutputStream publicOut;
            FileOutputStream privateOut;
            ObjectOutputStream stream = null;
            ObjectOutputStream stream2 = null;
               
            try   //write everything out to the correct files
            {
                  publicOut = new FileOutputStream("pubkey.rsa");
                  stream = new ObjectOutputStream(publicOut);
                  stream.write(publicData);
                  stream.close();
               
                  privateOut = new FileOutputStream("privkey.rsa");
                  stream2 = new ObjectOutputStream(privateOut);
                  stream2.write(privateData);
                  stream2.close();
                  System.out.println("ALL KEYS GENERATED CORRECTLY"); 
                  
                  stream.close();
                  stream2.close();
            }
            catch(Exception exception)
            {
                  System.out.println("Error writing to file. Program Terminating...");
            }
      }
      
}
