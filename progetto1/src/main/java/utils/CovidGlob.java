package utils;

import java.io.Serializable;
import java.util.List;

public class CovidGlob implements Serializable {
    String continet;
    String state;
    String country;
    Double lineCoefficent;
    List<Integer> contagiati;


    public CovidGlob(String state, String country, List<Integer> contagiati) {
        this.state = state;
        this.country = country;
        this.contagiati = contagiati;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }
    


    public List<Integer> getContagiati() {
        return contagiati;
    }

    public String getContinet() {
        return continet;
    }

    public void setContinet(String continet) {
        this.continet = continet;
    }

    public void setContagiati(List<Integer> contagiati) {
        this.contagiati = contagiati;
    }

    public Double getLineCoefficent() {
        return lineCoefficent;
    }

    public void setLineCoefficent(Double lineCoefficent) {
        this.lineCoefficent = lineCoefficent;
    }

    @Override
    public String toString() {
        return "CovidGlob [continet=" + continet + ", country=" + country + ", lineCoefficent=" + lineCoefficent
                + ", state=" + state + "]";
    }
}
