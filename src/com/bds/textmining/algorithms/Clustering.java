package com.bds.textmining.algorithms;

import java.util.*;
import java.util.stream.Collectors;

public class Clustering {

	public static Map<Integer,Map<String,List<Double>>> kmeans(Map<String,List<Double>> input, int k, String similarityMeasure){
		
		Map<Integer,Map<String,List<Double>>> clusters = new HashMap<Integer,Map<String,List<Double>>>(k);
		
		List<String> keys = new ArrayList<String>(input.keySet());

		List<String> centroidKeys = getRandomCentroids(keys,k);

		Map<Integer, List<Double>> centroids = new HashMap<Integer, List<Double>>(k);
		Map<Integer, List<Double>> prevCentroids = new HashMap<Integer, List<Double>>(k);
		
		int clusterNum = 0;
		
		for(String c: centroidKeys) {
			centroids.put(clusterNum, input.get(c));
			clusters.put(clusterNum++, new HashMap<String,List<Double>>());			
		}	
		
		int n_iters = 0;
		do {
			n_iters++;
			prevCentroids.putAll(centroids);
			
			Map<Integer,Map<String,List<Double>>> tempClusters = new HashMap<Integer,Map<String,List<Double>>>(k);
			
			for(int i = 0; i<k; i++)
				tempClusters.put(i, new HashMap<String,List<Double>>());		
			
			for(String key: keys) {
				
				List<Double> vec = input.get(key);
				
				if(similarityMeasure == null || similarityMeasure == "" || similarityMeasure.equalsIgnoreCase("euclidean")) {
					double min = Double.MAX_VALUE;
					int min_key = 0;
					for(int i = 0; i<k; i++) {
						double distance = SimilarityMeasures.euclideanDistance(prevCentroids.get(i), vec);
						if(distance < min) {
							min_key = i;
							min = distance;
						}
					}
					tempClusters.get(min_key).put(key, vec);
					
				} else if (similarityMeasure.equalsIgnoreCase("cosine")) {
					double max = Double.MIN_VALUE;
					int max_key = 0;
					for(int i = 0; i<k; i++) {
						double similarity = SimilarityMeasures.cosineSimilarity(prevCentroids.get(i), vec);
						if(similarity > max) {
							max_key = i;
							max = similarity;
						}
					}
					tempClusters.get(max_key).put(key, vec);
				}

			}
			for(int i = 0; i<k; i++) {
				List<Double> meanVec = columnwiseMean(tempClusters.get(i));
				centroids.put(i, meanVec);
			}
			
			clusters.clear();
			clusters.putAll(tempClusters);
		} while(!prevCentroids.equals(centroids));
		System.out.println("num iterations = "+n_iters);
		return clusters;
	}
	
	private static List<String> getRandomCentroids(List<String> keys, int k) {
		
		Collections.shuffle(keys, new Random(5));
		
		List<String> randKeys = keys.stream().limit(k).collect(Collectors.toList());
		
		System.out.println(randKeys);
		
		return randKeys;
	
	}
	
	public static List<Double> columnwiseMean(Map<String,List<Double>> input) {
		
		Collection<List<Double>> features= input.values();
		
		int numRows = features.size();
		
		int numCols = features.iterator().next().size();
		
		List<Double> meanVec = new ArrayList<Double>(Collections.nCopies(numCols, 0.0));
		
		for(List<Double> fea: features)
			for(int i = 0; i<numCols; i++)
				meanVec.set(i,meanVec.get(i)+(fea.get(i)/numRows));

		return meanVec;
	}
}
