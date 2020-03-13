package main;

import userInteraction.ShipConsoleApp;

/*
 *    Plik: MainThread.java
 *          
 *   Autor: Miron Oskroba
 *    Data: pazdziernik 2019 r.
 */
public class MainThread implements Runnable{
	/*
	 * 		MainThread.java provides main-loop that refreshes console every specified periods of time
	 */
	private TickHelper tickHelper;
	private ShipConsoleApp app;

    public MainThread(ShipConsoleApp app){
        this.app = app;
        tickHelper = new TickHelper();
    }

    @Override
    //thread loop allows to update application
    public void run() {
        while(app.isRunning()){
            tickHelper.calcTime();

            while(tickHelper.readyToUpdate()){
                tickHelper.update();
                app.update();//here
            }
            tickHelper.reset();
        }
        System.out.println("execute");
    }
    
    private class TickHelper {
    	/*
    	 * 			TickHelper.java is an inner class TickHelper to help MainThread calculate time between thread updates
    	 */
        private long lastTime = System.nanoTime();
        private long timer = System.currentTimeMillis();
        private double delta = 0.0;
        private double ns = 1000000000.0/60.0;

        void calcTime(){
            long now = System.nanoTime();
            delta+=(now-lastTime)/ns;
            lastTime = now;
        }

        boolean readyToUpdate() {
            return delta>=1;
        }

        void update(){
            delta--;
        }

        void reset(){
            if(System.currentTimeMillis()-timer>1000){
                timer+=1000;
            }
        }

    }

}
