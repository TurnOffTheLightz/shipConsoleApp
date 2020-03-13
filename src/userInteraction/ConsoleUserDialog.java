package userInteraction;

import java.util.Scanner;

import interfaces.UserInput;

/*
 *    Plik: ConsoleUserDialog.java
 *          
 *   Autor: Miron Oskroba
 *    Data: pazdziernik 2019 r.
 */
public class ConsoleUserDialog implements UserInput{
	/*
	 * 		ConsoleUserDialog.java provides user to input correct data so the program can use it in destinated purposes.
	 */
	 private final String  ERROR_MESSAGE =
	          "Nieprawid³owe dane!\nSpróbuj jeszcze raz.";
	 
	 private Scanner scanner = new Scanner(System.in);
	 
	 public void printMessage(String message) {
		 System.out.println(message);
	 }
	 
	 public void printErrorMessage(String message) {
		 System.out.println(message);
		 System.out.println("Naciœnij ENTER.");
		 enterString("");
	 }
	 
	 public int enterInt(String message) {
		 boolean error;
		 int num = 0;
			 do {
				 error = false;
				 try {
				 num = Integer.parseInt(enterString(message));
				 }catch(NumberFormatException e) {
					 printErrorMessage("Nieprawid³owy wybór." + e.getMessage());
					 error = true;
				 }
			 }while(error);
		 return num;
	 }
	 
	 public String enterString(String message) {
		 System.out.println(message);
		 return scanner.nextLine();
	 }
}
