package com.JTweet.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class classes {

	public void LogIn(String usr, String psw, Usuario usra,Scanner s,Connection con) throws SQLException {
		boolean data;
		Statement stmt = con.createStatement();
		String bring = "SELECT * FROM `usuarios` WHERE User='" + usr + "' AND Psw='" + psw + "'";
		ResultSet rs = stmt.executeQuery(bring);
		data=rs.next();
		if(data) {
			usra=new Usuario(rs.getString("User"), rs.getString("Psw"), rs.getString("name"), rs.getString("sureName"), rs.getString("eMail"), rs.getString("fecha"));
			usra.setID(rs.getInt("ID"));
			System.out.println("Bienvenido " + rs.getString("name") + " " + rs.getString("sureName") );
			main(data, s,stmt, usra);
		}else{
			System.out.println("!Usuario y contraseña no existente");
			logFailed(s, usra, stmt, usr,psw);
		}

	}
	public void logFailed(Scanner s,Usuario usr,Statement stmt,String usra,String psw) {
		String name,surename,email,fecha;
		System.out.println("Bienvenido a JTweet, para poder continuar debe crear un nuevo usuario, utilizaremos el usuario y la contraseña ingresada para crear el usuario en nuestra base de datos");
		System.out.println("Por favor ingrese su nombre");
		name=s.nextLine();
		System.out.println("Ingrese su apellido");
		surename=s.nextLine();
		System.out.println("Ingrese su eMail");
		email=s.nextLine();
		Pattern Mail= Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
		Matcher m = Mail.matcher(email);
		while(!m.find()) {
			System.out.println("E-mail no valido Ingreselo de nuevo");
			email=s.nextLine();
			m=Mail.matcher(email);
		}
		System.out.println("Ingrese su fecha de nacimiento en el siguiente formato YYYY-MM-DD");
		fecha=s.nextLine();
		Pattern fdn=Pattern.compile("([0-9]{4}+-+[0-9]{2}+-[0-9]{2})");
		m=fdn.matcher(fecha);
		while(!m.find()) {
			System.out.println("fecha de nacimiento no valida, Formato = YYYY-MM-DD");
			fecha=s.nextLine();
			m=fdn.matcher(fecha);
		}
		usr=new Usuario(usra, psw, name, surename, email, fecha);
		String insert="INSERT INTO `usuarios`(`User`, `name`, `sureName`, `fecha`, `eMail`, `Psw`) VALUES (\""+usr.getUser()+"\",\""+usr.getName()+"\",\""+usr.getSurename()+"\",\""+usr.getBorn()+"\",\""+usr.geteMail()+"\",\""+usr.getPsw()+"\")";
		try {
			stmt.execute(insert);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		main(true, s, stmt, usr);
		
		
	}
	public void main(boolean start, Scanner s,Statement stmt,Usuario usr) {
		int rta = 0;
		while (start) {
			System.out.println("1.	Escribir nuevo JTweet");
			System.out.println("2.	Buscar JTweets por usuario");
			System.out.println("3.	Buscar JTweets por texto");
			System.out.println("4.	Buscar JTweets por tema");
			System.out.println("5.	Ver JTweets en tiempo real.");
			rta=s.nextInt();
			s.nextLine();
			switch (rta) {
			case 1:
				System.out.println("Ingrese su tweet :D");
				escribirTweet(stmt, s.nextLine(), usr);
				break;
			case 2:
				System.out.println("Ingrese el nombre de usuario que escribio los twts");
				buscarTwtUsr(stmt, s.nextLine());
				break;
			case 3:
				System.out.println("Ingrese el texto que desea buscar");
				buscarTwt(stmt, s.nextLine());
				break;
			case 4:
				System.out.println("Ingrese el tema que desea buscar, Acuerdese de ingresar el Numeral #");
				buscarTwtTema(stmt, s.nextLine());
				break;
			case 5:
				System.out.println("Cuando desee dejar de actualizar la base ingrese Stop por favor");
				TweetsInRealTime twt= new TweetsInRealTime(stmt);
				Thread t  = new Thread(twt);
				t.start();
				while(twt.stop==true) {
					if(s.nextLine().toLowerCase().equals("stop")) {
						twt.stop=false;
					}
				}
				
				break;
			}
		}
	}
	public void escribirTweet(Statement stmt,String tweet,Usuario usr) {
		String loadTwt="INSERT INTO `jTweets`(`nombreusuario`, `texto`) VALUES (\""+usr.getUser()+"\",\""+tweet+"\")";
		try {
			stmt.execute(loadTwt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String bring = "SELECT idtweet FROM `jTweets` WHERE texto='" + tweet + "'";
		int id=0;
		try {
			ResultSet rs = stmt.executeQuery(bring);
			rs.next();
			id=rs.getInt("idtweet");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		Pattern p =Pattern.compile("(#\\w*)");
		Matcher m = p.matcher(tweet);
		while(m.find()) {
			String loadTema="INSERT INTO `temas`(`idtweet`, `temas`) VALUES (\""+ id+"\",\""+m.group()+"\")";
			try {
				stmt.execute(loadTema);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	public void buscarTwtUsr(Statement stmt,String usr) {
		String bring = "SELECT nombreusuario, texto FROM `jTweets` WHERE nombreusuario='" + usr + "'";
		System.out.println("Tweets del usuario: "+ usr);
		try {
			ResultSet rs = stmt.executeQuery(bring);
			while(rs.next()) {
				System.out.println("@"+rs.getString(1)+"> "+rs.getString(2)+"\n");
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	public void buscarTwt(Statement stmt,String twt) {
		String bring = "SELECT nombreusuario, texto FROM `jTweets` WHERE texto LIKE'%" + twt + "%'";
		try {
			ResultSet rs = stmt.executeQuery(bring);
			while(rs.next()) {
				System.out.println("@"+rs.getString(1)+"> "+rs.getString(2)+"\n");
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	public void buscarTwtTema(Statement stmt, String tema) {
		Pattern p =Pattern.compile("(#\\w+)");
		Matcher m = p.matcher(tema);
		ArrayList<Integer> id= new ArrayList<>();
		while(m.find()) {
		String bring = "SELECT * FROM `temas` WHERE temas='" + tema + "'";
		try {
			ResultSet rs = stmt.executeQuery(bring);
			while(rs.next()) {
				id.add(rs.getInt(2));
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		for(int a: id) {
			
			String bring2 = "SELECT nombreusuario, texto FROM `jTweets` WHERE idTweet='" + a + "'";
			try (ResultSet rs2 = stmt.executeQuery(bring2)){
				while(rs2.next()) {
					System.out.println("@"+rs2.getString(1)+" > "+ rs2.getString(2));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		}
	}
}