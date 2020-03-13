package interfaces;
/*
 *    Plik: UserInput.java
 *    
 *   Autor: Miron Oskroba
 *    Data: pazdziernik 2019 r.
 */
public interface UserInput {
	/*
	 * 			UserInput.java helps to organize UserConsoleDialog class public methods
	 */
	void printMessage(String msg);
	void printErrorMessage(String msg);
	int enterInt(String msg);
	String enterString(String mag);
}
