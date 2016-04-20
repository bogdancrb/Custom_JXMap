package ro.ucv.main.maps;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Area;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

import com.sun.mail.smtp.SMTPTransport;

import ro.bogdancrb.listeners.deleteButtonListener;
import ro.bogdancrb.listeners.editButtonListener;
import ro.bogdancrb.main.AreaModel;
import ro.bogdancrb.main.UserLocation;
import ro.bogdancrb.main.WaypointModel;
import ro.bogdancrb.main.waypointRender;

import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

public class MapGUI extends JFrame implements MouseListener 
{
	private JTextField longitude = new JTextField();
	private JTextField latitude = new JTextField();
	
	public static JTextField radius = new JTextField();
	public static JTextField nrpoints = new JTextField();
	
	public static JButton modeButton;
	public static JButton deleteWaypointButton;
	
	private deleteButtonListener deleteListener;
	
	public static int ovalRadius = 0;
	public static Double lastZoom;
	public static Point lastClick = null;
	private GeoPosition lastClickedLocation;
	
	private WaypointPainter waypointPainter = new WaypointPainter();
	private JXMapKit map;
	
	private Vector<WaypointModel> waypoints;
	private AreaModel area;
	
	public static boolean editModeActive;
	
	public MapGUI() 
	{
		super("Open Map - needs internet connection");

		this.setVisible(true);
		this.setBounds(200, 50, 910, 650);
		this.setLayout(null);

		UserLocation.getUserInfo();
		
		map = (JXMapKit) (new SetupMap()).createOpenMap(UserLocation.getUserCoordonates());
		map.setBounds(20, 20, 850, 500);
		map.getMainMap().addMouseListener(this);
		add(map);

		JLabel lblLong = new JLabel("Longitude");
		JLabel lblLat = new JLabel("Latitude");
		JLabel lblRadius = new JLabel("Radius");
		JLabel lblNrPoints = new JLabel("Points in radius");
		JLabel lblUserLocation = new JLabel("Your location: " + UserLocation.getUserCity().replace("+", " ") + " " + UserLocation.getUserCountry().replace("+", " "));
		lblLong.setBounds(20, 530, 150, 25);
		lblLat.setBounds(180, 530, 150, 25);
		lblRadius.setBounds(340, 530, 150, 25);
		lblNrPoints.setBounds(500, 530, 150, 25);
		lblUserLocation.setBounds(20, 0, 300, 25);
		add(lblLong);
		add(lblLat);
		add(lblRadius);
		add(lblNrPoints);
		add(lblUserLocation);
		
		longitude.setBounds(20, 565, 150, 25);
		latitude.setBounds(180, 565, 150, 25);
		radius.setBounds(340, 565, 150, 25);
		nrpoints.setBounds(500, 565, 150, 25);
		add(longitude);
		add(latitude);
		add(radius);
		add(nrpoints);
		
		modeButton = new JButton("Edit");
		deleteWaypointButton = new JButton("Delete");
		modeButton.setBounds(660, 565, 100, 25);
		deleteWaypointButton.setBounds(770, 565, 100, 25);
		add(modeButton);
		add(deleteWaypointButton);
		
		deleteWaypointButton.setEnabled(false);
		deleteWaypointButton.setForeground(Color.RED);
		
		waypoints = new Vector<WaypointModel>();
		area = new AreaModel(map);
		
		this.setRandomWaypoints();
		
		waypointPainter.setRenderer(new waypointRender(area));
		
		editModeActive = false;
		
		deleteListener = new deleteButtonListener(waypointPainter, map, waypoints);
		
		modeButton.addActionListener(new editButtonListener(waypointPainter, map, area));
		deleteWaypointButton.addActionListener(deleteListener);
		
		repaint();
	}
	
