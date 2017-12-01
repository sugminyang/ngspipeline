package snu.bike.ngspipeline;

public class SamtoolsExecutor extends Executor{
	private final String OUTPUT_SAMTOOLS = ".filtered.bam";
	private String mode;
	private int process;
	
	public SamtoolsExecutor()	{}
	
	public SamtoolsExecutor(String exePath, int process, String outputFile_bwa, String mode) {
		super();
		
		this.exePath = exePath;
		this.process = process;
		this.inputFile = outputFile_bwa;
		this.mode = mode;	//ngs mode rna or exom
		this.script_path =  "./"+ this.getClass().getSimpleName() + ".sh";
		this.outputFile = Utils.extractSampleName(inputFile,OUTPUT_SAMTOOLS);
		
		makeShellFile(this.script_path);
	}
	
	@Override
	protected String makeCommand() {
		//samtools view -bSh -F 0x800 test.sam -o test.filtered.bam

		StringBuilder builder = new StringBuilder();
		builder.append(exePath);	//TODO: After samtools lib install under /bin folder. then erase this comment line.
		builder.append("samtools");
		builder.append(WHITESPACE);
		
		if(mode.equalsIgnoreCase("rna"))	{
			builder.append("sort -@");
			builder.append(WHITESPACE);
			builder.append(process);
		}
		else if(mode.equalsIgnoreCase("exom"))	{
			builder.append("view -bSh -F 0x800");
		}
		builder.append(WHITESPACE);
		
		builder.append(inputFile);
		builder.append(WHITESPACE);
		
		builder.append("-o");
		builder.append(WHITESPACE);
		
		builder.append(outputFile);
		
		builder.append("\n");
		builder.append("rm");
		builder.append(WHITESPACE);
		builder.append(inputFile);	//remove intermediate creation file.
		
		return builder.toString();
	}
}


