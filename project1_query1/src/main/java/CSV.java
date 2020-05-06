import java.text.ParseException;

public class CSV {

    public static COVID parseCSV(String csvLine) {

        COVID cov = null;
        String[] csvValues = csvLine.split(",");

        //   if (csvValues.length != 7)
        //     return null;

//            1464894,1377987280,3.216,0,1,0,3
        if (csvValues[12].equals("tamponi")) {
            return null;
        }

        cov = new COVID(
                csvValues[0], // date
                csvValues[6], // positive
                csvValues[12] // tampons
        );
        int day = cov.getDay();
        if (day == 1 || day == 7) {
            return cov;
        }
        return null;
    }
}
