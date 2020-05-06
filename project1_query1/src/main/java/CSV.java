import java.text.ParseException;

public class CSV {

    public static COVID parseCSV(String csvLine) {

        COVID cov = null;
        String[] csvValues = csvLine.split(",");

        //   if (csvValues.length != 7)
        //     return null;

//            1464894,1377987280,3.216,0,1,0,3

        cov = new COVID(
                csvValues[0], // date
                csvValues[6], // positive
                csvValues[12] // tampons
        );

        return cov;
    }
}
