package test;

import java.io.IOException;

import snu.bike.ngspipeline.BWAExecutor;
import snu.bike.ngspipeline.CliParser;
import snu.bike.ngspipeline.SamtoolsExecutor;
import snu.bike.wholeExomSeq.mutect.BamtoolsExecutor;
import snu.bike.wholeExomSeq.mutect.GATKExecutor;
import snu.bike.wholeExomSeq.mutect.GATKRecaliExecutor;
import snu.bike.wholeExomSeq.mutect.GATKUtilExecutor;
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
			SamtoolsExecutor samtools_normal = new SamtoolsExecutor(cli.getExecutionPath(),cli.getProcess(),outputFile_bwa_normal,cli.getNgsMode());
			samtools_normal.excute();
			
			//3.preprocessing(cleaning,removeDuplication...)
			String outputFile_samtools_normal = samtools_normal.getOutputFile();
			GATKUtilExecutor gatk_util_normal = new GATKUtilExecutor(cli.getExecutionPath(), outputFile_samtools_normal);
			gatk_util_normal.excute();
			
			//4.bam indexing.
			String outputFile_gatkutil_normal = gatk_util_normal.getOutputFile();
			BamtoolsExecutor bamtools_normal = new BamtoolsExecutor(cli.getExecutionPath(),outputFile_gatkutil_normal);
			bamtools_normal.excute();
			
			System.out.println("\n\t[Tumor]");
//			/**
//			 * tumor
//			 * */
			BWAExecutor bwa_tumor = new BWAExecutor(cli.getExecutionPath(), cli.getProcess(),cli.getReferenceSequence(), cli.getInputTumor());
			bwa_tumor.excute();			

			String outputFile_bwa_tuomr = bwa_tumor.getOutputFile();
			SamtoolsExecutor samtools_tumor = new SamtoolsExecutor(cli.getExecutionPath(),cli.getProcess(),outputFile_bwa_tuomr,cli.getNgsMode());
			samtools_tumor.excute();
			
			String outputFile_samtools_tumor = samtools_tumor.getOutputFile();
			GATKUtilExecutor gatk_util_tumor = new GATKUtilExecutor(cli.getExecutionPath(), outputFile_samtools_tumor);
			gatk_util_tumor.excute();

			String outputFile_gatkutil_tumor = gatk_util_tumor.getOutputFile();
			BamtoolsExecutor bamtools_tumor = new BamtoolsExecutor(cli.getExecutionPath(),outputFile_gatkutil_tumor);
			bamtools_tumor.excute();
			
			System.out.println("\n\t[Indel realignment]");
			/**
			 * merge normal/tumor sample
			 */
			String outputFile_bamtools_normal = bamtools_normal.getOutputFile();
			String outputFile_bamtools_tumor = bamtools_tumor.getOutputFile();
			
			//GATK need two Input file (Normal/Tumor file)
			GATKExecutor gatk = new GATKExecutor(cli.getExecutionPath(),cli.getReferenceSequence(),cli.getIndelFile(),cli.getSnpFile(),outputFile_bamtools_normal,outputFile_bamtools_tumor);
			gatk.excute();
			
			String outputFile_gatk_normal = gatk.outputNormal();
			String outputFile_gatk_tumor = gatk.outputTumor();
			
			System.out.println("\n\t[recali normal]");
			/**
			 * normal
			 */
			GATKRecaliExecutor gatk_recali_normal = new GATKRecaliExecutor(cli.getExecutionPath(),cli.getReferenceSequence(),cli.getIndelFile(),cli.getSnpFile(), outputFile_gatk_normal);
			gatk_recali_normal.excute();

			System.out.println("\n\t[recali tumor]");
			/**
			 * tumor
			 */
			GATKRecaliExecutor gatk_recali_tumor = new GATKRecaliExecutor(cli.getExecutionPath(),cli.getReferenceSequence(),cli.getIndelFile(),cli.getSnpFile(), outputFile_gatk_tumor);
			gatk_recali_tumor.excute();
			
			System.out.println("\n\t[samtools mplieup]");
			
			String outputFile_gatk_recali_normal = gatk_recali_normal.getOutputFile();
			String outputFile_gatk_recali_tumor = gatk_recali_tumor.getOutputFile();
			SamtoolsMPileUP samtools_mpileip = new SamtoolsMPileUP(cli.getExecutionPath(),cli.getReferenceSequence(), outputFile_gatk_recali_normal, outputFile_gatk_recali_tumor);
			samtools_mpileip.excute();
			
			System.out.println("\n\t[VarScan]");			
			String outputFile_samtoolsMp = samtools_mpileip.getOutputFile();
			VarScanExecutor varscan = new VarScanExecutor(cli.getExecutionPath(), outputFile_samtoolsMp);
			varscan.excute();
			
			System.out.println("\n#########Finish all steps#########");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
