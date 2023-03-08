package com.bds.textmining.algorithms;

import static org.junit.Assert.*;

//import java.util.List;
import java.util.*;
import com.bds.textmining.algorithms.*;

import org.junit.Test;

public class ClusteringTest extends Clustering {
	
	
	@Test
	public void euclideanKMeansTest() {
		Map<String,List<Double>> coords = new HashMap<String,List<Double>>();
		coords.put("0", Arrays.asList(1.5,2.5));
		coords.put("1", Arrays.asList(2.3,4.3));
		coords.put("2", Arrays.asList(3.5,1.4));
		coords.put("3", Arrays.asList(10.0,12.0));
		coords.put("4", Arrays.asList(12.0,13.0));
		coords.put("5", Arrays.asList(13.0,11.0));
		
		Map<Integer,Map<String,List<Double>>> euclideanClusters = Clustering.kmeans(coords, 2, "euclidean");

		assertEquals(euclideanClusters.get(0).values().toString(),"[[1.5, 2.5], [2.3, 4.3], [3.5, 1.4]]");
		assertEquals(euclideanClusters.get(1).values().toString(),"[[10.0, 12.0], [12.0, 13.0], [13.0, 11.0]]");
		
	}

}
