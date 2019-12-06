package ro.mta.se.lab.weather;

import ro.mta.se.lab.city.City;
import ro.mta.se.lab.factory.ExceptionFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ro.mta.se.lab.factory.IException;

public class Weather {
    private static Weather INSTANCE = null;
    private ArrayList<City> cityArrayList;
    private ArrayList<Integer> cityIdList;
    private ArrayList<String> countryArrayList;
    private String fileName;

    private Weather(String fileName) {
        try {
            this.fileName=fileName;
            readInputFile(fileName);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public static Weather getINSTANCE(String fileName) {
        if (INSTANCE == null) {
            INSTANCE = new Weather(fileName);
        }
        return INSTANCE;
    }

    private void readInputFile(String fileName) throws IOException {
        try {
            if (fileName.isEmpty()) {
                IException exception = ExceptionFactory.getException("FileEmpty");
                exception.throwException("Input fileName is empty");
            }

            ArrayList<String> fileLines = new ArrayList<String>();
            String line;

            FileInputStream fis = new FileInputStream(fileName);

            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            //String validPattern = "^[\\d]+[\\t](([\\S+]+)|(\\S+)[ ](\\S+))[\\t](([-][\\d]{1,3})|([\\d]){1,3})[.][\\d]{6}[\\t](([-][\\d]{1,3})|([\\d]){1,3})[.][\\d]{6}[\\t][A-Z]{2}";
            String validPattern = "^[\\d]+[\\t](([\\S+]+)|(((\\S+)[ ]){1,6}(\\S+))|((\\S+)[ ]){1,5}[/][ ]((\\S+)[ ]){1,5}(\\S+))[\\t](([-][\\d]{1,3})|([\\d]){1,3})[.][\\d]{6}[\\t](([-][\\d]{1,3})|([\\d]){1,3})[.][\\d]{6}[\\t][A-Z]{2}";

            Pattern pattern = Pattern.compile(validPattern);

            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (!matcher.find()) {
                    IException exception = ExceptionFactory.getException("InvalidInput");
                    exception.throwException("Error on line " + lineNumber);
                } else {
                    fileLines.add(line);
                }
                lineNumber++;
            }
            populateCityArrayList(fileLines);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void populateCityArrayList(ArrayList<String> fileLine) {
        if (fileLine == null) {
            IException exception = ExceptionFactory.getException("NullException");
            exception.throwException("Null exception!");
        }
        if (fileLine.isEmpty()) {
            IException exception = ExceptionFactory.getException("NullException");
            exception.throwException("Null exception");
        }
        cityArrayList = new ArrayList<>();
        countryArrayList = null;
        for (String line : fileLine) {
            String[] words = line.split("\t");
            int count = 1;
            City newCity = new City();
            for (String word : words) {
                if (count % 5 == 1) {
                    newCity.setId(Integer.parseInt(word));
                } else if (count % 5 == 2) {
                    newCity.setName(word);
                } else if (count % 5 == 3) {
                    newCity.setLatitude(word);
                } else if (count % 5 == 4) {
                    newCity.setLongitude(word);
                } else if (count % 5 == 0) {
                    if (countryArrayList == null) {
                        countryArrayList = new ArrayList<>();
                        countryArrayList.add(word);
                    } else {
                        int ok = 0;
                        for (String country : countryArrayList) {
                            if (country.equals(word)) {
                                ok = 1;
                            }
                        }
                        if (ok == 0) countryArrayList.add(word);
                    }
                    newCity.setCountryCode(word);
                }
                count++;
            }
            cityArrayList.add(newCity);
        }

    }

    public ArrayList<String> getCountryArrayList() {

        Collections.sort(countryArrayList);

        return countryArrayList;
    }

    public ArrayList<String> getCitiesArrayList(String country) {
        ArrayList<String> cities = new ArrayList<>();
        cityIdList = new ArrayList<>();
        for (City city : cityArrayList) {
            if (city.getCountryCode().equals(country)) {
                cities.add(city.getName());
                cityIdList.add(city.getId());
            }
        }
        Collections.sort(cities);
        return cities;
    }

    public String getFileName() {
        return fileName;
    }
}
