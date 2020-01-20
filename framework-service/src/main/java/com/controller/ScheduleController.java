package com.controller;

import com.bo.ResponseData;
import com.entity.ScheduleInfo;
import com.mongodb.mode.PageInfo;
import com.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Classname ScheduleController
 * @Description TODO
 * @Date 2020/1/9 16:01
 * @Created by 125937
 */
@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView mv=new ModelAndView("schedule/index");
        mv.addObject("dd","sdfd");
        return mv;
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id")String id){
        ModelAndView mv=new ModelAndView("schedule/edit");
        ScheduleInfo scheduleInfo=scheduleService.findById(id);
        mv.addObject("data",scheduleInfo);
        return mv;
    }

    @RequestMapping("/index2")
    public ModelAndView index2(){
        ModelAndView mv=new ModelAndView("schedule/index2");
        mv.addObject("dd","sdfd");
        return mv;
    }

    @RequestMapping(value = "/pageInfo",method = RequestMethod.GET )
    @ResponseBody
    public PageInfo<ScheduleInfo> pageInfo(String taskName,String taskDesc,int limit, int offset){
        //ResponseData<PageInfo<ScheduleInfo>> responseData=new ResponseData<>();
        ScheduleInfo scheduleInfo=new ScheduleInfo();
        scheduleInfo.setTaskName(taskName);
        scheduleInfo.setTaskDesc(taskDesc);
        //responseData.setData(scheduleService.searchPage(scheduleInfo,offset,limit)).ok();
        return scheduleService.searchPage(scheduleInfo,offset,limit);
        //return responseData;
    }
}
