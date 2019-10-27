package com.ftp.deploy.controller;

import com.alibaba.fastjson.JSONObject;
import com.ftp.deploy.utils.CommonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ZERO
 */
@Controller
public class HomeController {

    @RequestMapping("/login")
    public String login(Model model,String error){
        if (!StringUtils.isEmpty(error)){
            model.addAttribute("error",error);
        }
        return "login";
    }
    @RequestMapping("/")
    public String home(Model model){
        CommonUtils commonUtils = new CommonUtils();
        File file = new File(commonUtils.getJarPath(), CommonUtils.FILE_NAME_PROGRAM);
        if (file.exists()){
            LineNumberReader reader = null;
            try {
                List<JSONObject> objects = new ArrayList<>();
                reader  = new LineNumberReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null){
                    if (!"".equals(line)){
                        objects.add(JSONObject.parseObject(line));
                    }
                }
                model.addAttribute("data",objects);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "index";
    }

    /**
     * 程序发布页面
     * @return
     */
    @RequestMapping("/releaseProgram.html")
    public String releaseProgram(Model model){
        model.addAttribute("mark", 1);
        return "release";
    }

    /**
     * 程序更新页面
     * @return
     */
    @RequestMapping("/updateProgram.html")
    public String updateProgram(Model model,@RequestParam Map<String,String> params){
        model.addAttribute("data", params);
        model.addAttribute("mark", 2);
        return "release";
    }

}
