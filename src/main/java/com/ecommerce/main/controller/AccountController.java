package com.ecommerce.main.controller;

import com.ecommerce.main.entity.User;
import com.ecommerce.main.security.FlipkartUserDetails;
import com.ecommerce.main.service.UserService;
import com.ecommerce.main.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Objects;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String viewAccountHandler(@AuthenticationPrincipal FlipkartUserDetails loggedUser,
                                     Model model){
        String email = loggedUser.getUsername();
        User user = userService.getByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("pageHeader", user.getFirstName()+" "+user.getLastName()+" Profile");
        return "admin/account_form";
    }

    @PostMapping("/account/update")
    public String saveUserHandler(@ModelAttribute("user") User user,
                                  RedirectAttributes redirectAttributes,
                                  @RequestParam("image") MultipartFile multipartFile,
                                  @AuthenticationPrincipal FlipkartUserDetails loggedUser) throws IOException {


        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

            // Validate the filename to prevent security issues
            if (fileName.contains("..")) {
                redirectAttributes.addFlashAttribute("message", "Invalid file path!");
                return "redirect:/users";
            }

            // Set the filename to the user's photo field and save
            user.setPhotos(fileName);
            User savedUser = userService.updateAccount(user);

            // Save the file in the specified directory
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }else{
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.updateAccount(user);
        }

        loggedUser.setFirstName(user.getFirstName()); // when form updated your name dynamically change in navigation bar top right corner
        loggedUser.setLastName(user.getLastName());
        redirectAttributes.addFlashAttribute("message", "Your account details have been updated.");

        return "redirect:/account";
    }


}
