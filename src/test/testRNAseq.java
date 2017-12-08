package test;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

import snu.bike.ngspipeline.CliParser;
import snu.bike.ngspipeline.SamplePair;
import snu.bike.ngspipeline.SamtoolsExecutor;
import snu.bike.rnaSeq.Hisat2Executor;
import snu.bike.rnaSeq.Stringtie;
import snu.bike.rnaSeq.StringtieBallgown;
import snu.bike.rnaSeq.StringtieMerge;

public class testRNAseq {

	public static void main(String[] args) {
		try {
			CliParser cli = new CliParser(args);
			
			long startTime = System.nanoTime();
			
			Vector<SamplePair> input = cli.getInputPair();
			
			Vector<String> bamFiles = new Vector<>();
			Vector<String> gtfFiles = new Vector<>();
			
			for(int i =0; i<input.size(); i++)	{

				System.out.println("\t [hisat2].. : "+ (i+1));
				System.out.println("num of sample..: " + input.size() + ", " + input.size());
				
				//1. alignment				
				Hisat2Executor hisat2 = new Hisat2Executor(cli.getExecutionPath(),cli.getProcess(),input.get(i).getPairedend1(),input.get(i).getPairedend2(),cli.getIndexes());
				hisat2.excute();
		
				System.out.println("\t [samtools]..:"+ (i+1));
				//convert .sam to .bam
				String output_hisat2 = hisat2.getOutputFile();
				SamtoolsExecutor samtools = new SamtoolsExecutor(cli.getExecutionPath(),cli.getProcess(),output_hisat2,cli.getNgsMode());
				samtools.excute();
				
				
				System.out.println("\t [stringtie]..: "+ (i+1));
				String output_samtools = samtools.getOutputFile();
				Stringtie stringtie = new Stringtie(cli.getExecutionPath(),cli.getProcess(),cli.getReferenceSequence(),output_samtools);
				stringtie.excute();
				
				bamFiles.add(output_samtools);
				gtfFiles.add(stringtie.getOutputFile());
			}
			
			System.out.println("\t [stringtie merge]..: ");
			StringtieMerge sMerge = new StringtieMerge(cli.getExecutionPath(),cli.getProcess(),cli.getReferenceSequence(),cli.getMergeFile());
			sMerge.excute();
			
			String output_sMerge = sMerge.getOutputFile();
			
			for(int i =0; i<bamFiles.size(); i++)	{
				System.out.println("\t [ballgown]..:"+ (i+1));
				StringtieBallgown bollgown = new StringtieBallgown(cli.getExecutionPath(), cli.getProcess(), output_sMerge, bamFiles.get(i),gtfFiles.get(i));
				bollgown.excute();
			}
			
			long endTime = System.nanoTime();
			System.out.println("===========finish rna-seq pipeline===========" + "duration: " + TimeUnit.SECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS)+"s");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
