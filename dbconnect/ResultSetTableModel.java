package dbconnect;
//copied from java how to program by deitel pg 1196
//A TableModel that supplies ResultSet data to a JTable
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.AbstractTableModel;

//ResultSet rows nd columns are counted from 1 and JTable rows and columns re counted from 0. When processing ResultSet rows or columns for use in a Jtable,
//it is necessary to add 1 to the row or column number to manipulate the appropriate ResultSet column

public class ResultSetTableModel extends AbstractTableModel{
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private int numberOfRows;
    
    //keep track of database connection status
    private boolean connectedToDatabase = false;
    
    // constructor initializes resultSet and obtains its meta data object;
    // determines number of rows
    public ResultSetTableModel(String url, String username, String password, String query) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
        
        //create statement to query database
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        
        //update database connection status
        connectedToDatabase = true;
        
        //set query and execute it
        setQuery(query);
    }
    
    //get class that represents column type
    @Override
    public Class getColumnClass(int column) throws IllegalStateException{
        //ensure database connection is available
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");
        
        //determine Java class of column
        try {
            String className = metaData.getColumnClassName(column + 1);
            //return Class object that represent className
            return Class.forName(className);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        
        return Object.class; //if problem occurs above, assume type Object
    }
    
    //return number of rows in ResultSet
    @Override
    public int getRowCount() throws IllegalStateException{
        //ensure database connection is available
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");
        
        return numberOfRows;
    }

    @Override
    public int getColumnCount() throws IllegalStateException{
        //ensure database connection is available
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");
        
        //determine number of columns
        try {
            return metaData.getColumnCount();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
        return 0; //if problem occur above ,return 0 for number of columns
    }
    
    //get name of a particular column in ResultSet
    @Override
    public  String getColumnName( int column) throws IllegalStateException {
        //ensure database connection is available
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");
        
        //determine column name
        try {
            return metaData.getColumnName(column + 1);
        }
        catch (SQLException sQLException) {
            sQLException.printStackTrace();
        }
        
        return ""; //if problems, return empty string for column
    }
    
    
    //obtain value in particular row and column
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) throws IllegalStateException{
    //ensure database connection is available
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");
        
        //obtain a value at specified ResultSet row and column
        try {
            resultSet.absolute(rowIndex + 1);
            return resultSet.getObject(columnIndex + 1);
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return "";//if problems, return empty string object
    }
    
    //set new database query string
    public void setQuery(String query) throws SQLException,IllegalStateException{
        //ensure database connection is available
        if (!connectedToDatabase)
            throw new IllegalStateException("Not Connected to Database");
        
        //specify query and execute it
        resultSet = statement.executeQuery(query);
        
        //obtain meta data for Resultset
        metaData = resultSet.getMetaData();
        
        //determine number of rows in resultSet
        resultSet.last(); //move to last row
        numberOfRows = resultSet.getRow(); //get row number
        
        //notify JTable that model has changed
        fireTableStructureChanged();
    }
    
    //close statement and connection
    public void disconnectFromDatabase() {
        if (connectedToDatabase){
            try {
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
}
