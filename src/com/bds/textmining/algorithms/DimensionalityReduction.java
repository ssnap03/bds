package com.bds.textmining.algorithms;

import java.util.List;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

public class DimensionalityReduction {
	
	public static double[][] _PCA(List<List<Double>> input, int nComponents) {
		
		double[][] features = new double[input.size()][input.get(0).size()];
		
		double[][] out = new double[input.size()][nComponents];
		
		for(int i = 0; i < input.size(); i++) {
		    for(int j = 0; j <input.get(0).size(); j++) {
		    	features[i][j] = input.get(i).get(j);
		    }
		}
		
		RealMatrix X = MatrixUtils.createRealMatrix(features);
		
		Covariance c = new Covariance(X);
		
		RealMatrix C = c.getCovarianceMatrix();
		
		EigenDecomposition ed = new EigenDecomposition(C);
		
		RealMatrix V = ed.getV();
		
		RealMatrix PC = X.multiply(V);
		
		PC.copySubMatrix(0,out.length-1,0,nComponents-1,out);
				
		return out;
	}
	
}/*

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class DimensionalityReduction {
	
	private double[][] matrix;
	private double[][] result;
	
	public DimensionalityReduction(double[][] matrix) {
		setMatrix(matrix);
	}
	
	public void projectMatrix(int d) {
		// create real matrix
		RealMatrix m = MatrixUtils.createRealMatrix(getMatrix());
		// compute its transpose
		RealMatrix mt = m.transpose();
		// compute co-variance matrix M^T M
		RealMatrix mtm = mt.multiply(m);
		// compute the eigen decomposition
		EigenDecomposition ed = new EigenDecomposition(mtm);
		// choose the d first columns of ed
		RealMatrix edp = ed.getV().getSubMatrix(0, ed.getV().getRowDimension()-1, 0, d-1);
		// project original matrix to new space
		RealMatrix projected = m.multiply(edp);
		// store the rows of the projected matrix into a double[][]
		double[][] temp = new double[projected.getRowDimension()][projected.getColumnDimension()];
		for(int i=0; i<temp.length; i++) {
			temp[i] = projected.getRow(i);
		}
		setResult(temp);
	}
	
	public double[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(double[][] matrix) {
		this.matrix = matrix;
	}

	public double[][] getResult() {
		return result;
	}

	public void setResult(double[][] result) {
		this.result = result;
	}

}*/
