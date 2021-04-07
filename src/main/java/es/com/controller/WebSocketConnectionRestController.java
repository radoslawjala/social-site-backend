package es.com.controller;

import es.com.dto.request.User;
import es.com.dto.response.MessageResponse;
import es.com.util.ActiveUserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@CrossOrigin
public class WebSocketConnectionRestController {

    @Autowired
    private ActiveUserManager activeSessionManager;

//    @PostMapping(value = "/rest/user-connect")
//    @CrossOrigin
//    public void userConnect(@RequestBody String title) {
//
//        System.out.println(title);
//    }
    @PostMapping("/rest/user-connect")
    public ResponseEntity<?> userConnect(@RequestBody String username, HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("Remote_Addr");
            if (StringUtils.isEmpty(remoteAddr)) {
                remoteAddr = request.getHeader("X-FORWARDED-FOR");
                if (remoteAddr == null || "".equals(remoteAddr)) {
                    remoteAddr = request.getRemoteAddr();
                }
            }
        }
        System.out.println("username:" + username + ", remoteAddr: " + remoteAddr);
        activeSessionManager.add(username, remoteAddr);
        return ResponseEntity.ok(new MessageResponse("powinno być sikalafa żelafą żymbalade sur nymbła"));
    }

//    @GetMapping("/rest/user-connect/info")
//    public void tmp(HttpServletRequest request) {
//        System.out.println("riszki sziszki");
//        String remoteAddr = "";
//        if (request != null) {
//            remoteAddr = request.getHeader("Remote_Addr");
//            if (StringUtils.isEmpty(remoteAddr)) {
//                remoteAddr = request.getHeader("X-FORWARDED-FOR");
//                if (remoteAddr == null || "".equals(remoteAddr)) {
//                    remoteAddr = request.getRemoteAddr();
//                    System.out.println(remoteAddr);
//                }
//            }
//        }
//    }

    @PostMapping("/rest/user-disconnect")
    public String userDisconnect(@ModelAttribute("username") String userName) {
        activeSessionManager.remove(userName);
        return "disconnected";
    }

    @GetMapping("/rest/active-users-except/{userName}")
    public ResponseEntity<?> getActiveUsersExceptCurrentUser(@PathVariable String userName) {
        ArrayList<String> users = new ArrayList<>();
        for(String name: activeSessionManager.getActiveUsersExceptCurrentUser(userName)) {
            users.add(name);
        }
        System.out.println("Users list: " + users);
        return ResponseEntity.ok(users);
    }
}

