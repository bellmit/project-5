package com.kindminds.drs.api.adapter;

import java.io.File;
import java.util.List;

public interface FileAdapter {
	public void save(String path,String fileName,byte[] bytes);
	public List<String> getFileList(String path);
	public File get(String path,String fileName);
	public void delete(String path,String fileName);
	public void deleteAll(String path);
}
