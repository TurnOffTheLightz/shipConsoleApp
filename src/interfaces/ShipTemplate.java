package interfaces;

import java.io.File;

import fileHelper.FileHelper;
import ship.Ship;
import ship.Ship.ShipException;
import ship.Ship.ShipType;
/*
 *    Plik: ShipTemplate.java
 *    
 *   Autor: Miron Oskroba
 *    Data: pazdziernik 2019 r.
 */
public interface ShipTemplate {
	/*
	 * 			ShipTemplate.java is a template that helps to organize Ship class public methods.
	 */
	
	void setName(String name) throws ShipException;
	void setShipType(String type) throws ShipException;
	void setConstructionYear(String birthYear) throws ShipException;
	void setCrew(String crew) throws ShipException;
	void setBeds(String beds) throws ShipException;
	
	String getName();
	ShipType getShipType();
	String getConstructionYear();
	int getBeds();
	int getCrew();
	boolean equals(Ship ship);
	
	void saveToFile();
	File getFile();
	FileHelper getFileHelper();
	
	String toString();
}
