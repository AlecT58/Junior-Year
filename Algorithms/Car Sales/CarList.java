import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Alec Trievel
 */
public class CarList 
{
    private HashMap indirection = new HashMap<String, CarData>();
    private HashMap minPrices = new HashMap<String, CarData>();
    private HashMap minMiles = new HashMap<String, CarData>();
    private MinPQ price = new MinPQ(5, true);
    private MinPQ miles = new MinPQ(5, false);
    
      public CarList()
      {};

      /**
       * Adds a car to the indirection and queue
       * @param aCar
       * @return true if added, false otherwise
       */
      public boolean add(CarData aCar)
      {
          if(indirection.containsKey(aCar.getVin()))
          {
              return false;
          }
          else
          {
              indirection.put(aCar.getVin(), aCar);
              price.insert(aCar);
              miles.insert(aCar);
              updateMins();
              return true;
          }
      }
    
      /**
       * Removes a car by the specified vin
       * @param vin
       * @return true if removed, false otherwise
       */
      public boolean remove(String vin)
      {
            CarData temp = (CarData) indirection.remove(vin);
            price = new MinPQ(5, true);
            miles = new MinPQ(5, false);

            if(temp == null)
            {
                  return false;
            }
            else
            {
                    price = new MinPQ(5, true);
                    miles = new MinPQ(5, false);
                    Iterator<Map.Entry<String, CarData>> iterator = indirection.entrySet().iterator();

                    while(iterator.hasNext())
                    {
                          Map.Entry<String, CarData> current = iterator.next();
                          price.insert(current.getValue());
                          miles.insert(current.getValue());
                    }

                    updateMins();
                    return true;
            }


      }

      /**
       *  updates a specific car 
       * @param vin 
       * @param newPrice set to new price if user edited that, -1 otherwise
       * @param newMiles set to new miles if user edited that, -1 otherwise
       * @param newColor set to new color if user edited that, null otherwise
       * @return true if updated, false otherwise
       */
      public boolean update(String vin, double newPrice, double newMiles, String newColor)
      {
            if(!indirection.containsKey(vin))
            {
                  return false;
            }
            else
            {
                  price = new MinPQ(5, true);
                  miles = new MinPQ(5, false);
                  CarData temp = (CarData) indirection.remove(vin);

                  if(newPrice > 0)
                  {
                        temp.addPrice(newPrice);
                  }
                  else if(newMiles > 0)
                  {
                        temp.addMileage(newMiles);
                  }
                  else if(newColor != null)
                  {
                        temp.addColor(newColor);
                  }

                  indirection.put(temp.getVin(), temp);
                  price = new MinPQ(5, true);
                  miles = new MinPQ(5, false);
                  Iterator<Map.Entry<String, CarData>> iterator = indirection.entrySet().iterator();

                  while(iterator.hasNext())
                  {
                        Map.Entry<String, CarData> current = iterator.next();
                        price.insert(current.getValue());
                        miles.insert(current.getValue());
                  }

                  updateMins();
                  return true;
            }
      }

      /**
       *  Returns lowest values
       * @param priceOrMiles true if searching for price, false for miles
       * @return the specified car
       */
      public CarData displayMin(boolean priceOrMiles)
      {
          if(priceOrMiles)
          {
                return (CarData) price.min();
          }
          else
          {
                 return (CarData) miles.min();
          }
      }

      /**
       *  Returns lowest values
       * @param makeAndModel the concat string of make and model
       * @param priceOrMiles priceOrMiles true if searching for price, false for miles
       * @return 
       */
      public CarData displayMin(String makeAndModel, boolean priceOrMiles)
      {
            if(priceOrMiles)
          {
                return (CarData) minPrices.get(makeAndModel);
          }
          else
          {
                 return (CarData) minMiles.get(makeAndModel);
          }
      }
       
      /**
       * If the queue is empty, return true
      */
      public boolean isEmpty()
      {
            return price.isEmpty();
      }
      
      /**
       * Helper function that uses the indirection HashMap to find a value
       * @param vin the vin of the car to search for
       * @return the car if found, null otherwise
       */
      public CarData find(String vin)
      {
          return (CarData) indirection.get(vin);
      }

      /**
       * Updates the mins by make and  model lists
       */
      private void updateMins()
      {
            minPrices = new HashMap<String, CarData>();
            minMiles = new HashMap<String, CarData>();
            Iterator<Map.Entry<String, CarData>> iterator = indirection.entrySet().iterator();

            while(iterator.hasNext())
            {
                  Map.Entry<String, CarData> current = iterator.next();
                  
                  if(!minPrices.containsKey(current.getValue().getMakeAndModel()))
                  {
                        minPrices.put(current.getValue().getMakeAndModel(), current.getValue());
                  }
                  else
                  {
                        CarData temp = (CarData) minPrices.remove(current.getValue().getMakeAndModel());
                        if(temp.getPrice() > current.getValue().getPrice())
                        {
                              minPrices.put(current.getValue().getMakeAndModel(), current.getValue());
                        }
                        else
                        {
                              minPrices.put(temp.getMakeAndModel(), temp);
                        }
                  }
                  
                  if(!minMiles.containsKey(current.getValue().getMakeAndModel()))
                  {
                        minMiles.put(current.getValue().getMakeAndModel(), current.getValue());
                  }
                  else
                  {
                        CarData temp = (CarData) minMiles.remove(current.getValue().getMakeAndModel());
                        if(temp.getMileage() > current.getValue().getMileage())
                        {
                              minMiles.put(current.getValue().getMakeAndModel(), current.getValue());
                        }
                        else
                        {
                              minMiles.put(temp.getMakeAndModel(), temp);
                        }
                  }
            }
      }
}