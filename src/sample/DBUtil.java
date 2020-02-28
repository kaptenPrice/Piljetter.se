package sample;

public class DBUtil {

    private static final String DATABASEINLOGG = "postgres";


    private static final String DATABASEINLOGGCUSTOMER = "customer"; //users id

    // private static final String DATABASEPASSWORD = "serasvic6";
    private static final String DATABASEPASSWORD = "1234";
    private static final String DATABASEPASSWORDCUSTOMER = "customer"; //Users password
    private static final String DATABASECONNECTION = "jdbc:postgresql://localhost:5432/piljetter";
    // private static final String DATABASECONNECTION = "jdbc:postgresql://localhost:5432/postgres";

    public String getDATABASEINLOGG() {
        return DATABASEINLOGG;
    }

    public String getDATABASEPASSWORD() {
        return DATABASEPASSWORD;
    }

    public String getDATABASECONNECTION() {
        return DATABASECONNECTION;
    }

    public static String getDATABASEINLOGGCUSTOMER() {
        return DATABASEINLOGGCUSTOMER;
    }

    public static String getDATABASEPASSWORDCUSTOMER() {
        return DATABASEPASSWORDCUSTOMER;
    }

}
