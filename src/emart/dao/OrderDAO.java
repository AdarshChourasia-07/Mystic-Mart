/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.OrdersPojo;
import emart.pojo.ProductsPojo;
import emart.pojo.UserProfile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hp
 */
public class OrderDAO {
    public static String getNextOrderId() throws SQLException
    { 
        Connection conn = DBConnection.getConnection();
        Statement st =conn.createStatement();
        ResultSet rs = st.executeQuery("Select max(order_id) from orders");
        rs.next();
        String ordId = rs.getString(1);
        if(ordId==null)
           return"O-101";
        
        int ordno = Integer.parseInt(ordId.substring(2));
        ordno++;
        return "O-"+ordno;
        
    }
    
    public static boolean addOrder(ArrayList<ProductsPojo> al,String ordId) throws SQLException
    {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps=conn.prepareStatement("Insert into orders values(?,?,?,?)");
        int count=0;
        for(ProductsPojo p:al)
        {
            ps.setString(1, ordId);
            ps.setString(2, p.getProductId());
            ps.setInt(3,p.getQuantity());
            ps.setString(4,UserProfile.getUserid());      
            count=count+ps.executeUpdate();
            
        }
        
        return count==al.size();
    }
    
    //Assignnment work
    
    
    public static List<String> getAllOrderId() throws SQLException
    {
        Connection con=DBConnection.getConnection();
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery("select order_id from orders group by order_id ");
        List <String> orderid=new ArrayList<>();
        while(rs.next())
        {
            String id=rs.getString(1);
            orderid.add(id);
        }
        return orderid;
        
    }
    public static List<String> getOrderId() throws SQLException
    {
        Connection conn=DBConnection.getConnection();
        PreparedStatement ps=conn.prepareStatement("select order_id from orders where userid=? group by order_id");
        ps.setString(1,UserProfile.getUserid());
        ResultSet rs=ps.executeQuery();
        List <String> orderid=new ArrayList<>();
        while(rs.next())
        {
            String id=rs.getString(1);
            orderid.add(id);
        }
        return orderid;
        
       
    }
    
    
    public static List<OrdersPojo> getOrderDetails(String oid) throws SQLException
    {
       Connection conn = DBConnection.getConnection();
       PreparedStatement ps= conn.prepareStatement("select products.p_name,products.p_companyname,products.p_price,products.our_price,products.p_tax,orders.quantity,products.p_id from products INNER JOIN orders ON products.p_id=orders.p_id where orders.order_id=?");
       
       ps.setString(1, oid);
       
       ResultSet rs = ps.executeQuery();
       ArrayList<OrdersPojo> ordersList = new ArrayList(); 
       while(rs.next())
       {
       OrdersPojo orders = new OrdersPojo();
       orders.setProductName(rs.getString(1));
       orders.setProductCompany(rs.getString(2));
       orders.setProductPrice(rs.getDouble(3));
       orders.setOurPrice(rs.getDouble(4));
       orders.setTax(rs.getInt(5));
       orders.setQuantity(rs.getInt(6));
       orders.setProductId(rs.getString(7));
       ordersList.add(orders);
       
       }
        return ordersList;
    }
    
}
