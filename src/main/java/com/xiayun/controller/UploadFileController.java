package com.xiayun.controller;

import com.xiayun.constants.FileDirConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadFileController {


    @GetMapping("/upload")
    public String index() {
        return "upload";
    }

    @PostMapping("/uploadFile")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            String name = file.getOriginalFilename();
            Path path = Paths.get(FileDirConstant.UPLOAD_DIR + name);
            File newFile = new File(path.toUri());
            String finalName;
            int i = 1;

            // 同名文件+1
            while (newFile.exists()) {
                if (name.contains(".")) {
                    int index = name.lastIndexOf(".");
                    finalName = name.substring(0, index) + "(" + i + ")" + name.substring(index, name.length());
                } else {
                    finalName = name + "(" + i + ")";
                }
                path = Paths.get(FileDirConstant.UPLOAD_DIR + finalName);
                newFile = new File(path.toUri());
                i++;
            }
            Files.write(path, bytes);
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }


    @GetMapping("uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }

}
