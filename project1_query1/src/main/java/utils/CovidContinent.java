package utils;

import java.io.Serializable;
import java.util.List;

public class CovidContinent implements Serializable{
    String continent;
    List<String> contry_in_continet;
    List<Integer> max_day_week;
    List<Integer> min_day_week;
    List<Double>  deviation_weeks;
    List<Double>  media_day_weeks;

    public CovidContinent(String continent, List<String> contry_in_continet, List<Integer> max_day_week,
			List<Integer> min_day_week, List<Double> deviation_weeks, List<Double> media_day_weeks) {
		this.continent = continent;
		this.contry_in_continet = contry_in_continet;
		this.max_day_week = max_day_week;
		this.min_day_week = min_day_week;
		this.deviation_weeks = deviation_weeks;
		this.media_day_weeks = media_day_weeks;
	}

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public List<String> getContry_in_continet() {
        return contry_in_continet;
    }

    public void setContry_in_continet(List<String> contry_in_continet) {
        this.contry_in_continet = contry_in_continet;
    }

    public List<Integer> getMax_day_week() {
        return max_day_week;
    }

    public void setMax_day_week(List<Integer> max_day_week) {
        this.max_day_week = max_day_week;
    }

    public List<Integer> getMin_day_week() {
        return min_day_week;
    }

    public void setMin_day_week(List<Integer> min_day_week) {
        this.min_day_week = min_day_week;
    }

    public List<Double> getDeviation_weeks() {
        return deviation_weeks;
    }

    public void setDeviation_weeks(List<Double> deviation_weeks) {
        this.deviation_weeks = deviation_weeks;
    }

    public List<Double> getMedia_day_weeks() {
        return media_day_weeks;
    }

    public void setMedia_day_weeks(List<Double> media_day_weeks) {
        this.media_day_weeks = media_day_weeks;
    }

    @Override
    public String toString() {
        return "CovidContinent [continent=" + continent + ", contry_in_continet=" + contry_in_continet
                + ", deviation_weeks=" + deviation_weeks + ", max_day_week=" + max_day_week + ", media_day_weeks="
                + media_day_weeks + ", min_day_week=" + min_day_week + "]";
    }

  

    
    
}