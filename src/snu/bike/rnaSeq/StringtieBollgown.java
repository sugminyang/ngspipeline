package snu.bike.rnaSeq;

import snu.bike.ngspipeline.Utils;

public class StringtieBollgown extends Stringtie{
	private String mergeFile;
	private String bamFile;
	private String gtfFile;

	public StringtieBollgown(String excutionPath, int process, String mergeFile, String bamFile, String gtfFile) {
		super();
		
		this.exePath = excutionPath;
		this.process = process;
		this.mergeFile = mergeFile;
		this.bamFile = bamFile;
		this.gtfFile = gtfFile;
		
		this.script_path =  "./"+ this.getClass().getSimpleName() + ".sh";
		
		String outputName = "stringtie_merged";
		this.outputFile = Utils.extractSampleName(outputName,OUTPUT_STRINGTIE);
		
		makeShellFile(this.script_path);
	}

	@Override
	protected String makeCommand() {
		/**
		 * stringtie –e 
		 * –B 
		 * -p 8 
		 * -G stringtie_merged.gtf 
		 * -o ballgown/ERR188044/ERR188044_chrX.gtf 
		 * ERR188044_chrX.bam
		 * */
				
		StringBuilder builder = new StringBuilder();
		builder.append(exePath + "stringtie");
		builder.append(WHITESPACE);
		builder.append("-e -B");
		builder.append(WHITESPACE);
		
		builder.append("-p");
		builder.append(WHITESPACE);
		builder.append(process);
		builder.append(WHITESPACE);
		
		builder.append("-G");
		builder.append(WHITESPACE);
		builder.append(mergeFile);
		builder.append(WHITESPACE);

		builder.append("-o");
		builder.append(WHITESPACE);
		
		String fileName = Utils.getSampleName(inputFile);
		if(fileName.contains("_"))	{
			int idx = fileName.indexOf("_");
			fileName = fileName.substring(0,idx);
		}
		builder.append("/ballgown/"+fileName+"/"+gtfFile);
		
		builder.append(WHITESPACE);		

		builder.append(bamFile);
		
		return builder.toString();		
	}
}
