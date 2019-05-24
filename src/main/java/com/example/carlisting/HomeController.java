package com.example.carlisting;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String homePage(Model model){
        model.addAttribute("categories", categoryRepository.findAll());
        return "categorylist";
    }
//-- Methods controlling Category ---------------------------------------------------------------
    @GetMapping("/addcategory")
    public String addCategory(Model model){
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("category", new Category());
        return "categoryform";
    }

    @PostMapping("/processcategory")
    public String processCategory(@Valid Category category, BindingResult result, Model model){
        model.addAttribute("categories", categoryRepository.findAll());
        if(result.hasErrors()){
            return "categoryform";
        }
        categoryRepository.save(category);
        return "redirect:/";
    }

    @RequestMapping("/viewcategory/{id}")
    public String viewCategory(@PathVariable("id") long id, Model model){
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("category", categoryRepository.findById(id).get());
        return "showcategory";
    }

    //-- Methods controlling Car ---------------------------------------------------------------------
    @GetMapping("/addcar")
    public String addCar(Model model){
        model.addAttribute("categories",categoryRepository.findAll());
        model.addAttribute("car", new Car());
        return "carform";
    }

    @PostMapping("/processcar")
    public String processCar(@RequestParam("file") MultipartFile file, @Valid Car car, BindingResult result){
        if(result.hasErrors()){
            return "carform";
        }
        if(file.isEmpty()){
            return "redirect:/";
        }
        try{
            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            car.setImgUrl(uploadResult.get("url").toString());
            carRepository.save(car);
        }catch (IOException e){
            e.printStackTrace();
            return "redirect:/";
        }
        return "redirect:/";
    }

    @RequestMapping("/updatecar/{id}")
    public String updateCategory(@PathVariable("id") long id, Model model){
        model.addAttribute("categories",categoryRepository.findAll());
        model.addAttribute("car", carRepository.findById(id).get());
        return "carform";
    }

    @RequestMapping("/viewcar/{id}")
    public String viewCar(@PathVariable("id") long id, Model model){
        model.addAttribute("car", carRepository.findById(id).get());
        model.addAttribute("categories", categoryRepository.findAll());
        return "showcar";
    }

    @RequestMapping("/delete/{id}")
    public String deleteCar(@PathVariable("id") long id){
        carRepository.deleteById(id);
        System.out.println(carRepository.count());
        return "redirect:/";
    }
}
