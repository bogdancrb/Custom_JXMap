package ro.bogdancrb.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;

import ro.bogdancrb.main.AreaModel;
import ro.bogdancrb.main.waypointRender;
import ro.ucv.main.maps.MapGUI;

public class editButtonListener implements ActionListener
{
	private WaypointPainter waypointPainter;
	private JXMapKit map;
	private AreaModel area;
	
	public editButtonListener(WaypointPainter waypointPainter, JXMapKit map, AreaModel area)
	{
		this.waypointPainter = waypointPainter;
		this.map = map;
		this.area = area;
	}
	
	public void actionPerformed(ActionEvent ae) 
	{
		if (MapGUI.editModeActive == false)
		{
			MapGUI.editModeActive = true;
			
			area.removeOldWaypoint(waypointPainter, area.getLatitude(), area.getLongitude());
			map.getMainMap().setOverlayPainter(waypointPainter);
			
			MapGUI.modeButton.setText("Radius");
			MapGUI.radius.setText("");
			MapGUI.nrpoints.setText("");
		}
		else
		{
			MapGUI.editModeActive = false;
			MapGUI.deleteWaypointButton.setEnabled(false);
			
			MapGUI.modeButton.setText("Edit");
		}
	}
}
