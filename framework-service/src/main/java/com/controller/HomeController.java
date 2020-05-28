package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Classname HomeController
 * @Description TODO
 * @Date 2020/3/2 16:38
 * @Created by 125937
 */
@Controller
public class HomeController {
    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView mv=new ModelAndView("home/index");
        mv.addObject("dd","sdfd");
        return mv;
    }

    @RequestMapping("/iframe-page")
    public ModelAndView iframe(){
        ModelAndView mv=new ModelAndView("home/iframe-page");
        mv.addObject("dd","sdfd");
        return mv;
    }
}
