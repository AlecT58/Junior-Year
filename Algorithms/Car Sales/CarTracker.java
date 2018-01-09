import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author Alec Trievel
 */
public class CarTracker 
{
      public static void main(String[] args) 
      {            
            CarList CARS = new CarList();
            Scanner in = new Scanner(System.in);
            int menu = -1;
            
            while(menu != 0)  //menu options for the user
            {
                  System.out.println("Select an option: ");
                  System.out.println("1  -  Add a car");
                  System.out.println("2  -  Update a car's data");
                  System.out.println("3  -  Remove a car");
                  System.out.println("4  -  Display the lowest-priced car");
                  System.out.println("5  -  Display the car with the lowest amount of miles");
                  System.out.println("6  -  Display the lowest-priced car of a specific make and model");
                  System.out.println("7  -  Display the lowest-mileage car of a specific make and model");
                  System.out.println("0  -  Exit the program");
                  System.out.print("Your choice: ");
                  
                  menu = in.nextInt();
                  
                  switch (menu) 
                  {
                        case 0:
                              System.out.println("\nProgram terminating..."); //end program
                              System.exit(0);
                        case 1:
                              addNewCar(CARS);  //add new car
                              break;
                        case 2:
                              updateCar(CARS);  //update car (list cannot be empty)
                              break;
                        case 3:
                              removeCar(CARS);  //remove car (list cannot be empty)
                              break;
                        case 4:
                              displayLowestCar(CARS, 0);    //next four options display the lowest cars by a specific value
                              break;
                        case 5:
                              displayLowestCar(CARS, 1);
                              break;
                        case 6:
                              displayLowestCar(CARS, 2);
                              break;
                        case 7:
                              displayLowestCar(CARS, 3);
                              break;
                        default:
                              System.out.println("\n Error. Invalid menu option."); //reset the menu
                              menu = -1;
                              break;                       
                  }
            }
      }
             
      /**
       *  Helper function to collect data of a new car and add it to the collection
       * @param cars the list of cars
       */
      private static void addNewCar(CarList cars)
      {
            System.out.println();
            Scanner in = new Scanner(System.in);
            boolean done = false;
            String userVIN = null, userMake = null, userModel = null, userColor = null;
            double userPrice = 0, userMiles = 0;
            
            while(!done)            //gets the VIN from the user and make sure it s of a vlaid format
            {
                  System.out.print("Enter the VIN of the new car: "); 
                  userVIN = in.next();
                  
                  if(!checkVINValidity(userVIN))
                  {
                        System.out.println("Invalid entry. VIN must be a 17 character string of numbers and capital letters (but no I (i), O (o), or Q (q)).");
                        done = false;
                  }
                  else
                  {
                        done = true;
                  }
            }
            
            System.out.print("Enter the make of the new car: ");  //gets the make 
            userMake = in.next();
            

            System.out.print("Enter the model of the new car: "); //gets the model 
            userModel = in.next();

            in.nextLine();
            done = false;
            while(!done)           //gets the price, loops until valid double
            {
                  try
                  {
                        System.out.print("Enter the price of the new car (must be a number): ");
                        userPrice = in.nextDouble();
                        done = true;
                  }
                  catch (Exception e)
                  {
                        System.out.println("Error. Value must be a number."); 
                        in.next();
                        done = false;
                  }
            }
            
            in.nextLine();
            done = false;
            while(!done)            //gets the mileage, loops until valid double
            {
                  try
                  {
                        System.out.print("Enter the mileage of the new car (must be a number): ");
                        userMiles = in.nextDouble();
                        done = true;
                  }
                  catch (Exception e)
                  {
                        System.out.println("Error. Value must be a number.");
                        in.next();
                        done = false;
                  }
            }
            
            System.out.print("Enter the color of the new car: "); //gets the color
            userColor = in.next();
            
            CarData newCar = new CarData(userVIN, userMake, userModel, userPrice, userMiles, userColor);    //adds car to list and displays the new information
            if(cars.add(newCar))
            {
                  System.out.println("\nHere is the information for the new car you entered: ");
                  newCar.displayDetails();
            }
            else
            {
                  System.out.println("\n Error adding new car to the list. The car may already be in the collection.");
            }
      }
      
