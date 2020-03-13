package main;

import userInteraction.ShipConsoleApp;

/*
 * Program: Aplikacja dzia³aj¹ca w oknie konsoli, która umo¿liwia testowanie 
 *          operacji wykonywanych na obiektach klasy Ship..
 *          
 *          ShipConsoleApp is database app, which aims to manage ship information. 
 *          It allows user to add, remove, save and read from file, ship information via console. 
 *          As an IT student its purpose is also to gain general skills in programming a small database,
 *          throwing own exceptions, projecting interfaces, file IO operations, java syntax and clean code.
 *          
 *    Plik: Main.java
 *          
 *   Autor: Miron Oskroba
 *    Data: pazdziernik 2019 r.
 */
public class Main {
	public static void main(String args[]) {
		new ShipConsoleApp().start();
	}
	
}
