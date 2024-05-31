//package com.example.application.controller;
//
//import com.example.application.service.SUserService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//public class SUserController {
//    private SUserService suserservice;
//
//    @PostMapping("/addSUser")
//    public SUser addSUser(@RequestBody SUser suser)
//    {
//        return suserservice.saveSUser(suser);
//    }
//    @PostMapping("/addSUser")
//    public List<SUser> addSUser(@RequestBody List<SUser> susers)
//    {
//        return suserservice.saveSUsers(susers);
//    }
//
//    @GetMapping("/SUser")
//    public List<SUser> findAllSUsers()
//    {
//        return suserservice.getSUsers();
//    }
//
//    @GetMapping("/SUserById/{id}")
//    public SUser findSUserById(@PathVariable int id)
//    {
//        return suserservice.getSUsersById(id);
//    }
//    @GetMapping("/SUserByUsername/{username}")
//    public SUser findAllSUsers(@PathVariable String username)
//    {
//        return suserservice.getSUsersByUsername(username);
//    }
//
//    @PutMapping("/update")
//    public SUser UpdateSUser(@RequestBody SUser suser)
//    {
//        return suserservice.updateSUser(suser);
//    }
//
//    @DeleteMapping("delete/{id}")
//    public String deleteSUser(@PathVariable int id)
//    {
//        return suserservice.deleteSUser(id);
//    }
//
//
//}
