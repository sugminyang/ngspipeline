package test;

import java.io.IOException;

import snu.bike.wholeExomSeq.BWAExecutor;
import snu.bike.wholeExomSeq.CliParser;
import snu.bike.wholeExomSeq.SamtoolsExecutor;
import snu.bike.wholeExomSeq.varscan.SamtoolsMPileUP;
import snu.bike.wholeExomSeq.varscan.VarScanExecutor;

public class testVarScan {

	public static void main(String[] args) {
		// TODO input option path setting !!!!!! big problem..!!!!
//		String cli_args = "-e /dev2/syang/wgs_wes/bin/ -p 8 -r ./util/ICGC_Pipeline/hg19.fasta -i /dev2/syang/wgs_wes/SRR925743_1.fastq -indel /dev2/syang/wgs_wes/util/Mills_and_1000G_gold_standard.indels.hg19.vcf -snp /dev2/syang/wgs_wes/util/dbsnp_138.hg19.vcf -c /dev2/syang/wgs_wes/util/b37_cosmic_v54_120711.vcf -o /dev2/syang/wgs_wes/normal";
		
		try {
			CliParser cli = new CliParser(args);
			
			System.out.println("\t[Normal]");
			/**
			 * normal
			 */
			//1. alignment
			BWAExecutor bwa_normal = new BWAExecutor(cli.getExecutionPath(), cli.getProcess(),cli.getReferenceSequence(), cli.getInputNormal());
			bwa_normal.excute();
			
			//2. convert sam to bam
			String outputFile_bwa_normal = bwa_normal.getOutputFile();
			SamtoolsExecutor samtools_normal = new SamtoolsExecutor(cli.getExecutionPath(),outputFile_bwa_normal);
			samtools_normal.excute();
			
			System.out.println("\n\t[Tumor]");
//			/**
//			 * tumor
//			 * */
			BWAExecutor bwa_tumor = new BWAExecutor(cli.getExecutionPath(), cli.getProcess(),cli.getReferenceSequence(), cli.getInputTumor());
			bwa_tumor.excute();			

			String outputFile_bwa_tuomr = bwa_tumor.getOutputFile();
			SamtoolsExecutor samtools_tumor = new SamtoolsExecutor(cli.getExecutionPath(),outputFile_bwa_tuomr);
			samtools_tumor.excute();
			
			SamtoolsMPileUP samtools_mpileip = new SamtoolsMPileUP(cli.getExecutionPath(),cli.getReferenceSequence(), samtools_normal.getOutputFile(), samtools_tumor.getOutputFile());
			samtools_mpileip.excute();
			
			String outputFile_samtoolsMp = samtools_mpileip.getOutputFile();
			VarScanExecutor varscan = new VarScanExecutor(cli.getExecutionPath(), outputFile_samtoolsMp);
			varscan.excute();
			
			System.out.println("\n#########Finish all steps#########");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
