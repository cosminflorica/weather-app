package ro.mta.se.lab.weather;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WeatherTest {


    private static List<String> testCountryList() {      //list with the first 5 countries from my input file in alphabetical order
        List<String> myarray = new ArrayList<>();
        myarray.add("AD");
        myarray.add("AE");
        myarray.add("AF");
        myarray.add("AG");
        myarray.add("AI");
        return myarray;
    }

    ;

    private static List<String> testCityList() {         //list with the first 5 cities in RO from my input
        List<String> myarray = new ArrayList<>();
        myarray.add("Abram");
        myarray.add("Abramut");
        myarray.add("Abrud");
        myarray.add("Acatari");
        myarray.add("Adamclisi");
        return myarray;
    }

    ;

    @Test
    public void getINSTANCE() throws Exception {
        Weather weatherApp = Weather.getINSTANCE("resources/input.txt");
        assertEquals("resources/input.txt", weatherApp.getFileName());
    }

    @Test
    public void getCountryArrayList() throws Exception {
        Weather weatherApp = Weather.getINSTANCE("resources/input.txt");
        assertEquals(weatherApp.getCountryArrayList().subList(0, 5), WeatherTest.testCountryList());
        //weatherApp.getCountryArrayList() returns an ArrayList with all countries in alphabetical order.
    }


    @Test
    public void getCitiesArrayList() throws Exception {
        Weather weatherApp = Weather.getINSTANCE("resources/input.txt");
        assertEquals(weatherApp.getCitiesArrayList("RO").subList(0, 5), WeatherTest.testCityList());
        //compare first 5 cities in RO from weatherApp with the first 5 cities in RO from my input file, in alphabetical order.
    }

}