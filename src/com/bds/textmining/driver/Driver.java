package com.bds.textmining.driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import com.bds.textmining.preprocessing.*;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import com.bds.textmining.topicmodeling.TopicModeler;
import com.bds.textmining.vectorization.Vectorizer;

public class Driver {

	public static void main(String[] args) throws IOException {
		Preprocessor preprocessor = new Preprocessor();
		Vectorizer vectorizer = new Vectorizer();
		List<List<String>> preprocessed = preprocessor.pipeline("src/data/", 3, 5);
		System.out.println(preprocessed.size());
		 Map<String, List<Double>> tfidf = (vectorizer.generateTfIdfMatrix(preprocessed));
		 String[] l = vectorizer.getVocab(preprocessed);
		 
		
	     TopicModeler t = new TopicModeler();
	     t.extractTopics(tfidf, Arrays.asList(vectorizer.getVocab(preprocessed)), 8);
	
		
	}
}
