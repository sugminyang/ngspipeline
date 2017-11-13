package snu.bike.wholeExomSeq.varscan;

import snu.bike.wholeExomSeq.SamtoolsExecutor;
import snu.bike.wholeExomSeq.Utils;

public class SamtoolsMPileUP extends SamtoolsExecutor{
	private final String OUTPUT_SAMTOOLS_MPILEUP = ".mpileup";
	private String refSeq;
	private String inputNormal;
	private String inputTumor;
	
	public SamtoolsMPileUP(String exePath,String reference_Seq, String inputNormal, String inputTumor) {
		this.exePath = exePath;
		this.refSeq = reference_Seq;
		this.inputNormal = inputNormal;
		this.inputTumor = inputTumor;
		this.script_path =  "./"+ this.getClass().getSimpleName() + ".sh";
		this.outputFile = Utils.extractSampleName(Utils.extractSampleName(inputNormal)
				+ "-"
				+ Utils.extractSampleName(inputTumor),OUTPUT_SAMTOOLS_MPILEUP);
		
		makeShellFile(this.script_path);
	}

	@Override
	protected String makeCommand() {
		//samtools view -bSh -F 0x800 test.sam -o test.filtered.bam

		StringBuilder builder = new StringBuilder();
//		builder.append(exePath);	//TODO: After samtools lib install under /bin folder. then erase this comment line.
		builder.append("samtools mpileup -B -q 1 -f");
		builder.append(WHITESPACE);
		
		builder.append(refSeq);
		builder.append(WHITESPACE);
		
		builder.append(inputNormal);
		builder.append(WHITESPACE);
		
		
		builder.append(inputTumor);
		builder.append(WHITESPACE);
		
		builder.append(">");
		builder.append(WHITESPACE);
		builder.append(outputFile);	//remove intermediate creation file.
		
		
		return builder.toString();
	}
	
}
