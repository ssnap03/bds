package com.bds.textmining.vectorization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Vectorizer {
	
	public String[] getVocab(List<List<String>> words){
        Set<String> vocabulary = new HashSet<String>();

		for(int i=0;i < words.size();i++) {
			List<String> l = words.get(i);
			String key = l.get(l.size()-1);
			for(int j =0;j<l.size()-1;j++) {
				vocabulary.add(l.get(j));
			}
		}
        String[] vocabList = vocabulary.toArray(new String[vocabulary.size()]);
        Arrays.sort(vocabList);
		return vocabList;
	}
	
	public Map<String, List<Double>> generateTfIdfMatrix(List<List<String>> words){
		Map<String, List<String>> terms = new HashMap<String, List<String>>();
        Set<String> vocabulary = new HashSet<String>();
        Map<String, List<Double>> tfidf =  new HashMap<>();

		for(int i=0;i < words.size();i++) {
			List<String> l = words.get(i);
			String key = l.get(l.size()-1);
			List<String> val = new ArrayList<>();
			for(int j =0;j<l.size()-1;j++) {
				val.add(l.get(j));
				vocabulary.add(l.get(j));
			}
			terms.put(key, val);
		}
        String[] vocabList = vocabulary.toArray(new String[vocabulary.size()]);
        Arrays.sort(vocabList);
        Map<String, Integer> word2idx = new HashMap<>();
        for(int i=0;i<vocabList.length;i++)
        	word2idx.put(vocabList[i], i);	
		 Iterator iter = terms.entrySet().iterator();
	     
        while (iter.hasNext()) {
 
            Map.Entry item = (Map.Entry)iter.next();
            List<String> wordlist = (List<String>) item.getValue();
            String file = (String) item.getKey();
            List<Double> freq =  Arrays.asList(new Double[vocabulary.size()]);
            for(int i=0;i<vocabList.length;i++)
            	freq.set(i,0.0);
            tfidf.put(file, freq);
            for(String w : wordlist) {
            	tfidf.get(file).set(word2idx.get(w), tfidf.get(file).get(word2idx.get(w))+1);
            }
            for(int i=0;i<vocabList.length;i++) {
            	tfidf.get(file).set(i,tfidf.get(file).get(i));///wordlist.size()
            	int docCount = 0;
            	if(tfidf.get(file).get(i)!=0.0) {
            		 Iterator it = terms.entrySet().iterator();
            	     
        	        while (it.hasNext()) {
        	 
        	            Map.Entry entry = (Map.Entry)it.next();
        	            List<String> wl = (List<String>) entry.getValue();
        	            for(String wr: wl) {
        	            	if(wr.equals(vocabList[i])) {
        	            		docCount++;
        	            		break;
        	            	}
        	            }           
            	}
        	    double idf = Math.log((double)(terms.size())/((double)docCount))/Math.log(2)+1;
        	    tfidf.get(file).set(i,tfidf.get(file).get(i)*idf);
            	}  
            }
        }
       
        
		return tfidf;
	}
	
	public Map<String, List<Double>> normalize(Map<String, List<Double>> v){
		 Iterator it = v.entrySet().iterator();
	     
	        while (it.hasNext()) {
	 
	            Map.Entry entry = (Map.Entry)it.next();
	            List<Double> wl = (List<Double>) entry.getValue();
	            double mag = 0;
	            for(Double s: wl) {
	            	mag += s*s;
	            }
	            mag = Math.sqrt(mag);
	            List<Double> d = new ArrayList<>();
	            for(Double s:wl) {
	            	d.add(s/mag);
	            }
	            v.put((String)entry.getKey(), d);
	        }
	        return v;
	}
	        
}
