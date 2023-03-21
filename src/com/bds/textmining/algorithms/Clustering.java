package com.bds.textmining.algorithms;

import java.util.*;
import java.util.stream.Collectors;

public class Clustering {
	
	public static Map<Integer,Map<String,List<Double>>> kmeansPlusPlus(Map<String,List<Double>> input, int k, String similarityMeasure) {
		
		List<String> centroidKeys = getKMeansPlusPlusCentroids(input,k);
		
		Map<Integer,Map<String,List<Double>>> clusters = runKmeans(input,centroidKeys,k,similarityMeasure);
		
		return clusters;
		
	}

	public static Map<Integer,Map<String,List<Double>>> kmeans(Map<String,List<Double>> input, int k, String similarityMeasure){

		List<String> centroidKeys = getRandomCentroids(input,k);

		Map<Integer,Map<String,List<Double>>> clusters = runKmeans(input,centroidKeys,k,similarityMeasure);
		
		return clusters;
	}
	
	private static List<String> getKMeansPlusPlusCentroids(Map<String,List<Double>> input, int k) {
		List<String> keys = new ArrayList<String>(input.keySet());
		
		Collections.shuffle(keys, new Random(2));
		
		List<String> centroidKeys = keys.stream().limit(1).collect(Collectors.toList()); 
		
		while(centroidKeys.size() < k) {
			double max_distance = Double.MIN_VALUE;
			String max_key = null;
			for(String key : keys) {
				if(centroidKeys.contains(key))
					continue;
				double min_distance = Double.MAX_VALUE;
				for(String centroidKey : centroidKeys) {
					double distance = SimilarityMeasures.euclideanDistance(input.get(centroidKey), 
							input.get(key));
					distance *= distance;
					if(distance < min_distance)
						min_distance = distance;
				}
				if(min_distance > max_distance) {
					max_distance = min_distance;
					max_key = key;
				}
			}
			centroidKeys.add(max_key);
		}
		
		return centroidKeys;
	}
	
	private static List<String> getRandomCentroids(Map<String,List<Double>> input, int k) {
		
		List<String> keys = new ArrayList<String>(input.keySet());
		
		Collections.shuffle(keys, new Random(2));
		
		List<String> randKeys = keys.stream().limit(k).collect(Collectors.toList());
		
		System.out.println(randKeys);
		
		return randKeys;
	
	}
	
private static Map<Integer,Map<String,List<Double>>> runKmeans(Map<String,List<Double>> input, List<String> centroidKeys, int k, String similarityMeasure) {
		
		Map<Integer,Map<String,List<Double>>> clusters = new HashMap<Integer,Map<String,List<Double>>>(k);
		
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
			
			List<String> keys = new ArrayList<String>(input.keySet());
			
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
		
		return clusters;
		
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
