package controller;

import java.lang.reflect.Array;
import java.util.Random;

import matlabcontrol.*;
import matlabcontrol.extensions.MatlabNumericArray;
import matlabcontrol.extensions.MatlabTypeConverter;
import controllers.Controller;
import simulation.Simulator;
import simulation.robot.CISensorWrapper;
import simulation.robot.DifferentialDriveRobot;
import simulation.robot.Robot;
import simulation.util.Arguments;

public class BrownianMotion extends Controller {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CISensorWrapper inside;
	private DifferentialDriveRobot robo;
	private double cumTime, stepSize, minimumSteps;
	private Random rand;
	
	private MatlabProxyFactory factory;
	private MatlabProxy proxy;
	MatlabTypeConverter processor;
	MatlabNumericArray array;
	
	public enum State {
			SET_STEP, STEP, CHANGE_ORIENTATION
	}
	private State currentState = State.SET_STEP;

	public BrownianMotion(Simulator simulator, Robot robot, Arguments args) throws MatlabInvocationException {
		super(simulator, robot, args);
		// TODO Auto-generated constructor stub
		robo = (DifferentialDriveRobot)robot;
		inside = (CISensorWrapper)robot.getSensorWithId(1);
		cumTime = 0;
		minimumSteps = 1000;
		rand = new Random();
		
		
		try{		
			factory = new MatlabProxyFactory();
			proxy = factory.getProxy();
			
			proxy.eval("gaussian_distribution");
			
			//Get result
			processor = new MatlabTypeConverter(proxy);
			array = processor.getNumericArray("brownianSteps");
			
			
		} catch (MatlabConnectionException matConExcep){
			matConExcep.printStackTrace();
		}
		
		
	}
	
	public void controlStep(double time){
		
		
		
		switch(currentState){
		case SET_STEP:
			
			
				setStep(time);
			
			
//			try{
//				proxy.eval("display('hey!')");
//			} catch (MatlabInvocationException matInvokeExcep){
//				matInvokeExcep.printStackTrace();
//			}
//			
			
			
			break;
			
		case STEP:
			brownian(time);
			break;
			
		case CHANGE_ORIENTATION:
			orient();
			break;
		}
	
	}

	private void setStep(double clock)  {
		// TODO Auto-generated method stub
		
		
			
			
		
		
		System.out.println(array.getRealValue(0));
		
		stepSize = Math.abs(((int) (rand.nextGaussian() * minimumSteps + minimumSteps)));
		stepSize = stepSize + clock;
		currentState = State.STEP;
	}

	private void orient() {
		// TODO Auto-generated method stub
		robo.setOrientation(rand.nextDouble() * Math.PI * 2);
		currentState = State.SET_STEP;
	}

	private void brownian(double clock) {
		// TODO Auto-generated method stub
		//step = step + clock;
		
		robo.setWheelSpeed(0.2, 0.2);
		
		System.out.println(stepSize);
		if(inside.getSensorReading(0) > 0.5) {
			robo.setOrientation(rand.nextDouble() * Math.PI * 2);
		}
		
		if(clock > stepSize){
			currentState = State.CHANGE_ORIENTATION;
		}
		/*Random r = new Random(); 
		
		if (inside.getSensorReading(0) > 0){
			
		}
		if (inside.getSensorReading(0) > 0.1) {
			System.out.println(inside.getSensorReading(0)+"\n");
			robo.setOrientation(r.nextGaussian() * Math.PI * 2);
			flag = 1;
		}
		if (counter == 999){
			robo.setOrientation(r.nextGaussian() * Math.PI * 2);
		}*/
	}

}
