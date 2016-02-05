package environment;


import java.util.LinkedList;

import commoninterface.AquaticDroneCI;
import commoninterface.entities.GeoFence;
import commoninterface.entities.Waypoint;
import commoninterface.utils.CoordinateUtilities;
import simulation.Simulator;
import simulation.util.Arguments;
import simulation.physicalobjects.LightPole;
import simulation.physicalobjects.Line;
import simulation.physicalobjects.PhysicalObject;
import simulation.physicalobjects.PhysicalObjectType;
import simulation.physicalobjects.collisionhandling.knotsandbolts.CircularShape;
import simulation.robot.AquaticDrone;
import simulation.robot.Robot;

public class BasicEnvironment extends OpenEnvironment{
	
	protected int numberOfRobots;
	protected GeoFence boundary;
	protected double width, height, rand = 0.5;

	public BasicEnvironment(Simulator simulator, Arguments args) {
		super(simulator, args);
		// TODO Auto-generated constructor stub
		rand = args.getArgumentAsDoubleOrSetDefault("random", rand);
		width = args.getArgumentAsDouble("width");
		height = args.getArgumentAsDouble("height");
	}
	
@Override
public void setup(Simulator simulator){
	super.setup(simulator);
	numberOfRobots = simulator.getRobots().size();
	positionDrones(simulator);
	drawGeoFence(simulator);
	addPollutant(simulator);
}

private void addPollutant(Simulator simulator) {
	// TODO Auto-generated method stub
	double X = simulator.getRandom().nextDouble() * (width/2);
	double Y = simulator.getRandom().nextDouble() * (height/2);
	
	
	LightPole pollutant = new simulation.physicalobjects.LightPole(simulator, "pollutant", 45, 32, 0.5);
	addObject(pollutant);
}

private void drawGeoFence(Simulator simulator) {
	// TODO Auto-generated method stub
	boundary = new GeoFence("boundary");
	boundary.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(-width/2, -height/2)));
	boundary.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(width/2, -height/2)));
	boundary.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(width/2, height/2)));
	boundary.addWaypoint(CoordinateUtilities.cartesianToGPS(new commoninterface.mathutils.Vector2d(-width/2, height/2)));
	addLines(boundary.getWaypoints(), simulator);
	
	for (Robot r : robots){
		AquaticDroneCI drone = (AquaticDroneCI)r;
		drone.getEntities().add(boundary);
	}
}

private void addLines(LinkedList<Waypoint> waypoints, Simulator simulator) {
	// TODO Auto-generated method stub
	for (int i =1; i < waypoints.size(); i++){
		
		Waypoint wa = waypoints.get(i-1);
		Waypoint wb = waypoints.get(i);
		commoninterface.mathutils.Vector2d va = CoordinateUtilities.GPSToCartesian(wa.getLatLon());
		commoninterface.mathutils.Vector2d vb = CoordinateUtilities.GPSToCartesian(wb.getLatLon());
		
		Line l = new simulation.physicalobjects.Line(simulator, "line" + i, va.getX(), va.getY(), vb.getX(), vb.getY());
		addObject(l);
		
	}
}

private void positionDrones(Simulator simulator) {
	// TODO Auto-generated method stub
	for (Robot r : robots){
		AquaticDrone drone = (AquaticDrone) r;
		drone.setPosition(-width/2+2, -height/2+2);
		drone.setOrientation(simulator.getRandom().nextDouble() * Math.PI * 2);
	}
}

}
