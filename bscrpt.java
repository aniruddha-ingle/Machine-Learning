import java.io.*;
import java.util.*;
import java.lang.*;
class bscrpt
{
	public static void main (String args []) throws IOException
	{
		bscrpt fs = new bscrpt();
		fs.displayData();
		fs.dataGet();
	}
	public void displayData() throws IOException
	{
		File fp = new File("buycR.txt");
		BufferedReader bfr = new BufferedReader (new FileReader(fp));
		String str;
		while((str = bfr.readLine()) != null)
			System.out.println(str);
	}
	public void dataGet() throws IOException
	{
		File fp = new File("buycR.txt");
		BufferedReader bfr = new BufferedReader (new FileReader(fp));
		
		String str;
		bscrpt fs = new bscrpt();
		
		int bcYesCount = 0;
		int tupleCount = -1;
		
		while((str = bfr.readLine()) != null)
		{
			tupleCount++;
			String [] arrOfStr = str.split(",",5);
			if (arrOfStr[4].equals("yes"))
				bcYesCount++;
		}
		double yProb = prob2(bcYesCount,(tupleCount-bcYesCount));
		double nProb = 1-yProb;
		System.out.println("Class (Buy Computer = No) : "+nProb);
		System.out.println("Class (Buy Computer = Yes) : "+yProb);
		
		fs.ukSamp(yProb,bcYesCount,tupleCount-bcYesCount);
	
	}
	public void ukSamp(double yProb,int bcYesCount,int bcNoCount) throws IOException
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("\nEnter number of unknown samples:");
		int n = sc.nextInt();
		sc.nextLine();
		bscrpt fs = new bscrpt();
		String [][] x = new String [n][4];
		for (int i = 0; i < n; i++)
		{	
			System.out.println("\nEnter the attributes for the unknown sample(X"+(i+1)+") :");

			System.out.println("Age: ");
			x[i][0] = sc.nextLine();
		
			System.out.println("Income: ");
			x[i][1] = sc.nextLine();

			System.out.println("Student?: ");
			x[i][2] = sc.nextLine();
		
			System.out.println("Credit Rating: ");
			x[i][3] = sc.nextLine();
		
			fs.bayesCount(x[i],yProb,bcYesCount,bcNoCount);
		}
	}
	public void bayesCount(String [] x, double yProb, int bcYesCount,int bcNoCount) throws IOException
	{
		bscrpt fs = new bscrpt();
		File fp = new File("buycR.txt");
		BufferedReader bfr = new BufferedReader (new FileReader(fp));
		String str;
		int [] classYesCount = new int [4];
		int [] classNoCount = new int [4];
		while((str = bfr.readLine()) != null)
		{
			String [] arrOfStr = str.split(",",5);
			if (arrOfStr[4].equals("yes"))
			{
				for (int i = 0; i < 4; i++)
					if(arrOfStr[i].equals(x[i]))
						classYesCount[i]++;
			}
			else
			{
				for (int i = 0; i < 4; i++)
					if(arrOfStr[i].equals(x[i]))
						classNoCount[i]++;
			}	
		}
		double classYesProb = fs.bayesProb(classYesCount,yProb,bcYesCount);
		System.out.println("P(C1/X):\t"+classYesProb);
		
		double classNoProb = fs.bayesProb(classNoCount,1-yProb,bcNoCount);
		System.out.println("P(C2/X):\t"+classNoProb);
		
		if (classNoProb >= classYesProb)
		{
			System.out.println("Computer will not be bought!");
		}
		else
		{
			System.out.println("Computer will be bought!");
		}
	}
	public double bayesProb(int [] count,double classProb,int deno){
		int n = 1;
		int d = 1;
		for (int i=0;i<4;i++)
		{
			n = n * count[i];
			d = d * deno;
		}
		return (double)n*classProb/d;
	}
	public double prob2(int x,int y){
		return (double)x/(x+y);
	}
}
