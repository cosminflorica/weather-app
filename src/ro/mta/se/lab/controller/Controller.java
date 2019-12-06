package ro.mta.se.lab.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ro.mta.se.lab.weather.Weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.URLConnection;

import com.google.gson.*;
import com.google.gson.reflect.*;

public class Controller implements Initializable {

    private String country;
    private String city;
    private String units;

    private Weather weatherApp;
    @FXML
    private ComboBox<String> comboCountry;
    @FXML
    private ComboBox<String> comboCity;
    @FXML
    private ComboBox<String> comboMetrics;
    @FXML
    private Text firstRowLeftPane;
    @FXML
    private Text secondRowLeftPane;
    @FXML
    private Text textCountry;
    @FXML
    private Text textCity;
    @FXML
    private Text thirdRowLeftPane;
    @FXML
    private Text temp;
    @FXML
    private Text minmax;
    @FXML
    private Text wind;
    @FXML
    private Text humidity;
    @FXML
    private Text windDirection;
    @FXML
    private Text pressure;
    @FXML
    private Pane leftPane;
    @FXML
    private Pane rightPane;
    @FXML
    private Button refreshButton;
    @FXML
    private Button changeCityButton;
    @FXML
    private Button searchButton;
    @FXML
    private ImageView imageView;

    @FXML
    public void comboCountryAction() {
        comboCity.setPromptText("Select a city:");
        ObservableList<String> listCities = FXCollections.observableArrayList(weatherApp.getCitiesArrayList(comboCountry.getSelectionModel().getSelectedItem()));
        comboCity.setItems(listCities);
    }

    @FXML
    public void searchAction() {

        if (comboCity.getSelectionModel().isEmpty() || comboCountry.getSelectionModel().isEmpty() || comboMetrics.getSelectionModel().isEmpty()) {
            leftPane.setVisible(true);
            secondRowLeftPane.setVisible(false);
            thirdRowLeftPane.setVisible(false);
            firstRowLeftPane.setVisible(true);
            imageView.setVisible(false);
            firstRowLeftPane.setText("Please select all dropdown menus !");
            refreshButton.setDisable(true);
            changeCityButton.setDisable(true);
        } else {
            String selectedCountry = comboCountry.getSelectionModel().getSelectedItem();
            String selectedCity = comboCity.getSelectionModel().getSelectedItem();
            String selectedUnits = comboMetrics.getSelectionModel().getSelectedItem();
            refreshButton.setDisable(true);
            changeCityButton.setDisable(true);
            leftPane.setVisible(true);
            rightPane.setVisible(true);
            leftPane.setVisible(true);
            firstRowLeftPane.setVisible(true);
            secondRowLeftPane.setVisible(true);
            thirdRowLeftPane.setVisible(true);
            imageView.setVisible(true);
            firstRowLeftPane.setText(selectedCity + ", " + selectedCountry);

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = new Date();
            secondRowLeftPane.setText(dateFormat.format(date));
            try {
                this.country = selectedCountry;
                this.city = selectedCity;
                this.units = selectedUnits;
                getWeather(selectedCountry, selectedCity, selectedUnits);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            comboCity.setDisable(true);
            comboMetrics.setDisable(true);
            comboCountry.setDisable(true);
            textCity.setVisible(false);
            textCountry.setVisible(false);
            searchButton.setVisible(false);
        }
    }

    private static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = new Gson().fromJson(
                str, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );
        return map;
    }

    public void refreshAction() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = new Date();
            secondRowLeftPane.setText(dateFormat.format(date));
            getWeather(this.country, this.city, this.units);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void changeCityAction() {
        comboMetrics.setDisable(false);
        comboCity.setDisable(false);
        comboCountry.setDisable(false);
        textCity.setVisible(true);
        textCountry.setVisible(true);
        searchButton.setVisible(true);
        leftPane.setVisible(false);
        rightPane.setVisible(false);
    }

    private void getWeather(String country, String city, String units) throws IOException {
        String API_KEY = "12ba0d075ea3d0e3f073518ee90dcbd2";
        String UNITS = units;
        String LOCATION = city + "," + country;
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + LOCATION + "&appid=" + API_KEY + "&units=" + UNITS;

        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();

            Map<String, Object> respMap = jsonToMap(result.toString());

            Map<String, Object> mainMap = jsonToMap(respMap.get("main").toString());
            Map<String, Object> windMap = jsonToMap(respMap.get("wind").toString());

            List<Object> list = Arrays.asList();

            String weather = ArrayList.class.cast(respMap.get("weather")).get(0).toString();

            String weatrerId = weather.substring(weather.indexOf("=") + 1, weather.indexOf(","));
            String weatherMain = weather.substring(weather.indexOf("=", (weather.indexOf("=") + 1)) + 1, weather.indexOf(",", weather.indexOf(",") + 1));
            String weatherDescription = weather.substring(weather.indexOf("=", (weather.indexOf("=", weather.indexOf("=") + 1) + 1)) + 1, weather.indexOf(",", weather.indexOf(",", weather.indexOf(",") + 1) + 1));
            String weatherIcon = weather.substring(weather.indexOf("=", (weather.indexOf("=", weather.indexOf("=", weather.indexOf("=") + 1) + 1) + 1) + 1) + 1, weather.indexOf("}"));
            thirdRowLeftPane.setText(weatherMain + ", " + weatherDescription);
            Image image = new Image("http://openweathermap.org/img/w/" + weatherIcon + ".png");
            imageView.setImage(image);

            if (this.units.equals("metric")) {
                temp.setText("Temperature: " + mainMap.get("temp").toString() + "°");
                minmax.setText("Min: " + mainMap.get("temp_min").toString() + "°" + " - Max: " + mainMap.get("temp_max").toString() + "°");
            } else {
                temp.setText("Temperature: " + mainMap.get("temp").toString() + " K");
                minmax.setText("Min: " + mainMap.get("temp_min").toString() + "K" + " - Max: " + mainMap.get("temp_max").toString() + "K");
            }
            pressure.setText("Pressure: " + mainMap.get("pressure").toString());
            humidity.setText("Humidity: " + mainMap.get("humidity").toString());

            wind.setText("Wind speed: " + windMap.get("speed"));
            windDirection.setText("Wind direction: " + windMap.get("deg").toString());

            refreshButton.setDisable(false);
            changeCityButton.setDisable(false);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        weatherApp = Weather.getINSTANCE("resources/input.txt");

        ObservableList<String> listCountry = FXCollections.observableArrayList(weatherApp.getCountryArrayList());
        comboCountry.setItems(listCountry);

        ObservableList<String> listMetrics = FXCollections.observableArrayList("imperial", "metric");
        comboMetrics.setItems(listMetrics);

    }
}