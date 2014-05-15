/**
 * (c) Copyright 2009 TeleNav.
 *  All Rights Reserved.
 */
package com.telenav.cserver.framework.trump;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.telenav.cserver.framework.trump.PathCollection.Path;
import com.telenav.cserver.framework.util.CSStringUtil;

/**
 * A customized ant task, in order to help making zip,every device folder will be zipped as a zip file.
 * 
 * @author kwwang
 * @date 2010-1-21
 */
public class ZipTask
{
    private static final int BUFFER_SIZE = 8 * 1024;

    private static final long EMPTY_CRC = new CRC32().getValue();

    public static final String FILE_SEPARATOR = "/";

    public static final String ZIP_SEPARATOR = "^";

    private File baseDir;

    private File toDir;

    public int level;

    private List<String> addedDirs = new ArrayList<String>();

    /**
     * if this params is empty all device will be zipped. or only pack devices in params
     */
    private String params;

    public String getParams()
    {
        return params;
    }

    public void setParams(String params)
    {
        this.params = params;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public File getBaseDir()
    {
        return baseDir;
    }

    public void setBaseDir(File baseDir)
    {
        this.baseDir = baseDir;
    }

    public File getToDir()
    {
        return toDir;
    }

    public void setToDir(File toDir)
    {
        this.toDir = toDir;
    }

    public void execute()
    {
        System.out.println("Start cszip");
        long start = System.currentTimeMillis();
        findSatisfiedDirs();
        System.out.println("End cszip, " + (System.currentTimeMillis() - start) + " ms in total");
    }

    private void findSatisfiedDirs()
    {
        File baseFile = baseDir;
        if (!baseFile.isDirectory())
        {
            System.out.println("baseDir should be directory.");
            return;
        }

        System.out.println("Start collecting path...");
        long start = System.currentTimeMillis();
        PathCollection pachCol = new PathCollection();
        collectRelativePath(baseFile, "", pachCol);
        System.out.println("End collecting path..." + (System.currentTimeMillis() - start) + " ms in total");

        for (Path f : pachCol.getPathList())
        {
            zip(f, baseFile);
        }
    }

    protected void zip(Path path, File baseFile)
    {
        String zipName = deviceZipName(path.getPath());
        System.out.println("CSZip " + zipName);
        try
        {
            File zipFile = new File(toDir, zipName);
            if (!zipFile.exists())
                zipFile.createNewFile();
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));

            // first add the folder structure
            String[] dirs = path.getPath().split("\\\\");
            String hold = "";
            for (int i = 0; i < dirs.length; i++)
            {
                if (i != 0)
                    hold = hold + File.separator + dirs[i];
                else
                    hold = dirs[i];
                zipFolder(zos, hold);
            }

            // write file
            for (String childPath : path.childrenPath)
            {
                File file = new File(baseFile, childPath);
                if (file.isDirectory())
                    zipFolder(zos, childPath);
                else
                    zipFile(zos, childPath, file);
            }

            zos.flush();
            zos.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void zipFile(ZipOutputStream zos, String fileName, File inFile) throws IOException
    {
        InputStream is = new FileInputStream(inFile);
        byte[] buffer = new byte[BUFFER_SIZE];
        ZipEntry ze = new ZipEntry(fileName);

        ze.setTime(System.currentTimeMillis());
        ze.setMethod(ZipEntry.DEFLATED);
        zos.putNextEntry(ze);

        int read = -1;
        // write from bis to zos
        while ((read = is.read(buffer, 0, buffer.length)) != -1)
        {
            zos.write(buffer, 0, read);
        }

        is.close();
    }

    private void zipFolder(ZipOutputStream zos, String folderName) throws IOException
    {
        if (addedDirs.contains(folderName))
            return;
        ZipEntry ze = new ZipEntry(folderName + FILE_SEPARATOR);
        ze.setTime(System.currentTimeMillis());
        ze.setMethod(ZipEntry.DEFLATED);
        ze.setSize(0);
        ze.setCrc(EMPTY_CRC);
        zos.putNextEntry(ze);
        addedDirs.add(folderName);
    }

    private String deviceZipName(String path)
    {
        return path.replaceAll(FILE_SEPARATOR, ZIP_SEPARATOR) + ".zip";
    }

    /**
     * collecting relative path from baseFile.
     * 
     * @author kwwang
     * @date 2010-1-21
     * @param baseFile
     * @param prefix
     * @param pachCol
     */
    protected void collectRelativePath(File baseFile, String prefix, PathCollection pachCol)
    {
        String[] files = baseFile.list();
        for (String fstr : files)
        {
            File file = new File(baseFile, fstr);

            // remove the svn folder/file
            if (file.isHidden())
                continue;

            String relativePath = CSStringUtil.isNotNil(prefix) ? prefix + FILE_SEPARATOR + fstr : fstr;
            if (relativePath.split(FILE_SEPARATOR).length == level + 1)
            {
                // check whether have filter empty will zip all device
                System.out.println(params + "   " + relativePath);
                if (CSStringUtil.isNotNil(params))
                {
                    if (!params.contains(relativePath))
                    {
                        continue;
                    }
                }
                pachCol.addPath(relativePath);
            }
            else if (relativePath.split(FILE_SEPARATOR).length > level + 1)
                pachCol.addChild(relativePath);
            if (file.isDirectory())
            {
                collectRelativePath(file, relativePath, pachCol);
            }
        }
    }
}
