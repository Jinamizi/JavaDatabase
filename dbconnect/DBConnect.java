package dbconnect;

import java.sql.*;

public class DBConnect {
    private Connection con;
    private Statement stm;
    private ResultSet rs;
    
    DBConnect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","");
            stm = con.createStatement();
        } catch(Exception e){
            System.out.println("Error occured");
        }
    }
    public void getData(String tableName){
        try{
            String query = "select * from " + tableName;
            rs = stm.executeQuery(query);
            String[] columnNames = getColumnNames(tableName);
            while(rs.next()){
                String s = "";
                for(String column : columnNames)
                    System.out.printf("%-8s", rs.getString(column));
                System.out.println();
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public String[] getColumnNames(String tableName) {
        String columns[] = null;
        try{
            String query = "select * from " + tableName;
            rs = stm.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            
            columns = new String[metaData.getColumnCount()];
            for (int i =0; i< columns.length; i++)
                columns[i] = metaData.getColumnName(i + 1);
        } catch (Exception e){
            e.printStackTrace();
        }
        return columns;
    }
    
    public void insertData(String fname, String lname) {
        try{
            String query = "insert into authors (Firstname, Lastname) values ( "+ fname +","+lname +")";
            stm.executeQuery(query);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        DBConnect connect = new DBConnect();
        connect.insertData("Sally", "Mauwa");
        for (String s : connect.getColumnNames("authors"))            
            System.out.printf("%-8s",s);
        System.out.println();
        connect.getData("authors");
    }
    
}
