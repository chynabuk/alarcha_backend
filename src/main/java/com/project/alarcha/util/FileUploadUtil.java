package com.project.alarcha.util;

import com.project.alarcha.exception.ApiFailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUploadUtil {
    @Value("{fileUploadDir}")
    private static String FILE_DIR;

    public static void saveFileToDIR(String additionalDIR, MultipartFile multipartFile){
        File file = new File(FILE_DIR + additionalDIR + "//" + multipartFile.getOriginalFilename());
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        catch (IOException ioe){
            throw new ApiFailException("Can not upload");
        }

    }
}
