package guru.springframework.controller;

import guru.springframework.dao.DogRepository;
import guru.springframework.model.Dog;
import guru.springframework.service.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class DogController {
    @Autowired
    DogRepository dogRepository;

    @Autowired
    DogService dogService;

    private ArrayList<Dog> dogModelList;

    private List<String> dogRiskList = null;

    @GetMapping("/")
    public String dogHome(
            @RequestParam(value = "search", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date q,
            Model model) {
        dogModelList = new ArrayList<>();
        System.out.println("q is = " + q);
        // this query doesn't seem to pay attention to q
        dogRiskList = dogService.atRiskDogs(q);
        for (String name : dogRiskList) {
            System.out.println("Dogs in repository are : " + dogRepository.findAll());
            Dog doggy = dogRepository.findByName(name);
            System.out.println(doggy.toString() + "doggy name : " + doggy.getName());
            dogModelList.add(doggy);
            System.out.println("This dog's name is : " + doggy.getName());
        }
        model.addAttribute("search", dogModelList);
        model.addAttribute("dogs", dogRepository.findAll());
        return "index";
    }

    @PostMapping(value = "/")
    public String addDog(@RequestParam("name") String name,
                         @RequestParam("rescued") @DateTimeFormat(pattern = "yyyy-MM-dd") Date rescued,
                         @RequestParam("vaccinated") Boolean vaccinated,
                         Model model) {
        dogService.addDog(name, rescued, vaccinated);
        System.out.println("name = " + name + ", rescued = " + rescued + ", vaccinated = " + vaccinated);
        return "redirect:/";
    }

    @PostMapping(value = "/delete")
    public String deleteDog(@RequestParam("name") String name,
                            @RequestParam("id") Long id) {
        dogService.deleteDog(name, id);
        System.out.println("Dog named = " + name + " was removed from our database.  Hopefully he or she was adopted.");
        return "redirect:/";
    }

    @PostMapping(value = "/genkey")
    public String genKey(@RequestParam("name") String name,
                         @RequestParam("rescued") @DateTimeFormat(pattern = "yyyy-MM-dd") Date rescued,
                         @RequestParam("vaccinated") Boolean vaccinated,
                         Model model) {
        Long id = dogService.getGeneratedKey(name, rescued, vaccinated);
        System.out.println("Added dog with id = " + id);
        System.out.println("name = " + name + ", rescued = " + rescued + ", vaccinated = " +vaccinated);
        return "redirect:/";
    }
}
