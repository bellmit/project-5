package com.kindminds.drs.adapter.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kindminds.drs.api.adapter.FileAdapter;

@Service
public class FileAdapterImpl implements FileAdapter {

	private String getRootFileDir(){ return System.getProperty("catalina.home"); }
	
	private static Logger logger = Logger.getLogger(FileAdapterImpl.class);

	@Override
	public void save(String path, String fileName, byte[] bytes) {
		File fullPath = new File(this.getRootFileDir()+File.separator+path);
		if(!fullPath.exists()) fullPath.mkdirs();
		File file = new File(fullPath.getAbsolutePath()+File.separator+fileName);
		try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))){
			stream.write(bytes);
		} catch (IOException e) {
			logger.info("IOException within save method:", e);
		} 
	}

	@Override
	public List<String> getFileList(String path) {
		List<String> fileList = new ArrayList<String>();
		File dir = new File(this.getRootFileDir()+File.separator+path);
		File[] fList = dir.listFiles();
		for (File file : fList){
			fileList.add(file.getName());
		}
		Collections.sort(fileList);
		return fileList;
	}

	@Override
	public File get(String path, String fileName) {
		return new File(this.getRootFileDir()+File.separator+path+File.separator+fileName);
	}

	@Override
	public void delete(String path, String fileName) {
		File file = new File(this.getRootFileDir()+File.separator+path+File.separator+fileName);		
		file.delete();
	}

	@Override
	public void deleteAll(String path) {
		Assert.notNull(path);
		File dir = new File(this.getRootFileDir()+File.separator+path);
		for(File file:dir.listFiles()){
			file.delete();
		}
	}

}
