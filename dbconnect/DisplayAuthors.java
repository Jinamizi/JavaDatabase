package dbconnect;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DisplayAuthors {
    //database url
    static final String DATABASE_URL = "jdbc:mysql://localhost/test";
    
    //launch the application
    public static void main(String args[]) {
        Connection connection = null; //manage connection
        Statement statement = null; //query statement
        ResultSet resultSet = null; //manage result
        
        //connect to database books and query database
        try {
            //establish connection to database
            connection = DriverManager.getConnection(DATABASE_URL,"root","");
            
            //create statement for querying database
            statement = connection.createStatement();
            
            //query database
            resultSet = statement.executeQuery("Select AuthorID, FirstName, LastName From Authors");
            
            //process query results
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            System.out.println("Authors Table of Books Database:\n");
            
            for(int i = 1; i<= numberOfColumns; i++)
                System.out.printf("%-8s\t",metaData.getColumnName(i));
            System.out.println();
            
            while(resultSet.next()){
                for (int i = 1; i <= numberOfColumns; i++)
                    System.out.printf("%-8s\t", resultSet.getObject(i));
                System.out.println();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        finally //ensure resultSet, statement and connection are closed
        {
            try{
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
