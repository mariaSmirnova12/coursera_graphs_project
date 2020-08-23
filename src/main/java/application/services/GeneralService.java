package application.services;

import application.DataSet;
import application.MarkerManager;
import application.SelectManager;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.*;

// class for map and general application services
public class GeneralService {
	private int currentState;
	private SelectManager selectManager;
	private GoogleMap map;
    private MarkerManager markerManager;
    private static final String DATA_FILE_DIR_STR = "data/maps/";

    public GeneralService(GoogleMapView mapComponent, SelectManager selectManager, MarkerManager markerManager) {
        // get map from GoogleMapView
    	this.map = mapComponent.getMap();
    	this.selectManager = selectManager;
        this.markerManager = markerManager;
        this.markerManager.setMap(map);
    }

    public static String getDataSetDirectory() { return GeneralService.DATA_FILE_DIR_STR; }

    // gets current bounds of map view
    public float[] getBoundsArray() {
        LatLong sw, ne;
    	LatLongBounds bounds = map.getBounds();

    	sw = bounds.getSouthWest();
    	ne = bounds.getNorthEast();

    	// [S, W, N, E]
    	return new float[] {(float) sw.getLatitude(), (float) sw.getLongitude(),
    			            (float) ne.getLatitude(), (float) ne.getLongitude()};
    }

    public void displayIntersections(DataSet dataset) {
        // remove old data set markers
    	if(markerManager == null){
    	  System.out.println("failure!");
    	}
        if(markerManager.getDataSet() != null) {
        	markerManager.clearMarkers();
            markerManager.getDataSet().setDisplayed(false);
        }

        // display new data set
    	selectManager.setAndDisplayData(dataset);
        dataset.setDisplayed(true);

    }
    
    public float boundsSize() {
    	float[] bounds = getBoundsArray();
    	return (bounds[2] - bounds[0]) * (bounds[3] - bounds[1]);
    }
    
    public boolean checkBoundsSize(double limit) {
    	if (boundsSize() > limit) {
    		return false;
    	}
    	return true;
    }

    public void setState(int state) {
    	currentState = state;
    }

    public double getState() { return currentState; }


}

