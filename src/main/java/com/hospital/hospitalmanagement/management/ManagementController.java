package com.hospital.hospitalmanagement.management;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path ="api/v1/" )
@AllArgsConstructor
public class ManagementController {

    private ManagementService registrationService;

    //register
    @PostMapping(path = "doctor/register")
    public String register(@RequestBody ManagementRequest request){
        return registrationService.register(request);
    }

    //update
    @PutMapping(path = "doctor/update")
    public String update (@RequestBody ManagementRequest request){
        return registrationService.update(request);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }

    @GetMapping
    public String register(){
        return "register_page";
    }


}
