package userInteraction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import fileHelper.FileHelper;
import main.MainThread;
import ship.Ship;
import ship.Ship.ShipException;

/*
 *    Plik: ShipConsoleApp.java
 *    
 *   Autor: Miron Oskroba
 *    Data: pazdziernik 2019 r.
 */
public class ShipConsoleApp {
	/*
	 * 		ShipConsoleApp.java provides user to make decisions, authorizes to manage ships data
	 */
	
	private static final String GREETING_MESSAGE = 
			"Program Ship - wersja konsolowa\n" + 
	        "Autor: Miron Oskroba \n" + 
			"Data:  pa�dziernik 2019 r.\n";

	private static final String MENU = 
			"\t---------------\n" +
			"\tO P E R A C J E\n" +
			"1 - Podaj dane nowego statku\n" +
			"2 - Usu� dane aktualnego statku       \n" +
			"3 - Modyfikuj dane aktualnego statku  \n" +
			"4 - Wy�wietl dane aktualnego statku \n" +
			"\t-------\n" +
			"\tP L I K\n" +
			"5 - Zapisz aktualny statek do pliku   \n" +
			"6 - Wczytaj statek z pliku   \n" +
			"7 - Usu� zapisany statek        \n" +
			"8 - Modyfikuj dane zapisanego statku   \n" +
			"9 - Wy�wietl zapisane statki  \n" +
			"10 - Wy�wietl dane zapisanych statk�w  \n" +
			"0 - Zako�cz program        \n";	
	
	private static final String MODIFY_SHIP_MENU = 
			"   Co zmieni�?     \n" + 
	        "1 - Imi�           \n" + 
			"2 - Typ statku       \n" + 
	        "3 - Rok konstrukcji  \n" + 
			"4 - Liczebno�� za�ogi     \n" +
			"5 - Liczba ��ek \n" +
	        "0 - Powr�t do menu g��wnego\n";
	
	private String SHIP_LIST;
	
	private Thread thread;
	private boolean running = false;
	
	private ConsoleUserDialog userConsole = new ConsoleUserDialog();
	private FileHelper fileHelper = new FileHelper(userConsole);
	
	private Ship currentShip = null;	
	
	private ArrayList<Ship> shipList = new ArrayList<Ship>();
	
	public ShipConsoleApp() {
		populateShipList();
	}

	//grants access to data needed to be stored between sessions
	//imports ships from /ships folder if they are compatible
	private void populateShipList() {
		try {
			shipList.addAll(fileHelper.readShips());
			updateShipListMenu();
			userConsole.printMessage("\t Wczytano z plik�w statki: \n" + SHIP_LIST+"\n");
		}catch(NullPointerException e) {
			userConsole.printErrorMessage("Brak danych statk�w do zaimportowania.");
		}
	}

	//called in thread loop in MainThread class
	
	public void update(){
		delay(500);
		switch(userConsole.enterInt(MENU + "\t->\t")){
		
		case 1:
			currentShip = updateCurrentShip();
			break;
		case 2:
			removeCurrentShip();
			break;
		case 3:
			modifyCurrentShip();
			break;
		case 4:
			showCurrentShip();
			break;
		case 5:
			saveCurrentShipToFile();
			break;
		case 6:
			currentShip = readShipFromFile();
			showCurrentShip();
			break;
		case 7:
			removeShipFromList();
			break;
		case 8:
			modifyShipInList();
			break;
		case 9:
			showShipListMenu();
			break;
		case 10:
			printShipsInfo();
			break;
		case 0:
			//stop(); //(?)
			System.exit(0);
			break;
		}
	}
	
	private Ship updateCurrentShip() {		
		String name = requestName();
		String shipType = requestShipType();
		String constructionYear = requestConstructionYear();
		String crew = requestCrew();
		String beds = requestBeds();
		Ship ship = null;
		try {
			ship = new Ship(name,shipType,constructionYear,crew,beds,fileHelper);
			
		}catch(ShipException e) {
			userConsole.printErrorMessage(e.getMessage());
		}
		return ship;
	}

	private void removeCurrentShip() {
		if(currentShip != null) {
			currentShip = null;
			userConsole.printMessage("\nUsuni�to dane aktualnego statku.\n");
		}else 
			userConsole.printMessage("Brak danych do usuni�cia.\n");
	}

