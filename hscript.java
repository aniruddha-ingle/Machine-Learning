// This program implements KNearestNeighbours Algorithm for a specific data set format.
import java.io.*;
import java.util.*;
import java.lang.*;
class hscript
{
	public static void main (String args []) throws IOException
	{
		hscript hs = new hscript();
		hs.quantifyData();
	}
	public double normaliseData(double min, double max, double val){
		return (val - min)/(max - min);
	}
	public String [] testSample(Double min, Double max){
		Scanner sc = new Scanner(System.in);
		System.out.println("\nEnter Unkown Sample:(m/f),(height in metres)");
		String in = sc.nextLine();
		String [] arrOfStr = in.split(",");
		if(arrOfStr[0].equals("m")){
			arrOfStr[0] = "0";
		}
		else{
			arrOfStr[0] = "1";
		}
		arrOfStr[1] = String.format("%.4f",normaliseData(min,max,Double.parseDouble(arrOfStr[1]))); 
		System.out.println("Normalised Test Sample: \n"+ Arrays.toString(arrOfStr));
		System.out.println("\n");
		return arrOfStr;
	}
	public double dist(String [] a, String [] b){
		String str;
		str = String.format("%.4f",Math.pow((Double.parseDouble(a[1]) - Double.parseDouble(b[0]) ),2 ));
		double x = Double.parseDouble(str);
		str = String.format("%.4f",Math.pow( (Double.parseDouble(a[2]) - Double.parseDouble(b[1]) ),2 ));
		double y = Double.parseDouble(str);
		str = String.format("%.4f",Math.sqrt(x+y));
		double z = Double.parseDouble(str);
		return(z);
	}
	public void quantifyData() throws IOException
	{
		hscript hs = new hscript();
		
		File fp = new File("hscript.txt");
		BufferedReader bfr = new BufferedReader(new FileReader(fp));	
		String [] arrOfStr;
		String numBuf;
		
		int tupCount = -2; //for mainData
		double min = 7200, max = -72, val = -72; //random values for debugging 
		
		while((numBuf = bfr.readLine()) != null){
			tupCount++;
			arrOfStr = numBuf.split(",");
			if (tupCount == 0){
				double temp = Double.parseDouble(arrOfStr[2]);
				max = temp;
				min = temp;
			}
			if (tupCount>0){
				double temp = Double.parseDouble(arrOfStr[2]);
				if (max < temp){
					max = temp;
				} 
				if (min > temp){
					min = temp;
				}
			}	
		}
		
		String [][] mainData = new String[tupCount+1][4];
		bfr = new BufferedReader(new FileReader(fp));//work around for reseting the file pointer
		tupCount = -2;
		
		while((numBuf = bfr.readLine()) != null){
			tupCount++;
			arrOfStr = numBuf.split(",");
			if (tupCount > -1){
				if(arrOfStr[1].equals("F")){
					arrOfStr[1]="1";
				}
				else{	
					arrOfStr[1]="0";
				}
			}
			if (tupCount > -1){
				double temp = Double.parseDouble(arrOfStr[2]);
				String dist = String.format("%.4f",normaliseData(min,max,temp));//work around for limiting precision and avoidning propogation of error
				arrOfStr[2] = dist;
			}
			if (tupCount > -1){				
				for (int j = 0; j < 4; j++){
					mainData[tupCount][j] = arrOfStr[j];
				}
			}
		}
		System.out.println(Arrays.deepToString(mainData));
		hs.knn(tupCount+1,mainData,min,max);
	}
	public void organise(int [] N, String [][] data, String [] tx ){
		for(int i=0;i<N.length;i++){
			for(int j=0;j<N.length-1;j++){
				if (dist(data[N[j]],tx)<dist(data[N[j+1]],tx)){
					int temp = N[j];
					N[j] = N[j+1];
					N[j+1] = temp;
				}
			}
		}
	}
	public String commonClass(int n, String [][] data, int [] N) //here n is number of nearest neighbors
	{
		int shortCount=0, tallCount=0, mediumCount=0;
		int index = 0;
		for(int i=0;i<n;i++){
			if(data[N[i]][3].equals("tall")){
				tallCount++;
			}
			else if(data[N[i]][3].equals("short")){
				shortCount++;
			}
			else if(data[N[i]][3].equals("medium")){
				mediumCount++;
			}
		}
		if(tallCount>shortCount && tallCount>mediumCount){
			return("tall");
		}
		else if (shortCount>tallCount && shortCount>mediumCount){
			return("short");
		}
		else{
			return("medium");
		}
	}
	public void displyN(int [] N, String [][] data){
		for(int i=0;i<N.length;i++){
			System.out.println(Arrays.toString(data[N[i]])+"\n");
		}
	}
	public void knn(int n, String [][] data, Double min, Double max) throws IOException
	{
		hscript hs = new hscript();
		int k = (int)Math.sqrt(n);
		int [] N = new int [k]; 
		String [] tx = hs.testSample(min,max);
		int j = 0;
		for(int i = 0; i < n; i++){
			if (i < k){
				N[j++] = i;
			}
			else{
				organise(N,data,tx);
				for(j=0;j<k;j++){
					if(dist(data[i],tx)<dist(data[N[j]],tx)){
						N[j] = i;
						organise(N,data,tx);//java arrays are mutable so change is not disjoint
						break;	
					}
				}
			}
		}
		System.out.println("Nearest Neighbor Set:\n");
		displyN(N,data);
		String cClass = commonClass(k,data,N);
		System.out.println("Tx belongs to class:\t"+cClass);
	}
}