	private void setRandomWaypoints()
	{
		for (int index = 0; index < 21; index++)
		{	
			double randLongitude = (Math.random() * 0.05 + 0.00) + UserLocation.getUserCoordonates().getLongitude() - 0.02;
			double randLatitude = (Math.random() * 0.05 + 0.00) + UserLocation.getUserCoordonates().getLatitude() - 0.04;
			
			waypoints.addElement(new WaypointModel());
			waypoints.elementAt(index).drawWaypoint(randLatitude, randLongitude, waypointPainter, map);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{

		JXMapViewer m = map.getMainMap();
		GeoPosition g = m.convertPointToGeoPosition(e.getPoint());

		System.out.println(" geo coordinates:  " + g);
		latitude.setText("" + g.getLatitude());
		longitude.setText("" + g.getLongitude());
		
		double min = 999;

		if (editModeActive)
		{
			if (e.getButton() == MouseEvent.BUTTON1) 
			{
				Iterator it = waypoints.iterator();
				int index = 0;
				boolean pointFound = false;
				
				while (it.hasNext())
				{
					Object wp = it.next();
					GeoPosition g1 = new GeoPosition(((WaypointModel)wp).getLatitude(), ((WaypointModel)wp).getLongitude());
					
					if (geoDistance(g, g1) <= (float)0.01 + (float)map.getMainMap().getZoom()/100)
					{
						latitude.setText("" + g1.getLatitude());
						longitude.setText("" + g1.getLongitude());
						
						deleteWaypointButton.setEnabled(true);
						deleteListener.setIndex(index);
						
						pointFound = true;
						break;
					}
					
					index++;
				}
				
				if (!pointFound)
				{
					waypoints.addElement(new WaypointModel());
					waypoints.elementAt(waypoints.size() - 1).drawWaypoint(g.getLatitude(), g.getLongitude(), waypointPainter, map);
				}
			} 
		}
		else
		{
			if (e.getButton() == MouseEvent.BUTTON1) 
			{
				lastClick = e.getPoint();
				lastClickedLocation = g;
				ovalRadius = 0;
				radius.setText("");
				nrpoints.setText("");
				area.drawWaypoint(g.getLatitude(), g.getLongitude(), waypointPainter, map);
			} 
			else 
			{
				if (lastClick != null) 
				{
					double radiusInKM = geoDistance(lastClickedLocation, g);
					
					radius.setText("" + radiusInKM);
					ovalRadius = ((Double) (lastClick.distance(e.getPoint()))).intValue();
					lastZoom=lastClick.distance(map.getMainMap().getCenter());
					
					Iterator it = waypoints.iterator();
					int pointsRadius = 0;
					
					while (it.hasNext())
					{
						Object wp = it.next();
						GeoPosition g1 = new GeoPosition(((WaypointModel)wp).getLatitude(), ((WaypointModel)wp).getLongitude());
						GeoPosition g2 = new GeoPosition(area.getLatitude(), area.getLongitude());
						
						if (geoDistance(g1, g2) <= radiusInKM)
						{
							area.setNrPointsInsideRadius(pointsRadius++);
						}
					}
					
					nrpoints.setText("" + pointsRadius);
				} 
			}
		}
		
		repaint();
	}

	public double geoDistance(GeoPosition g1, GeoPosition g2) 
	{
		final int EARTHRADIUS = 6371; // The radius of the earth in kilometers

		// Get the distance between latitudes and longitudes
		double deltaLat = Math.toRadians(g1.getLatitude() - g2.getLatitude());
		double deltaLong = Math.toRadians(g1.getLongitude() - g2.getLongitude());

		// Apply the Haversine function
		double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
				 + Math.cos(Math.toRadians(g2.getLatitude()))
				 * Math.cos(Math.toRadians(g1.getLatitude()))
				 * Math.sin(deltaLong / 2) * Math.sin(deltaLong / 2);
		return EARTHRADIUS * 2 * Math.asin(Math.sqrt(a));
	}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub
		// System.out.println("entered");
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub
		// System.out.println("exited");

	}

	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub
		// System.out.println("pressed");

	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub
		// System.out.println("releaseed");

	}
}
