package ro.bogdancrb.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.mapviewer.WaypointPainter;

import ro.bogdancrb.main.WaypointModel;
import ro.ucv.main.maps.MapGUI;

public class deleteButtonListener implements ActionListener
{
	private WaypointPainter waypointPainter;
	private JXMapKit map;
	private Vector<WaypointModel> waypoints;
	private int index;
	
	public deleteButtonListener(WaypointPainter waypointPainter, JXMapKit map, Vector<WaypointModel> waypoints)
	{
		this.waypointPainter = waypointPainter;
		this.map = map;
		this.waypoints = waypoints;
	}
	
	public void actionPerformed(ActionEvent ae) 
	{
		if (MapGUI.editModeActive)
		{
			double latitude = waypoints.elementAt(index).getLatitude();
			double longitude = waypoints.elementAt(index).getLongitude();
			
			waypoints.elementAt(index).removeWaypoint(waypointPainter, latitude, longitude);
			waypoints.removeElementAt(index);
			
			map.getMainMap().setOverlayPainter(waypointPainter);
			
			MapGUI.deleteWaypointButton.setEnabled(false);
		}
	}
	
	public void setIndex(int index)
	{
		this.index = index;
	}
}
