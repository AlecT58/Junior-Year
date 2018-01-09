/**
 * @author Alec Trievel
 */
public class CarData  
{
      private String vin;                       //the VIN of the car (expressed as a String)
      private String make;                  //the Make of the car (e.g. Ford, Toyota, Honda)               
      private String model;                 //the Model of the car (e.g. Fiesta, Camry, Civic)
      private double price;                //the Price of the car (in $)
      private double mileage;            //the Mileage of the car (in miles)
      private String color;                  //the Color of the car (expressed as a String)
      
      /**
       * Constructor method for creating a car and then adding it to the queue
       * @param vin the desired VIN for this car
       * @param make the make of the car
       * @param model the model of the car
       * @param price the price expressed in dollars as a double of the car
       * @param mileage the mileage expressed in miles as a double of the car
       * @param color  the color of the car
       */
      public CarData(String vin, String make, String model, double price, double mileage, String color)
      {
            this.vin = vin;
            this.make = make;
            this.model = model;
            this.price = price;
            this.mileage = mileage;
            this.color = color;
      }
      
      /**
       * Used to update the VIN of the car
       * @param vin the new VIN
       */
      public void addVin(String vin)
      {
            this.vin = vin;
      }
      
      /**
       * Used to update the make of the car
       * @param make the new make
       */
      public void addMake(String make)
      {
            this.make = make;
      }
      
      /**
       * Used to update the model of the car
       * @param model the new model
       */
      public void addModel(String model)
      {
            this.model = model;
      }
      
      /**
       * Used to update the price of the car
       * @param price the new price
       */
      public void addPrice(double price)
      {
            this.price = price;
      }
      
      /**
       * Used to update the mileage of the car
       * @param mileage the new mileage
       */
      public void addMileage(double mileage)
      {
            this.mileage = mileage;
      }
      
      /**
       * Used to update the color of the car
       * @param color the new color
       */
      public void addColor(String color)
      {
            this.color = color;
      }
      
      /**
       * Returns the String of the VIN
       * @return the VIN of the car
       */
      public String getVin()
      {
            return this.vin;
      }
      
      /**
       * Returns the String of the make 
       * @return the make of the car
       */
      public String getMake()
      {
            return this.make;
      }
      
      /**
       * Returns the String of the model 
       * @return the model of the car
       */
      public String getModel()
      {
            return this.model;
      }
      
      /**
       *  Returns the double of the price
       * @return the price of the car in dollars
       */
      public double getPrice()
      {
            return this.price;
      }
      
      /**
       * Returns the double of the mileage
       * @return the mileage of the car in miles
       */
      public double getMileage()
      {
            return this.mileage;
      }
      
      /**
       * Returns the String of the color
       * @return the color of the car
       */
      public String getColor()
      {
            return this.color;
      }
      
      public String getMakeAndModel()
      {
            return this.make + " " + this.model;
      }
      
      /**
       * Displays all the details about the car
       */
      public void displayDetails()
      {
            System.out.println("VIN: " + this.vin +
                                       "\nMake and Model: " + this.make + " " + this.model +
                                       "\nPrice: $" + this.price + 
                                        "\nMiles: " + this.mileage + " mi " + 
                                        "\nColor: " + this.color + "\n");
      }
}