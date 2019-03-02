package ru.fishbalka.sbootjdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

public abstract class AbstractDAO<T,V> {

    private Connection connection;

    public AbstractDAO(){
        Properties properties = new Properties();
        properties.put("user","root");
        properties.put("password","Bosco314");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankdb?serverTimezone=UTC", properties);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }


    public abstract List<T> getAll();
   public abstract T findById(V id);
   public abstract void update(T item);
   public abstract void delete(V id);
   public abstract void create(T item);


   public Statement getStatement(){
       Statement statement = null ;
       try{
           statement = connection.createStatement();
       }catch (SQLException e){
           e.printStackTrace();
       }
       return statement;
   }
   public void closeStatement(Statement statement){
       if(statement!=null){
           try {
               statement.close();
           }catch (SQLException e){
               e.printStackTrace();
           }
       }
   }


}
