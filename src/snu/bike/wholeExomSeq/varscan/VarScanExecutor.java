package snu.bike.wholeExomSeq.varscan;

import snu.bike.wholeExomSeq.Executor;
import snu.bike.wholeExomSeq.Utils;
	
public class VarScanExecutor extends Executor{
	public String output_indel;
	public String output_snp;
	
	public VarScanExecutor(String executionPath, String mpfile) {
		this.exePath = executionPath;
		this.inputFile = mpfile;
		this.script_path =  "./"+ this.getClass().getSimpleName() + ".sh";
		output_snp = Utils.extractSampleName(inputFile, ".snp.vcf");
		output_indel = Utils.extractSampleName(inputFile, ".indel.vcf");
		
		this.outputFile = output_snp + ", " + output_indel;
		
		makeShellFile(this.script_path);
	}
	
	@Override
	protected String makeCommand() {
		StringBuilder builder = new StringBuilder();
		makeSNPCommand(builder);
		builder.append("\n");
		makeIndelCommand(builder);
		
		return builder.toString();

	}

	private void makeIndelCommand(StringBuilder builder) {
		builder.append("java -jar");
		builder.append(WHITESPACE);

		builder.append(exePath + Utils.checkLibraryName(exePath,"VarScan.jar"));
		builder.append(WHITESPACE);

		builder.append("mpileup2indel");
		builder.append(WHITESPACE);

		builder.append(inputFile);
		builder.append(WHITESPACE);
		
		builder.append("--output-vcf 1 >");
		builder.append(WHITESPACE);
		
		builder.append(output_indel);		
	}

	private void makeSNPCommand(StringBuilder builder) {
		builder.append("java -jar");
		builder.append(WHITESPACE);

		builder.append(exePath + Utils.checkLibraryName(exePath,"VarScan.jar"));
		builder.append(WHITESPACE);

		builder.append("mpileup2snp");
		builder.append(WHITESPACE);

		builder.append(inputFile);
		builder.append(WHITESPACE);
		
		builder.append("--output-vcf 1 >");
		builder.append(WHITESPACE);
		
		builder.append(output_snp);
	}
	
}
