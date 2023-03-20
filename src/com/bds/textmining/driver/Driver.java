package driver;

import java.io.IOException;
import java.util.*;

import com.bds.textmining.preprocessing.*;
import com.bds.textmining.algorithms.*;
import com.bds.textmining.plotter.Plotter;

import vectorization.Vectorizer;

public class Driver {

	public static void main(String[] args) throws IOException {
		Preprocessor preprocessor = new Preprocessor();
		Vectorizer vectorizer = new Vectorizer();
		List<List<String>> preprocessed = preprocessor.pipeline("src/data/", 3, 5);

		Map<String, List<Double>> tfidf = (vectorizer.generateTfIdfMatrix(preprocessed));
		 
		 Map<Integer,Map<String,List<Double>>> clusters = Clustering.kmeansPlusPlus(tfidf, 3, "cosine");

		 Map<String,List<Double> > c0 = clusters.get(0);
		 Map<String,List<Double> > c1 = clusters.get(1);
		 Map<String,List<Double> > c2 = clusters.get(2);
		 
		 List<String> docTitles0 = new ArrayList<String>(c0.keySet());
		 List<String> docTitles1 = new ArrayList<String>(c1.keySet());
		 List<String> docTitles2 = new ArrayList<String>(c2.keySet());
		 
		 List<List<Double> > f0 = new ArrayList<List<Double>>(c0.values());
		 List<List<Double> > f1 = new ArrayList<List<Double>>(c1.values());
		 List<List<Double> > f2 = new ArrayList<List<Double>>(c2.values());
		 List<List<Double> > pcaIn = new ArrayList<List<Double>>(c0.values());
		 
		 System.out.println(f0.size());
		 System.out.println(f1.size());
		 System.out.println(f2.size());
		 
		 pcaIn.addAll(f1);
		 pcaIn.addAll(f2);
		
		int[] clusterInfo = new int[pcaIn.size()];
		String[] docTitles = new String[pcaIn.size()];
		
		int k = 0;
		for(int j =0; j<f0.size(); j++) {
			clusterInfo[k] = 0;
			docTitles[k++] = docTitles0.get(j);
		}
		for(int j =0; j<f1.size(); j++) {
			clusterInfo[k] = 1;
			docTitles[k++] = docTitles1.get(j);
		}
		for(int j =0; j<f2.size(); j++) {
			clusterInfo[k] = 2;
			docTitles[k++] = docTitles2.get(j);
		}
		
		double[][] out = DimensionalityReduction._PCA(pcaIn,2);
		 
		for(int i = 0; i<out.length; i++) {
			System.out.print("(");
			for(int j = 0; j < out[0].length; j++) {
				System.out.print(out[i][j]);
				System.out.print(",");
			}
			System.out.println(")");
		}
		
		Plotter.plotClusters(out, clusterInfo, docTitles);
		 
//		 String[] l = vectorizer.getVocab(preprocessed);
		 
		
//	     TopicModeler t = new TopicModeler();
//	     t.extractTopics(tfidf, Arrays.asList(vectorizer.getVocab(preprocessed)), 8);
	
		
	}

}