	private void modifyCurrentShip() {
		if(currentShip != null) {
			modifyShip(currentShip);
			showCurrentShip();
			userConsole.printMessage("Wprowadzona zmiana zosta�a zapisana\n");
		}else
			userConsole.printMessage("\nBrak danych do zmodyfikowania. Podaj dane nowego statku.\n");
	}

	/*
	 * 					OPERACJE - aktualna osoba
	 */
	private void showCurrentShip() {
		if(currentShip != null) {
			userConsole.printMessage("\n\tDANE AKTUALNEGO STATKU:\n");
			userConsole.printMessage(currentShip.toString());;
		}else
			userConsole.printMessage("\tBrak danych do wy�wietlenia.");
	}

	/*
	 * 
	 * 						P L I K - operacje na plikach
	 * 
	 */
	
	private void saveCurrentShipToFile() {
		if(isShipRepetition())
			return;
		
		if(currentShip != null) {
			currentShip.getFileHelper().checkFoldersExists();
			currentShip.saveToFile();
			addShipToList(currentShip);
			showShipListMenu();
			userConsole.printMessage("\nZAPISANO STATEK DO PLIKU: "+ "\nPATH:\t" + currentShip.getFile()+"\n");
		}else//currentShip == null
			userConsole.printMessage("Brak danych statku do zapisania. Dodaj lub wczytaj nowy statek.");
	}
	
	private boolean isShipRepetition() {
		for(Ship ship : shipList) {
			if(currentShip.equals(ship)) {
				userConsole.printMessage("Aktualny statek jest ju� zapisany.");
				return true;
			}
		}
		return false;
	}
	
	private Ship readShipFromFile() {
		Ship ship = null;
		if(!shipList.isEmpty()) {
			boolean error;
			do {
				error = false;
				String nameToRead = userConsole.enterString("\nPodaj imi� oraz typ statku kt�ry chcesz wczyta�.\nnp.->\t titanic passenger");
				try {
					ship = fileHelper.readShipFromFile(nameToRead);
				} catch (IOException e) {
					userConsole.printErrorMessage("Nie da�o si� odczyta� pliku. Podane dane nie istniej�."+ e.getMessage());
					error = true;
				}catch(ShipException e) {
					userConsole.printErrorMessage("Dane podane w pliku nie s� poprawne. Statek nie zostal wczytany" + e.getMessage());
					error = true;
				}
			}while(error);
		}else 
			userConsole.printMessage("Brak zapisanych statk�w. Najpierw zapisz dane statku do pliku.");
		
		return ship;
	}

	private void removeShipFromList() {
		if(!shipList.isEmpty()) {
			boolean error;
			do {
				error = false;
			showShipListMenu();
			userConsole.printMessage("\n0 - Powr�t");
			int indexToDelete = userConsole.enterInt("\nKt�ry statek usun��?") - 1; //korekta indeksu (wy�wietlanie)
			if(indexToDelete ==-1) return;// powrot do menu
			else{
				try {
					Ship shipToDelete = shipList.get(indexToDelete);
					fileHelper.deleteFile(shipToDelete.getFile());
					shipList.remove(indexToDelete);
					updateShipListMenu();
					showShipListMenu();
					userConsole.printMessage("\nUsuni�to dane zwi�zane z: " + shipToDelete.getName() + " " + shipToDelete.getShipType());
				}catch(IndexOutOfBoundsException e) {
					error = true;
					userConsole.printErrorMessage("Nieprawid�owy wyb�r. " + e.getMessage());
				}
				}
			}while(error);
		}else 
			userConsole.printMessage("Brak danych statk�w do usuni�cia.");
	}

	private void modifyShipInList() {
		if(!shipList.isEmpty()) {
			boolean error;
			do {
				error = false;
				showShipListMenu();
				int indexToModify = userConsole.enterInt("Wybierz statek do zmodyfikowania.") - 1; // korekta indeksu (wyswietlanie)
				try {
				Ship shipToModify = shipList.get(indexToModify);
		
				fileHelper.deleteFile(shipToModify.getFile());
				modifyShip(shipToModify);
				updateShipListMenu();
				fileHelper.saveToFile(shipToModify);
				
				showShipListMenu();
				
				}catch(IndexOutOfBoundsException e) {
					error = true;
					userConsole.printErrorMessage("Nieprawid�owy wyb�r. " + e.getMessage());
				}
			}while(error);
		}else {
			userConsole.printMessage("Brak zapisanych statk�w. Brak danych do zmodyfikowania");
		}
	}

