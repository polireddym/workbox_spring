package com.test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Main {
	
	public static void main(String[] args) {
		
		Map<Integer,String> map=new HashMap<Integer,String>();
		  map.put(1,"raju");
		  map.put(2, " jason statam");
		  map.put(3, "john");
		  List<String> list=new ArrayList<String>();
		
		//Set<Entry<Integer,String>> entryset = 
		
		Iterator<Entry<Integer,String>> it=map.entrySet().iterator();
		
		while (it.hasNext()) {
			list.add(it.next().getValue());
		}
		
		System.out.println(list);
	}

}
