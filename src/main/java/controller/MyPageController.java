package controller;

import domain.BookReport;
import domain.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import service.MyPageService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MyPageController {
    private final MyPageService myPageService;

    @Autowired
    public MyPageController(MyPageService myPageService){ //멤버서비스는 순수한 자바 코드라 자동으로 연결이 안 됨 -> @Service, @Repository
        this.myPageService = myPageService;
    }

    @GetMapping("/reports/new")
    public String uploadMyPage(Model model){
        model.addAttribute("MyPageForm", new MyPageForm());
        return "reports/uploadMyPageForm";
    }

    public String upload(MyPageForm form){
        MyPage myPage = new MyPage();
        myPage.setMyPageProfile(form.getMyPageProfile());
        myPage.setGoal(form.getGoal());
        myPage.setGenre(form.getGenre());
        myPage.setMember(form.getMember());

        myPageService.upload(myPage);
        return "redirect:/";
    }


    @GetMapping("/reports/edit/{myPageId}")
    public String modifyForm(@PathVariable Long myPageId, Model model){
        Optional<MyPage> myPage = myPageService.findOne(myPageId);

        if (myPage == null) {
            return "redirect:/";
        }

        MyPageForm form = new MyPageForm();
        form.setMyPageProfile(myPage.get().getMyPageProfile());
        form.setGoal(myPage.get().getGoal());
        form.setGenre(myPage.get().getGenre());
        form.setMember(myPage.get().getMember());

        model.addAttribute("MyPageForm", form);
        return "reports/modifyMyPageForm";
    }

    @PostMapping("/reports/edit/{myPageId}")
    public String modify(@PathVariable Long myPageId, @ModelAttribute MyPageForm myPageForm, BindingResult result){
        if (result.hasErrors()) {
            return "reports/modifyMyPageForm";
        }

        Optional<MyPage> myPage = myPageService.findOne(myPageId);

        if (myPage == null) {
            return "redirect:/";
        }

        myPage.get().setMyPageProfile(myPageForm.getMyPageProfile());
        myPage.get().setGoal(myPageForm.getGoal());
        myPage.get().setGenre(myPageForm.getGenre());
        myPage.get().setMember(myPageForm.getMember());

        myPageService.modify(myPage.get().getId(), myPage.get());
        return "redirect:/";
    }

    // 책 읽은 날짜들을 캘린더 형식으로 반환하는 엔드포인트
    @GetMapping("/mypage/{id}/calendar")
    public String getReadDatesCalendar(@PathVariable Long id, Model model) {
        Map<LocalDate, List<BookReport>> calendarData = myPageService.getReadDatesCalendar(id);
        model.addAttribute("calendarData", calendarData);
        return "calendarView"; // 캘린더를 표시할 뷰 이름
    }

    @GetMapping("/mypage/{id}")
    public String viewMyPage(@PathVariable Long id, Model model) {
        Optional<MyPage> myPage = myPageService.findOne(id);

        if (!myPage.isPresent()) {
            return "redirect:/"; // MyPage가 없으면 홈으로 리다이렉트
        }

        model.addAttribute("myPage", myPage.get());
        return "mypage/viewMyPage"; // MyPage를 표시할 뷰 이름
    }
}