	private void showShipListMenu() {
		if(!shipList.isEmpty())
			userConsole.printMessage("\nZAPISANE STATKI:\n" + SHIP_LIST);
		else userConsole.printMessage("Brak zapisanych statk�w. Brak danych do wy�wietlenia.");
	}

	private void printShipsInfo() {
		if(!shipList.isEmpty()) {
			boolean error;
			do {
				showShipListMenu();
				userConsole.printMessage("0 - Powr�t\n");
				error = false;
				int indexToPrint = userConsole.enterInt("Wybierz statek, kt�rego dane chcesz zobaczy�. ") - 1;//korekta indeksu
				if(indexToPrint == -1) return; // powrot
			try {
				userConsole.printMessage("\n::Dane::\n"+shipList.get(indexToPrint).toString());
			}catch(IndexOutOfBoundsException e) {
				error = true;
				userConsole.printErrorMessage("Nieprawid�owy wyb�r." + e.getMessage());
			}
			}while(error);
		}else {
			userConsole.printMessage("Brak zapisanych statk�w. Brak danych do wy�wietlenia.");
		}
	}
	
	/*
	 * into
	 */
	
	private String requestName() {
		return userConsole.enterString("Imi�:\t->");
	}
	private String requestShipType() {
		userConsole.printMessage("Wybierz jeden z typ�w statk�w: " + Arrays.deepToString(Ship.ShipType.values()));
		return userConsole.enterString("Typ statku:\t->");
	}
	private String requestConstructionYear(){
		return userConsole.enterString("Rok skonstruowania(1800-2019)\t->");
	}
	private String requestBeds() {
		return userConsole.enterString("Liczba ��ek(max 5000):\t->");
	}
	private String requestCrew() {
		return userConsole.enterString("Liczebno�� za�ogi(max 2000):\t->");
	}
	
	private void modifyShip(Ship ship) {
		boolean error;
		do {//while error->while ShipException thrown -> while unwanted(invalid) data is tried to be set
			error = false;
			userConsole.printMessage(ship.toString());
			try {
				userConsole.printMessage(MODIFY_SHIP_MENU);
				switch(userConsole.enterInt("Zmie� dane:")) {
				case 1:
					ship.setName(requestName());
					break;
				case 4:
					ship.setShipType(requestShipType());
					break;
				case 3:
					ship.setConstructionYear(requestConstructionYear());
					break;
				case 2:
					ship.setBeds(requestBeds());
					break;
				case 5:
					ship.setCrew(requestCrew());
				case 0:
					break;
				}
			}catch(Ship.ShipException e) {
				error = true;
				userConsole.printErrorMessage(e.getMessage());
			}
		}while(error);
	}
	
	private void addShipToList(Ship ship) {
		shipList.add(currentShip);
		updateShipListMenu();
	}
	
	private void updateShipListMenu() {
		StringBuilder stringBuilder = new StringBuilder();
		for(int shipIterator = 0; shipIterator < shipList.size() ; shipIterator++) {
			Ship currShip = shipList.get(shipIterator);
			stringBuilder.append((shipIterator+1) + " - " + currShip.getName() + " " + currShip.getShipType());
			if(shipIterator + 1 == shipList.size()) continue;
			else stringBuilder.append("\n");
		}
		SHIP_LIST = stringBuilder.toString();
	}
	
	//called in thread loop in MainThread class
	
	private void delay(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//QUEST:: czy jest mozliwosc zrobienia stop() i pozniej wywolanie system.exit(0)? czy jest sens?
    private synchronized void stop(){
        if(!running) return;
        running = false;
        try {
            thread.join();
        
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
	
    		/*
			 * 			THREAD
			 */
	//synchronized for multi-threading, to avoid one resource being accessed by more than one thread at a time
	//at this time just one thread is needed
	public synchronized void start(){
		if(running) return;
		running = true;
	    thread = new Thread(new MainThread(this));
	    thread.start();
	    
		userConsole.printMessage(GREETING_MESSAGE);
	}

	public boolean isRunning() { return running; }
}
