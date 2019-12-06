package ro.mta.se.lab.city;

public class City {

    private Integer id;
    private String name;
    private String latitude;
    private String longitude;
    private String countryCode;

    public City(Integer _id, String _name, String _latitude, String _longitude, String _countryCode){
        this.id=_id;
        this.name=_name;
        this.latitude=_latitude;
        this.longitude=_longitude;
        this.countryCode=_countryCode;
    }
    public City(){
        this.id=null;
        this.name=null;
        this.latitude=null;
        this.longitude=null;
        this.countryCode=null;
    }
    public Integer getId() {
        return id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }
}
