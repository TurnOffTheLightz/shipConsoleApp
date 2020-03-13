package fileHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import ship.Ship;
import ship.Ship.ShipException;
import userInteraction.ConsoleUserDialog;
/*
 *    Plik: FileHelper.java
 *          
 *   Autor: Miron Oskroba
 *    Data: pazdziernik 2019 r.
 *
 */
public class FileHelper {
	
	/*
	 * 		FileHelper.java helps to deal with files - reading and saving, checking and creating directories
	 */
	
    public final File SHIPS_DIRECTORY = new File(getAbsolutePath() + "/ships");
    
	private PrintWriter printWriter;
	private ConsoleUserDialog userDialog;
	
	public FileHelper(ConsoleUserDialog userDialog) {
		this.userDialog = userDialog;//grants fileHelper access to print to console
	}
    
    //ship TXT file need to be in /ships directory:
    public ArrayList<Ship> readShips(){
    	ArrayList<Ship> shipsInFile = new ArrayList<Ship>();
    	
    	if(!SHIPS_DIRECTORY.exists()) return null;
    	
    	//get filenames of all TXT files in directory:
    	for (File file : SHIPS_DIRECTORY.listFiles()){
    	   if (file.getName().toLowerCase().endsWith(".txt")){
    		   try {
				shipsInFile.add(readShipFromFile(file.toString()));//may throw ShipExceptiopn if data inside TXT does not match as we want
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ShipException e) {
				e.printStackTrace();
			}
    	   }
    	}
    	return shipsInFile;
    }
    
    public final Ship readShipFromFile(String nameToRead) throws IOException, ShipException {
    	String path, data;
    	//when user want to read specified TXT file:
    	if(!nameToRead.endsWith(".txt")) { 
    		path = SHIPS_DIRECTORY + "/"+ nameToRead.toLowerCase() + ".txt";
    		data = readStringFromFile(new File(path));
    	}else{
       	 //for already existing files:
       	 //nameToRead becomes 'fullPath'
    		String fullPath = nameToRead;
    		data = readStringFromFile(new File(fullPath));
    	}
    	
    	return getShipFromString(data);
	}
    
    private final Ship getShipFromString(String singleStringData) throws ShipException {

		String [] lines = singleStringData.split(";");
		
		for(String line : lines) 
			line = line.toLowerCase();
		
		String [] splitData = new String[5];
		
		if((lines[0]).startsWith("imie:")) {
			splitData[0] = lines[0].substring(5);
		}
		if((lines[1]).startsWith("typ_statku:")) {
			splitData[1] = lines[1].substring(11);
		}
		if((lines[2]).startsWith("rok_konstrukcji:")) {
			splitData[2] = lines[2].substring(16);
		}
		if((lines[3]).startsWith("liczebnosc_zalogi:")) {
			splitData[3] = lines[3].substring(18);
		}
		if((lines[4]).startsWith("liczba_lozek:")) {
			splitData[4] = lines[4].substring(13);
		}
		return new Ship(splitData[0],splitData[1],splitData[2],splitData[3],splitData[4],this);
    }
    /*
     * "imie:" + name + ";",
				"typ_statku:" + type.toString()  + ";",
				"rok_konstrukcji:" + constructionYear + ";", 
				"liczebnosc_zalogi:" + crew + ";"
				"liczba_lozek:" + beds + ";",
     */
    
    public void deleteFile(File file) {
    	file.delete();
    }
    
    public void saveToFile(Ship ship) {
    	ship.saveToFile();
    }

    //every TXT (ship data)file is stored in SHIP_DIRECTORY directory
	public void checkFoldersExists(){
        if(!SHIPS_DIRECTORY.exists()){
            createDirectory(SHIPS_DIRECTORY);
        }
    }

	//creates text file, every next index of String array is new line inside
	public void createTextFile(File file, final String[] data){
        openPrintWriter(file);
        for(String str : data) {
            addLineToFile(str);
        }
        closePrintWriter();
    }

	private void createDirectory(File file){
        if (!file.exists()) {
            if (file.mkdir()) {
            	userDialog.printMessage("\nDirectory " + file.toString() + " is created!");
            } else {
            	userDialog.printMessage("Failed to create directory!");
            }
        }
    }
	
	//returns one string for all data stored in a single TXT file
	private final String readStringFromFile(File file) throws IOException{
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		while((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
		}
		bufferedReader.close();
		return stringBuilder.toString();
	}
	
	
	private String getAbsolutePath(){
        return System.getProperty("user.dir");
    }
    
    /*
     * 					PrintWriter
     */
    private void addLineToFile(final String text){
        addTextToPrintWriter(text);
    }
    
    private void addTextToPrintWriter(final String text){
        printWriter.println(text);
    }
    
	private void openPrintWriter(File file){
        try {
            printWriter = new PrintWriter(file,"UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
	
    private void closePrintWriter(){
        printWriter.close();
    }
}
