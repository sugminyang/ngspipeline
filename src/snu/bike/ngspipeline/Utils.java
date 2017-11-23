package snu.bike.ngspipeline;

import java.io.File;
import java.io.IOException;

public class Utils {
	
	/**
	 * @param sDirectoryPath : a directory contains serveral input sets.
	 * @param setID : a Id of one set(normal/tumor)
	 * get files pair (normal / tumor) the same sample ID.
	 * */
	public static String[] recursiveFileRead(String sDirectoryPath,String setID, String mode) throws IOException
    {
         File dirFile = new File(sDirectoryPath);
         File[] fileList = dirFile.listFiles();
         String[] inputSet = new String[2];
         if(fileList == null)
              return null;
         
         for(File tempFile : fileList)     {
              if(tempFile.isFile())     {

                   String tempFileName = tempFile.getName();
                   
                   int commaIndex = tempFileName.indexOf(".");
                   String fileType = tempFileName.substring(commaIndex+1);

                   if(mode.equalsIgnoreCase("rna"))	{
	                   if(tempFileName.contains(setID) && fileType.contains("fastq"))		{
	                       if(tempFileName.contains("_1"))	{
	                    	   	inputSet[0] = sDirectoryPath+tempFileName;
	//                    	   	System.out.println(sDirectoryPath + tempFileName);
	                       }
	                       else if(tempFileName.contains("_2"))	{
	                    	   	inputSet[1] = sDirectoryPath+tempFileName;
	//                    	   	System.out.println(sDirectoryPath + tempFileName);
	                       }
	                   }
                   
                   }
                   else if(mode.equalsIgnoreCase("exome"))	{
	                   if(tempFileName.contains(setID) && fileType.equalsIgnoreCase("fastq"))		{
	                       if(tempFileName.contains("tumor"))	{
	                    	   	inputSet[1] = sDirectoryPath+tempFileName;
	//                    	   	System.out.println(sDirectoryPath + tempFileName);
	                       }
	                       else if(tempFileName.contains("normal"))	{
	                    	   	inputSet[0] = sDirectoryPath+tempFileName;
	//                    	   	System.out.println(sDirectoryPath + tempFileName);
	                       }
	                   }
                   }
              }
         }
//         System.out.println();
         
         return inputSet;
    }
	
	/**
	 * split setIDs each. 
	 * */
	public static String[] splitInputSets(String setList)	{
		String[] sets = setList.split(",");
		return sets;
	}
	
	/**
	 * use to link file names
	 * */
	public static String extractSampleName(String name,String postfix_output)	{
		int stIdx = name.lastIndexOf('/');
		if(stIdx == -1)	{
			stIdx = 0;	// name variable doesn't have path information. like /dev2/abc/SRR1234_1.fastq.
		}
		else		{
			stIdx += 1;
		}
		
		int edIdx = name.indexOf('.');	//get before first .(dot) index.
		if(edIdx == -1)	{	//dot doesn't exist.
			edIdx = name.length();
		}
		
		String sampleName = name.substring(stIdx, edIdx);
		
		return sampleName + postfix_output;
	}
	
	public static String extractSampleName(String name)	{
		return extractSampleName(name,"");
	}
	
	/**
	 * return a correct name of library. 
	 * e.g. 
	 * input exePath & library name.
	 * check name and version of library. 
	 * */
	public static String checkLibraryName(String exePath, String libName)	{
        File dirFile = new File(exePath);
        File[] fileList = dirFile.listFiles();
//        System.out.println(exePath);
        
        String libFormat = "";
        if(libName.contains("."))	{
        		int formatIdx = libName.lastIndexOf(".");
        		libFormat = libName.substring(formatIdx+1);
        		libName = libName.substring(0,formatIdx);
        		
//        		System.out.println(libFormat);
        }
        
		for(File tempFile : fileList)     {
            if(tempFile.isFile())     {
                 String tempFileName = tempFile.getName();
                 String fileType = "";
                 
                 int commaIndex = tempFileName.lastIndexOf(".");

                 if(commaIndex != -1)	{
                	 	fileType = tempFileName.substring(commaIndex+1);
                 }
                 
                 if(tempFileName.contains(libName) && fileType.equalsIgnoreCase(libFormat))		{
                	 	System.out.println("find: " + tempFileName);
                	 	return tempFileName;
                 }
            }
       }
		return libName;
	}
	
	public static String getSampleName(String path)	{
		int idx = path.lastIndexOf("/");
		int underIdx = path.lastIndexOf("_");
		
		return path.substring(idx+1,underIdx);
	}
}

