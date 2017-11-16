package snu.bike.wholeExomSeq.varscan;

import snu.bike.wholeExomSeq.Executor;
import snu.bike.wholeExomSeq.Utils;

public class VarScanExecutor extends Executor{
	public VarScanExecutor(String executionPath, String mpfile) {
		this.exePath = executionPath;
		this.inputFile = mpfile;
		this.script_path =  "./"+ this.getClass().getSimpleName() + ".sh";
		this.outputFile = Utils.extractSampleName(inputFile, ".snp.vcf");
		
		makeShellFile(this.script_path);
	}
	
	@Override
	protected String makeCommand() {
		StringBuilder builder = new StringBuilder();
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
		
		builder.append(outputFile);
		return builder.toString();

	}
}
