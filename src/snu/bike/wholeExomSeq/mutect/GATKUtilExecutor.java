package snu.bike.wholeExomSeq.mutect;

import snu.bike.wholeExomSeq.Executor;
import snu.bike.wholeExomSeq.Utils;

public class GATKUtilExecutor extends Executor{
	private final String OUTPUT_GATK = ".GR.bam";
	
	public GATKUtilExecutor(String excutionPath, String outputFile_samtools) {
		super();
		
		//TODO: error echk empty path
		this.exePath = excutionPath;
		
		//TODO: error check empty filename
		this.inputFile = outputFile_samtools;
		
		this.script_path =  "./"+ this.getClass().getSimpleName() + ".sh";
		
		//normally execute all of GATK library, will be made SRR925743_1.GR.bam like this.(sample)
		this.outputFile = Utils.extractSampleName(inputFile,OUTPUT_GATK);	
		
		makeShellFile(this.script_path);
	}
	
	@Override
	protected String makeCommand() {

		String defaultOptions = "java -Xmx2g -jar -Djava.io.tmpdir=./tmp";
		StringBuilder builder = new StringBuilder();
		
		//make CleanSam.jar execute script.
		//script scample(CleanSam)
		/* java -Xmx2g -jar -Djava.io.tmpdir=./tmp ./bin/CleanSam.jar 
		I=normal2.filtered.bam O=normal2.filtered.cleaned.bam  */
		String output_cleanSam = makeCleanSamCommand(builder, defaultOptions);
		builder.append("\n");
		
		//make AddOrReplaceReadGroups.jar execute script.
		/* java -Xmx2g -jar -Djava.io.tmpdir=./tmp  ./bin/AddOrReplaceReadGroups.jar   
		I=normal2.filtered.cleaned.bam O=normal2.filtered.group.bam 
		RGPL=illumina RGLB=LaneX RGPU=NONE RGSM=test VALIDATION_STRINGENCY=STRICT */
		//Change RGS value test -> inputfile's prefix
		String output_AddOrReplaceReadGroups = makeAddOrReplaceReadGroupsCommand(builder, defaultOptions,output_cleanSam);
		builder.append("\n");
		
		//make FixMateInformation.jar execute script.
		/* java -Xmx2g -jar -Djava.io.tmpdir=./tmp  ./bin/FixMateInformation.jar   
		I=normal2.filtered.group.bam O=normal2.sorted.bam SO=coordinate VALIDATION_STRINGENCY=STRICT */
		String output_FixMateInformation = makeFixMateInformationCommand(builder, defaultOptions,output_AddOrReplaceReadGroups);
		builder.append("\n");
		
		//make MarkDuplicates.jar execute script.
		/*java -Xmx2g -jar -Djava.io.tmpdir=./tmp  ./bin/MarkDuplicates.jar  
		I=normal2.sorted.bam O=normal2.GR.bam 
		METRICS_FILE=normal.sorted.bam.metrics REMOVE_DUPLICATES=true VALIDATION_STRINGENCY=STRICT*/
		String output_MarkDuplicates = makeMarkDuplicatesCommand(builder, defaultOptions,output_FixMateInformation);
		
		//System.out.println(output_MarkDuplicates);
		this.outputFile = output_MarkDuplicates;
		
		return builder.toString();
	}
	
	private String makeMarkDuplicatesCommand(StringBuilder builder, String defaultOptions,
			String input_FixMateInformation) {
		String output_MarkDuplicates = Utils.extractSampleName(input_FixMateInformation,".GR.bam");
		String defaultOption_MarkDuplicates = "METRICS_FILE=" + Utils.extractSampleName(input_FixMateInformation,".sorted.bam.metrics")  + " REMOVE_DUPLICATES=true VALIDATION_STRINGENCY=STRICT";
		
		builder.append(defaultOptions);
		builder.append(WHITESPACE);
		
		builder.append(exePath + "MarkDuplicates.jar");
		builder.append(WHITESPACE);
		
		builder.append("I=" + input_FixMateInformation);
		builder.append(WHITESPACE);
		
		builder.append("O=" + output_MarkDuplicates);
		builder.append(WHITESPACE);
		
		builder.append(defaultOption_MarkDuplicates);
		
		return output_MarkDuplicates;
	}

	private String makeFixMateInformationCommand(StringBuilder builder, String defaultOptions,
			String inputFile_AddOrReplaceReadGroups) {
		String output_FixMateInformationJar = Utils.extractSampleName(inputFile_AddOrReplaceReadGroups,".sorted.bam");
		String defaultOption_ddOrReplaceReadGroups = "SO=coordinate VALIDATION_STRINGENCY=STRICT";
		
		builder.append(defaultOptions);
		builder.append(WHITESPACE);
		
		builder.append(exePath + "FixMateInformation.jar");
		builder.append(WHITESPACE);
		
		builder.append("I=" + inputFile_AddOrReplaceReadGroups);
		builder.append(WHITESPACE);
		
		builder.append("O=" + output_FixMateInformationJar);
		builder.append(WHITESPACE);
		
		builder.append(defaultOption_ddOrReplaceReadGroups);
		
		return output_FixMateInformationJar;
	}

	private String makeAddOrReplaceReadGroupsCommand(StringBuilder builder, String defaultOptions, String inputFile_cleanSam) {
		String output_AddOrReplaceReadGroupsJar = Utils.extractSampleName(inputFile_cleanSam,".filtered.group.bam");
		String defaultOption_ddOrReplaceReadGroups = "RGPL=illumina RGLB=LaneX RGPU=NONE RGSM=" + Utils.extractSampleName(inputFile_cleanSam) + " VALIDATION_STRINGENCY=STRICT";
		
		builder.append(defaultOptions);
		builder.append(WHITESPACE);
		
		builder.append(exePath + "AddOrReplaceReadGroups.jar");
		builder.append(WHITESPACE);
		
		builder.append("I=" + inputFile_cleanSam);
		builder.append(WHITESPACE);
		
		builder.append("O=" + output_AddOrReplaceReadGroupsJar);
		builder.append(WHITESPACE);
		
		builder.append(defaultOption_ddOrReplaceReadGroups);
		
		return output_AddOrReplaceReadGroupsJar;
	}

	private String makeCleanSamCommand(StringBuilder builder, String defaultOptions)	{
		
		String output_cleanSamJar = Utils.extractSampleName(this.inputFile,".filtered.cleaned.bam");
		
		builder.append(defaultOptions);
		builder.append(WHITESPACE);
		
		builder.append(exePath + "CleanSam.jar");
		builder.append(WHITESPACE);
		
		builder.append("I=" + this.inputFile);
		builder.append(WHITESPACE);
		
		builder.append("O=" + output_cleanSamJar);
		builder.append(WHITESPACE);
		
		return output_cleanSamJar; 
	}
}
