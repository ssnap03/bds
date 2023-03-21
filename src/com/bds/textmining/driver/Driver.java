package com.bds.textmining.driver;

import java.io.IOException;
import java.util.*;

import com.bds.textmining.preprocessing.*;
import com.bds.textmining.topicmodeling.TopicModeler;
import com.bds.textmining.algorithms.*;
import com.bds.textmining.evaluation.Evaluator;
import com.bds.textmining.plotter.Plotter;

import com.bds.textmining.vectorization.Vectorizer;

public class Driver {
	
	public static void cluster(String method, Map<String, List<Double>> tfidf) {
		Map<Integer,Map<String,List<Double>>> clusters = null;
		if(method.equals("kmeans"))
			clusters = Clustering.kmeans(tfidf, 3, "cosine");
		else
			clusters = Clustering.kmeansPlusPlus(tfidf, 3, "cosine");

		 Map<String,List<Double> > c0 = clusters.get(0);
		 Map<String,List<Double> > c1 = clusters.get(1);
		 Map<String,List<Double> > c2 = clusters.get(2);
		 
		 List<String> docTitles0 = new ArrayList<String>(c0.keySet());
		 List<String> docTitles1 = new ArrayList<String>(c1.keySet());
		 List<String> docTitles2 = new ArrayList<String>(c2.keySet());
		 
		 List<List<Double> > f0 = new ArrayList<List<Double>>(c0.values());
		 List<List<Double> > f1 = new ArrayList<List<Double>>(c1.values());
		 List<List<Double> > f2 = new ArrayList<List<Double>>(c2.values());
		 List<List<Double> > pcaIn = new ArrayList<List<Double>>();
		 
		 System.out.println(method+"\nCluster Sizes : ");
		 
		 System.out.println(f0.size());
		 System.out.println(f1.size());
		 System.out.println(f2.size());
		 
		 pcaIn.addAll(f0);
		 pcaIn.addAll(f1);
		 pcaIn.addAll(f2);
		 
		 double pin[][] = new double[pcaIn.size()][pcaIn.get(0).size()];
		 for(int i=0;i<pcaIn.size();i++) {
			 for(int j=0;j<pcaIn.get(0).size();j++) {
				 double x = pcaIn.get(i).get(j);
				 if(x==0.0)
					 pin[i][j]= pcaIn.get(i).get(j);
				 else
					 pin[i][j]=pcaIn.get(i).get(j);
			 }
		 }
		
		int[] clusterInfo = new int[pcaIn.size()];
		String[] docTitles = new String[pcaIn.size()];
		String[] actual = new String[pcaIn.size()];
		String[] predicted = new String[pcaIn.size()];

		Map<String,Integer> m1 = new HashMap<>();
		Map<String,Integer> m2 = new HashMap<>();
		Map<String,Integer> m3 = new HashMap<>();
		TopicModeler t = new TopicModeler();

		int k = 0;
		System.out.println("\nCluster 1 : ");
		for(int j =0; j<f0.size(); j++) {
			clusterInfo[k] = 0;
			docTitles[k] = docTitles0.get(j);
			predicted[k++] = t.extractFolderName(docTitles0.get(j));
			System.out.println(docTitles0.get(j));
			if(m1.containsKey(t.extractFolderName(docTitles0.get(j))))
				m1.put(t.extractFolderName(docTitles0.get(j)),m1.get(t.extractFolderName(docTitles0.get(j)))+1);
			else
				m1.put(t.extractFolderName(docTitles0.get(j)), 1);
		}
		System.out.println("\nCluster 2 : ");

		for(int j =0; j<f1.size(); j++) {
			clusterInfo[k] = 1;
			docTitles[k] = docTitles1.get(j);
			predicted[k++] = t.extractFolderName(docTitles1.get(j));
			System.out.println(docTitles1.get(j));
			if(m2.containsKey(t.extractFolderName(docTitles1.get(j))))
				m2.put(t.extractFolderName(docTitles1.get(j)),m2.get(t.extractFolderName(docTitles1.get(j)))+1);
			else
				m2.put(t.extractFolderName(docTitles1.get(j)), 1);
		}
		System.out.println("\nCluster 3 : ");

		for(int j =0; j<f2.size(); j++) {
			clusterInfo[k] = 2;
			docTitles[k] = docTitles2.get(j);
			predicted[k++] = t.extractFolderName(docTitles2.get(j));
			System.out.println(docTitles2.get(j));
			if(m3.containsKey(t.extractFolderName(docTitles2.get(j))))
				m3.put(t.extractFolderName(docTitles2.get(j)),m3.get(t.extractFolderName(docTitles2.get(j)))+1);
			else
				m3.put(t.extractFolderName(docTitles2.get(j)), 1);
		}
		int max1=0,max2=0,max3=0;
		String l1="",l2="",l3="";
		for(String s : m1.keySet()) {
			if(m1.get(s)>max1) {
				max1= m1.get(s);
				l1 = s;
			}
		}
		for(String s : m2.keySet()) {
			if(m2.get(s)>max2) {
				max2= m2.get(s);
				l2 = s;
			}
		}
		for(String s : m3.keySet()) {
			if(m3.get(s)>max3) {
				max3= m3.get(s);
				l3 = s;
			}
		}
		int pos=0;
		for(int j =0; j<f0.size(); j++) 
			actual[pos++]=l1;
		for(int j =0; j<f1.size(); j++) 
			actual[pos++]=l2;
		for(int j =0; j<f2.size(); j++) 
			actual[pos++]=l3;
		
		Evaluator e = new Evaluator();
		
		e.computeConfusionMatrix(predicted, actual, 1);
		e.computeMetrics(predicted, actual);
		
		DimensionalityReduction d = new DimensionalityReduction();
		double out[][] = d._PCA(pcaIn,2);
		 
		
		
		Plotter.plotClusters(out, clusterInfo, docTitles);
	
	}

	public static void main(String[] args) throws IOException {
		Preprocessor preprocessor = new Preprocessor();
		Vectorizer vectorizer = new Vectorizer();
		List<List<String>> preprocessed = preprocessor.pipeline("src/data/", 3, 5);

		Map<String, List<Double>> tfidf = (vectorizer.generateTfIdfMatrix(preprocessed));
		
		
	     TopicModeler t = new TopicModeler();
	     t.extractTopics(tfidf, Arrays.asList(vectorizer.getVocab(preprocessed)), 8);
	
		 
		 cluster("kmeans",tfidf);	
		 cluster("kmeans++",tfidf);	

	}

}