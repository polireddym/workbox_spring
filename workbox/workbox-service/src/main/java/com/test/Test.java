package com.test;

import java.util.List;

import oneapp.incture.workbox.pmc.wsdlconsumers.UMEManagementEngineConsumer;

public class Test {
	
	public static void main(String[] args) {
		UMEManagementEngineConsumer umeConsumer=new UMEManagementEngineConsumer();
		
		List<String> list=umeConsumer.getUsersAssignedInGroup("Approver");
		for(String s:list){
			System.err.println(s);
		}	
	}

}
