package com.example.paw1;

import java.util.List;

public class Shelters {
    private String name;
    private String city;
    private String phone;
    private String species;        // "dog" or "cat"
    private List<String> breeds;   // e.g., ["Beagle","Poodle"]
    private double lat;
    private double lng;

    // Required empty constructor for Firestore
    public Shelters() {}

    // Optional: full constructor for convenience
    public Shelters(String name, String city, String phone,
                    String species, List<String> breeds,
                    double lat, double lng) {
        this.name = name;
        this.city = city;
        this.phone = phone;
        this.species = species;
        this.breeds = breeds;
        this.lat = lat;
        this.lng = lng;
    }

    // Getters
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getPhone() { return phone; }
    public String getSpecies() { return species; }
    public List<String> getBreeds() { return breeds; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setCity(String city) { this.city = city; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setSpecies(String species) { this.species = species; }
    public void setBreeds(List<String> breeds) { this.breeds = breeds; }
    public void setLat(double lat) { this.lat = lat; }
    public void setLng(double lng) { this.lng = lng; }
}
