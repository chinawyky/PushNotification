package com.telenav.cserver.module;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;


public class UploadAction implements Action{
    
    private File image; 
    private String imageFileName; 
    private String imageContentType; 

    public String execute() throws Exception {
        String realpath = ServletActionContext.getServletContext().getRealPath("/images");
        //D:\apache-tomcat-6.0.18\webapps\struts2_upload\images
        System.out.println("realpath: "+realpath);
        if (image != null) {
            File savefile = new File(new File(realpath), imageFileName);
            if (!savefile.getParentFile().exists())
                savefile.getParentFile().mkdirs();
            FileUtils.copyFile(image, savefile);
            ActionContext.getContext().put("message", "successful");
        }
        return "success";
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    
}
