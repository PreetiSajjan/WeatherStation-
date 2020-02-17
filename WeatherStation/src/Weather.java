import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Implementing class WeatherStation to analyze the data produced by a couple of weather stations.
 * @author Preeti Sajjan
 */
class WeatherStation {
	
	String city;  //Station location
	List<Measurement> measurements;  //List of measurement objects
	static List<WeatherStation> stations = new ArrayList<>();
	
	/**
	 * Defining a parameterized constructor to initialize the values
	 * @param city is the location of the station
	 * @param measurements is list of objects of class Measurements
	 */
	public WeatherStation(String city, List<Measurement> measurements) {
		this.city = city;
		this.measurements = measurements;
	}
	
	/**
	 * Method calculating the number of times a temperature in given interval [t-r,...,t+r]
	 * has been measured so far by any of the weather stations
	 * @param t1 is first interval
	 * @param t2 is second interval
	 * @param r is offset to be added and subtracted in the interval
	 * @return (key, value) pair as (t, count)
	 */
	public static Map<Double, Integer> countTemperatue(double t1, double t2, double r){
			
		
		List<KeyValue> list = new ArrayList<>();
		//double key;
		//creating a parallel stream for stations for temperature interval [t1-r,...,t1+r]
		stations.stream()
		.flatMap(station -> station.getMeasurements().parallelStream()) //streaming each station
				.forEach(x -> {
					double temp = x.getTemp();
					//checking temperature based on the interval and adding an entry if present
					if((t1-r) <= temp && temp <= (t1+r)) {
						list.add(new KeyValue(t1,1));
					}
					if((t2-r) <= temp && temp <= (t2+r)) {
						list.add(new KeyValue(t2,1));
					}				
				});
		
		//Creating a map to following shuffle with groupingBy and reduce with summingInt operation
		Map<Double, Integer> ShuffleReduce = new HashMap<>();		
		ShuffleReduce = list.parallelStream().collect(Collectors.groupingBy(KeyValue::getKey, Collectors.summingInt(KeyValue::getValue)));
		
		//returning the result
		return ShuffleReduce;		
	}
	
	/**
	 * Method to get measurement
	 * @return a list of objects of class Measurement
	 */
	public List<Measurement> getMeasurements() {
		return measurements;
	}
	
	/**
	 * Method to get city
	 * @return city
	 */
	public String getCity() {
		return city;
	}		
	
	public static void main(String args[]) {
		
		//Creating objects of class Measurement
		Measurement m1 = new Measurement(9, 20.0);
		Measurement m2 = new Measurement(15, 11.7);
		Measurement m3 = new Measurement(18, -5.4);
		Measurement m4 = new Measurement(14, 18.7);
		Measurement m5 = new Measurement(23, 20.9);
		
		//Creating list1 and adding the created Measurement objects to it
		List<Measurement> list1 = new ArrayList<>();
		list1.add(m1);
		list1.add(m2);
		list1.add(m3);
		list1.add(m4);
		list1.add(m5);
		
		//Creating our first WeatherStation
		WeatherStation WStation1 = new WeatherStation("Galway", list1);
		stations.add(WStation1); //adding our first WeatherStation to list of stations		
		Measurement measure1 = new Measurement(list1);
		
		//Calling maxTemperature to calculate the maximum temperature measured by weather station
		Double d1 = measure1.maxTemperature(12, 20);
		System.out.println("The maximum temperation in first Weather Station is "+d1);
		
		//Creating objects of class Measurement
		Measurement l1 = new Measurement(9, 8.4);
		Measurement l2 = new Measurement(15, 19.2);
		Measurement l3 = new Measurement(18, 7.2);
		
		//Creating list2 and adding the created Measurement objects to it
		ArrayList<Measurement> list2 = new ArrayList<Measurement>();
		list2.add(l1);
		list2.add(l2);
		list2.add(l3);
				
		//Creating our second WeatherStation
		WeatherStation WStation2 = new WeatherStation("Dublin", list2);
		stations.add(WStation2);   //adding our second WeatherStation to list of stations		
		Measurement measure2 = new Measurement(list2);
		
		//Calling maxTemperature to calculate the maximum temperature measured by weather station
		Double d2 = measure2.maxTemperature(12, 20);
		System.out.println("The maximum temperation in second Weather Station is "+d2);
		
		//Storing and displaying our result
		Map<Double, Integer> result = countTemperatue(19.0, 10.8, 2.1);	
		System.out.println();
		for(Entry<Double, Integer> entry : result.entrySet()) {
			System.out.print("["+entry.getKey()+", "+entry.getValue()+"]");
		}
		
	}
}