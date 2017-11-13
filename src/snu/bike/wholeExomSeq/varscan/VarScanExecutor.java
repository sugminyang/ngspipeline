package snu.bike.wholeExomSeq.varscan;

import snu.bike.wholeExomSeq.Executor;
import snu.bike.wholeExomSeq.Utils;

public class VarScanExecutor extends Executor{
	public VarScanExecutor(String executionPath, String mpfile) {
		this.exePath = executionPath;
		this.inputFile = mpfile;
		this.script_path =  "./"+ this.getClass().getSimpleName() + ".sh";
		
		makeShellFile(this.script_path);
	}
	
	@Override
	protected String makeCommand() {
		StringBuilder builder = new StringBuilder();
		builder.append("java -jar");
		builder.append(WHITESPACE);

		builder.append(Utils.checkLibraryName(exePath,"VarScan.jar"));
		builder.append(WHITESPACE);

		builder.append("mpileup2snp");
		builder.append(WHITESPACE);

		builder.append(inputFile);

		return builder.toString();

	}
}