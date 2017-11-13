package snu.bike.wholeExomSeq.mutect;

import snu.bike.wholeExomSeq.Executor;

public class BamtoolsExecutor extends Executor{
	public BamtoolsExecutor(String excutionPath, String outputFile_gatkutil) {
		super();
		
		//TODO: error echk empty path
		this.exePath = excutionPath;
		
		this.script_path =  "./"+ this.getClass().getSimpleName() + ".sh";
		
		//TODO: error check empty filename
		this.inputFile = this.outputFile = outputFile_gatkutil;
		
		makeShellFile(this.script_path);
	}

	@Override
	protected String makeCommand() {
		//sample script:
		//./bin/bamtools-2.4.0 index -in ./normal2.GR.bam
		String defaultOptions = "index -in";
		StringBuilder builder = new StringBuilder();
		
		builder.append(exePath + "bamtools");
		builder.append(WHITESPACE);
		
		builder.append(defaultOptions);
		builder.append(WHITESPACE);
		
		builder.append(inputFile);
		
		return builder.toString();
	}

}
