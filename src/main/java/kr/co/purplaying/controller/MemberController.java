                               package kr.co.purplaying.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.purplaying.dao.LeaveDao;
import kr.co.purplaying.dao.UserDao;
import kr.co.purplaying.domain.MemberDto;
import kr.co.purplaying.domain.UserDto;
import kr.co.purplaying.service.SettingService;

@Controller
@RequestMapping("/user")
public class MemberController {

    @Autowired
    UserDao userDao;
    
    @Autowired
    SettingService settingService;

    @Autowired(required=true)
    LeaveDao leaveDao;
    
    @Autowired
    private JavaMailSender mailSender;
  
    //GetMapping////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("iamport")
    public String iamport() {
      return "iamport";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "signIn";
    }

    @GetMapping("mypage")
    public String MyPage() {
      return "mypage";
    }
    
    @GetMapping("leave")
    public String Leave() {
      return "leave";
    }
    @GetMapping("setting")
    public String Setting() {
      return "setting";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    //@RequestMapping(value="signuppost", method = RequestMethod.POST)
    @GetMapping("signup")
    public String SignUppost() {
      return "signup";
    }
    
    @GetMapping("findaccount")
    public String Findaccount() {
      return "findAccount";
    }
    
    //PostMapping////////////////////////////////////////////////////////////////////////////////////
    
    @PostMapping("/sendMail")
    @ResponseBody
    public void sendMailTest(@RequestBody UserDto userDto) throws Exception{ 
        System.out.println(userDto);
        Random random = new Random();
        int checkNum = random.nextInt(888888) + 111111;
        userDto.setUser_pwd(String.valueOf(checkNum));
        System.out.println(userDto);
        if(userDao.updateUserPwd(userDto)!=1)
        {
          System.out.println("???????????? ????????????");
          return ;
        }
        
        String subject = "???????????? ??????????????????";
        String content = "?????? ???????????? : " + checkNum ;
        String from = "purplayingcorp@gmail.com";
        String to = userDto.getUser_id();
        
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail,true,"UTF-8");
            // true??? ???????????? ???????????? ?????????????????? ??????
            
            /*
             * ????????? ????????? ???????????? ???????????? ????????? ????????? ?????? ?????? 
             * MimeMessageHelper mailHelper = new MimeMessageHelper(mail,"UTF-8");
             */
            
            mailHelper.setFrom(from);
            // ?????? ????????? ????????? ?????? ????????? smtp ????????? ?????? ?????? ?????? ????????? ????????????(setFrom())????????? ??????
            // ??????????????? ??????????????? ?????????????????? ?????? ?????? ?????? ?????? ??????????????? ????????? ????????? ??????????????? ?????????.
            //mailHelper.setFrom("???????????? ?????? <???????????? ?????????@???????????????>");
            mailHelper.setTo(to);
            mailHelper.setSubject(subject);
            mailHelper.setText("<h2>"+content+ "</h2>", true);
            // true??? html??? ?????????????????? ???????????????.
            
            /*
             * ????????? ???????????? ?????????????????? ????????? ????????? ??????????????? ?????????. mailHelper.setText(content);
             */
            
            mailSender.send(mail);
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @PostMapping("/findaccount")
    @ResponseBody
    public String findAccount(@RequestBody UserDto userDto){
      System.out.println("phone : "+userDto.getUser_phone()+"name:"+userDto.getUser_name());
      try {
        int count =userDao.findUserData(userDto);
        System.out.println(count);
        if(count!=0) {
          return userDao.findUserId(userDto);
        }
        else {
          return null;
        }
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
      
    }
    
    @PostMapping("/login")
    public String login(String user_id, String user_pwd, boolean rememberId,String toURL
                        ,HttpServletRequest request, HttpServletResponse response) throws Exception {
                
        //1. id??? pw??? ??????
        if(!loginCheck(user_id, user_pwd)) {
            //2-1. ???????????? ?????????, loginForm?????? ??????     

            String msg = URLEncoder.encode("Id??? Password??? ???????????? ????????????.", "utf-8");

            return "redirect:/user/login?msg="+msg;
        }
        
        if(rememberId == true) {
            //2-2-1. ????????? ??????
            //2-2-2. ?????? ????????? ??????
            makeCookie(response, user_id);
        }
        else {
            //2-3-1. ????????? ??????
            //2-3-2. ?????? ????????? ??????
            deleteCookie(response, user_id);
        }
        
        UserDto userDto = userDao.selectUser(user_id);
        
        System.out.println("????????? : "+userDto);
        
        
        //3. ?????? ?????? ????????????.
        HttpSession session = request.getSession(true);
        //?????? ????????? id??? ??????
        
//        ArrayList<String> list = null;
//        list.add(user_id);
//        list.add(String.valueOf(userDto.getUser_role()));
        
        session.setAttribute("user_id", user_id);
        session.setAttribute("user_role", userDto.getUser_role());
        session.setAttribute("UserDto", userDto);
        System.out.println(userDto);
               
        System.out.println("user_role :"+session.getAttribute("user_role"));
        System.out.println("user_id :"+session.getAttribute("user_id"));
        
        //4. toUrl??? ??????????????? toUrl??? ??????
        toURL = toURL==null || toURL.equals("") ? "/" : toURL;
                
        //???????????? ????????? ??? ?????? (?????????)?????? ??????      
        return "redirect:"+toURL;
        

    }
        
    @ResponseBody
    @PostMapping("/signup")
    public void signup2(@RequestBody MemberDto memberDto, Model m ,HttpServletRequest request) throws Exception {
      System.out.println(memberDto);
      System.out.println("user_id"+memberDto.getUser_id());
      if(userDao.signUpUser(memberDto.getUser_id(),memberDto.getUser_pwd(),memberDto.getUser_name(),memberDto.getUser_nickname(),memberDto.getUser_phone())!=1) {
        System.out.println("??????");
      }
      UserDto user = userDao.searchUser_no(memberDto.getUser_id());
      System.out.println(user.getUser_no());
      
      if(userDao.userCheck(user.getUser_no(),memberDto.isAgree_age(),
                           memberDto.isAgree_terms(),memberDto.isAgree_inform(),
                           memberDto.isAgree_inform_third(),memberDto.isAgree_marketing())!=1) {
        System.out.println("??????");
      }
      System.out.println("marketing"+memberDto.isAgree_marketing());
      if(settingService.insertcheckbox(user.getUser_no(),memberDto.isAgree_marketing())!=1) {
        System.out.println("????????? insert ??????");
      }
      
   }
    
    @PostMapping("/leave")
    public String leave(int leave_category , String leave_reason, String user_pwd, HttpServletRequest request) throws Exception {
      
      HttpSession session = request.getSession();
      UserDto user = userDao.selectUser((String)session.getAttribute("user_id"));
      System.out.println(user);
      
      System.out.println(user.getUser_no());
      System.out.println(leave_category);
      System.out.println(leave_reason);
      
      if(leaveDao.insertLeaveReason(user.getUser_no(), leave_category, leave_reason)!=1) {
        System.out.println("??????");
      }
      if(user.getUser_pwd().equals(user_pwd)) {
        if(userDao.updateUserActivate(user.getUser_no())!=1) {
          System.out.println("????????????");
        }
      }
      session.invalidate();
      return "redirect:/";
    }
    
    //????????? ????????????///////////////////////////////////////////////////////////////////////
    @ResponseBody       
    @PostMapping("/chkuserid")
    public UserDto chkuserid(@RequestBody UserDto userDto) {
      UserDto user;
      try {
        user = userDao.selectUser(userDto.getUser_id());
        user.setUser_id("");
        return user;
      } catch (Exception e) {
        return userDto;
      }
    }
    
    public static void makeCookie(HttpServletResponse response, String user_id) {
        Cookie cookieid = new Cookie("user_id", user_id); 
        response.addCookie(cookieid);       
    }
    public static void deleteCookie(HttpServletResponse response, String user_id) {
        Cookie cookieid = new Cookie("user_id", user_id); 
        cookieid.setMaxAge(0);
        response.addCookie(cookieid);   
    }
    
  
    private boolean loginCheck(String user_id, String user_pwd) throws Exception {
      // TODO Auto-generated method stub
      UserDto userDto = userDao.selectUser(user_id);
      
      System.out.println(userDto);
      if(userDto==null) {
          System.out.println("userDto==null");
          return false;
      }
      
      return userDto.getUser_pwd().equals(user_pwd);
  }
    
    
    private boolean PwdCheck(String user_pwd, String chk_user_pwd) {
     
      return user_pwd.equals(chk_user_pwd);
  }
  
}
