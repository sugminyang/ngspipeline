package snu.bike.rnaSeq;

import snu.bike.ngspipeline.Executor;
import snu.bike.ngspipeline.Utils;

public class Stringtie extends Executor{
	protected final String OUTPUT_STRINGTIE = ".gtf";
	protected int process;
	protected String geneAnnotation;
	
	public Stringtie() {}
	
	public Stringtie(String excutionPath, int process, String reference, String output_samtools)	{
		this.exePath = excutionPath;
		this.process = process;
		this.geneAnnotation = reference;
		this.inputFile = output_samtools;

		this.script_path =  "./"+ this.getClass().getSimpleName() + ".sh";
		
		this.outputFile = Utils.extractSampleName(inputFile,OUTPUT_STRINGTIE);
		
		makeShellFile(this.script_path);
	}
	
	@Override
	protected String makeCommand() {
		/**
		 * stringtie 
		 * -p 8 
		 * -G chrX_data/genes/chrX.gtf 
		 * -o ERR188044_chrX.gtf 
		 * –l ERR188044 
		 * ERR188044_chrX.bam
		 * */

		StringBuilder builder = new StringBuilder();
		builder.append(exePath + "stringtie");
		builder.append(WHITESPACE);
		
		builder.append("-p");
		builder.append(WHITESPACE);
		builder.append(process);
		builder.append(WHITESPACE);
		
		builder.append("-G");
		builder.append(WHITESPACE);
		builder.append(geneAnnotation);
		builder.append(WHITESPACE);

		builder.append("-o");
		builder.append(WHITESPACE);
		builder.append(outputFile);
		builder.append(WHITESPACE);		

		builder.append("-l");
		builder.append(WHITESPACE);
		String fileName = Utils.getSampleName(inputFile);
		if(fileName.contains("_"))	{
			int idx = fileName.indexOf("_");
			fileName = fileName.substring(0,idx);
		}
		
		builder.append(fileName);
		builder.append(WHITESPACE);

		builder.append(inputFile);
		
		return builder.toString();		
	}

}
