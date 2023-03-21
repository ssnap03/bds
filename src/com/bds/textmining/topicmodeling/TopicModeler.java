package com.bds.textmining.topicmodeling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bds.textmining.preprocessing.FileReader;

import edu.stanford.nlp.util.Pair;
import com.bds.textmining.utils.Tuple;

public class TopicModeler {
	
	public String extractFolderName(String path) {
		int count = 0, start=0, end = 0;
		for(int i=0;i<path.length();i++) {
			if(path.charAt(i)=='/') {
				count++;
				if(count==2)
					start = i;
				if(count==3)
					end = i;
			}
				
		}
		return path.substring(start+1, end);
	}
	
	public void extractTopics(Map<String, List<Double>> features, List<String> vocab, int N) throws IOException {
		Map<String, List<List<Double>>> m = new HashMap<>(); 
		Set<String> keys = features.keySet();
		for(String k : keys){
			m.put(extractFolderName(k), new ArrayList<>());
		}
		for(String k : keys){
			List<List<Double>> l = m.get(extractFolderName(k));
			l.add(features.get(k));
			m.put(extractFolderName(k), l);
		}
		Map<Integer,String> idx2Word = new HashMap<>();
		for(int i=0;i<vocab.size();i++) {
			idx2Word.put(i, vocab.get(i));
		}
		Set<String> folders = m.keySet();
	    BufferedWriter writer = new BufferedWriter(new FileWriter("src/topics.txt"));

		for(String f : folders) {
			List<List<Double>> docVectors = m.get(f);
			Double maxVals[] = new Double[vocab.size()];
			for(int i=0;i<vocab.size();i++) {
				maxVals[i]=0.0;
			}
			for(int i=0;i<vocab.size();i++) {
				for(List<Double> d : docVectors) {
					maxVals[i] += d.get(i);
				}
			}
			List<Pair<String,Double>> s = new ArrayList<>();
			for(int i=0;i<vocab.size();i++) {
				s.add(new Pair(idx2Word.get(i),maxVals[i]));
			}
			
			Collections.sort(s, Comparator.comparing(p -> -p.second()));
			
			
			System.out.println("Topics extracted from folder "+f+" are: \n");

		    writer.write("Topics extracted from folder "+f+" are: \n");
			for(int i=0;i<N;i++) {
				System.out.println(s.get(i).first);
				writer.write(s.get(i).first+"\n");
			}
			System.out.println();
			writer.write('\n');

		}
	    writer.close();

	}
}