      /**
       *  Helper function to collect data of an updated car and make changes to the collection
       */
      private static void updateCar(CarList cars)
      {
            System.out.println();
            
            if(cars.isEmpty())
            {
                  System.out.println("Error. There are no cars in the colection. Try adding one first.\n");
                  return;
            }

            Scanner in = new Scanner(System.in);
            boolean done = false;
            String userVIN = null, userColor = null;
            double userPrice = 0, userMiles = 0;
            int menu = -1;
            
            while(!done)            //gets the VIN from the user
            {
                  System.out.print("Enter the VIN of the car you wish to update: "); 
                  userVIN = in.next();
                  
                  if(!checkVINValidity(userVIN))
                  {
                        System.out.println("Invalid entry. VIN must be a 17 character string of numbers and capital letters (but no I (i), O (o), or Q (q)).");
                        done = false;
                  }
                  else
                  {
                        done = true;
                  }
            }
            
            if(cars.find(userVIN) == null)
            {
                  System.out.println("Error. Car not in collection. Please try adding it first.\n");
                  return;
            }
            
            while(menu != 0)
            {
                  System.out.println("\nSelect an option: ");
                  System.out.println("1  -  Update car's price");
                  System.out.println("2  -  Update car's miles");
                  System.out.println("3  -  Update car's color");
                  System.out.println("0  -  Cancel update");
                  System.out.print("Your choice: ");
                  
                  menu = in.nextInt();
                  in.nextLine();
                  done = false;
                  
                  switch (menu)
                  {
                        case 0:
                              System.out.println("\nUpdate canceled.");
                              menu = 0;
                              return;
                        case 1:
                              while(!done)            
                              {
                                    try
                                    {
                                          System.out.print("\nEnter the updated price of the car (must be a number): ");
                                          userPrice = in.nextDouble();
                                          done = true;
                                    }
                                    catch (Exception e)
                                    {
                                          System.out.println("Error. Value must be a number.");
                                          in.next();
                                          done = false;
                                    }
                              }
                              menu = 0;
                              cars.update(userVIN, userPrice, -1, null);
                              break;
                        case 2:
                              while(!done)            
                              {
                                    try
                                    {
                                          System.out.print("\nEnter the updated mileage of the car (must be a number): ");
                                          userMiles = in.nextDouble();
                                          done = true;
                                    }
                                    catch (Exception e)
                                    {
                                          System.out.println("Error. Value must be a number.");
                                          in.next();
                                          done = false;
                                    }
                              }
                              menu = 0;
                              cars.update(userVIN, -1,userMiles, null);
                              break;
                        case 3:
                              System.out.print("\nEnter the new color of the car: ");
                              userColor = in.next();
                              menu = 0;
                              cars.update(userVIN, -1, -1, userColor);
                              break;
                        default:
                              System.out.println("Error. Invalid menu option. Please try again.\n");
                              menu = -1;
                              break;
                  }
            }
            
            System.out.println("\nHere is the information about the car you just updated: ");
            cars.find(userVIN).displayDetails();
      }
      
      /**
       *  Helper function to remove a car from the collection
       * @param cars the list of cars
       */
      private static void removeCar(CarList cars)
      {
            System.out.println();
            
            if(cars.isEmpty())
            {
                  System.out.println("Error. There are no cars in the colection. Try adding one first.\n");
                  return;
            }
            
            Scanner in = new Scanner(System.in);
            boolean done = false;
            String userVIN = null;
            
            while(!done)            //gets the VIN from the user
            {
                  System.out.print("Enter the VIN of the car to remove: "); 
                  userVIN = in.next();
                  
                  if(!checkVINValidity(userVIN))
                  {
                        System.out.println("Invalid entry. VIN must be a 17 character string of numbers and capital letters (but no I (i), O (o), or Q (q)).");
                        done = false;
                  }
                  else
                  {
                        done = true;
                  }
            }
            
            if(!cars.remove(userVIN))
            {
                  System.out.println("Error removing the car from the list.");
            }
            else
            {
                  System.out.println("Car with VIN " + userVIN + " removed successfully.\n");
            }
      }
      
      /**
       *  Helper function to display the min of some specific value/car
       * @param cars the list of cars
       * @param option 0 is min price, 1 is min miles, 2 is min miles w/make, 3 is min price w/make
       */
      private static void displayLowestCar(CarList cars, int option) 
      {
            System.out.println();
            
            if(cars.isEmpty())
            {
                  System.out.println("Error. There are no cars in the colection. Try adding one first.\n");
                  return;
            }
            
            Scanner in = new Scanner(System.in);
            CarData temp = null;
            String make = null, model = null;
            switch (option)
            {
                  case 0:
                        temp = cars.displayMin(true);
                        System.out.println("Here is the information about the car with the lowest price:");
                        temp.displayDetails();
                        break;
                  case 1:
                        temp = cars.displayMin(false);
                        System.out.println("Here is the information about the car with the lowest mileage:");
                        temp.displayDetails();
                        break;
                  case 2:
                        System.out.print("Enter the make of the car: ");
                        make = in.next();
                        System.out.print("Enter the model of the car: ");
                        model = in.next();
                        temp = cars.displayMin(make + " " + model, true);
                        System.out.println("\nHere is the information about the " + make + " " + model  + " with the lowest price:");
                        temp.displayDetails();
                        break;
                  case 3:
                        System.out.print("Enter the make of the car: ");
                        make = in.next();
                        System.out.print("Enter the model of the car: ");
                        model = in.next();
                        temp = cars.displayMin(make + " " + model, false);
                        System.out.println("\nHere is the information about the " + make + " " + model + " with the lowest mileage:");
                        temp.displayDetails();
                        break;
                  default:
                        break;
            }
                        
      }
      
      /**
       *  Checks to make sure if a user VIN is alphanumeric, is 17 characters long, and does not contain any I's, O's, or Q's
       * @param aVin a VIN to be checked
       * @return true if valid, false if not
       */
      private static boolean checkVINValidity(String aVin)
      {
            Pattern p = Pattern.compile("[^a-zA-Z0-9]");
            return !(aVin.length() != 17 || aVin.contains("o") ||  aVin.contains("O") || aVin.contains("i") ||  aVin.contains("I") || aVin.contains("q") ||  aVin.contains("Q") || p.matcher(aVin).find());
      }
}
