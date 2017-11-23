package test;

import java.util.Vector;

import snu.bike.ngspipeline.BWAExecutor;
import snu.bike.ngspipeline.CliParser;
import snu.bike.rnaSeq.Hisat2Executor;

public class testRNAseq {

	public static void main(String[] args) {
		try {
			CliParser cli = new CliParser(args);
			
			System.out.println("\tHISAT2...");

			//1. alignment

			
			for(int i =0; i<cli.getInputPair1().size(); i++)	{
				Vector<String> pair1 = cli.getInputPair1();
				Vector<String> pair2 = cli.getInputPair2();
				System.out.println("[pair input]: " + pair1.get(i) + ", " + pair2.get(i));
				
				Hisat2Executor hisat2 = new Hisat2Executor(cli.getExecutionPath(),cli.getProcess(),pair1.get(i),pair2.get(i),cli.getIndexes());
				hisat2.excute();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
