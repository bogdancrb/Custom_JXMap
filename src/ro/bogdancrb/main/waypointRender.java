package ro.bogdancrb.main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

import ro.ucv.main.maps.MapGUI;

public class waypointRender implements WaypointRenderer
{
	private Object area;
	private int nr;
	
	public waypointRender(Object area)
	{
		this.area = area;
	}

	public boolean paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint wp) 
	{
		float alpha = 0.6f;
		int type = AlphaComposite.SRC_OVER;
		AlphaComposite composite = AlphaComposite.getInstance(type, alpha);

		if (wp.getPosition().getLatitude() == ((AreaModel)area).getLatitude() && 
			wp.getPosition().getLongitude() == ((AreaModel)area).getLongitude())
		{
			Color color = new Color(0, 1, 0, alpha); // Green
			
			g.setColor(color);
			g.fillOval(-5, -5, 10,10);
			g.setColor(Color.BLACK);
			g.drawOval(-5, -5, 10,10);
			
			if (MapGUI.ovalRadius > 0) 
			{
				Double currentZoom = MapGUI.lastClick.distance(map.getCenter());
				Double r = currentZoom/MapGUI.lastZoom * MapGUI.ovalRadius;
				int zoomedRadius = r.intValue();
				
				System.out.println("radius "+MapGUI.ovalRadius+"   zoomed"+zoomedRadius);
				
				g.setColor(color);
				g.fillOval(-zoomedRadius, -zoomedRadius, 2 * zoomedRadius, 2 * zoomedRadius);
				
				g.setColor(Color.BLACK);
				g.drawOval(-zoomedRadius, -zoomedRadius, 2 * zoomedRadius, 2 * zoomedRadius);
			}
		}
		else
		{
			Color color = new Color(1, 0, 0, alpha); // Red
			
			g.setColor(color);
			g.fillOval(-5, -5, 10,10);
			g.setColor(Color.BLACK);
			g.drawOval(-5, -5, 10,10);
			
			if (map.getZoom() <= 2)
			{
				Color bgColor = new Color(1, 1, 1, alpha); // White
				
				AttributedString pointCoord = new AttributedString("X: " + wp.getPosition().getLatitude());
				pointCoord.addAttribute(TextAttribute.BACKGROUND, bgColor); // White background
				g.drawString(pointCoord.getIterator(), 10, 0);
				
				pointCoord = new AttributedString("Y: " + wp.getPosition().getLongitude());
				pointCoord.addAttribute(TextAttribute.BACKGROUND, bgColor); // White background
				g.drawString(pointCoord.getIterator(), 10, 15);
			}
		}
		
		return true;
	}

}
