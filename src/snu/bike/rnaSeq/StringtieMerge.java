package snu.bike.rnaSeq;

import snu.bike.ngspipeline.Utils;

public class StringtieMerge extends Stringtie{
	private String mergeFile;
	
	public StringtieMerge(String excutionPath, int process, String reference, String mergeFile) {
		super();
		
		this.exePath = excutionPath;
		this.process = process;
		this.geneAnnotation = reference;
		this.mergeFile = mergeFile;
		
		this.script_path =  "./"+ this.getClass().getSimpleName() + ".sh";
		
		String outputName = "stringtie_merged";
		this.outputFile = Utils.extractSampleName(outputName,OUTPUT_STRINGTIE);
		
		makeShellFile(this.script_path);
	}

	@Override
	protected String makeCommand() {
		/**
		 * ./stringtie --merge 
		 * –p 4 
		 * -G chrX_data/genes/chrX.gtf 
		 * -o stringtie_merged.gtf 
		 * chrX_data/mergelist.txt
		 * 
		 * */
				
		StringBuilder builder = new StringBuilder();
		builder.append(exePath + "stringtie");
		builder.append(WHITESPACE);
		builder.append("--merge");
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

		builder.append(mergeFile);
		
		return builder.toString();		
	}
}
