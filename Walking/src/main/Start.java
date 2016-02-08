package main;

import gui.CombinedGui;
//import matlabcontrol.*;


public class Start {
	
	public static void main (String[] args) {
		try{
			new CombinedGui(new String[]{"--gui", "classname=CIResultViewerGui,renderer=(classname=CITwoDRenderer))"});
		} catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		
//		try{
//			MatlabProxyFactory factory = new MatlabProxyFactory();
//			MatlabProxy proxy = factory.getProxy();
//			proxy.eval("display('hey!')");
//			proxy.disconnect();
//		} catch(MatlabConnectionException mConExcep){
//			mConExcep.printStackTrace();
//		}
		
		
	}	
		
	

}


//Undefined variable "matlabcontrol" or class
//"matlabcontrol.MatlabClassLoaderHelper.configureClassLoading".