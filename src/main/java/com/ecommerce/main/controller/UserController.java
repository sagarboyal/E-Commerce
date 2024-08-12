package com.ecommerce.main.controller;

import com.ecommerce.main.configuration.FileUploadUtil;
import com.ecommerce.main.entity.User;
import com.ecommerce.main.exceptions.UserNotFoundException;
import com.ecommerce.main.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listAll(Model model){
        return listByPage(model, 1, "id", "asc"); // sortField name same as the class variable name
    }
    @GetMapping("page/{pageNumber}")
    public String listByPage(Model model, @PathVariable("pageNumber")int pageNumber,
                             @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir){
        Page<User> userPage = userService.listByPage(pageNumber, sortField, sortDir);
        List<User> userList = userPage.getContent();

        long startCount = (long) (pageNumber - 1) * UserService.USER_PER_PAGE + 1;
        long endCount = startCount + UserService.USER_PER_PAGE - 1;
        if(endCount > userPage.getTotalElements()){
            endCount = userPage.getTotalElements();
        }

        model.addAttribute("listUsers", userList);
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "admin/users";
    }
    @GetMapping("/new")
    public String newUserHandler(Model model){
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("pageTitle", "Register New User");
        model.addAttribute("user", user);
        model.addAttribute("listRoles", userService.listRoles());
        return "admin/user_form";
    }
    @PostMapping("/save")
    public String saveUserHandler(@ModelAttribute("user") User user,
                                  RedirectAttributes redirectAttributes,
                                  @RequestParam("image") MultipartFile multipartFile) throws IOException {


        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

            // Validate the filename to prevent security issues
            if (fileName.contains("..")) {
                redirectAttributes.addFlashAttribute("message", "Invalid file path!");
                return "redirect:/users";
            }

            // Set the filename to the user's photo field and save
            user.setPhotos(fileName);
            User savedUser = userService.saveUser(user);

            // Save the file in the specified directory
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }else{
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.saveUser(user);
        }

        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
        return "redirect:/users";
    }
    @GetMapping("update/{id}")
    public String updateUserHandler(@PathVariable(name = "id") Integer id,
                                    Model model,
                                    RedirectAttributes redirectAttributes){
        try{
            User data = userService.findById(id);
            model.addAttribute("pageTitle", "Edit User With ID "+id);
            model.addAttribute("user", data);
            model.addAttribute("listRoles", userService.listRoles());
            return "admin/user_form";
        }catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/users";
        }
    }
    @GetMapping("delete/{id}")
    public String deleteUserHandler(@PathVariable(name = "id") Integer id,
                                    RedirectAttributes redirectAttributes){
        try{
            User data = userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", data.getFirstName()+" "+data.getLastName()+" deleted with id: "+id);
        }catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/users";
    }
    @GetMapping("{id}/enabled/{status}")
    public String userStatusHandler(@PathVariable(name = "id") Integer id,
                                        @PathVariable("status") boolean status,
                                            RedirectAttributes redirectAttributes){
        userService.updateUserStatus(id, status);
        String s = (status)?"enabled":"disabled";
        redirectAttributes.addFlashAttribute("message", "The user ID " + id +
                                                " has been " + s);
        return "redirect:/users";
    }
}
