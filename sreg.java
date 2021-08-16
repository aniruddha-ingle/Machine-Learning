import java.io.*;
import java.util.*;
import java.lang.*;
class sreg
{
	public static void main(String [] args) throws IOException{
		sreg ks = new sreg();
		ks.proc();
	}
	public void proc() throws IOException{
		sreg ks = new sreg();
		File fp = new File("sreg.txt");
		BufferedReader bfr = new BufferedReader(new FileReader(fp));
		String [] arrOfStr;
		String in;
		int tupCount = 0;
		
		//Analysing Database Schema
		in = bfr.readLine();
		System.out.println(in);
		arrOfStr = in.split(",");
		int n = arrOfStr.length;
		//System.out.println(n);
		
		double [][] a = new double[n][n];
		double [] b = new double[n];//ySum and singleSum[n-1]
		double [] w = new double[n]; //result of inv(A)B
		
		double [] singleSum = new double[n];
		double [] ySum = new double[n-1];
		double [][] sumMat = new double[n-1][n-1];

		//Initial Parse
		while((in = bfr.readLine()) != null){
			System.out.println(in);
			arrOfStr = in.split(",");
			for(int i=0;i<n;i++){
				if(i<n-1){
					singleSum[i]+=Double.parseDouble(arrOfStr[i]);
					ySum[i]+=Double.parseDouble(arrOfStr[i])*Double.parseDouble(arrOfStr[n-1]);
					for(int j=0;j<n-1;j++){
						sumMat[i][j] += Double.parseDouble(arrOfStr[i])*Double.parseDouble(arrOfStr[j]);
					}
				}
				else{
					singleSum[i]+=Double.parseDouble(arrOfStr[i]);
				}
			}
			tupCount++;
		}
		System.out.println("");
		//System.out.println("1"+Arrays.toString(singleSum));
		//System.out.println("2"+Arrays.toString(ySum));
		//System.out.println("3"+Arrays.deepToString(sumMat));
		String [][] dat = new String [tupCount][n];
		
		bfr = new BufferedReader(new FileReader(fp));
		tupCount = -1;
		
		//Reading Actual Data
		while((in = bfr.readLine()) != null){
			//System.out.println(in);
			arrOfStr = in.split(",");
			if(tupCount>-1){
				for(int i=0;i<n;i++){
					dat[tupCount][i] = arrOfStr[i];
				}
			}
			tupCount++;
		}
		//System.out.println(tupCount);
		//System.out.println(Arrays.deepToString(dat));
		for(int i=0;i<n;i++){
			if(i == 0){
				b[i] = singleSum[n-1];
			}
			else{
				b[i] = ySum[i-1];
			}
		}
		a[0][0] = tupCount;
		for(int i=1;i<n;i++){
			a[0][i] = singleSum[i-1];
			a[i][0] = singleSum[i-1];
		}
		for(int i=1;i<n;i++){
			for(int j=1;j<n;j++){
				a[i][j] = sumMat[i-1][j-1];
			}
		}
		System.out.println("Sub Matrix of A: \n"+Arrays.deepToString(sumMat));
		System.out.println("Vector B: \n"+Arrays.toString(b));
		System.out.println("Matrix A: \n"+Arrays.deepToString(a));
		a = invert(a);
		w = multiplyMatrices(a,b,n);
		System.out.println("Vector W: \n"+Arrays.toString(w));
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Value(s) of Independent Variables:");
		in = sc.nextLine();
		arrOfStr = in.split(",");
	
		double [] x = new double[n-1];
		for(int i=0;i<n-1;i++){
			x[i] = Double.parseDouble(arrOfStr[i]);
		}
		double result = multiplyMatrices(w,x,n);
		System.out.println("Resultant Value is:\t"+result);
	}
	//Utilities
	//the logic for matrix multiplication and inversion was understood froum sanfoundary
	 public static double[][] invert(double a[][]) 
    {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i=0; i<n; ++i) 
            b[i][i] = 1;
        gaussian(a, index);
        for (int i=0; i<n-1; ++i)
            for (int j=i+1; j<n; ++j)
                for (int k=0; k<n; ++k)
                    b[index[j]][k]
                    	    -= a[index[j]][i]*b[index[i]][k];
 
        for (int i=0; i<n; ++i) 
        {
            x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
            for (int j=n-2; j>=0; --j) 
            {
                x[j][i] = b[index[j]][i];
                for (int k=j+1; k<n; ++k) 
                {
                    x[j][i] -= a[index[j]][k]*x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    } 
    public static void gaussian(double a[][], int index[]) 
    {
        int n = index.length;
        double c[] = new double[n];
 
        for (int i=0; i<n; ++i) 
            index[i] = i;
 
        for (int i=0; i<n; ++i) 
        {
            double c1 = 0;
            for (int j=0; j<n; ++j) 
            {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }
 
        int k = 0;
        for (int j=0; j<n-1; ++j) 
        {
            double pi1 = 0;
            for (int i=j; i<n; ++i) 
            {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1) 
                {
                    pi1 = pi0;
                    k = i;
                }
            }
 
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i=j+1; i<n; ++i) 	
            {
                double pj = a[index[i]][j]/a[index[j]][j];
 
                a[index[i]][j] = pj;
 
                for (int l=j+1; l<n; ++l)
                    a[index[i]][l] -= pj*a[index[j]][l];
            }
        }
    }
	public static double[] multiplyMatrices(double[][] firstMatrix, double[] secondMatrix, int n) {
		double[] product = new double[n];
        for(int i = 0; i < n; i++) {
            for (int j = 0; j < 1; j++) {
                for (int k = 0; k < n; k++) {
                    product[i] += firstMatrix[i][k] * secondMatrix[k];
                }
            }
        }
        return product;
    }
	public static double multiplyMatrices(double[] firstMatrix, double[] secondMatrix, int n) {
		double product = firstMatrix[0];       
        for (int i = 0; i < n-1; i++) {
            product += firstMatrix[i+1] * secondMatrix[i];
        }
        return product;
    }
}
