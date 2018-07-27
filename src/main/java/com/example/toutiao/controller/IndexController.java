package com.example.toutiao.controller;

import com.example.toutiao.aspect.LogAspect;
import com.example.toutiao.model.User;
import com.example.toutiao.service.TouTiaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.*;
import java.util.*;

/**
 * @author tell
 */
@Controller
public class IndexController {
    private  static  final Logger LOG=LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private TouTiaoService touTiaoService;


    @RequestMapping("/")
    @ResponseBody
    public  String index(HttpSession session){
        touTiaoService.say();
        LOG.info("Visit Index");
        return  "Hello NoeCoder"+session.getAttribute("msg")+"<br>"+touTiaoService.say();
    }
    @RequestMapping(value = "/profile/{groupId}/{userId}")
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value = "type",defaultValue = "1")int type,
                          @RequestParam(value = "key",defaultValue = "newcode")String key){
        return String.format("{%s},{%d},{%d},{%s}",groupId,userId,type,key);
    }

    @RequestMapping(value = {"/vm"})
    public String news(Model model){
        model.addAttribute("value1","vv1");
        List<String> colors =Arrays.asList(new String[]{"RED","GERNM","RED","BLUE"});
        Map<String,String> map=new HashMap<String, String>();
        for (int i=0 ;i<4;++i){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("colors",colors);
        model.addAttribute("map",map);
        model.addAttribute("user",new User("Tom"));
        return "news";
    }

    @RequestMapping("/request")
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session){
       StringBuffer sb=new StringBuffer();
       Enumeration<String> headerNames=request.getHeaderNames();
       while (headerNames.hasMoreElements()){
           String name=headerNames.nextElement();
           sb.append(name+":"+request.getHeader(name)+"<br>");
       }
       for(Cookie cookie:request.getCookies()){
           sb.append("Cookie");
           sb.append(cookie.getName());
           sb.append(":");
           sb.append(cookie.getValue());
           sb.append("<br>");
       }
       sb.append("getMethod"+request.getMethod()+"<br>");
        sb.append("getPathInfo"+request.getPathInfo()+"<br>");
        sb.append("getQueryString"+request.getQueryString()+"<br>");
       return sb.toString() ;
    }
    @RequestMapping("/response")
    @ResponseBody
    public String response(@CookieValue(value = "nowcoder",defaultValue = "a") String nowcoder,
                           @RequestParam(value = "key",defaultValue = "key") String key,
                           @RequestParam(value = "value",defaultValue = "value")String vaule,
                           HttpServletResponse response){
        response.addCookie(new Cookie(key,vaule));
        response.addHeader(key,vaule);
        return  "NowCoder From Cookie:" +nowcoder;
    }

    @RequestMapping("/redirect/{code}")
    public String redirect(@PathVariable("code") int code,HttpSession session){
        RedirectView red =new RedirectView("/",true);
      /*  if(code==301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return  red;*/
      session.setAttribute("msg","Jump from redirect");

        return "redirect:/";//默认302临时跳转重定向
    }

    @RequestMapping("/admin")
    @ResponseBody
    public String admin(@RequestParam(value = "key",required = false)String key) throws IllegalAccessException {
        if("admin".equals(key)){
            return "Hello admin";
        }
        throw new  IllegalArgumentException("key 错误");
    }

    @ExceptionHandler
    @ResponseBody
    public  String error(Exception e){
        return  "error"+e.getMessage();
    }
}