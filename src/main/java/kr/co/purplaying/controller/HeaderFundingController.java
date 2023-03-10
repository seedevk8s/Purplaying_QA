package kr.co.purplaying.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.purplaying.dao.HeaderFundingDao;
import kr.co.purplaying.domain.PageResolver2;
import kr.co.purplaying.domain.ProjectDto;
import kr.co.purplaying.domain.SearchItem2;
import kr.co.purplaying.service.LikeService;

@Controller
public class HeaderFundingController { 
  @Autowired
  HeaderFundingDao headerFundingDao;
  
  @Autowired
  LikeService likeService;
  
  @GetMapping("/popularFunding")
  public String popularFunding(SearchItem2 sc2, Model m, HttpSession session) {
    
    String id = (String)session.getAttribute("user_id");
    
    try {
      int totalCnt = headerFundingDao.getSearchResultCnt(sc2);
      m.addAttribute("totalCnt", totalCnt);
      
      PageResolver2 pageResolver2 = new PageResolver2(totalCnt, sc2);
      
      List<ProjectDto> list_p = headerFundingDao.getSearchResultPage_p(sc2);
      m.addAttribute("list_p", list_p);
      m.addAttribute("pr", pageResolver2);
      
      
//      Map map = new HashMap();
//      List<ProjectDto> list_p = headerFundingDao.popularFunding(map);
//      m.addAttribute("list_p",list_p);
      
      if(id!=null) {
        boolean likecheck = false;
        List<Integer> Likelist = likeService.selectLikelist(id);
        System.out.println(Likelist);
        m.addAttribute("Likelist",Likelist);
        
      }      
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return "popularFunding";
  }
  
  @GetMapping("/newFunding")
  public String newFunding(ProjectDto projectDto, Model m, HttpSession session) {
    
    String id = (String)session.getAttribute("user_id");
    
    try {
      Map map = new HashMap();
      List<ProjectDto> list_n = headerFundingDao.newFunding(map);
      m.addAttribute("list_n",list_n);
      
      
      if(id!=null) {
        boolean likecheck = false;
        List<Integer> Likelist = likeService.selectLikelist(id);
        System.out.println(Likelist);
     
   
        m.addAttribute("Likelist",Likelist);
        
      }      
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return "newFunding";
  }
  

  @GetMapping("/comingsoonFunding")
  public String getPage(ProjectDto projectDto, Model m, HttpSession session ) {
    
    String id = (String)session.getAttribute("user_id");
    
    try {
      Map map = new HashMap();
      List<ProjectDto> list_c = headerFundingDao.comingsoonFunding(map);
      m.addAttribute("list_c",list_c);
      
      if(id!=null) {
        boolean likecheck = false;
        List<Integer> Likelist = likeService.selectLikelist(id);
        System.out.println(Likelist);
     
   
        m.addAttribute("Likelist",Likelist);
        
      }      
      

    } catch (Exception e) {
      e.printStackTrace();
    }
    return "comingsoonFunding";
  }
}
