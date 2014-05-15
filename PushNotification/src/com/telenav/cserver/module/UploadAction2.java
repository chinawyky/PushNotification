package com.telenav.cserver.module;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.File;

import com.opensymphony.xwork2.Action;
import org.apache.struts2.ServletActionContext;

public class UploadAction2 implements Action {

	 
    private File image;
    
    private String imageContentType;
   
    private String imageFileName;
    
    private String savePath;

    @Override
    public String execute() {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
        
            System.out.println(getSavePath());
            fos = new FileOutputStream(getSavePath() + "\\" + getImageFileName());
          
            fis = new FileInputStream(getImage());
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            System.out.println("fail");
            e.printStackTrace();
        } finally {
            close(fos, fis);
        }
        return SUCCESS;
    }

  
    public String getSavePath() throws Exception{
        return ServletActionContext.getServletContext().getRealPath(savePath); 
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    private void close(FileOutputStream fos, FileInputStream fis) {
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                System.out.println("FileInputStream close failure");
                e.printStackTrace();
            }
        }
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                System.out.println("FileOutputStream close failure");
                e.printStackTrace();
            }
        }
    }

}
