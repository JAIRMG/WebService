/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base_mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Jorge
 */
public class ConexionDB_mysql {
    public static Connection conexion=null;
            
    public static Connection GetConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url="jdbc:mysql://localhost:3306/base_web?user=root&password=&?autoReconnect=true&useSSl=false";
            conexion= DriverManager.getConnection(url);
        }
        catch(ClassNotFoundException ex){
            JOptionPane.showMessageDialog(null, ex, "Error1 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex, "Error2 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex, "Error3 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        finally{
            return conexion;
        }
    }

    public ResultSet runSql(String sql) throws SQLException {
	Statement sta = conexion.createStatement();
	return sta.executeQuery(sql);
    }
 
    public boolean runSql2(String sql) throws SQLException {
	Statement sta = conexion.createStatement();
	return sta.execute(sql);
    }
 
    @Override
    protected void finalize() throws Throwable {
    	if (conexion != null || !conexion.isClosed()) {
            conexion.close();
	}
    }

    java.beans.Statement createStatement() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
