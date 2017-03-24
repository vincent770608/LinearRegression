package jch.ml.linearregression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinearRegression 
{
	public static void main(String [] args)
	{
		LinearRegression lr = new LinearRegression();
		lr.standRegres();
		
//		double[][] ary = {{3, 0, 6, 0}, 
//				{0, 0, 0, 8}, 
//				{0, 3, 0, 0},
//				{2, 0, 0, 0}}; 
//		double[][] ans = lr.inverse(ary); 
//			
//		 for(int i=0;i<ary.length;i++)
//	        {
//	        	for(int j=0;j<ary.length;j++)
//	        	{
//	        		System.out.print(ans[i][j]+",");
//	        	}
//	        	System.out.println();
//	        }			
	}
	
	public void standRegres()
	{
		List<double[]> inputs = new ArrayList<double[]>();
		
		List<Integer> outputs = new ArrayList<Integer>();
		
		Map<List<Integer>,List<double[]>> labeldata = this.getData(new File("dataset/train"));
		for(Map.Entry<List<Integer>, List<double[]>> getentry : labeldata.entrySet())
		{
			inputs = getentry.getValue();
			outputs = getentry.getKey();
		}
		
		double [][] matrix = new double[inputs.size()][inputs.get(0).length];
		double [][] y = new double[outputs.size()][1];
		for(int i=0; i<inputs.size();i++)
		{
			matrix[i] = inputs.get(i);
			y[i][0] = (double)outputs.get(i);
//			System.out.println("y["+i+"]["+0+"]:"+y[i][0]);
		}
		System.out.println("y:"+y.length+","+y[0].length);
		System.out.println("matrix:"+matrix.length+","+matrix[0].length);
		double [][] transmatrix = this.transpose(matrix);
		
		double [][] multiplicatedmatrix = this.multiplicate(transmatrix,matrix);
		System.out.println("multiplicatedmatrix:"+multiplicatedmatrix.length+","+multiplicatedmatrix[0].length+","+multiplicatedmatrix[0][0]);
		
		double [][] inversedmatrix = this.inverse(multiplicatedmatrix);
		System.out.println("inversedmatrix:"+inversedmatrix.length+","+inversedmatrix[0].length+","+inversedmatrix[0][0]);
		
		double [][] pseudoinversed  = this.multiplicate(inversedmatrix,transmatrix);
		System.out.println("pseudoinversed:"+pseudoinversed.length+","+pseudoinversed[0].length+","+pseudoinversed[0][0]);
		
		double [][] weights  = this.multiplicate(pseudoinversed,y);
		System.out.println("weights:"+weights.length+","+weights[0].length+","+weights[0][0]);
		
		try 
		{
			File wfile = new File("dataset/LinearRegression_weight");
			FileWriter wfilewr = new FileWriter(wfile);
			BufferedWriter wfilebr = new BufferedWriter(wfilewr);
			
			for(int k=0; k<weights.length;k++)
			{
				wfilebr.write(""+weights[k][0]);
				wfilebr.write("\r\n");

//				System.out.println(k+" : "+weights.get(k));
			}
			
			wfilebr.close();
			wfilewr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Map<List<Integer>,List<double[]>> getData(File file)
	{
		List<Integer> labels = new ArrayList<Integer>();
		List<double[]> dataset = new ArrayList<double[]>();
		Map<List<Integer>,List<double[]>> labeldata = new HashMap<List<Integer>,List<double[]>>();
		FileReader fr;
		try 
		{

			fr = new FileReader(file);
		
			BufferedReader br = new BufferedReader(fr);
			String line = null;
//			int datanum =0;
			while ((line = br.readLine()) != null) 
			{
//				System.out.println(line);
			    
				String[] datas = line.split(" ");
				
				String answer = datas[0].replace("+", "");
//				System.out.println(answer);
//				y[datanum] = Integer.parseInt(answer);
				labels.add(Integer.parseInt(answer));
				
				double [] x = new double[datas.length-1];//4145
			    for(int i=0;i<datas.length-1;i++)//
			    {
			    	String [] inval = datas[i+1].split(":");
			    	x[i] = Double.parseDouble(inval[1]);
			    }
//			    datanum++;
			    
			    dataset.add(x);
//			    System.out.println(labels);
//			    System.out.println(dataset);
			}
			
			labeldata.put(labels, dataset);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return labeldata;
	}
	
	public double[][] transpose(double[][] matrix)
	{
		double[][] transmatrix = new double [matrix[0].length][matrix.length];
		
		for(int i=0;i<transmatrix.length;i++)
		{
			for(int j=0;j<transmatrix[0].length;j++)
			{
				transmatrix[i][j] = matrix[j][i];
			}
		}
		
		System.out.println("transmatrix:"+transmatrix.length+","+transmatrix[0].length);
		return transmatrix;
	}
	
	
	public double[][] multiplicate(double[][] a,double[][] b)
	{
		double [][] multiplicatedmatrix =  new double[a.length][b[0].length];
		for(int i=0;i<a.length;i++)  
		{
			for(int j=0;j<b[0].length;j++)  
			{
				multiplicatedmatrix[i][j] = 0.0;

				for(int k=0;k<a[0].length;k++)  
				{
//					if(a[0].length == 4145)
//					{
//						System.out.println("a["+i+"]["+k+"]*b["+k+"]["+j+"]:"+a[i][k]+"*"+b[k][j]);
//					}
					
					multiplicatedmatrix[i][j] += a[i][k]*b[k][j];
//					System.out.println("multiplicatedmatrix["+i+"]["+j+"]:"+multiplicatedmatrix[i][j]);
//					multiplicatedmatrix[j][i]+=a[j][k]*b[k][i];
				}
				
			}
		}
		return multiplicatedmatrix;
	}

	public double[][] inverse(double[][] matrix)
	{
		int n = matrix.length;
		if (n != matrix[0].length) 
		{
			return null;
        }
		else
		{	
	        double identity[][] = new double[n][n];
	        
	        //identity matrix
	        for (int i=0; i<n; i++) 
	        {
	        	for(int j=0;j<n;j++)
	        	{
	        		if(j==i)
	        		{
	        			identity[i][j] = 1;
	        		}
	        		
	        		else
	        		{
	        			identity[i][j] = 0;	
	        		}
	        	}
	        }
	            
	        //Transform the matrix into an upper triangle
	        for(int i=0; i<n; i++)//column
	        {
	        	for(int j=i+1; j<n; j++) 
				{ 
					if(Math.abs(matrix[j][i]) > Math.abs(matrix[i][i])) 
					{ 
						double temp[] = new double[n]; 
						temp=matrix[i]; matrix[i]=matrix[j]; matrix[j]=temp; // äº’æ?›çŸ©?™£?…©è¡? 
						temp=identity[i]; identity[i]=identity[j]; identity[j]=temp; // C ??šå?Œæ¨£??‹ç?? 
					} 
				} 
	        	
	        	for(int j=i+1; j<n; j++)//row
	        	{
	        		if(j!=i)
	        		{
	        			if(matrix[j][i]!=0)//row
		        		{
//		        			System.out.println(matrix[j][i]);
		        			double diff = -matrix[i][i]/matrix[j][i];
//	        				double diff = matrix[j][i]/matrix[i][i];
		        			for(int k=0; k<n; k++)
		        			{	        			
		        				matrix[j][k] = matrix[j][k]*diff+matrix[i][k];
		        				identity[j][k] = identity[j][k]*diff+identity[i][k];
		        				
//		        				matrix[j][i] = matrix[j][i]*-(matrix[i][i]/matrix[j][i])+matrix[i][i];
//				        		identity[j][i] = identity[j][i]*-(identity[i][i]/identity[j][i])+identity[i][i];	
		        			}	
		        		}
	        		}
	        	}
	        }
	        //change the numbers on the diagonal to 1
	        for(int i=0; i<n; i++)
	        {
	        	for(int j=0;j<n;j++)
	        	{
		        	identity[i][j] /= matrix[i][i];
	        	}
	        }
	        
	        //reverse iteration
	        for(int i=n-1;i>0;i--)//column
	        {
	        	for(int j=i-1;j>=0;j--)
	        	{
	        		if(matrix[j][i]!=0.0)//row
	        		{
	        			double diff = -matrix[j][i]/matrix[i][i];
	        			for(int k=0; k<n ;k++)
	        			{
	        				matrix[j][k] += matrix[i][k]*diff;
	        				identity[j][k] += identity[i][k]*diff;
	        			}
	        		}
	        	}
	        }   
	        return identity;
		}
    }
}
