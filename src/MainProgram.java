import com.JTweet.classes.*;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;

public class MainProgram {
	
	final static String url = "jdbc:mysql://localhost:3306/TrabajoPractico";
	public static void main(String[] args) throws SQLException {
		Scanner s = new Scanner (System.in);
		String usr,psw;
		Usuario p1=null;
		Connection con = DriverManager.getConnection(url, "root", "");
		classes cls = new classes();
		System.out.println("Ingrese Usuario");
		usr=s.nextLine();
		System.out.println("Ingrese Contraseña");
		psw=s.nextLine();
		try {
			cls.LogIn(usr, psw,p1, s,con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.close();

	}

}
