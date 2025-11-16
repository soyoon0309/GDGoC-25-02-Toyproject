package org.example.springbootdeveloper.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ExampleController {
    @GetMapping("/thymeleaf/example")
    public String thymeleafExample(Model model) {
        //뷰로 데이터를 넘겨주는 모델 객체
        //모델은 html 쪽으로 값을 넘겨주는 객체이다
        Person examplePerson=new Person();
        examplePerson.setId(1L);
        examplePerson.setName("홍길동");
        examplePerson.setAge(11);
        examplePerson.setHobbies(List.of("운동","독서"));

        //"key"값에 정보 저장
        model.addAttribute("person",examplePerson);
        model.addAttribute("today", LocalDate.now());
return "example"; //example.html라는 뷰 조회 => resoruces/templates 디렉터리에서 examp.html 을 찾은 다음 웹 브라우저에 해당 파일을 보여줌
    }

    @Setter
    @Getter
    class Person {
        private Long id;
        private String name;
        private int age;
        private List<String> hobbies;
    }
}
