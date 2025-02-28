package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	//	@RequestMapping("/")
	//	public ModelAndView redirectToLogin() {
	//		return new ModelAndView("redirect:/login");
	//	}

	//最初に表示する画面をログイン画面に設定
	//	@RequestMapping("/login/login")
	//	public String showLogin() {
	//		return "login/login";
	//	}

	//ログイン画面でユーザーデータの入力を行う
	@GetMapping("/login")
	public String loginSetUser(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "login/login";
	}

	//新規登録画面でユーザーデータの入力を請求
	@GetMapping("/createAccount")
	public String showCreateAccount(Model model) {
		User user = new User();
		model.addAttribute(user);
		return "login/createAccount";
	}

	@PostMapping("/createAccout")
	public String doCreateAccount(User user, Model model) {
		List<User> users = userRepository.findAll();

		//passwordとconfirmPassword一致するかどうかをチェック
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			System.out.println("Password doesnt match");
			System.out.println(user.getPassword() + user.getConfirmPassword());
			model.addAttribute("errorMessage_pw", "Password doesn't match asdas");
			return "login/createAccount";
		}
		//既存アカウントであるかをチェック
		for (User existingUser : users) {
			if (existingUser.getEmail().equals(user.getEmail())) {
				System.out.println("Almost exist accout please try another");
				model.addAttribute("errorMessage_email", "Almost exist email please try another");
				return "login/createAccount";
			}
		}
		userService.saveUser(user);
		return "redirect:/login";
	}

	//一覧画面へ移動
	@GetMapping("/show")
	public String showUserPage(Model model) {
		return "login/show";
	}

	//一覧画面へ移動時の処理
	@PostMapping("/show")
	public RedirectView doCheckUserData(@ModelAttribute User user, Model model) {

		//データベースでuserをチェックし、あるなら/showを表示する、ないならloginに戻る
		model.addAttribute(user);
		List<User> users = userRepository.findAll();

		for (User existingUser : users) {
			if (existingUser.getEmail().equals(user.getEmail())
					&& existingUser.getPassword().equals(user.getPassword())) {
				return new RedirectView("/show");
			}
		}
		model.addAttribute("errorMessage_login", "Email or Password is incorrect");
		return new RedirectView("/login");
	}
}
