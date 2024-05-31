//package com.example.application.service;
//
//import com.example.application.controller.SUser;
//import com.example.application.repository.SUserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class SUserService {
//    @Autowired
//    private SUserRepository suserrepo;
//    public SUser saveSUser(SUser suser)
//    {
//        return suserrepo.save(suser);
//    }
//
//    public List<SUser> saveSUsers(List<SUser> susers)
//    {
//        return suserrepo.saveAll(susers);
//    }
//
//    public List<SUser> getSUsers()
//    {
//        return suserrepo.findAll();
//    }
//
//    public SUser getSUsersById(int id)
//    {
//        return suserrepo.findById(id).orElse(null);
//    }
//
//    public SUser getSUsersByUsername(String username)
//    {
//        return suserrepo.findByUsername(username);
//    }
//    public String deleteSUser(int id)
//    {
//        suserrepo.deleteById(id);
//        return "removed" + id;
//    }
//
//    public SUser updateSUser( SUser suser)
//    {
//        SUser existing = suserrepo.findById(suser.getId()).orElse(null);
//        existing.setName(suser.getName());
//        existing.setPassword(suser.getPassword());
//        existing.setUsername(suser.getUsername());
//        return suserrepo.save(existing);
//    }
//
//}
